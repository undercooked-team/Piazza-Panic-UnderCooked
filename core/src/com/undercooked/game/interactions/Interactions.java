package com.undercooked.game.interactions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Items;
import com.undercooked.game.interactions.steps.ForgetInputStep;
import com.undercooked.game.interactions.steps.RemoveStep;
import com.undercooked.game.interactions.steps.SetStep;
import com.undercooked.game.interactions.steps.StopInputStep;
import com.undercooked.game.interactions.steps.TimedInputStep;
import com.undercooked.game.interactions.steps.WaitStep;
import com.undercooked.game.interactions.steps.cook.CookRemoveStep;
import com.undercooked.game.interactions.steps.cook.CookTakeStep;
import com.undercooked.game.interactions.steps.cook.GiveStep;
import com.undercooked.game.interactions.steps.cook.LockCookStep;
import com.undercooked.game.interactions.steps.cook.UnlockCookStep;
import com.undercooked.game.interactions.steps.cook.input.JustPressedStep;
import com.undercooked.game.interactions.steps.cook.input.PressedStep;
import com.undercooked.game.interactions.steps.cook.input.ReleasedStep;
import com.undercooked.game.station.StationController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.json.JsonFormat;

/**
 * A class that holds all of the {@link InteractionObject}s in the game.
 */
public class Interactions {

  /**
   * A mapping of stationId to the interactionIds.
   */
  private final ObjectMap<String, Array<String>> stationInteractions;

  /**
   * A mapping of interactionID to the interactions + the ingredients needed.
   */
  private final ObjectMap<String, InteractionObject> interactions;

  /**
   * Constructor for the class, setting up the class' {@link ObjectMap}s.
   */
  public Interactions() {
    this.stationInteractions = new ObjectMap<>();
    this.interactions = new ObjectMap<>();
  }

  /**
   * Loads an interaction from the asset path, only loading if the file was found.
   *
   * @param assetPath         {@link String} : The path to the asset.
   * @param stationController {@link StationController} : The {@link StationController} to load the
   *                          station needed for the interaction.
   * @param audioManager      {@link AudioManager} : The {@link AudioManager} to use to load sounds.
   * @param items             {@link Items} : The {@link Items} to load the items needed
   *                          for the interaction.
   */
  public void loadInteractionAsset(String assetPath, StationController stationController,
                                   AudioManager audioManager, Items items) {
    JsonValue interactionRoot = FileControl.loadJsonAsset(assetPath, "interactions");
    // If it's not null...
    if (interactionRoot != null) {
      // load the interaction
      loadInteraction(assetPath, interactionRoot, stationController, audioManager, items);
    }
  }

  /**
   * Goes through an interaction to load all of the {@link InteractionStep}s,
   * returning {@code null} if it can't any of the {@link InteractionStep}s.
   *
   * @param interactionRoot {@link JsonValue} : The interaction's json.
   * @param audioManager    {@link AudioManager} : The {@link AudioManager} to use to load sounds.
   * @param items           {@link Items} : The {@link Items} to use to load items.
   * @return {@link Array}&lt;{@link InteractionStep}&gt; : An {@link Array} of the loaded
   *                                                        {@link InteractionStep}s,
   *                                                        or {@code null} if it failed to load.
   */
  private Array<InteractionStep> addInteractions(JsonValue interactionRoot,
                                                 AudioManager audioManager, Items items) {
    Array<InteractionStep> steps = new Array<>();
    // Loop through the interactions
    for (JsonValue interaction : interactionRoot.iterator()) {
      // Add this interaction
      InteractionStep thisStep = addInteraction(interaction, audioManager, items);
      if (thisStep == null) {
        System.out.println("Failed on step: " + interaction.getString("type"));
        return null;
      }
      steps.add(thisStep);
    }
    return steps;
  }

