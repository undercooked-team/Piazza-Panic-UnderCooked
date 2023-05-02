package com.undercooked.game.util;

import com.badlogic.gdx.math.Rectangle;

/**
 * A class that provides functions for useful calculations.
 */
public class MathUtil {

  /**
   * Calculates and returns the distance between the two {@link Rectangle}s.
   *
   * @param rect1 {@link Rectangle}
   * @param rect2 {@link Rectangle}
   * @return {@code double} : The distance between the two {@link Rectangle}s.
   */
  public static double distanceBetweenRectangles(Rectangle rect1, Rectangle rect2) {

    // If either of the rectangles are null, return infinity
    if (rect1 == null || rect2 == null) {
      return Float.POSITIVE_INFINITY;
    }

    float x1 = rect1.x + rect1.width / 2;
    float y1 = rect1.y + rect1.height / 2;
    float x2 = rect2.x + rect2.width / 2;
    float y2 = rect2.y + rect2.height / 2;

    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
  }

}
