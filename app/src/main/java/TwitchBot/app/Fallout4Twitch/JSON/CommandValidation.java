package TwitchBot.app.Fallout4Twitch.JSON;

import static TwitchBot.app.Fallout4Twitch.JSON.SettingsParser.*;

import TwitchBot.app.Fallout4Twitch.BethesdaTwitchIntegration;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;

public class CommandValidation {

  public Gson gson = new Gson();

  // Item Command Properties
  public static int itemCost = 0;
  public static String itemID = "null";
  public static boolean itemsMultiplied = false;

  // Spawn Command Properties
  public static int npcCost = 0;
  public static String npcToSpawn = "null";
  public static int npcAmountToSpawn = 0;
  public static boolean allowedInInterior = true;

  // Weather Command Properties
  public static String weatherID = "null";
  public static int weatherCost = 0;

  // Misc Command Properties
  public static int miscCost = 0;
  public static String miscCommand = "null";

  // Misc Properties
  public static String defaultPlugin = "null";
  public static String pluginToLookFor = "null";

  public static boolean validateSpawn(String spawnCommand) {
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

      for (String keyIterator : (Iterable<String>) jsonObject.keySet()) {
        if (keyIterator.equalsIgnoreCase(spawnCommand)) {
          JsonArray jsonArray = (JsonArray) jsonObject.get(keyIterator);

          long tempCost = (long) jsonArray.get(0).getAsLong();
          npcCost = (int) tempCost;
          npcToSpawn = (String) jsonArray.get(1).getAsString();
          long tempAmount = (long) jsonArray.get(2).getAsLong();
          npcAmountToSpawn = (int) tempAmount;
          allowedInInterior = (boolean) jsonArray.get(3).getAsBoolean();
          if (jsonArray.size() == 5) {
            pluginToLookFor = (String) jsonArray.get(4).getAsString();
          } else pluginToLookFor = defaultPlugin;

          return true;
        }
      }
      return false;
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static boolean validateMisc(String miscCommand) {
    JsonParser jsonParser = new JsonParser();
    try {
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/" +
          gameJSON +
          "/" +
          gameJSON +
          "misc.json"
        )
      );

      for (String keyIterator : (Iterable<String>) jsonObject.keySet()) {
        if (keyIterator.equalsIgnoreCase(miscCommand)) {
          JsonArray jsonArray = (JsonArray) jsonObject.get(keyIterator);
          long tempCost = (long) jsonArray.get(0).getAsLong();
          miscCost = (int) tempCost;
          CommandValidation.miscCommand =
            (String) jsonArray.get(1).getAsString();
          return true;
        }
      }
      return false;
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static boolean validateWeather(String weatherCommand) {
    JsonParser jsonParser = new JsonParser();
    try {
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/" +
          gameJSON +
          "/" +
          gameJSON +
          "weathers.json"
        )
      );
      BethesdaTwitchIntegration.sendMessage(weatherCommand);
      for (String keyIterator : (Iterable<String>) jsonObject.keySet()) {
        if (keyIterator.equalsIgnoreCase(weatherCommand)) {
          BethesdaTwitchIntegration.sendMessage("found it");
          JsonArray jsonArray = (JsonArray) jsonObject.get(keyIterator);
          long tempCost = (long) jsonArray.get(0).getAsLong();
          weatherCost = (int) tempCost;
          weatherID = (String) jsonArray.get(1).getAsString();
          if (jsonArray.size() == 3) {
            pluginToLookFor = (String) jsonArray.get(2).getAsString();
          } else pluginToLookFor = defaultPlugin;
          BethesdaTwitchIntegration.sendMessage("cleared it");
          return true;
        }
      }
      return false;
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static boolean validateGive(String giveCommand) {
    JsonParser jsonParser = new JsonParser();
    try {
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/" +
          gameJSON +
          "/" +
          gameJSON +
          "items.json"
        )
      );

      for (String keyIterator : (Iterable<String>) jsonObject.keySet()) {
        if (keyIterator.equalsIgnoreCase(giveCommand)) {
          JsonArray jsonArray = (JsonArray) jsonObject.get(keyIterator);
          long tempCost = (long) jsonArray.get(0).getAsLong();
          itemCost = (int) tempCost;
          itemsMultiplied =
            giveCommand.startsWith("ammo_") ||
            giveCommand.startsWith("resource_");
          itemID = (String) jsonArray.get(1).getAsString();

          if (jsonArray.size() == 3) {
            pluginToLookFor = (String) jsonArray.get(2).getAsString();
          } else pluginToLookFor = defaultPlugin;
          return true;
        }
      }
      return false;
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
    return false;
  }
}
