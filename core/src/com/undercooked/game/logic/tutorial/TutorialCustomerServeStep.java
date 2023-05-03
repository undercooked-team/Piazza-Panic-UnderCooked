package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.cook.CookController;

/**
 * A {@link TutorialStep} for serving a {@link com.undercooked.game.entity.customer.Customer}.
 */
public class TutorialCustomerServeStep extends TutorialFollowCurrentCookStep {
  /**
   * Constructor for the {@link TutorialCustomerServeStep}.
   *
   * @param text           {@link String} : The text to display
   * @param textSpeed      {@link float} : The number of characters to add per second.
   * @param cookController {@link CookController} : The {@link CookController} to follow.
   */
  public TutorialCustomerServeStep(String text, float textSpeed, CookController cookController) {
    super(text, textSpeed, cookController);
    skippable = false;
  }
}
