package TwitchBot.app.Fallout4Twitch.Commands;

import static TwitchBot.app.Fallout4Twitch.JSON.CommandValidation.*;
import static TwitchBot.app.Fallout4Twitch.JSON.SettingsParser.*;

import TwitchBot.app.Fallout4Twitch.BethesdaTwitchIntegration;
import TwitchBot.app.Fallout4Twitch.JSON.CommandValidation;
import TwitchBot.app.Fallout4Twitch.JSON.PointLogic;
import TwitchBot.app.Fallout4Twitch.Misc.MiscLogic;

public class WeatherCommand {

  public static void handleWeather(String username, String weatherCommand) {
    BethesdaTwitchIntegration.sendMessage("stuck prior to validation");
    if (CommandValidation.validateWeather(weatherCommand)) {
      BethesdaTwitchIntegration.sendMessage("stuck after validation");
      if (PointLogic.getUserPoints(username) < weatherCost) {
        BethesdaTwitchIntegration.sendMissingPoints(username);
      } else {
        PointLogic.userPointModifier(username, -weatherCost);
        String commandForxSE = "weather " + weatherID + " " + pluginToLookFor;
        MiscLogic.createCommandCall(commandForxSE);
        lastSpecialCommand = System.currentTimeMillis();
        lastCommandGlobal = System.currentTimeMillis();
      }
    } else {
      BethesdaTwitchIntegration.sendInvalidCommand(username);
    }
  }
}
