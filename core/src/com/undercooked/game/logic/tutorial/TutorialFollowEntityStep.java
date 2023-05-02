package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.Entity;

/**
 * A {@link TutorialStep} for following an {@link Entity}.
 */
public class TutorialFollowEntityStep extends TutorialStep {

  /**
   * The {@link Entity} to follow.
   */
  protected Entity targetEntity;

  /**
   * Constructor for the {@link TutorialFollowEntityStep}.
   *
   * @param text         {@link String} : The text to display
   * @param textSpeed    {@link float} : The number of characters to add per second.
   * @param targetEntity {@link Entity} : The {@link Entity} to follow.
   */
  public TutorialFollowEntityStep(String text, float textSpeed, Entity targetEntity) {
    super(text, textSpeed);
    this.targetEntity = targetEntity;
  }

  @Override
  public void update(float delta) {
    // Update x and y
    if (targetEntity != null) {
      posX = targetEntity.getX();
      posY = targetEntity.getY();
    }
    super.update(delta);
  }
}
