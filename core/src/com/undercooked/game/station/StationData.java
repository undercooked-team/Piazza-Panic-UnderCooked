package com.undercooked.game.station;

/**
 * A class that stores the information on the {@link Station}s.
 */
public class StationData {
    private String texturePath;
    private String defaultBase;
    private String floorTile;
    private final String id;
    private int width, height;
    private int holds;
    private int price;
    private float collisionWidth, collisionHeight,
          collisionOffsetX, collisionOffsetY;
    private boolean hasCollision;

    /**
     * Constructor for the {@link StationData}.
     * @param id {@link String} : The id of the {@link Station}.
     */
    public StationData(String id) {
        this.id = id;
        this.holds = 0;
        this.price = 0;
    }

    /**
     * @param texturePath {@link String} : The asset path to the {@link Station}'s
     *                                     {@link com.badlogic.gdx.graphics.Texture}.
     */
    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    /**
     * @param defaultBase {@link String} : The asset path to the {@link com.badlogic.gdx.graphics.Texture}.
     */
    public void setDefaultBase(String defaultBase) {
        this.defaultBase = defaultBase;
    }

    /**
     * @param floorTile {@link String} : The asset path to the {@link com.badlogic.gdx.graphics.Texture}.
     */
    public void setFloorTile(String floorTile) {
        this.floorTile = floorTile;
    }

    /**
     * @param width {@code int} : The new width of the {@link Station}.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @param height {@code int} : The new height of the {@link Station}.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @param collisionWidth {@code float} : The new collision width of the {@link Station}.
     */
    public void setCollisionWidth(float collisionWidth) {
        this.collisionWidth = collisionWidth;
    }

    /**
     * @param collisionHeight {@code float} : The new collision height of the {@link Station}.
     */
    public void setCollisionHeight(float collisionHeight) {
        this.collisionHeight = collisionHeight;
    }

    /**
     * @param collisionOffsetX {@code float} : The new collision x offset of the {@link Station}.
     */
    public void setCollisionOffsetX(float collisionOffsetX) {
        this.collisionOffsetX = collisionOffsetX;
    }

    /**
     * @param collisionOffsetY {@code float} : The new collision y offset of the {@link Station}.
     */
    public void setCollisionOffsetY(float collisionOffsetY) {
        this.collisionOffsetY = collisionOffsetY;
    }

    /**
     * @param hasCollision {@code boolean} : Whether the {@link Station} does or does not have collision.
     */
    public void setCollidable(boolean hasCollision) {
        this.hasCollision = hasCollision;
    }

    /**
     * @param holds {@code int} : The new number of items that the {@link Station} can hold.
     */
    public void setHoldCount(int holds) {
        this.holds = Math.max(0,holds);
    }

    /**
     * @param price {@code int} : The new default price of the {@link Station}.
     */
    public void setPrice(int price) {
        this.price = Math.max(0,price);
    }

    /**
     * @return {@link String} : The asset path to the {@link Station}'s
     *                          {@link com.badlogic.gdx.graphics.Texture}.
     */
    public String getTexturePath() {
        return texturePath;
    }

    /**
     * @return {@link String} : The asset path to the {@link Station} base's
     *                          {@link com.badlogic.gdx.graphics.Texture}.
     */
    public String getDefaultBase() {
        return defaultBase;
    }

    /**
     * @return {@link String} : The asset path to the floor tile's
     *                          {@link com.badlogic.gdx.graphics.Texture}.
     */
    public String getFloorTile() {
        return floorTile;
    }

    /**
     * @return {@code int} : The width of the {@link Station}.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return {@code int} : The height of the {@link Station}.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return {@code float} : The collision width of the {@link Station}.
     */
    public float getCollisionWidth() {
        return collisionWidth;
    }

    /**
     * @return {@code float} : The collision height of the {@link Station}.
     */
    public float getCollisionHeight() {
        return collisionHeight;
    }

    /**
     * @return {@code float} : The collision x offset of the {@link Station}.
     */
    public float getCollisionOffsetX() {
        return collisionOffsetX;
    }

    /**
     * @return {@code float} : The collision y offset of the {@link Station}.
     */
    public float getCollisionOffsetY() {
        return collisionOffsetY;
    }

    /**
     * @return {@code boolean} : {@code true} if the {@link Station} is collidable,
     *                           {@code false} if not.
     */
    public boolean isCollidable() {
        return hasCollision;
    }

    /**
     * @return {@code int} : The number of items that the {@link Station} can hold.
     */
    public int getHoldCount() {
        return holds;
    }

    /**
     * @return {@code int} : The price of the {@link Station}.
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return {@link String} : The id of the {@link Station}.
     */
    public String getID() {
        return id;
    }
}