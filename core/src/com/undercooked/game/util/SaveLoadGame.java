package com.undercooked.game.util;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.logic.GameLogic;

/**
 * A class that simply saves and loads a game.
 */
public class SaveLoadGame {

  /**
   * Saves the {@link JsonValue} produced by the
   * {@link GameLogic#serialise()} function.
   *
   * @param gameLogic {@link GameLogic} : The {@link GameLogic} to save.
   */
  public static void saveGame(GameLogic gameLogic) {
    JsonValue save = gameLogic.serialise();
    FileControl.saveJsonData("save.json", save);
  }

  /**
   * Loads the save {@link JsonValue} and returns it.
   *
   * @return {@link JsonValue} : The save data.
   */
  public static JsonValue loadGameJson() {
    return FileControl.loadJsonData("save.json");
  }

  /**
   * Loads a {@link JsonValue} of save data, and loads it into
   * a {@link GameLogic}.
   *
   * @param gameLogic {@link GameLogic} : The {@link GameLogic} to load to.
   * @param saveData  {@link JsonValue} : The save Json.
   */
  public static void loadGame(GameLogic gameLogic, JsonValue saveData) {
    gameLogic.deserialise(saveData);
  }
}
