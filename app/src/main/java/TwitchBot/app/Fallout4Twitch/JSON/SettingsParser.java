package TwitchBot.app.Fallout4Twitch.JSON;

import static TwitchBot.app.Fallout4Twitch.JSON.CommandValidation.defaultPlugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;

public class SettingsParser {

  // Util. Properties
  public static boolean SystemEnabled = false;
  public static boolean PointWhenDisabled = true;
  public static final String sharedPrefix = "#";
  public static String channelToJoin = "null";
  public static long subscriptionPoints = 0;
  public static long pointsPerMinute = 2;
  public static boolean pointWhenDisabled = true;
  public static long bitMultiplier = 0;
  public static String customDocumentsPath = "null";
  public static long randomspawncost = 0;

  // Command Properties
  public static long lastDefaultCommand = 0;
  public static long lastSpecialCommand = 0;
  public static long commandDelayMillis = 0;
  public static long lastCommandGlobal = 0;
  public static long specialCommandDelayMillis = 0;
  public static String gameJSON = "null";
  public static String gamePath = "null";

  public static void settingsParser() {
    //JsonParser jsonParser = new JsonParser();

    try {
      JsonObject jsonObject = JsonParser
        .parseReader(
          new FileReader(
            "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/settings.json"
          )
        )
        .getAsJsonObject();

      channelToJoin =
        (String) jsonObject.get("channelToConnectTo").getAsString();

      commandDelayMillis =
        (long) jsonObject.get("commandDelayMillis").getAsLong();
      specialCommandDelayMillis =
        (long) jsonObject.get("specialCommandDelayMillis").getAsLong();

      pointsPerMinute = (long) jsonObject.get("pointsPerMinute").getAsLong();
      pointWhenDisabled =
        (boolean) jsonObject.get("pointsWhenDisabled").getAsBoolean();

      subscriptionPoints =
        (long) jsonObject.get("subscriptionPoints").getAsLong();

      bitMultiplier = (long) jsonObject.get("bitMultiplier").getAsLong();

      customDocumentsPath =
        (String) jsonObject.get("customDocumentsPath").getAsString();

      randomspawncost = (long) jsonObject.get("randomSpawnCost").getAsLong();

      String gameMode = (String) jsonObject.get("gamemode").getAsString();

      switch (gameMode.toLowerCase()) {
        case "fallout4":
          gameJSON = "Fallout4";
          defaultPlugin = "Fallout4.esm";
          gamePath = "Fallout4/F4SE";
          break;
        case "skyrimle":
          gameJSON = "SkyrimLE";
          defaultPlugin = "Skyrim.esm";
          gamePath = "Skyrim/SKSE";
          break;
        case "skyrimse":
          gameJSON = "SkyrimSE";
          defaultPlugin = "Skyrim.esm";
          gamePath = "Skyrim Special Edition/SKSE";
          break;
        case "falloutnewvegas":
          gameJSON = "NewVegas";
          defaultPlugin = "FalloutNV.esm";
          gamePath = "FalloutNV/NVSE";
          break;
        case "fallout3":
          gameJSON = "Fallout3";
          defaultPlugin = "Fallout3.esm";
          gamePath = "Fallout3/FOSE";
          break;
        case "oblivion":
          gameJSON = "Oblivion";
          defaultPlugin = "Oblivion.esm";
          gamePath = "Oblivion/OBSE";
          break;
      }
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
  }
}
