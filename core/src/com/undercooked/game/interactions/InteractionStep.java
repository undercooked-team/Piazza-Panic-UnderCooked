package com.undercooked.game.interactions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.input.InputType;
import java.util.Collections;

/**
 * An abstract class of a step within an interaction.
 */
public abstract class InteractionStep {

  /**
   * The list of steps to take on success.
   */
  protected Array<InteractionStep> success;

  /**
   * The list of steps to take on failure.
   */
  protected Array<InteractionStep> failure;

  /**
   * The time required for the Interaction.
   */
  protected String sound;

  /**
   * The ID of the sound as it's playing.
   */
  protected float time;

  /**
   * The value of the Interaction.
   */
  protected String value;

  /**
   * The last {@link Cook} to interact.
   */
  protected Cook lastInteractedCook;

  /**
   * Update the time value of the instance.
   *
   * @param instance          {@link InteractionInstance} : The interaction instance.
   * @param delta             {@code float} : The time since the last frame.
   * @param powerUpMultiplier {@code float} : The multiplier via power up.
   */
  public void updateTime(InteractionInstance instance, float delta, float powerUpMultiplier) {
    instance.elapsedTime += delta * powerUpMultiplier;
  }

  /**
   * Play the sound of the instance.
   *
   * @param instance {@link InteractionInstance} : The interaction instance.
   */
  public final void playSound(InteractionInstance instance) {
    instance.playSound(sound);
  }

  /**
   * Called when the {@link InteractionStep} has finished and needs to move to the
   * next {@link InteractionStep}.
   *
   * @param instance  {@link InteractionInstance} : The interaction instance
   * @param cook      {@link Cook} : The {@link Cook} involved in the {@link InteractionStep}.
   * @param inputId   {@link String} : The ID of the key used to complete the
   *                  {@link InteractionStep}.
   * @param inputType {@link InputType} : The {@link InputType} used to complete the
   *                  {@link InteractionStep}.
   * @param success   {@code boolean} : {@code true} if the interaction was successful,
   *                  {@code false} if it was not.
   * @return {@link InteractResult} : The result of the {@link InteractionStep},
   *                                  if anything specific is needed.
   */
  public final InteractResult finished(InteractionInstance instance, Cook cook,
                                       String inputId, InputType inputType,
                                       boolean success) {
    // Stop playing the sound
    instance.stopSound(sound);
    // Tell the Interact Controller it's done, so it can move on to the next Interaction
    return instance.interactControl.finished(cook, inputId, inputType, success);
  }

  /**
   * Called when the {@link InteractionStep} has finished and needs to move to the
   * next {@link InteractionStep}.
   * <br>This version calls the main function based on the {@link Cook} involved and
   * the success of the {@link InteractionStep}, ignoring the other values.
   *
   * @param instance {@link InteractionInstance} : The interaction instance
   * @param cook     {@link Cook} : The {@link Cook} involved in the {@link InteractionStep}.
   * @param success  {@code boolean} : {@code true} if the interaction was successful,
   *                 {@code false} if it was not.
   * @return {@link InteractResult} : The result of the {@link InteractionStep},
   *                                  if anything specific is needed.
   */
  public final InteractResult finished(InteractionInstance instance, Cook cook, boolean success) {
    return finished(instance, cook, null, null, success);
  }

  /**
   * Called when the {@link InteractionStep} has finished and needs to move to the
   * next {@link InteractionStep}.
   * <br>This version calls the main function based on the {@link Cook} involved,
   * the key and {@link InputType} involved, ignoring the other values.
   *
   * @param instance  {@link InteractionInstance} : The interaction instance
   * @param cook      {@link Cook} : The {@link Cook} involved in the {@link InteractionStep}.
   * @param inputId   {@link String} : The ID of the key used to complete the
   *                  {@link InteractionStep}.
   * @param inputType {@link InputType} : The {@link InputType} used to complete
   *                  the {@link InteractionStep}.
   * @return {@link InteractResult} : The result of the {@link InteractionStep},
   *                                  if anything specific is needed.
   */
  public final InteractResult finished(InteractionInstance instance, Cook cook,
                                       String inputId, InputType inputType) {
    return finished(instance, cook, inputId, inputType, true);
  }

  /**
   * Called when the {@link InteractionStep} has finished and needs to move to the
   * next {@link InteractionStep}.
   * <br>This version calls the main function based on success, ignoring the other values.
   *
   * @param instance {@link InteractionInstance} : The interaction instance
   * @param success  {@code boolean} : {@code true} if the interaction was successful,
   *                 {@code false} if it was not.
   * @return {@link InteractResult} : The result of the {@link InteractionStep}.
   */
  public final InteractResult finished(InteractionInstance instance, boolean success) {
    return finished(instance, null, null, null, success);
  }

  /**
   * Called when the {@link InteractionStep} is stopped.
   * <br>Not called when the previous ends, but when, for example, a {@link Cook}
   * takes an item from the {@link com.undercooked.game.station.Station} and
   * the interaction is no longer valid.
   *
   * @param instance {@link InteractionStep} :
   */
  public void stop(InteractionInstance instance) {
    // First unlock the Cooks
    instance.station.unlockCooks();
    // Then stop playing the sound
    instance.stopSound(sound);
  }

  /**
   * Updates interactions for the interactions instance. Potentially useful if the items
   * on the {@link com.undercooked.game.station.Station} changes at all, and you want to
   * check for a different interaction based on that.
   *
   * @param instance {@link InteractionInstance} : The interaction instance.
   */
  public final void updateInteractions(InteractionInstance instance) {
    instance.updateInteractions();
  }

