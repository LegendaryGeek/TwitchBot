package TwitchBot.app.Fallout4Twitch;

import TwitchBot.app.App;
import TwitchBot.app.Fallout4Twitch.Commands.CommandHandler;
import TwitchBot.app.Fallout4Twitch.JSON.*;
import TwitchBot.app.Fallout4Twitch.Misc.*;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.GiftSubscriptionsEvent;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;
import com.github.twitch4j.tmi.domain.Chatters;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BethesdaTwitchIntegration extends Thread {

  static TwitchClient twitchClient;
  private static Logger log = LoggerFactory.getLogger(
    "TwitchBot.app.Fallout4Twitch.BethesdaTwitchIntegration"
  );

  public void BethesdaTwitchIntegrationRun() {
    log = LoggerFactory.getLogger(this.getClass().toString());
    SettingsParser.settingsParser();

    DirectoryMonitoring directoryMonitoring = new DirectoryMonitoring();
    directoryMonitoring.start();
    MiscLogic.startCleanup();

    log.info("[BTI] Connected to {}.", SettingsParser.channelToJoin);

    twitchClient =
      TwitchClientBuilder
        .builder()
        .withEnableChat(true)
        .withChatAccount(TwitchBot.app.App.credential)
        .withEnableTMI(true)
        .withEnableGraphQL(true)
        .build();
    twitchClient.getChat().joinChannel(SettingsParser.channelToJoin);

    SimpleEventHandler eventHandler = twitchClient
      .getEventManager()
      .getEventHandler(SimpleEventHandler.class);
    eventHandler.onEvent(
      CheerEvent.class,
      e -> {
        if (!PointLogic.userExists(e.getUser().getName().toLowerCase())) {
          PointLogic.addUser(e.getUser().getName().toLowerCase());
        }
        PointLogic.userPointModifier(
          e.getUser().getName().toLowerCase(),
          (int) (e.getBits() * SettingsParser.bitMultiplier)
        );
      }
    );

    /*
     * Handles the subscription event.
     *
     * Points get multiplied by tier level (1, 2, 3).
     *
     * Only fires if it's not a gifted subscription.
     *
     */
    eventHandler.onEvent(
      SubscriptionEvent.class,
      e -> {
        if (!e.getGifted()) {
          int levelMultiplier = 0;

          switch (e.getSubscriptionPlan()) {
            case "1000":
              levelMultiplier = 1;
              break;
            case "2000":
              levelMultiplier = 2;
              break;
            case "3000":
              levelMultiplier = 3;
              break;
          }
          if (!PointLogic.userExists(e.getUser().getName().toLowerCase())) {
            PointLogic.addUser(e.getUser().getName().toLowerCase());
          }
          PointLogic.userPointModifier(
            e.getUser().getName().toLowerCase(),
            (int) SettingsParser.subscriptionPoints * levelMultiplier
          );
        }
      }
    );

    /*
     * Handles the gift sub event.
     *
     * Points get multiplied by tier level (1, 2, 3) and then by sub amount.
     *
     */

    eventHandler.onEvent(
      GiftSubscriptionsEvent.class,
      e -> {
        int levelMultiplier = 0;

        switch (e.getSubscriptionPlan()) {
          case "1000":
            levelMultiplier = 1;
            break;
          case "2000":
            levelMultiplier = 2;
            break;
          case "3000":
            levelMultiplier = 3;
            break;
        }

        if (!PointLogic.userExists(e.getUser().getName().toLowerCase())) {
          PointLogic.addUser(e.getUser().getName().toLowerCase());
        }

        PointLogic.userPointModifier(
          e.getUser().getName().toLowerCase(),
          (int) SettingsParser.subscriptionPoints * levelMultiplier
        );
      }
    );

    eventHandler.onEvent(
      ChannelMessageEvent.class,
      e -> {
        if (
          e
            .getMessage()
            .substring(0, 1)
            .equalsIgnoreCase(SettingsParser.sharedPrefix)
        ) {
          String[] commandString = e.getMessage().split(" ");
          CommandHandler.handleCommand(
            e.getUser().getName(),
            commandString,
            twitchClient
          );
        }
      }
    );

    Chatters chatters = twitchClient
      .getMessagingInterface()
      .getChatters(SettingsParser.channelToJoin.toLowerCase())
      .execute();

    // com.github.twitch4j.graphql.internal.FetchChattersQuery.Data chatters2 = twitchClient
    //   .getGraphQL()
    //   .fetchChatters(App.credential, SettingsParser.channelToJoin)
    //   .execute();
    //log.info("chatters bypass data: {}", chatters2.toString());
    TimedLogic.userLogger(chatters.getAllViewers());
    timedLoop(twitchClient);
  }

  /*
   * A few message functions for ease-of-life.
   */

  public static void sendMessage(String message) {
    twitchClient.getChat().sendMessage(SettingsParser.channelToJoin, message);
  }

  public static void sendInvalidCommand(String username) {
    twitchClient
      .getChat()
      .sendMessage(
        SettingsParser.channelToJoin,
        "Invalid command @" + username
      );
  }

  public static void sendMissingPoints(String username) {
    twitchClient
      .getChat()
      .sendMessage(
        SettingsParser.channelToJoin,
        "You don't have enough points to do that @" + username
      );
  }

  public static void timedLoop(TwitchClient twitchClient) {
    Timer timer = new Timer();
    timer.schedule(
      new TimerTask() {
        @Override
        public void run() {
          if (SettingsParser.SystemEnabled) {
            Chatters chatters = twitchClient
              .getMessagingInterface()
              .getChatters(SettingsParser.channelToJoin.toLowerCase())
              .execute();
            TimedLogic.defaultPointLoop(chatters.getAllViewers());
            PointLogic.userPointModifier(
              SettingsParser.channelToJoin,
              (int) SettingsParser.pointsPerMinute
            ); //TODO Remove
          } else if (SettingsParser.PointWhenDisabled) {
            Chatters chatters = twitchClient
              .getMessagingInterface()
              .getChatters(SettingsParser.channelToJoin.toLowerCase())
              .execute();
            // com.github.twitch4j.graphql.internal.FetchChattersQuery.Data chatters2 = twitchClient
            //   .getGraphQL()
            //   .fetchChatters(App.credential, SettingsParser.channelToJoin)
            //   .execute();
            //log.info("chatters bypass data: {}", chatters2.toString());
            TimedLogic.defaultPointLoop(chatters.getAllViewers());
            PointLogic.userPointModifier(
              SettingsParser.channelToJoin,
              (int) SettingsParser.pointsPerMinute
            ); //TODO Remove
          }
        }
      },
      0,
      60 * 1000
    );
  }

  @Override
  public void run() {
    log.debug("###############Starting Fallout Bot");
    BethesdaTwitchIntegrationRun();
  }
}
