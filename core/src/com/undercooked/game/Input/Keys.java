package com.undercooked.game.Input;

/** The ids of the keys used within the controls.json folder. */
public class Keys {

    //region Cook Keys
    // Movement//
    /** {@link com.undercooked.game.entity.cook.Cook} move up. */
    public static final String cook_up = "c_up";

    /** {@link com.undercooked.game.entity.cook.Cook} move down. */
    public static final String cook_down = "c_down";

    /** {@link com.undercooked.game.entity.cook.Cook} move left. */
    public static final String cook_left = "c_left";

    /** {@link com.undercooked.game.entity.cook.Cook} move right. */
    public static final String cook_right = "c_right";

    // Swapping
    /** Change to previous {@link com.undercooked.game.entity.cook.Cook}. */
    public static final String cook_prev = "c_prev";

    /** Change to next {@link com.undercooked.game.entity.cook.Cook}. */
    public static final String cook_next = "c_next";

    // Item Interactions
    /** Interact with a {@link com.undercooked.game.station.Station}. */
    public static final String interact = "interact";

    /**
     * Take an {@link com.undercooked.game.food.Item} from a
     * {@link com.undercooked.game.station.Station}.
     */
    public static final String take = "take";

    /**
     * Drop an {@link com.undercooked.game.food.Item} onto a
     * {@link com.undercooked.game.station.Station}.
     */
    public static final String drop = "drop";

    //endregion

}
