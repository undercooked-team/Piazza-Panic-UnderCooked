package com.undercooked.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.StringUtil;
import com.undercooked.game.util.json.JsonArray;
import com.undercooked.game.util.json.JsonBool;
import com.undercooked.game.util.json.JsonFormat;
import com.undercooked.game.util.json.JsonObject;
import com.undercooked.game.util.json.JsonString;

/**
 * A class composing of static functions, with no constructor,
 * to easily allow checking of inputs throughout anywhere in the game.
 */
public class InputController {

  /**
   * A map of the input ids to their {@link InputKey}.
   */
  static final ObjectMap<String, InputKey> inputs = new ObjectMap<>();

  /**
   * Returns all the mappings of input ids to their {@link InputKey}s.
   *
   * @return {@link ObjectMap}&lt;{@link String},{@link InputKey}@gt; : The {@link #inputs}.
   */
  public static ObjectMap<String, InputKey> getInputs() {
    return inputs;
  }

  /**
   * Links a new key to a key id.
   *
   * @param inputId {@link String} : The key's id.
   * @param newKey  {@code int} : The key value identifier.
   */
  public static void addKey(String inputId, int newKey) {
    // First check that the key actually exists in the inputs map
    if (!inputs.containsKey(inputId)) {
      // If it doesn't, add it.
      inputs.put(inputId, new InputKey());
    }
    // Add the key
    inputs.get(inputId).addKey(newKey);
  }

  /**
   * Set whether a certain input id is an interaction
   * key or not.
   *
   * @param inputId     {@link String} : The key's id.
   * @param interaction {@code boolean} : Whether the key is an
   *                    interaction or not.
   */
  public static void setInteraction(String inputId, boolean interaction) {
    // First check that the key actually exists in the inputs map
    if (!inputs.containsKey(inputId)) {
      // If it doesn't, add it.
      inputs.put(inputId, new InputKey());
    }
    // Set the interaction value
    inputs.get(inputId).interaction = interaction;
  }

  /**
   * Returns whether a certain key is an interaction or not.
   *
   * @param inputId {@link String} : The key's id.
   * @return {@code boolean} : If the key id is ({@code true}) or
   *                           isn't ({@code false}) an interaction.
   */
  public static boolean isInteraction(String inputId) {
    // First check that the key actually exists in the inputs map
    if (!inputs.containsKey(inputId)) {
      // If it doesn't, return false.
      return false;
    }
    // Return the interaction value
    return inputs.get(inputId).interaction;
  }

  /**
   * Updates all of the {@link InputKey}s in the
   * {@link #inputs}.
   */
  public static void updateKeys() {
    for (InputKey key : inputs.values()) {
      key.update();
    }
  }

  /**
   * Returns whether the input is being pressed or not.
   *
   * @param inputId {@link String} : The key's id.
   * @return {@code boolean} : {@code true} if the key is being pressed,
   *                           {@code false} false if not.
   */
  public static boolean isInputPressed(String inputId) {
    if (!inputExists(inputId)) {
      return false;
    }

    return inputs.get(inputId).isPressed();
  }

  /**
   * Returns whether the input was just pressed or not.
   *
   * @param inputId {@link String} : The key's id.
   * @return {@code boolean} : {@code true} if the key was just pressed,
   *                           {@code false} false if not.
   */
  public static boolean isInputJustPressed(String inputId) {
    if (!inputExists(inputId)) {
      return false;
    }

    return inputs.get(inputId).isJustPressed();
  }

  /**
   * Returns whether the input is released or not.
   *
   * @param inputId {@link String} : The key's id.
   * @return {@code boolean} : {@code true} if the key is released,
   *                           {@code false} false if not.
   */
  public static boolean isInputReleased(String inputId) {
    if (!inputExists(inputId)) {
      return false;
    }

    return inputs.get(inputId).isReleased();
  }

  /**
   * Returns whether the input has just been released or not.
   *
   * @param inputId {@link String} : The input's id.
   * @return {@code boolean} : {@code true} if the key was just released,
   *                           {@code false} false if not.
   */
  public static boolean isInputJustReleased(String inputId) {
    if (!inputExists(inputId)) {
      return false;
    }

    return inputs.get(inputId).isJustReleased();
  }

  /**
   * Returns whether a key is being given a certain {@link InputType}.
   *
   * @param inputId   {@link String} : The key's id.
   * @param inputType {@link InputType} : The type of input to check for.
   * @return {@code boolean} : {@code true} if the key has an input of {@code inputType},
   *                           {@code false} if not.
   */
  public static boolean isInput(String inputId, InputType inputType) {
    switch (inputType) {
      case PRESSED:
        return isInputPressed(inputId);
      case JUST_PRESSED:
        return isInputJustPressed(inputId);
      case RELEASED:
        return isInputReleased(inputId);
      case JUST_RELEASED:
        return isInputJustReleased(inputId);
      default:
        return false;
    }
  }

  /**
   * Returns whether an input id exists or not.
   *
   * @param inputId {@link String} : The input's id.
   * @return {@code boolean} : {@code true} if the input exists,
   *                           {@code false} if not.
   */
  public static boolean inputExists(String inputId) {
    return inputs.containsKey(inputId);
  }

