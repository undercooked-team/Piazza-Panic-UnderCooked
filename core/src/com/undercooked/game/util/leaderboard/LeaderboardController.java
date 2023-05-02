package com.undercooked.game.util.leaderboard;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.GameType;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.StringUtil;
import com.undercooked.game.util.json.JsonFormat;
import java.util.Comparator;

/**
 * Use this static class to manipulate the leaderboards using the enum
 * LeaderboardNames.
 * <br><br>
 * {@link #loadLeaderboard()} must be called before any of the functions
 * for interacting with the leaderboards in this class can be used.
 * Otherwise, they will all fail.
 */
public final class LeaderboardController {

  /**
   * Converts one of the {@link GameType} to a {@link String}
   * value. This is the id of them inside the json file.
   *
   * @param gameType {@link GameType} : The name to get.
   * @return {@link String} : The name of the leaderboard.
   */
  public static String getLeaderboardId(GameType gameType) {
    // If it's null, return null
    if (gameType == null) {
      return "null";
    }

    // Otherwise, return the name
    switch (gameType) {
      case ENDLESS:
        return "endless";
      case SCENARIO:
        return "scenarios";
      default:
        // Shouldn't reach here
        return "error";
    }
  }

  /**
   * Converts one of the {@link GameType} to a {@link String}
   * value. This is the name to display them as.
   *
   * @param gameType {@link GameType} : The name to get.
   * @return {@link String} : The name of the leaderboard.
   */
  public static String getLeaderboardName(GameType gameType) {
    // If it's null, return null
    if (gameType == null) {
      return "null";
    }

    // Otherwise, return the name
    switch (gameType) {
      case ENDLESS:
        return "Endless";
      case SCENARIO:
        return "Scenario";
      default:
        // Shouldn't reach here
        return "error";
    }
  }

  /**
   * Converts a score value to a {@link String} formatted based
   * on what {@link GameType} it should use.
   *
   * @param leaderboardType {@link GameType} : The leaderboard to use.
   * @param score        {@code float} : The score to get a {@link String} of.
   * @return {@link String} : The score as a {@link String}.
   */
  public static String scoreToString(GameType leaderboardType, float score) {
    if (leaderboardType == null) {
      return Float.toString(score);
    }

    switch (leaderboardType) {
      case SCENARIO:
        return StringUtil.formatSeconds(score, 2);
      case ENDLESS:
        return Integer.toString((int) score);
      default:
        // Shouldn't reach here
        return "error";
    }
  }

