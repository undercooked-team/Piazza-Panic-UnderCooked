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
   * The text form of the Easy value.
   */
  public static final String EASY_TEXT = "easy";

  /**
   * The value for Medium difficulty.
   */
  public static final int MEDIUM = 1;

  /**
   * The text form of the Medium value.
   */
  public static final String MEDIUM_TEXT = "medium";

  /**
   * The value for Hard difficulty.
   */
  public static final int HARD = 2;

  /**
   * The text form of the Hard value.
   */
  public static final String HARD_TEXT = "hard";

  /**
   * Converts a difficulty value into a string.
   *
   * @param difficulty {@code int} : The difficulty value.
   * @return {@link String} : The difficulty as a {@link String}.
   */
  public static String toString(int difficulty) {
    switch (difficulty) {
      case EASY:
        return EASY_TEXT;
      case MEDIUM:
        return MEDIUM_TEXT;
      case HARD:
        return HARD_TEXT;
      default:
        return "invalid";
    }
  }

  /**
   * Converts a difficulty string into a value.
   *
   * @param difficulty {@code int} : The difficulty string.
   * @return {@link int} : The difficulty value.
   */
  public static int asInt(String difficulty) {
    if (difficulty == null) {
      return -1;
    }
    switch (difficulty.toLowerCase()) {
      case EASY_TEXT:
        return EASY;
      case MEDIUM_TEXT:
        return MEDIUM;
      case HARD_TEXT:
        return HARD;
      default:
        return -1;
    }
  }
}
