package com.undercooked.game.interactions.steps;

import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.input.InputType;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionInstance;
import com.undercooked.game.interactions.InteractionStep;

/**
 * An interaction step that continues to the next interaction step after
 * forgetting the last interaction provided.
 *
 * <br>Similar to {@link StopInputStep}, but continues the interactions.
 * <br>Fails if no interaction is being passed down.
 */
public class ForgetInputStep extends InteractionStep {

  @Override
  public InteractResult finishedLast(InteractionInstance instance,
                                     Cook cook, String inputId, InputType inputType) {
    return interact(instance, cook, inputId, inputType);
  }

  @Override
  public void update(InteractionInstance instance, Cook cook,
                     float delta, float powerUpMultiplier) {
    finished(instance, cook, true);
  }

  @Override
  public InteractResult interact(InteractionInstance instance, Cook cook,
                                 String inputId, InputType inputType) {
    return finished(instance, cook, true);
  }
}
