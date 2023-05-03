package com.undercooked.game.interactions.steps.cook.input;

import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.input.InputType;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionInstance;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step for taking an input from the {@link Cook}
 * and checking if it matches the key specified in {@link #value},
 * and that it's just pressed.
 *
 * <br>Cannot fail, but will not continue from this step until the
 * input is given.
 */
public class JustPressedStep extends InteractionStep {
  @Override
  public InteractResult interact(InteractionInstance instance, Cook cook,
                                 String inputId, InputType inputType) {
    // If it uses the key set in value
    if (inputId.equals(value) && inputType == InputType.JUST_PRESSED) {
      // Then move to the next instruction
      return finished(instance, cook, inputId, inputType, true);
    }
    // Otherwise, just ignore
    return InteractResult.NONE;
  }
}
