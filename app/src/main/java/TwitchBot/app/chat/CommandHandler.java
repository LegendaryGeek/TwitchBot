package TwitchBot.app.chat;

import TwitchBot.app.App;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.CheerEvent;
import com.github.twitch4j.chat.events.channel.GiftSubscriptionsEvent;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;
import com.github.twitch4j.eventsub.events.ChannelPointsCustomRewardEvent;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.pubsub.events.ChannelPointsRedemptionEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandHandler {

  Logger log = LoggerFactory.getLogger("TwitchBot.app.chat.CommandHandler");
  User trisStreamEvents = App.client
    .getClientHelper()
    .enableStreamEventListener("trisdove");
  User bioStreamEvents = App.client
    .getClientHelper()
    .enableStreamEventListener("bionic_caliber_x");

  public CommandHandler(SimpleEventHandler eventHandler) {
    eventHandler.onEvent(
      ChannelMessageEvent.class,
      event -> onChannelMessage(event)
    );
    eventHandler.onEvent(
      SubscriptionEvent.class,
      event -> {
        log.info(
          "sub to {} from {}. stream sub count: {}",
          event.getChannel().getName(),
          event.getUser().getName(),
          App.client
            .getHelix()
            .getSubscriptions(
              null,
              event.getChannel().getId(),
              App.client
                .getClientHelper()
                .enableStreamEventListener(event.getChannel().getName())
                .getCreatedAt()
                .toString(),
              event.getFiredAt().toString(),
              new Integer(20)
            )
        );
      }
    );
    eventHandler.onEvent(
      GiftSubscriptionsEvent.class,
      event -> {
        log.info(
          "sub to {} from {}. count: {}. stream sub count: {}",
          event.getChannel().getName(),
          event.getUser().getName(),
          event.getCount(),
          App.client
            .getHelix()
            .getSubscriptions(
              null,
              event.getChannel().getId(),
              App.client
                .getClientHelper()
                .enableStreamEventListener(event.getChannel().getName())
                .getCreatedAt()
                .toString(),
              event.getFiredAt().toString(),
              new Integer(20)
            )
        );
      }
    );
    eventHandler.onEvent(
      ChannelPointsCustomRewardEvent.class,
      event -> {
        log.info("channel point redeem event: {}", event.getTitle());
      }
    );

    eventHandler.onEvent(
      ChannelPointsRedemptionEvent.class,
      event -> {
        log.info(
          "channel point redeem event: {}",
          event.getRedemption().getReward().getTitle()
        );
      }
    );

    eventHandler.onEvent(
      RewardRedeemedEvent.class,
      event -> {
        log.info(
          "channel point redeem event: {}",
          event.getRedemption().getReward().getTitle()
        );
      }
    );

    eventHandler.onEvent(
      IRCMessageEvent.class,
      event -> {
        log.info("IRC Message Event: {}", event.getRawMessage());
      }
    );

    eventHandler.onEvent(
      CheerEvent.class,
      event -> {
        log.info(
          "bits to {} from {}. bits: {}.",
          event.getChannel().getName(),
          event.getUser().getName(),
          event.getBits()
        );
      }
    );
    // eventHandler.onEvent(
    //   ChannelJoinEvent.class,
    //   event -> {
    //     log.info(
    //       "sub to {} from {}. stream sub count: {}",
    //       event.getChannel().getName(),
    //       event.getUser().getName(),
    //       App.client
    //         .getHelix()
    //         .getSubscriptions(
    //           null,
    //           event.getChannel().getId(),
    //           App.client
    //             .getClientHelper()
    //             .enableStreamEventListener(event.getChannel().getName())
    //             .getCreatedAt()
    //             .toString()
    //             .toString(),
    //           event.getFiredAt().toString(),
    //           new Integer(20)
    //         )
    //         .getNumberEmissions()
    //     );
    //   }
    // );
  }

  public void onChannelMessage(ChannelMessageEvent event) {
    if (event.getMessage().startsWith("!gstop")) {
      new CmdStop(event);
    } else if (event.getMessage().startsWith("!join")) {
      new CmdJoin(event);
    } else if (event.getMessage().startsWith("!toggleFallout")) {
      new CmdFallout(event);
    } else {
      log.info(
        "got message with unknown command: {} from user {} in channel {}",
        event.getMessage(),
        event.getUser(),
        event.getChannel().getName()
      );
    }
  }
}
