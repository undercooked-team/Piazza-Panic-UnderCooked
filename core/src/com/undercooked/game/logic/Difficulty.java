package com.undercooked.game.logic;

/**
 * A class that contains the different difficulties of the game,
 * and provides functions for changing them between integers and strings.
 */
public class Difficulty {
  /**
   * The value for Easy difficulty.
   */
  public static final int EASY = 0;

  /**
   * The value for Medium difficulty.
   */
  public static final int MEDIUM = 1;

  /**
   * The value for Hard difficulty.
   */
  public static final int HARD = 2;

  /**
   * Converts a difficulty value into a string.
   *
   * @param difficulty {@code int} : The difficulty value.
   * @return {@link String} : The difficulty as a {@link String}.
   */
  public static String toString(int difficulty) {
    switch (difficulty) {
      case EASY:
        return "easy";
      case MEDIUM:
        return "medium";
      case HARD:
        return "hard";
      default:
        return "default";
    }
  }

  /**
   * Converts a difficulty string into a value.
   *
   * @param difficulty {@code int} : The difficulty string.
   * @return {@link int} : The difficulty value.
   */
  public static int asInt(String difficulty) {
    switch (difficulty) {
      case "easy":
        return EASY;
      case "medium":
        return MEDIUM;
      case "hard":
        return HARD;
      default:
        return -1;
    }
  }
}