  /**
   * Comparator for the Scenario mode scores.
   */
  private static final Comparator<LeaderboardEntry> scenarioComparator =
      new Comparator<LeaderboardEntry>() {
          @Override
          public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
            // score being lower is better
            if (o1.score < o2.score) {
              return 1;
            } else if (o2.score < o1.score) {
              return -1;
            }
            return 0;
          }
      };

  /**
   * Comparator for the Endless mode scores.
   */
  private static final Comparator<LeaderboardEntry> endlessComparator =
      new Comparator<LeaderboardEntry>() {
          @Override
          public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
              // score being lower is better
              if (o1.score > o2.score) {
                return 1;
              } else if (o2.score > o1.score) {
                return -1;
              }
              return 0;
          }
      };

  /**
   * Returns a comparator of the scores based on {@link GameType}.
   * This is because Scenarios favours lower time taken, while Endless mode
   * favours higher customers served.
   * <br>0 means they are equal.
   * <br>1 means the left side is better.
   * <br>-1 means the right side is better.
   *
   * @param gameType {@link GameType} : The leaderboard to use.
   * @return {@link Comparator}&lt;{@link LeaderboardEntry}&gt;
   *                    : The {@link Comparator} for the scores of the {@link GameType}.
   */
  public static Comparator<LeaderboardEntry> getScoreComparator(GameType gameType) {
    if (gameType == null) {
      return new Comparator<LeaderboardEntry>() {
        @Override
        public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
          return 0;
        }
      };
    }

    switch (gameType) {
      case SCENARIO:
        return scenarioComparator;

      case ENDLESS:
        return endlessComparator;

      default:
        // Shouldn't reach here
        return null;
    }
  }

  /**
   * Returns a comparison of the scores based on {@link GameType}.
   * This is because Scenarios favours lower time taken, while Endless mode
   * favours higher customers served.
   *
   * @param gameType {@link GameType} : The leaderboard to check.
   * @param score1   {@link float} : The score of the first entry.
   * @param score2   {@link float} : The score of the second entry.
   * @return {@link int} : 1 if {@code score1} is better,
   *                       0 if they are the same,
   *                       -1 if {@code score2} is better.
   */
  public static int compareScore(GameType gameType, float score1, float score2) {
    if (gameType == null) {
      return 0;
    }

    switch (gameType) {
      case SCENARIO:
        // score being lower is better
        if (score1 < score2) {
          return 1;
        } else if (score2 < score1) {
          return -1;
        }
        return 0;

      case ENDLESS:
        // score being higher is better
        if (score1 > score2) {
          return 1;
        } else if (score2 > score1) {
          return -1;
        }
        return 0;
      default:
        // Shouldn't reach here
        return 0;
    }
  }

  /**
   * Root JsonValue of Leaderboard. Contains:
   * <ul>
   * <li>"Example": "leaderboard",
   * <li>"SCENARIO": [...],<br>
   * <li>"ENDLESS": [...]
   * </ul>
   */
  private static final ObjectMap<GameType, ObjectMap<String, Leaderboard>> leaderboardData =
          new ObjectMap<>();

  /**
   * The file location of the leaderboard file in the data folder.
   */
  private static final String leaderboardFile = "leaderboard.json";

  /**
   * Whether the leaderboards are loaded or not.
   */
  private static boolean loaded = false;

  /**
   * Loads the leaderboard.
   * <p>
   * This method MUST be called before any other method in this class.
   * </p>
   */
  public static void loadLeaderboard() {
    // If leaderboardData is not null, then unload it first
    unloadLeaderboard();

    JsonValue root = FileControl.loadJsonData(leaderboardFile);
    // Create leaderboard.json if it doesn't exist
    if (root == null) {
      // Create a new JsonValue, and then format it using the leaderboard format
      root = new JsonValue(JsonValue.ValueType.object);
    }
    // Format the Json
    JsonFormat.formatJson(root, DefaultJson.leaderboardFormat());

    // Load it into the ObjectMap.
    for (GameType leaderboardType : GameType.values()) {
      // Add the ObjectMap for the type
      ObjectMap<String, Leaderboard> newObjectMap = new ObjectMap<>();
      leaderboardData.put(leaderboardType, newObjectMap);
      // Then, copy all the leaderboard values over
      for (JsonValue leaderboard : root.get(getLeaderboardId(leaderboardType))) {
        // If the leaderboard id is already in the ObjectMap, then skip
        if (newObjectMap.containsKey(leaderboard.getString("id"))) {
          continue;
        }
        // Otherwise, make the leaderboard
        Leaderboard newLeaderboard = new Leaderboard();
        newLeaderboard.name = leaderboard.getString("name");
        // Set the comparator
        newLeaderboard.setComparator(getScoreComparator(leaderboardType));
        // And add it to the leaderboard to the leaderboard type
        newObjectMap.put(leaderboard.getString("id"), newLeaderboard);

        // Now loop through the entries and add them
        for (JsonValue entry : leaderboard.get("scores")) {
          LeaderboardEntry newEntry = newLeaderboard.addLeaderboardEntry(
                  entry.getString("name"),
                  entry.getFloat("score")
          );

          // Make sure to set the date of the entry
          newEntry.setDate(entry.getString("date"));
        }
      }
    }
    loaded = true;
  }

  /**
   * Unloads the leaderboard, if it is no longer needed.
   * <br>
   * If the leaderboard is not needed, such as when the game
   * is running, then this function can be run and the leaderboard
   * entries will be deleted by the garbage collector.
   */
  public static void unloadLeaderboard() {
    leaderboardData.clear();
    loaded = false;
  }

  /**
   * Adds a {@link LeaderboardEntry} to the {@link Leaderboard} specified.
   *
   * @param leaderboardType {@link GameType} : The type of {@link Leaderboard} to save the score to.
   * @param id              {@link String} : The id of the {@link Leaderboard}.
   * @param leaderboardName {@link String} : The name to use for the {@link Leaderboard} if
   *                        it doesn't have one already.
   * @param name            {@link String} : The name of the {@link LeaderboardEntry}.
   * @param score           {@code float} : The score of the {@link LeaderboardEntry}.
   */
  public static void addEntry(GameType leaderboardType, String id,
                              String leaderboardName, String name,
                              float score) {
    // Get the leaderboard by id
    Leaderboard leaderboard = getLeaderboard(leaderboardType, id);
    // If it's null, then add the id
    if (leaderboard == null) {
      leaderboard = addLeaderboard(leaderboardType, leaderboardName, id);
    }

    // Add it to the leaderboard
    leaderboard.addLeaderboardEntry(name, score);
  }

  private static Leaderboard addLeaderboard(GameType leaderboardType, String name, String id) {
    // First, get the leaderboards
    ObjectMap<String, Leaderboard> leaderboards = getLeaderboards(leaderboardType);
    // If it exists, just ignore
    if (leaderboards.containsKey(id)) {
      return leaderboards.get(id);
    }

    // If it doesn't exist, create it
    Leaderboard newLeaderboard = new Leaderboard();
    newLeaderboard.name = name;
    newLeaderboard.setComparator(getScoreComparator(leaderboardType));
    // Add it to the ObjectMap
    leaderboards.put(id, newLeaderboard);
    // And return the new leaderboard that was created
    return newLeaderboard;
  }

  /**
   * Returns the sorted scores of a leaderboard using the id provided.
   *
   * @param leaderboardType {@link GameType} : The leaderboard to use.
   * @param id    {@link String} : The id of the leaderboard.
   * @return {@link Array}&lt;{@link LeaderboardEntry}&gt;
   *                                : An {@link Array} of the {@link LeaderboardEntry}s for
   *                                  the requested {@link Leaderboard}.
   */
  public static Array<LeaderboardEntry> getEntries(GameType leaderboardType, String id) {
    // Get the leaderboard by id
    Leaderboard leaderboard = getLeaderboard(leaderboardType, id);
    // Only continue if it's not null
    if (leaderboard == null) {
      return null;
    }

    // Now return a copy of the array
    return leaderboard.copyLeaderboard();
  }

  /**
   * Remove a {@link LeaderboardEntry} from a {@link Leaderboard}.
   *
   * @param leaderboardType {@link GameType} : The type of the {@link Leaderboard}.
   * @param id    {@link String} : The id of the {@link Leaderboard}.
   * @param index {@code int} : The index of the {@link LeaderboardEntry}.
   * @return {@code boolean} : {@code true} if it was deleted successfully,
   *                           {@code false} if not.
   */
  public static boolean removeEntry(GameType leaderboardType, String id, int index) {
    // Get the leaderboard by id
    Leaderboard leaderboard = getLeaderboard(leaderboardType, id);
    // Only continue if it's not null
    if (leaderboard == null) {
      return false;
    }

    // Check that the index is a valid number
    if (index < 0 || index >= leaderboard.size()) {
      return false;
    }

    // Then, remove it
    leaderboard.remove(index);

    // Return true, as it was successfully removed
    return true;
  }

  /**
   * Remove all {@link LeaderboardEntry} from a {@link Leaderboard}
   * that have a matching name.
   *
   * @param leaderboardType {@link GameType} : The type of the {@link Leaderboard}.
   * @param id    {@link String} : The id of the {@link Leaderboard}.
   * @param name  {@link String} : The name in the {@link LeaderboardEntry}.
   * @return {@code int} : The number of {@link LeaderboardEntry}s that were removed.
   */
  public static int removeEntry(GameType leaderboardType, String id, String name) {
    // Get the leaderboard by id
    Leaderboard leaderboard = getLeaderboard(leaderboardType, id);
    // Only continue if it's not null
    if (leaderboard == null) {
      return -1;
    }

    // Tell the leaderboard to remove the entry
    return leaderboard.removeEntry(name);
  }

  /**
   * Get a {@link Leaderboard} by its {@link GameType} and id.
   *
   * @param leaderboardType {@link GameType} : The type of the {@link Leaderboard}.
   * @param id    {@link String} : The id of the {@link Leaderboard}.
   * @return {@link Leaderboard} : The {@link Leaderboard} matching the type and id.
   */
  public static Leaderboard getLeaderboard(GameType leaderboardType, String id) {
    // Get the array of entries
    ObjectMap<String, Leaderboard> leaderboards = getLeaderboards(leaderboardType);
    // If the id doesn't exist, add it if addIfNull is true
    if (!leaderboards.containsKey(id)) {
      return addLeaderboard(leaderboardType, "missing name", id);
    }
    // Then return the leaderboard
    return leaderboards.get(id);
  }

  /**
   * Returns all of the {@link Leaderboard}s of the {@link GameType}.
   *
   * @param leaderboardType {@link GameType} : The {@link Leaderboard}s' type.
   * @return {@link ObjectMap}&lt;{@link String}, {@link Leaderboard}&gt;
   *                    : The mapping of {@link Leaderboard}s and their ids.
   */
  private static ObjectMap<String, Leaderboard> getLeaderboards(GameType leaderboardType) {
    // If the leaderboard type doesn't exist, return null
    if (!leaderboardData.containsKey(leaderboardType)) {
      ObjectMap<String, Leaderboard> newLeaderboards = new ObjectMap<>();
      leaderboardData.put(leaderboardType, newLeaderboards);
      return newLeaderboards;
    }
    // Return the leaderboards
    return leaderboardData.get(leaderboardType);
  }

  /**
   * Returns all of the {@link Leaderboard} ids of the {@link GameType}.
   *
   * @param gameType {@link GameType} : The {@link Leaderboard}s' type.
   * @return {@link Array}&lt;{@link String}&gt;
   *                    : An {@link Array} of the {@link Leaderboard}s' ids.
   */
  public static Array<String> getIds(GameType gameType) {
    // If the leaderboard type doesn't exist, return null
    ObjectMap<String, Leaderboard> leaderboards = getLeaderboards(gameType);
    if (leaderboards == null) {
      return null;
    }

    // Return an array of the keys
    return leaderboards.keys().toArray();
  }

  /**
   * Saves the leaderboard data to the leaderboard file in the data folder.
   */
  public static void saveLeaderboard() {
    // Convert all the data into a JsonValue
    FileControl.saveJsonData(leaderboardFile, asJsonValue());
  }

  /**
   * Converts the leaderboard data into a {@link JsonValue}.
   *
   * @return {@link JsonValue} : The leaderboard data in Json form.
   */
  public static JsonValue asJsonValue() {
    JsonValue root = new JsonValue(JsonValue.ValueType.object);
    // Loop through each of the keys in the object map
    for (GameType leaderboardType : leaderboardData.keys()) {
      // Add the type
      JsonValue leaderboards = new JsonValue(JsonValue.ValueType.array);
      root.addChild(getLeaderboardId(leaderboardType), leaderboards);
      // Then loop through the children
      for (String leaderboardId : leaderboardData.get(leaderboardType).keys()) {
        // Make the JsonValue for it
        Leaderboard leaderboard = leaderboardData.get(leaderboardType).get(leaderboardId);
        JsonValue leaderboardJson = new JsonValue(JsonValue.ValueType.object);
        leaderboardJson.addChild("id", new JsonValue(leaderboardId));
        leaderboardJson.addChild("name", new JsonValue(leaderboard.name));

        JsonValue scores = new JsonValue(JsonValue.ValueType.array);
        leaderboardJson.addChild("scores", scores);

        leaderboards.addChild(leaderboardJson);
        int index = 0;
        // Then get the scores and loop through them
        for (LeaderboardEntry entry : leaderboard.getLeaderboard()) {
          JsonValue entryJson = new JsonValue(JsonValue.ValueType.object);
          entryJson.addChild("name", new JsonValue(entry.name));
          entryJson.addChild("score", new JsonValue(entry.score));
          entryJson.addChild("date", new JsonValue(entry.dateAsString()));
          // And add it to the leaderboard
          scores.addChild(Integer.toString(index), entryJson);
          index++;
        }
      }
    }
    return root;
  }

  /**
   * Returns whether the leaderboard data is loaded or not.
   *
   * @return {@code boolean} : {@code true} if the leaderboard data is loaded,
   *                           {@code false} if not.
   */
  public static boolean isLoaded() {
    return loaded;
  }
}
