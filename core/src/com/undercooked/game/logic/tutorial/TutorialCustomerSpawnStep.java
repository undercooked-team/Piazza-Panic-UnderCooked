package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.entity.customer.CustomerController;
import com.undercooked.game.food.Request;

/**
 * A {@link TutorialStep} for spawning a single {@link Customer}.
 */
public class TutorialCustomerSpawnStep extends TutorialFollowEntityStep {

    /** The {@link CustomerController} to spawn the {@link Customer}. */
    protected CustomerController customerController;

    /** The spawned {@link Customer} to follow. */
    protected Customer spawnedCustomer;

    /** The {@link Customer}'s {@link Request}. */
    protected Request request;

    /**
     *
     * @param text {@link String} : The text to display
     * @param textSpeed {@link float} : The number of characters to add per second.
     * @param customerController {@link CustomerController} : The {@link CustomerController} to spawn using.
     * @param request {@link Request} : The {@link Request} of the {@link Customer}.
     */
    public TutorialCustomerSpawnStep(String text, float textSpeed, CustomerController customerController, Request request) {
        super(text, textSpeed, null);
        this.customerController = customerController;
        this.request = request;
    }

    @Override
    public void update(float delta) {
        if (spawnedCustomer != null) {
            x = spawnedCustomer.getX();
            y = spawnedCustomer.getY();
        }
        super.update(delta);
    }

    @Override
    public void start() {
        super.start();
        // Spawn a customer
        spawnedCustomer = customerController.spawnCustomer(request);
    }
}
