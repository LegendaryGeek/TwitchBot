package TwitchBot.app.Fallout4Twitch.Commands;

import static TwitchBot.app.Fallout4Twitch.JSON.CommandValidation.*;
import static TwitchBot.app.Fallout4Twitch.JSON.SettingsParser.*;

import TwitchBot.app.Fallout4Twitch.BethesdaTwitchIntegration;
import TwitchBot.app.Fallout4Twitch.JSON.CommandValidation;
import TwitchBot.app.Fallout4Twitch.JSON.PointLogic;
import TwitchBot.app.Fallout4Twitch.Misc.MiscLogic;

public class MiscCommand {

  public static void handleMisc(String username, String miscCommand) {
    if (CommandValidation.validateMisc(miscCommand)) {
      if (PointLogic.getUserPoints(username) < miscCost) {
        BethesdaTwitchIntegration.sendMissingPoints(username);
      } else {
        PointLogic.userPointModifier(username, -miscCost);
        MiscLogic.createCommandCall(CommandValidation.miscCommand);
        lastSpecialCommand = System.currentTimeMillis();
        lastCommandGlobal = System.currentTimeMillis();
      }
    }
  }
}
