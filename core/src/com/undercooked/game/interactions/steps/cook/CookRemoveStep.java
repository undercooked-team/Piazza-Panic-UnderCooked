package com.undercooked.game.interactions.steps.cook;

import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.input.InputType;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionInstance;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step which removes the topmost item of
 * the {@link Cook}.
 *
 * <br>Fails if the {@link Cook} is null, or if they
 * have no items.
 */
public class CookRemoveStep extends InteractionStep {
  @Override
  public InteractResult finishedLast(InteractionInstance instance, Cook cook,
                                     String inputId, InputType inputType) {
    if (cook == null) {
      return finished(instance, null, inputId, inputType, false);
    }
    if (cook.heldItems.size() <= 0) {
      return finished(instance, cook, inputId, inputType, false);
    }
    // Remove the item from the cook
    cook.takeItem();
    // Finish step
    return finished(instance, cook, inputId, inputType, true);
  }

  @Override
  public void update(InteractionInstance instance, Cook cook,
                     float delta, float powerUpMultiplier) {
    finishedLast(instance, cook, null, null);
  }
}
