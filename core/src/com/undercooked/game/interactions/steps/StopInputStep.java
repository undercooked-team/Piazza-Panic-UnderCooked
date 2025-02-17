package com.undercooked.game.interactions.steps;

import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.input.InputType;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionInstance;
import com.undercooked.game.interactions.InteractionStep;

/**
 * An interaction step that stops checking for interactions for
 * the selected {@link Cook} on the current frame.
 *
 * <br>Similar to {@link StopInputStep}, but stops the interactions.
 * <br>Fails if it does not continue from an interact.
 */
public class StopInputStep extends InteractionStep {
  @Override
  public InteractResult finishedLast(InteractionInstance instance, Cook cook,
                                     String inputId, InputType inputType) {
    return interact(instance, cook, inputId, inputType);
  }

  @Override
  public void update(InteractionInstance instance, Cook cook,
                     float delta, float powerUpMultiplier) {
    finished(instance, cook, false);
  }

  @Override
  public InteractResult interact(InteractionInstance instance, Cook cook,
                                 String inputId, InputType inputType) {
    return InteractResult.STOP;
  }
}