  /**
   * Function to update the station.
   *
   * @param instance          {@link InteractionInstance} : The interaction instance for a
   *                          {@link com.undercooked.game.station.Station}.
   * @param cook              {@link Cook} : The {@link Cook} locked to the
   *                          {@link com.undercooked.game.station.Station},
   *                          from a previous {@link InteractionStep} or {@code null}.
   * @param delta             {@code float} : The time since the last frame.
   * @param powerUpMultiplier {@code float} : The multiplier for the power up.
   */
  public void update(InteractionInstance instance, Cook cook,
                     float delta, float powerUpMultiplier) {
  }

  /**
   * Function to update the station.
   *
   * @param instance {@link InteractionInstance} : The interaction instance for a
   *                 {@link com.undercooked.game.station.Station}.
   * @param cook     {@link Cook} : The {@link Cook} locked to the
   *                 {@link com.undercooked.game.station.Station},
   *                 from a previous {@link InteractionStep}.
   *                 Can also be {@code null}.
   * @param delta    {@code float} : The time since the last frame.
   */
  public final void update(InteractionInstance instance, Cook cook, float delta) {
    update(instance, cook, delta, 1);
  }

  /**
   * Called when the previous {@link InteractionStep} just finished,
   * passing along the cook, keyID and inputType.
   *
   * @param instance  {@link InteractionInstance} : The interaction instance.
   * @param cook      {@link Cook} : The {@link Cook} that interacted.
   * @param inputId   {@link String} : The ID of the Key used.
   * @param inputType {@link InputType} : The type of input used.
   * @return {@link InteractResult} : The result of the {@link InteractionStep}.
   */
  public InteractResult finishedLast(InteractionInstance instance, Cook cook,
                                     String inputId, InputType inputType) {
    return InteractResult.NONE;
  }

  /**
   * Interaction between a {@link Cook} and the {@link InteractionStep}.
   *
   * @param instance  {@link InteractionInstance} : The interaction instance.
   * @param cook      {@link Cook} : The {@link Cook} that interacted.
   * @param inputId   {@link String} : The ID of the Key used.
   * @param inputType {@link InputType} : The type of input used.
   * @return {@link InteractResult} : The result of the {@link InteractionStep}.
   */
  public InteractResult interact(InteractionInstance instance, Cook cook,
                                 String inputId, InputType inputType) {
    return InteractResult.NONE;
  }

  /**
   * Draws the {@link InteractionStep} visually on the screen, using
   * the {@link SpriteBatch}.
   * <br>To be override by children classes.
   *
   * @param instance {@link InteractionInstance} : The interaction instance.
   * @param batch    {@link SpriteBatch} : The {@link SpriteBatch} to draw to.
   * @see #drawPost(InteractionInstance, SpriteBatch)
   */
  public void draw(InteractionInstance instance, SpriteBatch batch) {

  }

  /**
   * Draws the {@link InteractionStep} visually on the screen, using
   * the {@link ShapeRenderer}.
   * <br>To be override by children classes.
   *
   * @param instance {@link InteractionInstance} : The interaction instance.
   * @param shape    {@link ShapeRenderer} : The {@link ShapeRenderer} to draw to.
   */
  public void draw(InteractionInstance instance, ShapeRenderer shape) {

  }

  /**
   * A version of the {@link #draw(InteractionInstance, SpriteBatch)} that occurs after the
   * draw functions.
   *
   * @param instance {@link InteractionInstance} : The interaction instance.
   * @param batch    {@link SpriteBatch} : The {@link SpriteBatch} to draw to.
   * @see #draw(InteractionInstance, SpriteBatch)
   */
  public void drawPost(InteractionInstance instance, SpriteBatch batch) {

  }

  /**
   * Update the sound value.
   *
   * @param sound {@link String} : The sound asset path.
   */
  public void setSound(String sound) {
    this.sound = sound;
  }

  /**
   * Update the time value.
   *
   * @param time {@link float} : The time of the interaction.
   */
  public void setTime(float time) {
    this.time = time;
  }

  /**
   * Update the value value.
   *
   * @param value {@link String} : The value of the interaction.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * A step of the {@link #output()} function. Calls recursively for
   * each of the {@link InteractionStep}s within the {@link InteractionStep}'s
   * success and failure {@link Array}s.
   *
   * @param curStep {@link InteractionStep} : The current {@link InteractionStep}.
   * @param tabs    {@code int} : The number of tabs to insert before the text.
   */
  private static void outputStep(InteractionStep curStep, int tabs) {
    String tabStr = String.join("", Collections.nCopies(tabs, "\t"));
    System.out.println(tabStr + curStep.getClass().getSimpleName());
    if (curStep.success != null) {
      System.out.println(tabStr + "\t" + "success:");
      for (InteractionStep nextStep : curStep.success) {
        outputStep(nextStep, tabs + 2);
      }
    }
    if (curStep.failure != null) {
      System.out.println(tabStr + "\t" + "failure:");
      for (InteractionStep nextStep : curStep.failure) {
        outputStep(nextStep, tabs + 2);
      }
    }
  }

  /**
   * Outputs the interaction step, and it's success and failure children
   * into {@link System#out} to see the different steps.
   */
  public void output() {
    outputStep(this, 0);
  }

  /**
   * Outputs the interaction step, and it's success and failure children
   * into {@link System#out} to see the different steps.
   *
   * @param tabs {@code int} : The number of tab characters to use at the start.
   */
  public void output(int tabs) {
    outputStep(this, tabs);
  }
}
