package TwitchBot.app.chat;

import TwitchBot.app.App;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdStop {

  Logger log = LoggerFactory.getLogger("TwitchBot.app.chat.CmdStop");

  public CmdStop(ChannelMessageEvent event) {
    log.debug("recieved stop event: {}", event.getMessage());
    if (event.getUser().getName() == "LegendaryGeekGaming") {
      App.client.close();
      System.exit(0);
    }
  }
}
