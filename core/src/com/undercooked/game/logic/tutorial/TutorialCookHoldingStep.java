package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.cook.CookController;

public class TutorialCookHoldingStep extends TutorialFollowCurrentCookStep {

    /** The {@link com.undercooked.game.food.Item} id that the {@link Cook} needs to hold. */
    protected String itemID;

    /**
     * Constructor for the {@link TutorialCookHoldingStep}.
     * @param text {@link String} : The text to display
     * @param textSpeed {@link float} : The number of characters to add per second.
     * @param cookController {@link CookController} : The {@link CookController} to follow.
     * @param itemID {@link String} : The {@link com.undercooked.game.food.Item} id to hold.
     */
    public TutorialCookHoldingStep(String text, float textSpeed, CookController cookController, String itemID) {
        super(text, textSpeed, cookController);
        skippable = false;
        this.itemID = itemID;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // Check if the current cook is holding the item
        Cook currentCook = cookController.getCurrentCook();
        if (currentCook == null || currentCook.heldItems.hasID(itemID)) {
            finished();
        }
    }
}
