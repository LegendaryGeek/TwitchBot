package TwitchBot.app.chat;

import TwitchBot.app.App;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdJoin {

  Logger log = LoggerFactory.getLogger("TwitchBot.app.chat.CmdJoin");
  public static ArrayList<String> ChannelsJoined;

  public CmdJoin(ChannelMessageEvent event) {
    log.debug("recieved join event: {}", event.getMessage());
    String[] channelToJoin = event.getMessage().split(" ");
    log.debug("was asked to join channel: {}", channelToJoin[1]);
  }
}
