package com.undercooked.game.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.MainGameClass;

/**
 * The class used for more easily swapping between the
 * different {@link Screen}s, and loading them before swapping to them.
 */
public class ScreenController {

  /**
   * The loading {@link Screen}.
   */
  private final LoadScreen loadScreen;

  /**
   * The mapping of {@link Screen}s and their ids.
   */
  private final ObjectMap<String, Screen> screens;

  /**
   * The {@link MainGameClass} of the game.
   */
  private final MainGameClass game;

  /**
   * The {@link Screen} stack, where the last {@link Screen}
   * of the {@link Array} is the top of the stack.
   */
  private final Array<Screen> screenStack;

  /**
   * Constructor for the {@link ScreenController}.
   *
   * @param game         {@link MainGameClass} : The {@link MainGameClass} of the game.
   * @param assetManager {@link AssetManager} : The {@link AssetManager} to load using.
   */
  public ScreenController(MainGameClass game, AssetManager assetManager) {
    this.screens = new ObjectMap<>();
    this.loadScreen = new LoadScreen(assetManager, game);
    this.game = game;
    this.screenStack = new Array<>();
  }

  /**
   * Move to the {@link LoadScreen}, and start loading the next {@link Screen}.
   *
   * @param lastScreen {@link Screen} : The previous {@link Screen} that the
   *                   game was on.
   * @param nextScreen {@link Screen} : The next {@link Screen} that the game
   *                   will be on.
   */
  private void startLoading(Screen lastScreen, Screen nextScreen) {
    nextScreen.preLoad();
    loadScreen.setScreens(lastScreen, nextScreen);
    loadScreen.start();
  }

  /**
   * Go back to the previous {@link Screen}.
   */
  public void backScreen() {
    // If there are no screens beyond the current, then go to the MissingScreen
    if (screenStack.size <= 1) {
      goToScreen(new MissingScreen(game));
      return;
    }
    // Get the current screen.
    Screen current = screenStack.pop();
    // Unload the current Screen
    unload(current);
    // Move to the previous Screen (it will already be loaded)
    game.setScreen(screenStack.peek());
  }

  /**
   * Set the {@link Screen} that the game is on to the {@link Screen}
   * assigned to the id, after clearing the current screen stack.
   *
   * @param id {@link String} : The id of the {@link Screen} to set to.
   */
  public void goToScreen(String id) {
    // First make sure that the screen ID exists
    if (!screens.containsKey(id)) {
      // If it doesn't throw an error.
      throw new RuntimeException(String.format("Screen with ID %s does not exist.", id));
    }
    goToScreen(screens.get(id));
  }

  /**
   * Set the {@link Screen} that the game is on, after clearing the
   * current screen stack.
   *
   * @param screen {@link Screen} : The {@link Screen} to set to.
   */
  public void goToScreen(Screen screen) {
    // First, make sure that the screenStack is unloaded
    unloadStack();

    // When it's all unloaded, then load the new screen
    // It won't be loaded before this point, so no need to check.
    screen.load();
    screen.changeLoaded(false);

    // Then open the load screen
    startLoading(game.getScreen(), screen);

    // Add it to the screen stack
    screenStack.add(screen);
  }

  /**
   * Set the {@link Screen} that the game is on, adding the {@link Screen}
   * to the screen stack, not unloading the previous {@link Screen}.
   *
   * @param id {@link String} : The id of the {@link Screen} to go to.
   */
  public void nextScreen(String id) {
    // First make sure that the screen ID exists
    if (!screens.containsKey(id)) {
      // If it doesn't throw an error.
      throw new RuntimeException(String.format("Screen with ID %s does not exist.", id));
    }
    nextScreen(screens.get(id));
  }

  /**
   * Set the {@link Screen} that the game is on, adding the {@link Screen}
   * to the screen stack, not unloading the previous {@link Screen}.
   *
   * @param screen {@link Screen} : The {@link Screen} to go to.
   */
  public void nextScreen(Screen screen) {
    // Load the screen, if it isn't already.
    screen.load();
    screen.changeLoaded(false);

    // Then start loading
    startLoading(game.getScreen(), screen);

    // Add it to the screen stack
    screenStack.add(screen);
  }

  /**
   * Unload the {@link Screen}.
   *
   * @param screen {@link Screen} : The {@link Screen} to unload.
   */
  public void unload(Screen screen) {
    // If it matches, lower loaded by 1
    screen.changeLoaded(true);
    // If it's no longer loaded, unload it
    if (!screen.isLoaded()) {
      screen.unload();
    }
  }

  /**
   * Unload all of the {@link Screen}s in the screen stack,
   * and then clear the screen stack.
   */
  public void unloadStack() {
    // If the stack is empty, just return
    if (screenStack.size == 0) {
      return;
    }

    // Loop through the screen stack and unload the screens.
    while (screenStack.size > 0) {
      Screen thisScreen = screenStack.pop();

      // Only unload it if it's loaded.
      if (thisScreen.isLoaded()) {
        thisScreen.unload();
        thisScreen.resetLoaded();
      }
    }
    // Clear the stack
    screenStack.clear();
  }

  /**
   * Add a {@link Screen} to the {@link ScreenController} so that
   * it can be changed to using the id, only as long as there isn't
   * a {@link Screen} assigned to the id already.
   *
   * @param screen {@link Screen} : The {@link Screen} to assign.
   * @param id     {@link String} : The id to assign the {@link Screen} to.
   */
  public void addScreen(Screen screen, String id) {
    // If the ID is already in there, don't do anything.
    if (screens.containsKey(id)) {
      return;
    }
    // If it doesn't already exist, add it.
    setScreen(screen, id);
  }

  /**
   * Set the mapping of the id to the {@link Screen}, ignoring if
   * there was a {@link Screen} assigned to it prior.
   *
   * @param screen {@link Screen} : The {@link Screen} to assign.
   * @param id     {@link String} : The id to assign the {@link Screen} to.
   */
  public void setScreen(Screen screen, String id) {
    screens.put(id, screen);
  }

  /**
   * Returns the {@link Screen} assigned to the id.
   *
   * @param id {@link String} : The id of the {@link Screen} to get.
   * @return {@link Screen} : The {@link Screen} assigned to the id.
   */
  public Screen getScreen(String id) {
    return screens.get(id);
  }

  /**
   * Removes a {@link Screen} assigned to the id from the {@link ScreenController}.
   *
   * @param id {@link String} : The id of the {@link Screen} to remove.
   */
  public void removeScreen(String id) {
    screens.remove(id);
  }

  /**
   * Returns whether the game is {@link ScreenController} is currently
   * set on a {@link Screen} by its id.
   *
   * @param id {@link String} : The id of the {@link Screen} to check.
   * @return {@code boolean} : {@code true} if it's on the {@link Screen},
   *                           {@code false} if it's not.
   */
  public boolean onScreen(String id) {
    if (!screens.containsKey(id)) {
      return false;
    }
    return onScreen(screens.get(id));
  }

  /**
   * Returns whether the game is {@link ScreenController} is currently
   * set on a {@link Screen} or not.
   *
   * @param screen {@link Screen} : The {@link Screen} to check.
   * @return {@code boolean} : {@code true} if it's on the {@link Screen},
   *                           {@code false} if it's not.
   */
  public boolean onScreen(Screen screen) {
    return screenStack.peek() == screen;
  }
}
