package com.undercooked.game.interactions.steps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.input.InputController;
import com.undercooked.game.input.InputType;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionInstance;

/**
 * Interaction step for wanting an input before a length of time
 * specified by the {@link #time} value passes.
 *
 * <br>Cannot fail, but will not continue from this step until the
 * wait is finished.
 */
public class TimedInputStep extends WaitStep {

  GlyphLayout text;
  BitmapFont font;
  static final float textPadding = 5;
  static final float textScale = 0.5f;

  /**
   * Constructor for the {@link TimedInputStep}.
   *
   * @param font {@link BitmapFont} : The {@link BitmapFont} to use to draw the
   *             input required.
   */
  public TimedInputStep(BitmapFont font) {
    super();
    this.font = font;
    this.text = new GlyphLayout(this.font, "missing text");
  }

  @Override
  public void setValue(String value) {
    // Update the value
    super.setValue(value);

    // And then update the text
    font.getData().setScale(textScale);
    text.setText(font, InputController.getInputString(value));
    font.getData().setScale(1f);
  }

  @Override
  public void updateTime(InteractionInstance instance, float delta, float powerUpMultiplier) {
    instance.elapsedTime += delta / powerUpMultiplier;
  }

  @Override
  public void waitFinished(InteractionInstance instance, Cook cook) {
    // If the wait finishes, then it's a failure
    finished(instance, cook, null, null, false);
  }

  @Override
  public InteractResult interact(InteractionInstance instance, Cook cook,
                                 String inputId, InputType inputType) {
    // Check that the interaction is correct
    if (inputId.equals(value) && inputType == InputType.JUST_PRESSED) {
      // If this is the case, return that it successfully finished
      return finished(instance, cook, inputId, inputType, true);
    }
    // If it's not, return
    return InteractResult.NONE;
  }

  /**
   * Calculate the X drawing position.
   *
   * @param instance {@link InteractionInstance} : The interaction instance.
   * @return {@code float} : The x position to draw at.
   */
  private float calculateDrawX(InteractionInstance instance) {
    return instance.station.getX()
            + instance.station.getInteractBox().width / 2f - text.width / 2f;
  }

  /**
   * Calculate the Y drawing position.
   *
   * @param instance {@link InteractionInstance} : The interaction instance.
   * @return {@code float} : The y position to draw at.
   */
  private float calculateDrawY(InteractionInstance instance) {
    return instance.station.getY()
            + instance.station.getInteractBox().height / 2f + padding * 2 + offsetY + height;
  }

  @Override
  public void drawPost(InteractionInstance instance, SpriteBatch batch) {
    font.getData().setScale(textScale);
    //// Above the bar, display the input that needs to be pressed
    // Calculate the x and y for the drawing
    float drawX = calculateDrawX(instance);
    float drawY = calculateDrawY(instance);
    // Draw the text
    font.draw(batch, text, drawX, drawY + textPadding + text.height);

    font.getData().setScale(1f);
  }

  @Override
  public void draw(InteractionInstance instance, ShapeRenderer shape) {
    super.draw(instance, shape);
    //// Above the bar, display the input that needs to be pressed
    // Calculate the x and y for the drawing
    float drawX = calculateDrawX(instance);
    float drawY = calculateDrawY(instance);
    // Draw a box behind it
    shape.setColor(Color.GRAY);
    shape.rect(drawX - textPadding, drawY,
            text.width + textPadding * 2, text.height + textPadding * 2);
    shape.setColor(Color.WHITE);
  }

  @Override
  public float getDrawPercent(InteractionInstance instance) {
    return 1f - (instance.elapsedTime / time);
  }

  @Override
  public void setBarColor(InteractionInstance instance, ShapeRenderer shape) {
    float drawPercent = getDrawPercent(instance);
    shape.setColor(1f - drawPercent, drawPercent, 0, 1);
  }
}
