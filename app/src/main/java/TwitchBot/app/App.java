/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package TwitchBot.app;

import static TwitchBot.app.MessageUtils.getMessage;
import static TwitchBot.utilities.StringUtils.join;
import static TwitchBot.utilities.StringUtils.split;

import TwitchBot.app.chat.CmdJoin;
import TwitchBot.app.chat.CmdStop;
import TwitchBot.app.chat.CommandHandler;
import TwitchBot.list.LinkedList;
import com.github.philippheuer.credentialmanager.domain.Credential;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.enums.TMIConnectionState;
import java.net.MalformedURLException;
import java.net.URL;
import net.sourceforge.jwbf.core.bots.WikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  public static TwitchClient client;
  public static final String channel = "LegendaryGeekGaming";
  public static OAuth2Credential credential = new OAuth2Credential(
    "twitch",
    ""
  );
  private static Logger log;
  private String clientSecret = "";
  private String clientId = "";
  private String oauthToken = "";

  // private static MediaWikiBot wiki;

  public static void main(String[] args) {
    log = LoggerFactory.getLogger("TwitchBot.app.App");

    // try {
    //   wiki =
    //     new MediaWikiBot(new URL("https://bulbapedia.bulbagarden.net/wiki"));
    // } catch (MalformedURLException e) {
    //   // TODO Auto-generated catch block
    //   log.error("Error with Wiki", e);
    // }

    client =
      TwitchClientBuilder
        .builder()
        //.withDefaultAuthToken(credential)
        .withClientId(this.clientId)
        .withClientSecret(this.clientSecret)
        .withEnableChat(true)
        .withEnableHelix(true)
        .withEnableTMI(true)
        .withEnablePubSub(true)
        .withChatAccount(credential)
        .build();

    registerFeatures();
    TwitchChat chat = client.getChat();
    chat.connect();

    // Article test = wiki.getArticle("sylveon");
    // log.info(
    //   "test wiki output: title: {} ; Content: {}",
    //   test.getTitle(),
    //   test.getText()
    // );

    while (
      !chat.isChannelJoined("LegendaryGeekGaming")/*||
      !chat.isChannelJoined("trisdove") ||
      !chat.isChannelJoined("bionic_caliber_x")*/
    ) {
      System.out.println("channel not joined. attempting to join.");
      chat.joinChannel("LegendaryGeekGaming");
      CmdJoin.ChannelsJoined.add("LegendaryGeekGaming");
      //chat.joinChannel("trisdove");
      //chat.joinChannel("bionic_caliber_x");
      //     log.info(
      //       "sub to {} from {}. count: {}. stream sub count: {}",
      //       "bionic_caliber_x",
      //       "start up",
      //       0,
      //       App.client
      //         .getHelix()
      //         .getSubscriptions(
      //           "oauth:k9sxuivlr4rg5scbimpgdgdt2779r7",
      //           event.getChannel().getId(),
      //           App.client
      //             .getClientHelper()
      //             .enableStreamEventListener(event.getChannel().getName())
      //             .getCreatedAt()
      //             .toString(),
      //           event.getFiredAt().toString(),
      //           new Integer(20)
      //         )

      // );
    }

    log.debug("Joined Channel");

    if (chat.getConnectionState().equals(TMIConnectionState.CONNECTED)) {
      //chat.sendMessage("LegendaryGeekGaming", "Starting up...");
      log.debug("Sent Test Message");
    }

    //default stuff generated by gradle, leaving here so i know where the end is.
    LinkedList tokens;
    tokens = split(getMessage());
    String result = join(tokens);
    System.out.println(WordUtils.capitalize(result));
  }

  public static void registerFeatures() {
    SimpleEventHandler eventHandler = client
      .getEventManager()
      .getEventHandler(SimpleEventHandler.class);
    client
      .getPubSub()
      .listenForChannelPointsRedemptionEvents(credential, channel);
    CommandHandler commandHandler = new CommandHandler(eventHandler);
    log.debug("Features registered");
  }
}