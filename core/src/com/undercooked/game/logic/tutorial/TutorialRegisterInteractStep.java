package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.cook.CookController;

/**
 * A {@link TutorialStep} for interacting with a {@link com.undercooked.game.map.Register}.
 */
public class TutorialRegisterInteractStep extends TutorialFollowCurrentCookStep {
  /**
   * Constructor for the {@link TutorialRegisterInteractStep}.
   *
   * @param text           {@link String} : The text to display
   * @param textSpeed      {@link float} : The number of characters to add per second.
   * @param cookController {@link CookController} : The {@link CookController} to follow.
   */
  public TutorialRegisterInteractStep(String text, float textSpeed, CookController cookController) {
    super(text, textSpeed, cookController);
    skippable = false;
  }
}
