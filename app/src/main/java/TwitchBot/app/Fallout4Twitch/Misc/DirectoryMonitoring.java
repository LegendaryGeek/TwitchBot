package TwitchBot.app.Fallout4Twitch.Misc;

import static TwitchBot.app.Fallout4Twitch.JSON.SettingsParser.*;

import TwitchBot.app.Fallout4Twitch.Commands.SpawnCommand;
import TwitchBot.app.Fallout4Twitch.JSON.PointLogic;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class DirectoryMonitoring extends Thread {

  public void run() {
    String documentsPath;

    if (customDocumentsPath.equalsIgnoreCase("default")) {
      documentsPath =
        System.getProperty("user.home") + File.separator + "Documents";
    } else {
      documentsPath = customDocumentsPath;
    }

    try (
      WatchService watchService = FileSystems.getDefault().newWatchService()
    ) {
      Path path = Paths.get(
        documentsPath + "/My Games/" + gamePath + "/TwitchIntegration"
      );
      path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
      while (true) {
        WatchKey watchKey = watchService.take();
        for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
          Path context = (Path) watchEvent.context();

          if (context.toString().equalsIgnoreCase("commandFeedback.txt")) {
            File file = new File(
              documentsPath +
              "/My Games/" +
              gamePath +
              "/TwitchIntegration/" +
              context.toString()
            );
            if (file.delete()) {
              String[] commandReturn = SpawnCommand.lastCommandFired.split(" ");
              PointLogic.userPointModifier(
                commandReturn[0],
                Integer.parseInt(commandReturn[1])
              );
            }
          }
        }
        boolean valid = watchKey.reset();
        if (!valid) {
          break;
        }
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
