package com.undercooked.game.entity;

/**
 * The different types of {@link PowerUp}s that can
 * spawn in the game.
 */
public enum PowerUpType {
    /** Movement speed of the {@link com.undercooked.game.entity.cook.Cook} increases. */
    COOK_SPEED_UP("<main>:powerup/speed.png"),
    /** Number of items the {@link com.undercooked.game.entity.cook.Cook} can hold increases. */
    COOK_ITEM_MAX_UP("<main>:powerup/carry.png"),
    /** Interaction speed of the {@link com.undercooked.game.station.Station} increases. */
    INTERACT_FAST("<main>:powerup/cooking.png"),
    /** Wait timer of the  {@link com.undercooked.game.entity.customer.Customer} slows. */
    CUSTOMER_WAIT_SLOW("<main>:powerup/happy.png"),
    /** Spawn Timer of the {@link com.undercooked.game.entity.cook.Cook} slows. (ENDLESS) */
    CUSTOMER_SPAWN_PREVENT(""),
    /** Money is just increased. */
    MONEY_UP("<main>:powerup/money.png"),
    /** Reputation is just increased. */
    REPUTATION_UP("<main>:powerup/reputation.png");

    /** The asset path to the {@link PowerUp}'s {@link com.badlogic.gdx.graphics.Texture}. */
    public final String texturePath;

    /**
     * Constructor for the {@link PowerUpType}.
     * @param texturePath {@link String} : The path to the asset.
     */
    PowerUpType(String texturePath) {
        this.texturePath = texturePath;
    }
}
