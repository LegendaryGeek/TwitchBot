package TwitchBot.app.Fallout4Twitch.Commands;

import static TwitchBot.app.Fallout4Twitch.JSON.CommandValidation.*;
import static TwitchBot.app.Fallout4Twitch.JSON.SettingsParser.lastCommandGlobal;
import static TwitchBot.app.Fallout4Twitch.JSON.SettingsParser.lastDefaultCommand;

import TwitchBot.app.Fallout4Twitch.BethesdaTwitchIntegration;
import TwitchBot.app.Fallout4Twitch.JSON.CommandValidation;
import TwitchBot.app.Fallout4Twitch.JSON.PointLogic;
import TwitchBot.app.Fallout4Twitch.Misc.MiscLogic;

public class GiveCommand {

  public static void handleGive(
    String username,
    String giveCommand,
    int amount
  ) {
    if (CommandValidation.validateGive(giveCommand)) {
      if (PointLogic.getUserPoints(username) < itemCost) {
        BethesdaTwitchIntegration.sendMissingPoints(username);
      } else {
        PointLogic.userPointModifier(username, -itemCost);
        int itemAmount;

        if (itemsMultiplied) {
          itemAmount = amount * 10;
        } else {
          itemAmount = amount;
        }

        String commandForxSE =
          "player.additem " + itemID + " " + itemAmount + " " + pluginToLookFor;
        MiscLogic.createCommandCall(commandForxSE);
        lastDefaultCommand = System.currentTimeMillis();
        lastCommandGlobal = System.currentTimeMillis();
      }
    } else {
      BethesdaTwitchIntegration.sendInvalidCommand(username);
    }
  }
}
