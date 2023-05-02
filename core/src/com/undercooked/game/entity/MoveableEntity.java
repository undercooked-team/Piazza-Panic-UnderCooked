package com.undercooked.game.entity;

import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapManager;

/**
 * A class for {@link Entity}s that are able to move.
 */
public class MoveableEntity extends Entity {

  /**
   * The movement speed of the {@link MoveableEntity}.
   */
  public float speed;

  /**
   * The movement of the {@link Entity} on the X.
   */
  public float dirX = 0;

  /**
   * The movement of the {@link Entity} on the Y.
   */
  public float dirY = 0;

  /**
   * If the last {@link #moveAndCollide(Map, float, float, float)} collided on the X.
   */
  protected boolean collidedX = false;

  /**
   * If the last {@link #moveAndCollide(Map, float, float, float)} collided on the Y.
   */
  protected boolean collidedY = false;

  /**
   * Calculates the movement distance of the {@link MoveableEntity} on a {@link Map}.
   *
   * @param dirMultiplier {@link float} : The direction multiplier of the movement.
   * @return {@code float} : The distance to move.
   */
  public float moveCalc(float dirMultiplier) {
    return dirMultiplier * MapManager.gridToPos(speed);
  }

  /**
   * Calculates the movement distance of the {@link MoveableEntity}
   * on a {@link Map} over an amount of time.
   *
   * @param dirMultiplier {@link float} : The direction multiplier of the movement.
   * @param delta         {@link float} : The time since the last frame.
   * @return {@code float} : The distance to move.
   */
  public float moveCalc(float dirMultiplier, float delta) {
    return moveCalc(dirMultiplier) * delta;
  }

  /**
   * Move the {@link MoveableEntity}.
   *
   * @param delta {@code float} : The time since the last frame.
   */
  public void move(float delta) {
    move(dirX, dirY, delta);
  }

  /**
   * Move the {@link MoveableEntity} a distance x and y over an amount of time.
   *
   * @param x     {@code float} : The distance to move x.
   * @param y     {@code float} : the distance to move y.
   * @param delta {@code float} : The time since the last frame.
   */
  public void move(float x, float y, float delta) {
    pos.x += moveCalc(x, delta);
    pos.y += moveCalc(y, delta);
  }

  /**
   * Move the {@link MoveableEntity} a distance x and y.
   *
   * @param x {@code float} : The distance to move x.
   * @param y {@code float} : the distance to move y.
   */
  public void move(float x, float y) {
    pos.x += moveCalc(x);
    pos.y += moveCalc(y);
  }

  /**
   * Move and collide until either the {@link MoveableEntity} has finished
   * moving, or until it can't move.
   *
   * @param map   {@link Map} : The {@link Map} to use for collisions.
   * @param delta {@link float} : The time the {@link MoveableEntity} has been moving for.
   */
  public void moveAndCollide(Map map, float delta) {
    moveAndCollide(map, dirX, dirY, delta);
  }

  /**
   * Move and collide until either the {@link MoveableEntity} has finished
   * moving, or until it can't move.
   *
   * @param x     {@code float} : The distance to move x.
   * @param y     {@code float} : the distance to move y.
   * @param map   {@link Map} : The {@link Map} to use for collisions.
   * @param delta {@link float} : The time the {@link MoveableEntity} has been moving for.
   */
  public void moveAndCollide(Map map, float x, float y, float delta) {

    collidedX = false;
    collidedY = false;

    x = moveCalc(x, delta);
    y = moveCalc(y, delta);

    boolean finishedX = (x == 0);
    boolean finishedY = (y == 0);

    // Repeat until x and y are finished
    while (!finishedX || !finishedY) {

      float moveX = Math.min(collision.width, Math.signum(x) * speed);
      float moveY = Math.min(collision.height, Math.signum(y) * speed);

      // Change moveX and moveY depending on distance
      if (Math.abs(x - moveX) >= Math.abs(0 - x)) {
        moveX = x;
      }
      if (Math.abs(y - moveY) >= Math.abs(0 - y)) {
        moveY = y;
      }

      // Check collision
      // X
      if (map.checkCollision(this, collision.x + moveX, collision.y)) {
        float moveDist = 0.01F * Math.signum(moveX);
        // Move the player as close as possible on the x
        while (!map.checkCollision(this, collision.x + moveDist, collision.y)) {
          collision.x += moveDist;
        }
        collision.x -= moveDist;
        pos.x = collision.x - offsetX;
        collidedX = true;
        moveX = 0;
      }

      // Y
      if (map.checkCollision(this, collision.x, collision.y + moveY)) {
        float moveDist = 0.01F * Math.signum(moveY);
        // Move the player as close as possible on the y
        while (!map.checkCollision(this, collision.x, collision.y + moveDist)) {
          collision.y += moveDist;
        }
        collision.y -= moveDist;
        collidedY = true;
        moveY = 0;
      }

      // And then move
      collision.x += moveX;
      collision.y += moveY;

      // Update x and y, if the move distance isn't 0
      x -= moveX;
      y -= moveY;

      // Update xFinished and yFinished
      finishedX = (moveX == 0);
      finishedY = (moveY == 0);
    }
    pos.x = collision.x - offsetX;
    pos.y = collision.y - offsetY;
  }
}
