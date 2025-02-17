package com.undercooked.game.screen;

import com.undercooked.game.MainGameClass;
import com.undercooked.game.util.Constants;

/**
 * A screen for if the code tries to go ta screen that doesn't exist.
 */
public class MissingScreen extends Screen {

  /**
   * The time that has passed.
   */
  float timeCount;

  /**
   * Constructor for the {@link MissingScreen}.
   *
   * @param game {@link MainGameClass} : The {@link MainGameClass} of the game.
   */
  protected MissingScreen(MainGameClass game) {
    super(game);
  }

  @Override
  public void load() {

  }

  @Override
  public void unload() {

  }

  @Override
  public void show() {
    timeCount = 0;
  }

  @Override
  public void render(float delta) {
    // If this screen is ever opened, go to the main screen after 1 second
    timeCount += delta;
    if (timeCount >= 1) {
      getScreenController().goToScreen(Constants.MAIN_SCREEN_ID);
    }
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {

  }
}
