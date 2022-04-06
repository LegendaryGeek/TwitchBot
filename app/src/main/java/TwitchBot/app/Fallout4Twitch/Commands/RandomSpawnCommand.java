package TwitchBot.app.Fallout4Twitch.Commands;

import static TwitchBot.app.Fallout4Twitch.JSON.CommandValidation.*;
import static TwitchBot.app.Fallout4Twitch.JSON.SettingsParser.*;

import TwitchBot.app.Fallout4Twitch.BethesdaTwitchIntegration;
import TwitchBot.app.Fallout4Twitch.Misc.MiscLogic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class RandomSpawnCommand {

  Gson gson = new Gson();

  public static void handleRandomSpawn(String username) {
    JsonParser jsonParser = new JsonParser();
    try {
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/" +
          gameJSON +
          "/" +
          gameJSON +
          "spawns.json"
        )
      );

      String[] jsonToStringArray = (String[]) jsonObject
        .keySet()
        .toArray(new String[jsonObject.size()]);

      Random random = new Random();
      int randomSpawn = random.nextInt(jsonToStringArray.length);

      JsonArray jsonArray = (JsonArray) jsonObject.get(
        jsonToStringArray[randomSpawn]
      );

      npcToSpawn = (String) jsonArray.get(1).getAsString();
      long tempAmount = (long) jsonArray.get(2).getAsLong();
      npcAmountToSpawn = (int) tempAmount;

      allowedInInterior = (boolean) jsonArray.get(3).getAsBoolean();

      if (jsonArray.size() == 5) {
        pluginToLookFor = (String) jsonArray.get(4).getAsString();
      } else pluginToLookFor = defaultPlugin;

      int allowedInInteriorValue;
      if (allowedInInterior) {
        allowedInInteriorValue = 1;
      } else {
        allowedInInteriorValue = 0;
      }

      String commandForxSE =
        "spawn " +
        npcToSpawn +
        " " +
        npcAmountToSpawn +
        " " +
        allowedInInteriorValue +
        " " +
        pluginToLookFor;
      MiscLogic.createCommandCall(commandForxSE);
      lastDefaultCommand = System.currentTimeMillis();
      lastCommandGlobal = System.currentTimeMillis();
      SpawnCommand.lastCommandFired = username + " " + randomspawncost;
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
  }
}
