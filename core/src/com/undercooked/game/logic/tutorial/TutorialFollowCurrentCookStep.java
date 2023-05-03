package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.cook.CookController;

/**
 * A {@link TutorialStep} for following the currently selected
 * {@link com.undercooked.game.entity.cook.Cook}.
 */
public class TutorialFollowCurrentCookStep extends TutorialFollowEntityStep {

  /**
   * {@link CookController} : The {@link CookController} to follow.
   */
  protected CookController cookController;

  /**
   * Constructor for the {@link TutorialFollowCurrentCookStep}.
   *
   * @param text           {@link String} : The text to display
   * @param textSpeed      {@link float} : The number of characters to add per second.
   * @param cookController {@link CookController} : The {@link CookController} to follow.
   */
  public TutorialFollowCurrentCookStep(String text, float textSpeed,
                                       CookController cookController) {
    super(text, textSpeed, null);
    this.cookController = cookController;
    this.processInput = true;
  }

  @Override
  public void update(float delta) {
    targetEntity = cookController.getCurrentCook();
    super.update(delta);
  }

  @Override
  public void start() {
    super.start();
    targetEntity = cookController.getCurrentCook();
  }
}
