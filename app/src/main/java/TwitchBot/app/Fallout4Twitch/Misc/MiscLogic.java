package TwitchBot.app.Fallout4Twitch.Misc;

import static TwitchBot.app.Fallout4Twitch.JSON.SettingsParser.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MiscLogic {

  /*
   * Check if string value is a valid integer value.
   */

  public static boolean isInteger(String string) {
    if (string == null) {
      return false;
    }
    int length = string.length();
    if (length == 0) {
      return false;
    }

    int i = 0;
    if (string.charAt(0) == '.') {
      if (length == 1) {
        return false;
      }
      i = 1;
    }
    for (; i < length; i++) {
      char c = string.charAt(i);
      if (c < '0' || c > '9') {
        return false;
      }
    }
    return true;
  }

  /*
   * Creates a command for xSE to detect and execute.
   */
  public static void createCommandCall(String command) {
    String documentsPath;

    if (customDocumentsPath.equalsIgnoreCase("default")) {
      documentsPath =
        System.getProperty("user.home") + File.separator + "Documents";
    } else {
      documentsPath = customDocumentsPath;
    }

    try (
      PrintWriter out = new PrintWriter(
        documentsPath +
        "/My Games/" +
        gamePath +
        "/TwitchIntegration/command.txt"
      )
    ) {
      out.println(command);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void startCleanup() {
    lastDefaultCommand = System.currentTimeMillis() - commandDelayMillis;
    lastSpecialCommand = System.currentTimeMillis() - specialCommandDelayMillis;
    lastCommandGlobal = System.currentTimeMillis() - 250;

    String documentsPath;

    if (customDocumentsPath.equalsIgnoreCase("default")) {
      documentsPath =
        System.getProperty("user.home") + File.separator + "Documents";
    } else {
      documentsPath = customDocumentsPath;
    }

    try {
      File command = new File(
        documentsPath +
        "/My Games/" +
        gamePath +
        "/TwitchIntegration/command.txt"
      );
      File commandFeedback = new File(
        documentsPath +
        "/My Games/" +
        gamePath +
        "/TwitchIntegration/commandFeedback.txt"
      );
      command.delete();
      commandFeedback.delete();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
