package TwitchBot.app.Fallout4Twitch.JSON;

import static TwitchBot.app.Fallout4Twitch.JSON.SettingsParser.*;

import TwitchBot.app.Fallout4Twitch.BethesdaTwitchIntegration;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TimedLogic extends BethesdaTwitchIntegration {

  Gson gson = new Gson();

  public static void defaultPointLoop(List<String> viewers) {
    for (String viewerToModify : viewers) {
      PointLogic.userPointModifier(viewerToModify, (int) pointsPerMinute);
    }
  }

  public static void userLogger(List<String> viewers) {
    JsonParser jsonParser = new JsonParser();
    try {
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/json_viewers.json"
        )
      );
      for (String viewerToAdd : viewers) {
        if (!jsonObject.has(viewerToAdd)) {
          Gson gson = new Gson();
          jsonObject.addProperty(viewerToAdd, 0);
          FileWriter fileWriter = new FileWriter(
            "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/json_viewers.json"
          );
          fileWriter.write(gson.toJson(jsonObject));
          fileWriter.close();
        }
      }
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
  }
}
