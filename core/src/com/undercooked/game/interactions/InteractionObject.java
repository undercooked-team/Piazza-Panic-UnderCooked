package com.undercooked.game.interactions;

import com.badlogic.gdx.utils.Array;

/**
 * A class of an interaction having the id of the {@link com.undercooked.game.food.Item}s
 * needed for the interaction to take place, and an {@link Array} of the
 * {@link InteractionStep}s for the interaction.
 */
public class InteractionObject {
  /**
   * The ids of the {@link com.undercooked.game.food.Item}s needed
   * for the interaction to take place.
   */
  protected Array<String> items;

  /**
   * An {@link Array} of the {@link InteractionStep}s that the
   * interaction is made of.
   */
  protected Array<InteractionStep> steps;

  /**
   * Constructor for the {@link InteractionObject}.
   *
   * @param steps       {@link Array}&lt;{@link InteractionStep}&gt;
   *                    : The {@link InteractionStep}s that make up the interaction.
   * @param ingredients {@link Array}&gt;{@link String}&lt;
   *                    : The ids of the {@link com.undercooked.game.food.Item}s
   *                    that are needed for the interaction to take place.
   */
  public InteractionObject(Array<InteractionStep> steps, Array<String> ingredients) {
    this.steps = steps;
    this.items = ingredients;
  }

  /**
   * Returns an {@link Array} of the item ids, as {@link String}s,
   * which are needed for the interaction to take place.
   *
   * @return {@link Array}&gt;{@link String}&lt;
   *                : The ids of the {@link com.undercooked.game.food.Item}s
   *                  needed for the interaction.
   */
  public Array<String> getItems() {
    return items;
  }

  /**
   * Returns the {@link Array} of {@link InteractionStep}s that make
   * up this interaction.
   *
   * @return {@link Array}&lt;{@link InteractionStep}&gt;
   *                : The {@link Array} of {@link InteractionStep}s.
   */
  public Array<InteractionStep> getSteps() {
    return steps;
  }

  /**
   * Unload the {@link InteractionObject} by clearing the
   * {@link InteractionStep}s {@link Array}.
   */
  public void unload() {
    steps.clear();
  }
}