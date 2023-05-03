package com.undercooked.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Helper class to store keys better in the inputs map.
 */
public class InputKey {
  /**
   * An array of the keys for this input.
   */
  Array<Integer> keys;
  /**
   * Whether the key is pressed or not.
   */
  boolean keyPressed;
  /**
   * Whether the key is just released or not.
   */
  boolean keyJustPressed;
  /**
   * Whether the key is released or not.
   */
  boolean keyReleased;
  /**
   * Whether the key is just released or not.
   */
  boolean keyJustReleased;
  boolean interaction;

  /**
   * Constructor, requiring whether the input key is an
   * interaction or not.
   *
   * @param interaction {@code boolean} : If the key is for an interaction
   *                    or not.
   */
  public InputKey(boolean interaction) {
    this.keys = new Array<>();
    this.interaction = interaction;
  }

  /**
   * Constructor, which defaults to the input not being
   * an interaction.
   */
  public InputKey() {
    this(false);
  }

  /**
   * Returns the number of keys that are assigned to this input.
   *
   * @return {@code int} : The number of keys in {@link #keys}.
   */
  public int size() {
    return keys.size;
  }

  /**
   * Returns a key in the {@link #keys} array.
   *
   * @param index {@code int} : The index to get.
   * @return {@code int} : The key value at the index requested.
   */
  public int getKey(int index) {
    return keys.get(index);
  }

  /**
   * Adds a key for this input.
   *
   * @param key {@code int} : The value of the key.
   */
  public void addKey(int key) {
    // Don't add the key if it's already there
    if (keys.contains(key, true)) {
      return;
    }
    // Add the key
    keys.add(key);
  }

  /**
   * Clears the array of {@link #keys}.
   */
  public void clear() {
    keys.clear();
  }

  /**
   * Returns if the input has was pressed or not during when
   * the input was last updated.
   *
   * @return {@code boolean} : Whether the input is currently pressed or not.
   * @see #isJustPressed()
   * @see #update()
   */
  public boolean isPressed() {
    return keyPressed;
  }

  /**
   * Returns if the input had just been pressed or not when
   * the input was last updated.
   *
   * @return {@code boolean} : Whether the input was just pressed or not.
   * @see #isPressed()
   * @see #update()
   */
  public boolean isJustPressed() {
    return keyJustPressed;
  }

  /**
   * Returns if the input has was released or not when
   * the input was last updated.
   *
   * @return {@code boolean} : Whether the input is currently released or not.
   * @see #isJustReleased()
   * @see #update()
   */
  public boolean isReleased() {
    return keyReleased;
  }

  /**
   * Returns if the input had just been released or not when
   * the input was last updated.
   *
   * @return {@code boolean} : Whether the input was just released or not.
   * @see #isReleased()
   * @see #update()
   */
  public boolean isJustReleased() {
    return keyJustReleased;
  }

  /**
   * Resets the different pressed and releasaed
   * variables to false.
   */
  public void resetKeys() {
    keyPressed = false;
    keyJustPressed = false;
    keyReleased = false;
    keyJustReleased = false;
  }

  /**
   * Updates the pressed and released variables
   * to be true and false depending on if they match
   * in {@link Gdx#input}.
   */
  public void update() {
    boolean beforePressed = keyPressed;
    resetKeys();
    for (int key : keys) {
      keyPressed = keyPressed || Gdx.input.isKeyPressed(key);
      keyJustPressed = keyJustPressed || Gdx.input.isKeyJustPressed(key);
    }
    // Check for released
    if (!keyPressed) {
      keyReleased = true;
      // Only just pressed if pressed before
      if (beforePressed) {
        keyJustReleased = true;
      }
    }
  }
}