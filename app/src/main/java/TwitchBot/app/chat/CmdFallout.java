package TwitchBot.app.chat;

import TwitchBot.app.App;
import TwitchBot.app.Fallout4Twitch.BethesdaTwitchIntegration;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import java.lang.Thread.State;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdFallout {

  Thread FalloutRunner = new BethesdaTwitchIntegration();

  Logger log = LoggerFactory.getLogger("TwitchBot.app.chat.CmdFallout");
  public static ArrayList<String> ChannelsJoined;

  public CmdFallout(ChannelMessageEvent event) {
    FalloutRunner.run();
    log.debug(
      "recieved Fallout Integration Toggle Event: {}",
      event.getMessage()
    );
    if (!event.getChannel().getName().equals("legendarygeekgaming")) {
      log.debug(
        "Fallout Integration Toggle Event was canceled because its in another ",
        event.getMessage()
      );
      App.client
        .getChat()
        .sendMessage(
          event.getChannel().getName(),
          "this command can only be used in my owner's channel"
        );
    } else if (
      event.getChannel().getName().equals("legendarygeekgaming") &&
      !event.getMessageEvent().getBadges().containsKey("broadcaster")
    ) {
      App.client
        .getChat()
        .sendMessage(
          event.getChannel().getName(),
          "this command can only be used by the streamer"
        );
    } else if (
      event.getChannel().getName().equals("legendarygeekgaming") &&
      event.getMessageEvent().getBadges().containsKey("broadcaster")
    ) {
      log.debug(
        "fallout toggle status before toggle: {}",
        FalloutRunner.getState().toString()
      );
      if (FalloutRunner.getState().equals(State.RUNNABLE)) {
        FalloutRunner.run();
        log.debug(
          "fallout toggle status after running: {}",
          FalloutRunner.getState().toString()
        );
      }
    }
  }
}