  /**
   * Goes through the {@link JsonValue} for the interaction, and loads
   * it into a {@link InteractionStep} class, returning {@code null} if
   * it fails to load.
   * <br>
   * Recursively calls the {@link #addInteractions(JsonValue, AudioManager, Items)}
   * function for the success and failure {@link InteractionStep}s.
   *
   * @param interactionRoot {@link JsonValue} : The interaction's json.
   * @param audioManager    {@link AudioManager} : The {@link AudioManager} to use to load sounds.
   * @param items           {@link Items} : The {@link Items} to use to load items.
   * @return {@link InteractionStep} : The {@link InteractionStep} loaded from
   *                                   the {@link JsonValue}.
   */
  private InteractionStep addInteraction(JsonValue interactionRoot,
                                         AudioManager audioManager, Items items) {
    InteractionStep interactionStep;
    // Get the interaction step type
    String type = interactionRoot.getString("type");
    String value = interactionRoot.getString("value");
    // Depending on the type, initialise interactionStep based on that
    // Depending on the instruction, it may also need to load an ingredient
    // If it can't load it, then return null
    switch (type) {
      // Cook Interactions
      case "wait":
        interactionStep = new WaitStep();
        break;
      case "set":
        interactionStep = new SetStep();
        // If it fails to load, return null as it could mess up the
        // whole interaction
        if (items.addItemAsset(value) == null) {
          System.out.println("Set failed to load: " + value + " does not exist.");
          return null;
        }
        break;
      case "give":
        interactionStep = new GiveStep();
        // If it fails to load, return null as it could mess up
        // the whole interaction
        if (items.addItemAsset(value) == null) {
          System.out.println("Give failed to load: " + value + " does not exist.");
          return null;
        }
        break;
      case "lock_cook":
        interactionStep = new LockCookStep();
        break;
      case "unlock_cook":
        interactionStep = new UnlockCookStep();
        break;
      case "cook_take":
        interactionStep = new CookTakeStep();
        break;
      case "cook_remove":
        interactionStep = new CookRemoveStep();
        break;

      // Station Interactions
      case "remove":
        interactionStep = new RemoveStep();
        break;

      // Inputs
      case "timed_input":
        interactionStep = new TimedInputStep(MainGameClass.font);
        break;
      case "pressed":
        interactionStep = new PressedStep();
        break;
      case "just_pressed":
        interactionStep = new JustPressedStep();
        break;
      case "released":
        interactionStep = new ReleasedStep();
        break;

      case "stop_input":
        interactionStep = new StopInputStep();
        break;
      case "forget_input":
        interactionStep = new ForgetInputStep();
        break;
      default:
        // If it is none of the above cases, then return null
        // as the interaction has failed to load
        return null;
    }

    // Set the values of the interactionStep
    interactionStep.setTime(interactionRoot.getFloat("time"));
    interactionStep.setSound(interactionRoot.getString("sound"));
    if (interactionStep.sound != null) {
      audioManager.loadMusicAsset(interactionStep.sound, Constants.GAME_GROUP);
    }
    interactionStep.setValue(interactionRoot.getString("value"));

    // If there are success interactions, add those
    if (interactionRoot.get("success").size > 0) {
      interactionStep.success = addInteractions(interactionRoot.get("success"),
              audioManager, items);
      // If it returns null, return null.
    }
    // Repeat for failure
    if (interactionRoot.get("failure").size > 0) {
      interactionStep.failure = addInteractions(interactionRoot.get("failure"),
              audioManager, items);
      // If it returns null, return null.
    }
    return interactionStep;
  }

  /**
   * Loads a {@link JsonValue} interaction to the {@link #interactions} and
   * {@link #stationInteractions} {@link ObjectMap}s, mapped the
   * {@code interactionID} provided.
   *
   * @param interactionId     {@link String} : The id of the interaction.
   * @param interactionRoot   {@link JsonValue} : The Json for the interaction.
   * @param stationController {@link StationController} : The {@link StationController} to load the
   *                                                      station needed for the interaction.
   * @param audioManager      {@link AudioManager} : The {@link AudioManager} to use to load sounds.
   * @param items             {@link Items} : The {@link Items} to use to load items.
   */
  private void loadInteraction(String interactionId, JsonValue interactionRoot,
                               StationController stationController, AudioManager audioManager,
                               Items items) {
    // Make sure it's formatted correctly
    JsonFormat.formatJson(interactionRoot, DefaultJson.interactionFormat());
    // Check for ID
    Array<InteractionStep> out;
    Array<String> neededIngredients;
    if (interactionId != null) {
      neededIngredients = new Array<>();
      // If the id is there, then check the items. They should be an array of strings.
      for (JsonValue itemId : interactionRoot.get("items")) {
        String id = itemId.toString();
        if (id != null) {
          // If the id is not null, then add it to the ingredients array.
          // Repeats are allowed, as recipes may need multiple of the same item.
          neededIngredients.add(id);
          // Try to load this needed ingredient.
          if (items.addItemAsset(id) == null) {
            // If it fails, then interaction can't work, so don't
            // save it.
            return;
          }
        } else {
          // If it fails to load, return as it could mess up the
          // whole interaction
          return;
        }
      }

      // Check that the station exists
      if (stationController.hasId(interactionRoot.getString("station_id"))) {
        // If it doesn't, try to load it.
        if (stationController.loadStation(interactionRoot.getString("station_id")) == null) {
          // If it doesn't load successfully, then this interaction can't be loaded.
          return;
        }
      }

      // Then, Make sure that there's at least one step.
      // Loop through all the Interactions recursively in order to get them all stored
      // in the array
      out = addInteractions(interactionRoot.get("steps"), audioManager, items);
    } else {
      return;
    }
    // If out is null, then return
    if (out == null) {
      return;
    }
    String stationId = interactionRoot.getString("station_id");
    // Finally, if it all loaded well, add it to the ObjectMap(s).
    // If the station map doesn't exist yet, add it
    if (!stationInteractions.containsKey(stationId)) {
      stationInteractions.put(stationId, new Array<String>());
    }
    // Add the id
    stationInteractions.get(stationId).add(interactionId);

    // Then do the same for the interaction itself
    interactions.put(interactionId, new InteractionObject(out, neededIngredients));
  }

  /**
   * Unloads all the loaded interactions.
   */
  public void unload() {
    stationInteractions.clear();
    for (InteractionObject intObj : interactions.values()) {
      intObj.unload();
    }
  }

  /**
   * Returns an array of the interaction ids that a specific
   * {@link String} station id can do.
   *
   * @param stationId {@link String} : The id of the {@link com.undercooked.game.station.Station}.
   * @return {@link Array}&lt;{@link String}&gt; : An {@link Array} of the interaction ids
   *                                               that can be done by the
   *                                               {@link com.undercooked.game.station.Station}.
   */
  public Array<String> getStationInteractions(String stationId) {
    return stationInteractions.get(stationId);
  }

  /**
   * Returns the {@link InteractionObject} that is mapped to the id provided.
   *
   * @param interactionId {@link String} : The id of the interaction.
   * @return {@link InteractionObject} : The interaction requested.
   */
  public InteractionObject getInteractionSteps(String interactionId) {
    return interactions.get(interactionId);
  }

}
