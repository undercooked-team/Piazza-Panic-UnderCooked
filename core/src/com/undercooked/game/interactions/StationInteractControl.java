package com.undercooked.game.interactions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.ItemStack;
import com.undercooked.game.food.Items;
import com.undercooked.game.input.InputType;
import com.undercooked.game.station.Station;

/**
 * The {@link InteractionInstance} controller for a {@link Station}.
 */
public class StationInteractControl {

  /**
   * The {@link Station} that owns this {@link StationInteractControl}.
   */
  private final Station station;

  /**
   * The {@link InteractionInstance} of the {@link Station}.
   */
  private final InteractionInstance interactionInstance;

  /**
   * The {@link Interactions} of the game.
   */
  private Interactions interactions;

  /**
   * Possible {@link InteractionStep}s that can be started.
   */
  private final Array<InteractionObject> possibleInteractions;

  /**
   * The currently active {@link InteractionStep}.
   */
  private InteractionStep currentInteraction;

  /**
   * The steps to follow the {@link #currentInteraction}.
   */
  private final Array<InteractionStep> stepsToFollow;

  /**
   * Constructor for the {@link StationInteractControl}.
   *
   * @param station      {@link Station} : The {@link Station} that owns this
   *                                       {@link StationInteractControl}.
   * @param audioManager {@link AudioManager} : The {@link AudioManager} to use for sounds.
   * @param items        {@link Items} : The {@link Items} of the game.
   */
  public StationInteractControl(Station station, AudioManager audioManager, Items items) {
    this.station = station;
    this.interactionInstance = new InteractionInstance(station, this, audioManager, items);

    this.possibleInteractions = new Array<>();
    this.stepsToFollow = new Array<>();
  }

  /**
   * Updates the {@link InteractionInstance} through the {@link #currentInteraction}.
   *
   * @param cook              {@link Cook} : The locked {@link Cook}, if there is one.
   * @param powerUpMultiplier {@code float} : The multiplier from power ups.
   */
  public void update(Cook cook, float powerUpMultiplier) {
    // Only update if there's an interaction currently
    if (currentInteraction != null) {
      interactionInstance.updateDelta();
      currentInteraction.playSound(interactionInstance);
      currentInteraction.update(interactionInstance, cook,
                                interactionInstance.getDelta(), powerUpMultiplier);
    }
  }

  /**
   * Go to the next {@link InteractionStep}.
   */
  private void nextInteraction() {
    InteractionStep nextStep = null;
    if (stepsToFollow.size > 0) {
      nextStep = stepsToFollow.first();
      stepsToFollow.removeIndex(0);
    }
    currentInteraction = nextStep;
  }

  /**
   * Set the {@link #currentInteraction}.
   *
   * @param interaction {@link InteractionStep} : The {@link InteractionStep} to set to.
   */
  private void setInteraction(InteractionStep interaction) {
    interactionInstance.reset();
    stepsToFollow.clear();

    currentInteraction = interaction;
  }

  /**
   * Add {@link InteractionStep}s to the {@link #stepsToFollow} that come after
   * the {@link #currentInteraction}.
   */
  private void addFollowingInteractions(Array<InteractionStep> interactions) {
    if (interactions == null) {
      return;
    }
    // Add them to the front of the stepsToFollow array, from back to front of
    // the interactions array so that they go before whatever else was there before,
    // and also in the correct order.
    for (int i = interactions.size - 1; i >= 0; i--) {
      stepsToFollow.insert(0, interactions.get(i));
    }
  }

  /**
   * Set the {@link #currentInteraction} and {@link #stepsToFollow}.
   *
   * @param interactions {@link Array}&lt;{@link InteractionStep}&gt; : The steps to use.
   */
  private void setInteractionSteps(Array<InteractionStep> interactions) {
    clear();
    if (interactions == null || interactions.size == 0) {
      return;
    }
    // Make a copy of the array
    Array<InteractionStep> arrayCopy = new Array<>();
    for (InteractionStep interaction : interactions) {
      arrayCopy.add(interaction);
    }
    // Set the interaction to the first index
    setInteraction(arrayCopy.first());
    // Remove the first index
    arrayCopy.removeIndex(0);
    // Then add the rest
    addFollowingInteractions(arrayCopy);
  }

  /**
   * Set the {@link Interactions} that the {@link StationInteractControl} should
   * be finding what {@link InteractionStep}s it can use.
   *
   * @param interactions {@link Interactions} : The {@link Interactions} of the game.
   */
  public void setInteractions(Interactions interactions) {
    this.interactions = interactions;
  }

  /**
   * Clear the currently loaded interaction.
   */
  public void clear() {
    currentInteraction = null;
    stepsToFollow.clear();
  }

  /**
   * Interact with the {@link InteractionStep}.
   *
   * @param cook      {@link Cook} : The {@link Cook} that interacted.
   * @param inputId   {@link String} : The id of the key that was used to interact.
   * @param inputType {@link InputType} : The type of input provided.
   * @return {@link InteractResult} : The result of the interaction.
   */
  public InteractResult interact(Cook cook, String inputId, InputType inputType) {

    if (currentInteraction != null) {
      return currentInteraction.interact(interactionInstance, cook, inputId, inputType);
    }
    return InteractResult.NONE;
  }

