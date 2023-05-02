package com.undercooked.game.audio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.util.Listener;

/**
 * A class to easily add the {@link Slider}s for
 * setting the volume of the {@link AudioSettings}
 * to be drawn.
 */
public class AudioSliders {

  /**
   * The x position.
   */
  private float posX;

  /**
   * The y position.
   */
  private float posY;

  /**
   * The width of the background.
   */
  private float width;

  /**
   * The height of the background.
   */
  private float height;

  /**
   * Percentage of height a {@link Slider} uses for its area.
   */
  private float slideHeight;

  /**
   * Percent horizontal padding for the {@link Slider}s.
   */
  private float paddingSides;

  /**
   * Percent vertical padding for the {@link Slider}s.
   */
  private float paddingVertical;

  /**
   * Percentage of the {@link Slider} used for the button.
   */
  private float slideButtonWidth;
  private final Sprite backSprite;
  private final Array<Slider> sliders;

  /**
   * Constructor for the AudioSliders.
   *
   * @param x             {@code float} : The {@code x} position of the {@link AudioSliders}.
   * @param y             {@code float} : The {@code y} position of the {@link AudioSliders}.
   * @param width         {@code float} : The width of the {@link AudioSliders}.
   * @param height        {@code float} : The height of the {@link AudioSliders}.
   * @param backgroundTex {@link Texture} : The {@link Texture} for the background behind
   *                      the {@link Slider}s.
   */
  public AudioSliders(float x, float y, float width, float height, Texture backgroundTex) {
    this.posX = x;
    this.posY = y;
    this.width = width;
    this.height = height;
    this.backSprite = new Sprite(backgroundTex);
    this.paddingSides = 0.1F;
    this.paddingVertical = 0.05F;
    this.slideHeight = 0.5F; // Use up half the area.
    this.slideButtonWidth = 0.05F;

    this.sliders = new Array<>();
  }

  /**
   * Calculates the new position, width and height of the
   * {@link Slider}s.
   */
  public void update() {
    // Resize the sprite to fit the width / height
    backSprite.setSize(width, height);
    backSprite.setPosition(posX, posY);

    // Convert the percentage padding to the amount they
    // are on width / height
    // It is /2 as it's for one side only.
    float paddingX = (width * paddingSides) / 2F;
    float paddingY = (height * paddingVertical) / 2F;

    // Calculate the corners of the Sliders' area.
    float topX = width - paddingX;
    float topY = height - paddingY;

    // Calculate the width of the sliders.
    float slideWidth = topX - paddingX;

    // Calculate the vertical area given to each slider.
    float percentSlide = 1F / sliders.size;
    float slideYroom = (topY - paddingY) * percentSlide;

    // Calculate the height of the sliders.
    float slideHeight = this.slideHeight * slideYroom;

    // Finally, update the Sliders
    for (int i = sliders.size - 1; i >= 0; i--) {
      Slider slider = sliders.get(i);
      slider.setWidth(slideWidth);
      slider.setHeight(slideHeight);
      slider.setSliderSize(slideButtonWidth * slideWidth, slideHeight);
      slider.setX(posX + paddingX);
      slider.setY(posY + paddingY + (slideYroom - slideHeight) / 2
              + slideYroom * (sliders.size - 1 - i));
    }
  }

  /**
   * Add a new {@link Slider} to the {@link AudioSliders}.
   *
   * @param listener      {@link Listener}&lt;{@link Float}&gt; :
   *                      The {@link Listener} of the {@link Slider}.
   * @param sliderTexture {@link Texture} : The {@link Texture} of the {@link Slider}'s button.
   * @return {@link Slider} : The newly created {@link Slider}.
   */
  public Slider addSlider(Listener<Float> listener, Texture sliderTexture) {
    Slider newSlider = new Slider(posX, posY, 0.5F, 0F, 1F,
            sliderTexture);
    newSlider.addChangeListener(listener);
    sliders.add(newSlider);
    update();
    return newSlider;
  }

  /**
   * Remove a {@link Slider} from the {@link AudioSliders}.
   *
   * @param index {@code int} : The index of the {@link Slider} to remove.
   */
  public void removeSliderInd(int index) {
    sliders.removeIndex(index);
    update();
  }