  /**
   * Returns the {@link Input.Keys} value of the key name provided.
   *
   * @param keyName {@link String} : The name of the key.
   * @return {@code int} : The value of the key.
   */
  public static int getKeyVal(String keyName) {
    // Convert to title case, as that is what LibGDX uses
    keyName = StringUtil.convertToTitleCase(keyName);
    // Return the value of the key name
    return Input.Keys.valueOf(keyName);
  }

  /**
   * Returns the name of the first linked key of a key id.
   *
   * @param inputId {@link String} : The key's id.
   * @return {@link String} : The key id's input.
   */
  public static String getInputString(String inputId) {
    // If it doesn't exist, stop
    if (!inputs.containsKey(inputId)) {
      return "Key doesn't exist: " + inputId;
    }
    // If it does, return it formatted to use title case
    return StringUtil.convertToTitleCase(Input.Keys.toString(inputs.get(inputId).keys.get(0)));
  }

  /**
   * Loads inputs from the controls.json.
   * <br><br>
   * If it does not exist in the data path, then it will copy from
   * the defaults folder and then save to the data path.
   */
  public static void loadControls() {
    // Try to load the external json file
    JsonValue root = FileControl.loadJsonData("controls.json");
    JsonValue defaultRoot = FileControl.loadJsonFile("defaults", "controls.json", true);
    // If the json hasn't loaded...
    if (root == null) {
      System.out.println("No controls.json exists. It will be created from the default.");
      // Load the json from the defaults folder internally instead, and save it to the data path
      root = FileControl.loadJsonFile("defaults", "controls.json", true);
      if (root == null) {
        throw new RuntimeException("Default controls file not found.");
      }
      // Save it to the data path
      FileControl.saveJsonData("controls.json", root);
    } else {
      // File in Data folder already exists, so map any missing / incorrect values in
      // the default over the root.
      // If any keys are overwritten, save the current as a backup.
      // In any case of modification, overwrite.
      String backup = root.toJson(JsonWriter.OutputType.json);
      boolean changed = false;
      boolean needBackup = false;

      JsonObject keyFormat = new JsonObject();
      keyFormat.addValue(new JsonArray("keys", new JsonString(null, null)));
      keyFormat.addValue(new JsonBool("interaction", false));

      // Loop through all the keys in the defaultRoot.
      for (JsonValue currentKey : defaultRoot) {
        // Check if the save file already has it
        if (root.has(currentKey.name())) {
          // If it has the key, then ensure that the value is valid. If not, overwrite and
          // set "needBackup" to true.
          JsonValue thisId = root.get(currentKey.name());
          // First, value must be a list
          if (!thisId.isObject()) {
            // If it's not, then overwrite
            root.remove(currentKey.name());
            root.addChild(currentKey);
            changed = true;
            needBackup = true;
            continue;
          }

          // Format it to make sure it's valid
          JsonFormat.formatJson(thisId, keyFormat);

          // Make sure it has the key
          JsonValue thisKeys = thisId.get("keys");
          // Make sure it's an array, otherwise overwrite
          if (thisKeys.isArray()) {
            // If it is, make sure all the "keys" children are strings
            // This doesn't need a backup, as they can just be ignored
            for (JsonValue keyVal : thisKeys.iterator()) {
              if (!keyVal.isString()) {
                thisKeys.remove(keyVal.name());
              }
            }

            // If there are no items left, then just copy over the default one, and save.
            if (thisKeys.size == 0 && currentKey.child().size != 0) {
              thisId.remove("keys");
              thisId.addChild(thisKeys);
              changed = true;
              needBackup = true;
            }
          }
          // Interaction can be ignored.
        } else {
          // If it doesn't have the key, then add it.
          root.addChild(currentKey);
        }
      }

      // Now overwrite or backup if needed
      if (changed) {
        FileControl.saveJsonData("controls.json", root);
        // This can be in here as it should only need a backup if it has been changed.
        if (needBackup) {
          FileControl.saveData("controls-backup.json", backup);
        }
      }

    }

    //// Load the controls into the inputs ObjectMap
    // First iterate through all keys
    for (JsonValue key : root.iterator()) {
      // Make sure it has the "keys" array
      if (!key.has("keys")) {
        // If not, then continue to next
        continue;
      }
      JsonValue keys = key.get("keys");
      // Check to make sure that the current key is an array
      if (!keys.isArray()) {
        // If not, then continue to next
        continue;
      }

      String inputId = key.name();
      // If it is an array, then loop through the values inside
      for (JsonValue keyName : keys.iterator()) {
        // Check to make sure the name is a String
        if (!keyName.isString()) {
          // If not, then continue to next
          continue;
        }

        // Get the value of the key
        int keyVal = getKeyVal(keyName.toString());

        // If it's -1, then it's invalid
        if (keyVal == -1) {
          // If it's invalid, skip to next
          continue;
        }

        // If it's valid, add it to the inputs map
        addKey(inputId, keyVal);
      }

      // Check if interaction exists
      if (key.has("interaction") && key.get("interaction").isBoolean()) {
        // If it does, set it the key to that value
        setInteraction(inputId, key.getBoolean("interaction"));
      } else {
        // If the conditional is false, just assume true
        setInteraction(inputId, true);
      }
    }
  }
}