package com.undercooked.game.station;

/**
 * A class that stores the information on the {@link Station}s.
 */
public class StationData {
  private String texturePath;
  private String defaultBase;
  private String floorTile;
  private final String id;
  private int width;
  private int height;
  private int holds;
  private int price;
  private float collisionWidth;
  private float collisionHeight;
  private float collisionOffsetX;
  private float collisionOffsetY;
  private boolean hasCollision;

  /**
   * Constructor for the {@link StationData}.
   *
   * @param id {@link String} : The id of the {@link Station}.
   */
  public StationData(String id) {
    this.id = id;
    this.holds = 0;
    this.price = 0;
  }

  /**
   * Set the asset path to the station
   * {@link com.badlogic.gdx.graphics.Texture}.
   *
   * @param texturePath {@link String} : The asset path to the {@link Station}'s
   *                                     {@link com.badlogic.gdx.graphics.Texture}.
   */
  public void setTexturePath(String texturePath) {
    this.texturePath = texturePath;
  }

  /**
   * Set the asset path to the base
   * {@link com.badlogic.gdx.graphics.Texture}.
   *
   * @param defaultBase {@link String} : The asset path to the
   *                                     {@link com.badlogic.gdx.graphics.Texture}.
   */
  public void setDefaultBase(String defaultBase) {
    this.defaultBase = defaultBase;
  }

  /**
   * Set the asset path to the floor tile
   * {@link com.badlogic.gdx.graphics.Texture}.
   *
   * @param floorTile {@link String} : The asset path to the
   *                                   {@link com.badlogic.gdx.graphics.Texture}.
   */
  public void setFloorTile(String floorTile) {
    this.floorTile = floorTile;
  }

  /**
   * Sets the default width of the station in map cells.
   *
   * @param width {@code int} : The new width of the {@link Station}.
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Sets the default height of the station in map cells.
   *
   * @param height {@code int} : The new height of the {@link Station}.
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Sets the default width of the collision.
   *
   * @param collisionWidth {@code float} : The new collision width of the {@link Station}.
   */
  public void setCollisionWidth(float collisionWidth) {
    this.collisionWidth = collisionWidth;
  }

  /**
   * Sets the default height of the collision.
   *
   * @param collisionHeight {@code float} : The new collision height of the {@link Station}.
   */
  public void setCollisionHeight(float collisionHeight) {
    this.collisionHeight = collisionHeight;
  }

  /**
   * Sets the default y offset of the collision.
   *
   * @param collisionOffsetX {@code float} : The new collision x offset of the {@link Station}.
   */
  public void setCollisionOffsetX(float collisionOffsetX) {
    this.collisionOffsetX = collisionOffsetX;
  }

  /**
   * Sets the default x offset of the collision.
   *
   * @param collisionOffsetY {@code float} : The new collision y offset of the {@link Station}.
   */
  public void setCollisionOffsetY(float collisionOffsetY) {
    this.collisionOffsetY = collisionOffsetY;
  }

  /**
   * Sets whether the station has collision by default.
   *
   * @param hasCollision {@code boolean} : Whether the {@link Station} does or
   *                                       does not have collision.
   */
  public void setCollidable(boolean hasCollision) {
    this.hasCollision = hasCollision;
  }

  /**
   * Sets the hold limit of the station, with a minimum of 0.
   *
   * @param holds {@code int} : The new number of items that the {@link Station} can hold.
   */
  public void setHoldCount(int holds) {
    this.holds = Math.max(0, holds);
  }

  /**
   * Sets the price of the station, with a minimum of 0.
   *
   * @param price {@code int} : The new default price of the {@link Station}.
   */
  public void setPrice(int price) {
    this.price = Math.max(0, price);
  }

  /**
   * Returns the asset path to get to the station
   * {@link com.badlogic.gdx.graphics.Texture}.
   *
   * @return {@link String} : The asset path to the {@link Station}'s
   *                          {@link com.badlogic.gdx.graphics.Texture}.
   */
  public String getTexturePath() {
    return texturePath;
  }

  /**
   * Returns the asset path to get to the base
   * {@link com.badlogic.gdx.graphics.Texture}.
   *
   * @return {@link String} : The asset path to the {@link Station} base's
   *                          {@link com.badlogic.gdx.graphics.Texture}.
   */
  public String getDefaultBase() {
    return defaultBase;
  }

  /**
   * Returns the asset path to get to the floor tile
   * {@link com.badlogic.gdx.graphics.Texture}.
   *
   * @return {@link String} : The asset path to the floor tile's
   *                          {@link com.badlogic.gdx.graphics.Texture}.
   */
  public String getFloorTile() {
    return floorTile;
  }

  /**
   * Returns the default width of the station in map cells.
   *
   * @return {@code int} : The width of the {@link Station}.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Returns the default height of the station in map cells.
   *
   * @return {@code int} : The height of the {@link Station}.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Returns the default collision width in pixels.
   *
   * @return {@code float} : The collision width of the {@link Station}.
   */
  public float getCollisionWidth() {
    return collisionWidth;
  }

  /**
   * Returns the default collision height in pixels.
   *
   * @return {@code float} : The collision height of the {@link Station}.
   */
  public float getCollisionHeight() {
    return collisionHeight;
  }

  /**
   * Returns the default collision offset on the y in pixels.
   *
   * @return {@code float} : The collision x offset of the {@link Station}.
   */
  public float getCollisionOffsetX() {
    return collisionOffsetX;
  }

  /**
   * Returns the default collision offset on the x in pixels.
   *
   * @return {@code float} : The collision y offset of the {@link Station}.
   */
  public float getCollisionOffsetY() {
    return collisionOffsetY;
  }

  /**
   * Returns whether the station has collision by default or not.
   *
   * @return {@code boolean} : {@code true} if the {@link Station} is collidable,
   *                           {@code false} if not.
   */
  public boolean isCollidable() {
    return hasCollision;
  }

  /**
   * Returns the hold limit.
   *
   * @return {@code int} : The number of items that the {@link Station} can hold.
   */
  public int getHoldCount() {
    return holds;
  }

  /**
   * Returns the price.
   *
   * @return {@code int} : The price of the {@link Station}.
   */
  public int getPrice() {
    return price;
  }

  /**
   * Returns the id.
   *
   * @return {@link String} : The id of the {@link Station}.
   */
  public String getId() {
    return id;
  }
}