  /**
   * Called when the current interaction has finished, adding either the
   * success or failure {@link InteractionStep}s that follow from the
   * current interaction and then move on to the next interactions.
   * {@link InteractionStep}.
   *
   * @param cook      {@link Cook} : The {@link Cook} that was involved in the
   *                                 {@link InteractionStep},
   *                                 or {@code null} if there was none.
   * @param inputId     {@link String} : The id of the key used in the {@link InteractionStep},
   *                                   or {@code null} if there was none.
   * @param inputType {@link InputType} : The input type used in the {@link InteractionStep},
   *                                      or {@code null} if there was none.
   * @param success   {@code boolean} : {@code true} if the {@link InteractionStep}
   *                                    ended in success,
   *                                    {@code false} if it failed.
   * @return {@link InteractResult} : The result of the interaction.
   */
  public InteractResult finished(Cook cook, String inputId, InputType inputType, boolean success) {
    // First add the following Interactions
    if (success) {
      addFollowingInteractions(currentInteraction.success);
    } else {
      addFollowingInteractions(currentInteraction.failure);
    }
    // Reset interaction variables
    interactionInstance.reset();
    // Then move to the next Interaction
    nextInteraction();
    interactionInstance.updateDelta();
    // If currentInteraction is null, update.
    if (currentInteraction == null) {
      station.updateStationInteractions();
      return InteractResult.STOP;
    } else {
      // Otherwise call the finished last of the previous
      return currentInteraction.finishedLast(interactionInstance, cook, inputId, inputType);
    }
  }

  /**
   * Update the array of {@link #possibleInteractions}.
   *
   * @param stationId {@link String} : The id of the {@link Station} to get
   *                                   the possible {@link InteractionStep}s for.
   */
  public void updatePossibleInteractions(String stationId) {
    // Clear the possible reactions
    possibleInteractions.clear();
    // If the interactions instance is null, return
    if (interactions == null) {
      return;
    }
    Array<String> stationInteractions = interactions.getStationInteractions(stationId);

    // If it's null, return
    if (stationInteractions == null) {
      possibleInteractions.clear();
      return;
    }

    // Load all the possible interactions for this station
    for (String stationInteraction : stationInteractions) {
      possibleInteractions.add(interactions.getInteractionSteps(stationInteraction));
    }
  }

  /**
   * Find the possible {@link InteractionObject} that the {@link Station}
   * can do currently with its current {@link ItemStack}.
   *
   * @param items {@link ItemStack} : The stack of {@link Item}s currently
   *              on the {@link Station}.
   * @return {@link InteractionObject} : The possible {@link InteractionStep}s that can be
   *                                     taken, if there is a valid one.
   *                                     Otherwise, returns {@code null}.
   */
  public InteractionObject findValidInteraction(ItemStack items) {
    // Find an interaction with a matching item requirement.
    // Loop through possibleInteractions
    for (InteractionObject possibleInt : possibleInteractions) {
      // First make sure the stack sizes are the same
      if (possibleInt.items.size != items.size()) {
        // If they aren't, then it's invalid
        continue;
      }

      // Check if, for every item in the ItemStack, that there is matching item
      // in the possibleInteraction items.
      Array<Item> checkArray = items.asArray();

      // Loop through the item IDs in the possibleInteractions
      for (String itemId : possibleInt.items) {
        // Check for if the checkArray has the ID.
        int itemInd = -1;
        for (int i = 0; i < checkArray.size; i++) {
          Item item = checkArray.get(i);
          if (item.getId().equals(itemId)) {
            itemInd = i;
            break;
          }
        }
        // If it doesn't, this is an invalid interaction
        if (itemInd == -1) {
          break;
        }
        // If it does, then remove the ingredient from the
        // items list
        checkArray.removeIndex(itemInd);
      }

      // Finally, if the checkArray is empty, then it's valid
      if (checkArray.size <= 0) {
        return possibleInt;
      }
    }
    // Nothing matches, so return null.
    return null;
  }

  /**
   * Set the current interaction being done.
   *
   * @param interaction {@link InteractionObject} : The interaction to set to.
   */
  public void setCurrentInteraction(InteractionObject interaction) {
    // First stop the current interaction (if there is one)
    if (currentInteraction != null) {
      currentInteraction.stop(interactionInstance);
    }
    // Then set the new interaction, if it's not null
    if (interaction != null) {
      // Move to the new interactions
      setInteractionSteps(interaction.steps);
      // Update last delta check
      interactionInstance.updateDelta();
      return;
    }
    // If it's null, then just clear
    clear();
  }

  /**
   * Stop the currently active interaction, if there is one.
   */
  public void stop() {
    // If current interaction is not null...
    // Stop the interaction instance
    if (currentInteraction != null) {
      currentInteraction.stop(interactionInstance);
    }
  }

  /**
   * Draw the {@link InteractionStep} using {@link SpriteBatch}.
   *
   * @param batch {@link SpriteBatch} : The {@link SpriteBatch}.
   */
  public void draw(SpriteBatch batch) {
    if (currentInteraction == null) {
      return;
    }
    currentInteraction.draw(interactionInstance, batch);
  }

  /**
   * Draw the {@link InteractionStep} using {@link ShapeRenderer}.
   *
   * @param shape {@link ShapeRenderer} : The {@link ShapeRenderer}.
   */
  public void draw(ShapeRenderer shape) {
    if (currentInteraction == null) {
      return;
    }
    currentInteraction.draw(interactionInstance, shape);
  }

  /**
   * Draw the {@link InteractionStep} after the normal {@code draw()} functions
   * using {@link SpriteBatch}.
   *
   * @param batch {@link SpriteBatch} : The {@link SpriteBatch}.
   */
  public void drawPost(SpriteBatch batch) {
    if (currentInteraction == null) {
      return;
    }
    currentInteraction.drawPost(interactionInstance, batch);
  }
}
