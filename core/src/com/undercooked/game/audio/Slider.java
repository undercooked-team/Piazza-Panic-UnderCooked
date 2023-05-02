package com.undercooked.game.audio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.ListenerController;

/**
 * The class for having a slider {@link Button} that allows
 * for easier selecting values between a minimum and maximum.
 */

public class Slider {

  /**
   * The {@link Sprite} of the {@link Button}.
   */
  private Sprite sliderSprite;

  /**
   * The {@link Button} of the {@link Slider}.
   */
  private final Button sliderButton;

  /**
   * The current percentage that the {@link #sliderButton} is across the {@link Slider}.
   */
  private float percent;

  /**
   * The minimum value of the {@link Slider}.
   */
  private final float minValue;

  /**
   * The maximum value of the {@link Slider}.
   */
  private final float maxValue;

  /**
   * The value that the {@link Slider} was at before it was changed.
   */
  private float beforeVal;

  /**
   * The {@link Listener}s for when the {@link #percent} value is changed.
   */
  private final ListenerController<Float> changeListeners;

  /**
   * The {@link Listener}s for when the {@link #sliderButton} is released.
   */
  private final ListenerController<Float> releaseListeners;

  /**
   * Constructor for the {@link Slider}.
   *
   * @param x         {@code int} : The x position.
   * @param y         {@code int} : The y position.
   * @param value     {@code float} : The starting value.
   * @param minValue  {@code float} : The minimum value.
   * @param maxValue  {@code float} : The maximum value.
   * @param sliderTex {@code Texture} : The {@link Texture} of the {@link Button}.
   */
  public Slider(float x, float y, float value, float minValue, float maxValue, Texture sliderTex) {
    this.sliderButton = new Button(new Button.ButtonStyle());
    this.sliderButton.setPosition(x, y);
    this.sliderSprite = new Sprite(sliderTex);

    this.percent = (value - minValue) / (maxValue - minValue);
    this.minValue = minValue;
    this.maxValue = maxValue;

    this.changeListeners = new ListenerController<>();
    this.releaseListeners = new ListenerController<>();

    this.sliderButton.addListener(new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        beforeVal = percent;
        interact(x, y);
        return super.touchDown(event, x, y, pointer, button);
      }

      @Override
      public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        interact(x, y);
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        releaseListeners.tellListeners(percent - beforeVal);
      }
    });
  }

  /**
   * Called when the {@link Slider} is interacted with.
   *
   * @param x {@code float} : The x of the interaction.
   * @param y {@code float} : The y of the interaction.
   */
  public void interact(float x, float y) {
    setPercent((x - sliderSprite.getWidth() / 2)
            / (sliderButton.getWidth() - sliderSprite.getWidth()));
    changeListeners.tellListeners(percent);
  }

  /**
   * Add a {@link Listener} for when the value of the {@link Slider} changes.
   *
   * @param listener {@link Listener}&lt;{@link Float}&gt; : The {@link Listener} to add.
   */
  public void addChangeListener(Listener<Float> listener) {
    changeListeners.addListener(listener);
  }

  /**
   * Remove a {@link Listener} for when the value of the {@link Slider} changes.
   *
   * @param listener {@link Listener}&lt;{@link Float}&gt; : The {@link Listener} to remove.
   */
  public void removeChangeListener(Listener<Float> listener) {
    changeListeners.removeListener(listener);
  }

  /**
   * Add a {@link Listener} for when the {@link Button} of the {@link Slider} is released.
   *
   * @param listener {@link Listener}&lt;{@link Float}&gt; : The {@link Listener} to add.
   */
  public void addReleaseListener(Listener<Float> listener) {
    releaseListeners.addListener(listener);
  }

  /**
   * Remove a {@link Listener} for when the {@link Button} of the {@link Slider} is released.
   *
   * @param listener {@link Listener}&lt;{@link Float}&gt; : The {@link Listener} to remove.
   */
  public void removeReleaseListener(Listener<Float> listener) {
    releaseListeners.removeListener(listener);
  }

  /**
   * Get the numeric value of the slider's current selected percentage.
   *
   * @return {@link float} : The value of the slider between the minimum and maximum.
   */
  public float getValue() {
    return Math.max(Math.min(minValue + percent * (maxValue - minValue), maxValue), minValue);
  }

  /**
   * Set the size of the {@link Button}.
   *
   * @param width  {@link float} : The new width of the {@link Button}.
   * @param height {@link float} : The new height of the {@link Button}.
   */
  public void setSize(float width, float height) {
    sliderButton.setSize(width, height);
  }

  /**
   * Set the size of the {@link Slider}. If the {@link Button} is bigger
   * in width than the {@link Slider}, it will decrease to match the
   * {@link Slider}'s width.
   *
   * @param width  {@code float} : The new width of the {@link Slider}.
   * @param height {@code float} : The new height of the {@link Slider}.
   */
  public void setSliderSize(float width, float height) {
    // Limit the width to the button size.
    if (sliderButton.getWidth() < width) {
      width = sliderButton.getWidth();
    }
    sliderSprite.setSize(width, height);
  }

  /**
   * Set the {@link Sprite} of the {@link Slider}.
   *
   * @param sprite {@link Sprite} : The new {@link Sprite} to use.
   */
  public void setSliderSprite(Sprite sprite) {
    sliderSprite = sprite;
  }

  /**
   * Render the {@link Sprite}.
   *
   * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
   */
  public void render(SpriteBatch batch) {
    // sliderButton.draw(batch, 1F);
    // Position of sliderSprite on the Slider so that it visually touches the border of
    // the Slider (so that anywhere that the sliderSprite is pressed will count as pressing it.
    sliderSprite.setPosition(sliderButton.getX()
                    + (sliderButton.getWidth() - sliderSprite.getWidth()) * percent,
            sliderButton.getY() + sliderButton.getHeight() / 2 - sliderSprite.getHeight() / 2);
    sliderSprite.draw(batch);
  }

  /**
   * Add the {@link Slider} to a {@link Stage} so it can be used
   * with the {@link com.badlogic.gdx.InputProcessor}.
   *
   * @param stage {@link Stage} : The {@link Stage} to add the {@link Slider} to.
   */
  public void addToStage(Stage stage) {
    stage.addActor(this.sliderButton);
  }

  // region Setters

  /**
   * Set the {@code x} position of the {@link Slider}.
   *
   * @param x {@code float} : The {@code x} position to set to.
   */
  public void setX(float x) {
    sliderButton.setX(x);
  }

  /**
   * Set the {@code y} position of the {@link Slider}.
   *
   * @param y {@code float} : The {@code y} position to set to.
   */
  public void setY(float y) {
    sliderButton.setY(y);
  }

  /**
   * Set the {@code width} of the {@link Slider}.
   *
   * @param width {@code float} : The {@code width} to set to.
   */
  public void setWidth(float width) {
    sliderButton.setWidth(width);
  }

  /**
   * Set the {@code height} of the {@link Slider}.
   *
   * @param height {@code float} : The {@code height} to set to.
   */
  public void setHeight(float height) {
    sliderButton.setHeight(height);
  }

  /**
   * Set the value of the {@link Slider}, clamped between the
   * minimum and maximum percentage.
   *
   * @param value {@code float} : The value to set to.
   * @return {@code float} : The value it was set to.
   */
  public float setValue(float value) {
    float percent = value / (maxValue - minValue);
    return setPercent(percent);
  }

  /**
   * Set the percentage value of the {@link Slider}, 0 - 1.
   *
   * @param percent {@code float} : The percentage to set to.
   * @return {@code float} : The percentage it was set to.
   */
  public float setPercent(float percent) {
    this.percent = Math.max(Math.min(percent, 1F), 0F);
    return this.percent;
  }

  /**
   * Set whether the {@link Button} of the {@link Slider} is touchable,
   * or not.
   *
   * @param touchable {@code boolean} : Whether the {@link Button} should be touchable or not.
   */
  public void setTouchable(Touchable touchable) {
    this.sliderButton.setTouchable(touchable);
  }

  // endregion

}