  /**
   * Remove a {@link Slider} from the {@link AudioSliders}.
   *
   * @param slider {@link Slider} : The {@link Slider to remove.}
   */
  public void removeSlider(Slider slider) {
    removeSliderInd(sliders.indexOf(slider, true));
  }

  /**
   * Draws the {@link Slider}s on the window.
   *
   * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
   */
  public void render(SpriteBatch batch) {

    // Draw the background sprite behind.
    backSprite.draw(batch);

    // Draw the Sliders
    for (Slider slider : sliders) {
      slider.render(batch);
    }
  }

  //region Getters

  /**
   * Get the x position of the slider box.
   *
   * @return {@code float} : The {@code x} position of the {@link AudioSliders}.
   */
  public float getX() {
    return posX;
  }

  /**
   * Get the y position of the slider box.
   *
   * @return {@code float} : The {@code y} position of the {@link AudioSliders}.
   */
  public float getY() {
    return posY;
  }

  /**
   * Get the width of the slider box.
   *
   * @return {@code float} : The width of the {@link AudioSliders}.
   */
  public float getWidth() {
    return width;
  }

  /**
   * Get the height of the slider box.
   *
   * @return {@code float} : The height of the {@link AudioSliders}.
   */
  public float getHeight() {
    return height;
  }

  /**
   * Get the percentage padding on the sides of the slider box.
   *
   * @return {@code float} : The side padding of the {@link AudioSliders}.
   */
  public float getPaddingSides() {
    return paddingSides;
  }

  /**
   * Get the percentage padding on the top / bottom of the slider box.
   *
   * @return {@code float} : The vertical padding of the {@link AudioSliders}.
   */
  public float getPaddingVertical() {
    return paddingVertical;
  }

  /**
   * Get the {@link Slider} at the index requested.
   *
   * @param index {@code int} : The index of the {@link Slider}.
   * @return {@link Slider} : The {@link Slider} at that index.
   */
  public Slider getSlider(int index) {
    return sliders.get(index);
  }

  /**
   * Returns whether the {@link Slider} is linked to this
   * {@link AudioSliders}.
   *
   * @param slider {@link Slider} : The {@link Slider} to check for.
   * @return {@code boolean} : {@code true} if the {@link Slider} is present,
   *                           {@code false} if it is not.
   */
  public boolean hasSlider(Slider slider) {
    return sliders.contains(slider, true);
  }

  /**
   * Returns the number of {@link Slider}s linked to this
   * {@link AudioSliders}.
   *
   * @return {@code int} : The number of {@link Slider}s.
   */
  public int size() {
    return sliders.size;
  }

  // endregion

  //region Setters

  /**
   * Set the {@code x} position of the {@link AudioSliders}.
   *
   * @param x {@code float} : The {@code x} position to set to.
   */
  public void setX(float x) {
    this.posX = x;
    update();
  }

  /**
   * Set the {@code y} position of the {@link AudioSliders}.
   *
   * @param y {@code float} : The {@code y} position to set to.
   */
  public void setY(float y) {
    this.posY = y;
    update();
  }

  /**
   * Set the width of the {@link AudioSliders}.
   *
   * @param width {@code float} : The width to set to.
   */
  public void setWidth(float width) {
    this.width = width;
    update();
  }

  /**
   * Set the height of the {@link AudioSliders}.
   *
   * @param height {@code float} : The height to set to.
   */
  public void setHeight(float height) {
    this.height = height;
    update();
  }

  /**
   * Set the side padding of the {@link AudioSliders}.
   *
   * @param paddingSides {@code float} : The side padding to set to.
   */
  public void setPaddingSides(float paddingSides) {
    this.paddingSides = paddingSides;
    update();
  }

  /**
   * Set the vertical padding of the {@link AudioSliders}.
   *
   * @param paddingVertical {@code float} : The vertical padding to set to.
   */
  public void setPaddingVertical(float paddingVertical) {
    this.paddingVertical = paddingVertical;
    update();
  }

  /**
   * Set the width of the {@link Slider}s, a percentage 0 - 1,
   * of the {@link AudioSliders}'s width.
   *
   * @param sliderWidthPercent {@code float} : The percentage width of the {@link Slider}s.
   */
  public void setSliderWidth(float sliderWidthPercent) {
    this.slideButtonWidth = Math.max(Math.min(1, sliderWidthPercent), 0);
    update();
  }

  //endregion
}
