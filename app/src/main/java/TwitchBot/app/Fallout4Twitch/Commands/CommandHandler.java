package TwitchBot.app.Fallout4Twitch.Commands;

import static TwitchBot.app.Fallout4Twitch.JSON.SettingsParser.*;

import TwitchBot.app.Fallout4Twitch.BethesdaTwitchIntegration;
import TwitchBot.app.Fallout4Twitch.JSON.PointLogic;
import TwitchBot.app.Fallout4Twitch.JSON.SettingsParser;
import TwitchBot.app.Fallout4Twitch.Misc.MiscLogic;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.tmi.domain.Chatters;

public class CommandHandler {

  public static void handleCommand(
    String username,
    String[] command,
    TwitchClient twitchClient
  ) {
    long currentTimeMillis = System.currentTimeMillis();
    String commandToValidate = command[0].toLowerCase().substring(1);

    //Checks with the impacted commands if the overhead system is flagged as enabled.
    if (!SystemEnabled) {
      switch (commandToValidate) {
        case "spawn":
        case "misc":
        case "give":
        case "weather":
        case "randomspawn":
          BethesdaTwitchIntegration.sendMessage(
            "Twitch Integration is not currently enabled."
          );
          return;
      }
    }

    /*
     * Low-Delay Command Time Verification
     */
    if (
      currentTimeMillis <= lastDefaultCommand + commandDelayMillis ||
      currentTimeMillis <= lastCommandGlobal + 250
    ) {
      switch (commandToValidate) {
        case "spawn":
        case "give":
        case "randomspawn":
          BethesdaTwitchIntegration.sendMessage(
            "Too many commands, please wait a bit."
          );
          return;
      }
    }

    /*
     * High-Delay Command Time Verification
     */
    if (
      currentTimeMillis <= lastSpecialCommand + specialCommandDelayMillis ||
      currentTimeMillis <= lastCommandGlobal + 250
    ) {
      switch (commandToValidate) {
        case "weather":
        case "misc":
          BethesdaTwitchIntegration.sendMessage(
            "Too many commands, please wait a bit."
          );
          return;
      }
    }

    switch (commandToValidate) {
      /*
       * "Utility" Commands
       */

      case "reload": //Reloads all settings
        if (command.length == 1) {
          if (higherPermissions(twitchClient, username)) {
            SettingsParser.settingsParser();
            MiscLogic.startCleanup();
          }
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }
      case "addpointsall": //Add points to all current viewers.
        if (command.length == 2) {
          if (higherPermissions(twitchClient, username)) {
            if (MiscLogic.isInteger(command[1])) {
              Chatters chatters = twitchClient
                .getMessagingInterface()
                .getChatters(channelToJoin.toLowerCase())
                .execute();
              for (String viewer : chatters.getAllViewers()) {
                PointLogic.userPointModifier(
                  viewer,
                  Integer.parseInt(command[1])
                );
              }
            } else {
              BethesdaTwitchIntegration.sendInvalidCommand(username);
            }
          }
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }

        break;
      case "addpoints": //Add points to a specific viewer,
        if (command.length == 3) {
          if (higherPermissions(twitchClient, username)) {
            if (
              PointLogic.userExists(command[1]) &&
              MiscLogic.isInteger(command[2])
            ) {
              if (username.equalsIgnoreCase(command[1])) {
                BethesdaTwitchIntegration.sendMessage(
                  "You can't add points to yourself @" + username
                );
              } else {
                PointLogic.userPointModifier(
                  username,
                  Integer.parseInt(command[2])
                );
              }
            } else {
              BethesdaTwitchIntegration.sendInvalidCommand(username);
            }
          }
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }

        break;
      case "points": //Allows users to check their current point amount.
        if (command.length == 1) {
          int tempValue = PointLogic.getUserPoints(username);
          if (tempValue >= 0) {
            BethesdaTwitchIntegration.sendMessage(
              "You have " + tempValue + " points @" + username
            );
          } else {
            BethesdaTwitchIntegration.sendMessage(
              "You have 0 points @" + username
            );
          }
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }

        break;
      case "givepoints":
        if (command.length == 3) {
          if (
            PointLogic.userExists(command[1].toLowerCase()) &&
            MiscLogic.isInteger(command[2]) &&
            !command[1].equalsIgnoreCase(username)
          ) {
            if (
              PointLogic.getUserPoints(username) >= Integer.parseInt(command[2])
            ) {
              PointLogic.userPointModifier(
                username,
                -Integer.parseInt(command[2])
              );
              PointLogic.userPointModifier(
                command[1].toLowerCase(),
                Integer.parseInt(command[2])
              );
              BethesdaTwitchIntegration.sendMessage(
                "@" +
                username +
                " gave " +
                command[2] +
                " points to @" +
                command[1].toLowerCase()
              );
            } else {
              BethesdaTwitchIntegration.sendMissingPoints(username);
            }
          } else {
            BethesdaTwitchIntegration.sendInvalidCommand(username);
          }
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }

        break;
      case "purge":
        if (command.length == 1) {
          if (higherPermissions(twitchClient, username)) {
            if (MiscLogic.isInteger(command[1])) {
              PointLogic.purgeJSON(Integer.parseInt(command[1]));
            } else {
              BethesdaTwitchIntegration.sendInvalidCommand(username);
            }
          } else {
            BethesdaTwitchIntegration.sendInvalidCommand(username);
          }
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }

        break;
      case "toggle":
        if (command.length == 1) {
          if (higherPermissions(twitchClient, username)) {
            SystemEnabled = !SystemEnabled;
            if (SystemEnabled) {
              BethesdaTwitchIntegration.sendMessage(
                "Enabled Twitch Integration."
              );
            } else {
              BethesdaTwitchIntegration.sendMessage(
                "Disabled Twitch Integration."
              );
            }
          } else {
            BethesdaTwitchIntegration.sendInvalidCommand(username);
          }
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }

        break;
      case "tada":
        if (username.equalsIgnoreCase("Flenarn_")) {
          BethesdaTwitchIntegration.sendMessage(
            "Hey, look everyone! It's @flenarn_, he developed this!"
          );
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }
        break;
      /*
       * Interaction Commands
       */

      case "spawn":
        if (command.length == 2) {
          SpawnCommand.handleSpawn(username, command[1]);
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }
        break;
      case "misc":
        if (command.length == 2) {
          MiscCommand.handleMisc(username, command[1]);
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }
        System.out.println("#misc command called.");
        break;
      case "give":
        if (command.length == 3) {
          int amount;
          if (MiscLogic.isInteger(command[2])) {
            amount = Integer.parseInt(command[2]);
          } else {
            BethesdaTwitchIntegration.sendInvalidCommand(username);
            return;
          }

          if (amount > 1000) {
            BethesdaTwitchIntegration.sendMessage(
              "Max amount to give is 1000 @" + username
            );
          } else {
            GiveCommand.handleGive(username, command[1], amount);
          }
        } else if (command.length == 2) {
          GiveCommand.handleGive(username, command[1], 1);
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }
        break;
      case "randomspawn":
        if (command.length == 1) {
          if (PointLogic.getUserPoints(username) < randomspawncost) {
            BethesdaTwitchIntegration.sendMissingPoints(username);
          } else {
            RandomSpawnCommand.handleRandomSpawn(username);
          }
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }

        break;
      case "weather":
        if (command.length == 2) {
          WeatherCommand.handleWeather(username, command[1]);
        } else {
          BethesdaTwitchIntegration.sendInvalidCommand(username);
        }
        break;
      default:
        BethesdaTwitchIntegration.sendMessage("Invalid command @" + username);
        break;
    }
  }

  /*
   * Basic permission check, so command-caller is mod/admin or the streamer.
   */
  public static boolean higherPermissions(
    TwitchClient twitchClient,
    String username
  ) {
    Chatters chatters = twitchClient
      .getMessagingInterface()
      .getChatters(channelToJoin.toLowerCase())
      .execute();
    if (
      chatters.getModerators().contains(username) ||
      chatters.getAdmins().contains(username) ||
      username.equalsIgnoreCase(channelToJoin)
    ) {
      return true;
    } else {
      BethesdaTwitchIntegration.sendInvalidCommand(username);
      return false;
    }
  }
}
