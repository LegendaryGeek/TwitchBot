package TwitchBot.app.Fallout4Twitch.JSON;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PointLogic {

  Gson gson = new Gson();

  //Verify that a user/viewer exists.
  public static boolean userExists(String username) {
    JsonParser jsonParser = new JsonParser();
    try {
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/json_viewers.json"
        )
      );
      return jsonObject.has(username.toLowerCase());
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  //Modify the points o the specified user/viewer.
  public static void userPointModifier(String username, int pointsToModify) {
    JsonParser jsonParser = new JsonParser();
    try {
      Gson gson = new Gson();
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/json_viewers.json"
        )
      );
      if (jsonObject.has(username.toLowerCase())) {
        long oldPoints = (Long) jsonObject
          .get(username.toLowerCase())
          .getAsLong();
        int newPoints = (int) oldPoints + pointsToModify;
        jsonObject.addProperty(username.toLowerCase(), newPoints);
        FileWriter fileWriter = new FileWriter(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/json_viewers.json"
        );
        fileWriter.write(gson.toJson(jsonObject));
        fileWriter.close();
      }
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
  }

  //Return points of the specified user/viewer as a int.
  public static int getUserPoints(String username) {
    JsonParser jsonParser = new JsonParser();
    try {
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/json_viewers.json"
        )
      );
      if (jsonObject.has(username)) {
        long tempValue = (Long) jsonObject.get(username).getAsLong();
        return (int) tempValue;
      } else {
        addUser(username);
        return -1;
      }
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
    return -1;
  }

  //Add a specified user/viewer to the JSON
  public static void addUser(String username) {
    JsonParser jsonParser = new JsonParser();
    try {
      Gson gson = new Gson();
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/json_viewers.json"
        )
      );
      if (!jsonObject.has(username.toLowerCase())) {
        jsonObject.addProperty(username.toLowerCase(), 0);
        FileWriter fileWriter = new FileWriter(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/json_viewers.json"
        );
        fileWriter.write(gson.toJson(jsonObject));
        fileWriter.close();
      }
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
  }

  public static void purgeJSON(int cutOffPoint) {
    JsonParser jsonParser = new JsonParser();
    try {
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/json_viewers.json"
        )
      );
      for (Object object : jsonObject.keySet()) {
        String currentViewer = (String) object;
        long userPoints = (long) jsonObject.get(currentViewer).getAsLong();
        if (userPoints < cutOffPoint) {
          removeUser(currentViewer);
        }
      }
    } catch (JsonParseException | IOException e) {
      e.printStackTrace();
    }
  }

  public static void removeUser(String username) {
    JsonParser jsonParser = new JsonParser();
    try {
      Gson gson = new Gson();
      JsonObject jsonObject = (JsonObject) jsonParser.parse(
        new FileReader(
          "E:/SteamLibrary/steamapps/common/Fallout 4/Data/bethesdatwitchintegration-v.1.5.0/twitchBot/data/json_viewers.json"
        )
      );
      for (String keyIterator : (Iterable<String>) jsonObject.keySet()) {
        if (keyIterator.equalsIgnoreCase(username)) {
          jsonObject.remove(username);
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
