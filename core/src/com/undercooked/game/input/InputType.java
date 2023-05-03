package com.undercooked.game.input;

/**
 * The different types of inputs that can be done.
 */
public enum InputType {
  /**
   * The input is being held down.
   */
  PRESSED,
  /**
   * The input has <b>just</b> been held down.
   */
  JUST_PRESSED,
  /**
   * The input is not being held down.
   */
  RELEASED,
  /**
   * The input has <b>just</b> stopped being held down.
   */
  JUST_RELEASED
}
