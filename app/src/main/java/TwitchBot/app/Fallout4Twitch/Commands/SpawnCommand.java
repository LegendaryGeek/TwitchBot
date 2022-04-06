package TwitchBot.app.Fallout4Twitch.Commands;

import static TwitchBot.app.Fallout4Twitch.JSON.CommandValidation.*;

import TwitchBot.app.Fallout4Twitch.BethesdaTwitchIntegration;
import TwitchBot.app.Fallout4Twitch.JSON.CommandValidation;
import TwitchBot.app.Fallout4Twitch.JSON.PointLogic;
import TwitchBot.app.Fallout4Twitch.JSON.SettingsParser;
import TwitchBot.app.Fallout4Twitch.Misc.MiscLogic;

public class SpawnCommand {

  public static String lastCommandFired = "null 0";

  public static void handleSpawn(String username, String spawnCommand) {
    if (CommandValidation.validateSpawn(spawnCommand)) {
      int allowedInInteriorInt;

      if (allowedInInterior) {
        allowedInInteriorInt = 1;
      } else {
        allowedInInteriorInt = 0;
      }

      if (PointLogic.getUserPoints(username) < npcCost) {
        BethesdaTwitchIntegration.sendMissingPoints(username);
      } else {
        PointLogic.userPointModifier(username, -npcCost);
        String commandForxSE =
          "spawn " +
          npcToSpawn +
          " " +
          npcAmountToSpawn +
          " " +
          allowedInInteriorInt +
          " " +
          pluginToLookFor;
        MiscLogic.createCommandCall(commandForxSE);
        SettingsParser.lastDefaultCommand = System.currentTimeMillis();
        SettingsParser.lastCommandGlobal = System.currentTimeMillis();
        lastCommandFired = username + " " + npcCost;
      }
    } else {
      BethesdaTwitchIntegration.sendInvalidCommand(username);
    }
  }
}
