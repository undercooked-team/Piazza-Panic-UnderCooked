package com.undercooked.game.screen.buttons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.undercooked.game.GameType;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Listener;

/**
 * A class for the mode button, allowing for selecting between the
 * {@link GameType#SCENARIO} and {@link GameType#ENDLESS} modes.
 */
public class ModeButton {
  private GameType currentType;
  private final TextureManager textureManager;
  private Button scenarioBtn;
  private Button endlessBtn;
  private TextureRegionDrawable scenarioBtnDrawable;
  private TextureRegionDrawable endlessBtnDrawable;
  private Listener<GameType> listener;
  private float buttonWidth;
  private float buttonHeight;
  private float posX;
  private float posY;

  /**
   * Constructor for the {@link ModeButton}.
   *
   * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
   */
  public ModeButton(TextureManager textureManager) {
    currentType = GameType.SCENARIO;
    setScale(1f);
    this.textureManager = textureManager;
  }

  /**
   * Set the {@link TextureManager} to load the {@link com.badlogic.gdx.graphics.Texture}s
   * of the {@link Button}s using the texture group.
   *
   * @param textureGroup {@link String} : The texture group to use.
   */
  public void load(String textureGroup) {
    textureManager.load(textureGroup, "uielements/mode/scenario.png");
    textureManager.load(textureGroup, "uielements/mode/scenario_off.png");
    textureManager.load(textureGroup, "uielements/mode/endless.png");
    textureManager.load(textureGroup, "uielements/mode/endless_off.png");
  }

  /**
   * Unload all of the {@link com.badlogic.gdx.graphics.Texture}s of the {@link Button}s.
   */
  public void unload() {
    textureManager.unloadTexture("uielements/mode/scenario.png");
    textureManager.unloadTexture("uielements/mode/scenario_off.png");
    textureManager.unloadTexture("uielements/mode/endless.png");
    textureManager.unloadTexture("uielements/mode/endless_off.png");
  }

  /**
   * Post load the {@link ModeButton} to set up the {@link Button}s.
   */
  public void postLoad() {
    scenarioBtnDrawable = new TextureRegionDrawable();
    scenarioBtn = new Button(scenarioBtnDrawable);
    endlessBtnDrawable = new TextureRegionDrawable();
    endlessBtn = new Button(endlessBtnDrawable);

    scenarioBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        setCurrentType(GameType.SCENARIO, true);
      }
    });
    endlessBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        setCurrentType(GameType.ENDLESS, true);
      }
    });

    updateButtonSize();
  }

  /**
   * Update the {@link com.badlogic.gdx.graphics.Texture}s of the {@link Button}s.
   */
  public void update() {
    // Don't continue if either of the drawables are null
    if (scenarioBtnDrawable == null || endlessBtnDrawable == null) {
      return;
    }

    switch (currentType) {
      case SCENARIO:
        // Scenario is active, Endless is not
        scenarioBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/mode/scenario.png")
        ));
        endlessBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/mode/endless_off.png")
        ));
        return;
      case ENDLESS:
        // Endless is active, Scenario is not
        endlessBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/mode/endless.png")
        ));
        scenarioBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/mode/scenario_off.png")
        ));
        return;
      default:
        return;
    }
  }

  /**
   * Add the {@link Button}s to a {@link Stage}.
   *
   * @param stage {@link Stage} : The {@link Stage} to add the {@link Button}s to.
   */
  public void addToStage(Stage stage) {
    stage.addActor(scenarioBtn);
    stage.addActor(endlessBtn);
  }

  /**
   * Set the current {@link GameType} of the {@link ModeButton}.
   *
   * @param gameType {@link GameType} : The new {@link GameType}.
   */
  public void setCurrentType(GameType gameType) {
    setCurrentType(gameType, false);
  }

  /**
   * Set the current {@link GameType} of the {@link ModeButton}.
   *
   * @param gameType     {@link GameType} : The new {@link GameType}.
   * @param tellListener {@code boolean} : Whether it should ({@code true}) tell
   *                     the listener or not ({@code false}).
   */
  private void setCurrentType(GameType gameType, boolean tellListener) {
    GameType beforeType = this.currentType;
    this.currentType = gameType;
    // If the gameType is the same, no need to update the textures
    if (gameType != beforeType) {
      update();
    }
    // If listener is not null, tell it
    if (tellListener && listener != null) {
      listener.tell(gameType);
    }
  }

  /**
   * Set the position of the {@link Button}s.
   * <br><br>
   * The x is the center position between them.
   *
   * @param x {@code float} : The new x position.
   * @param y {@code float} : The new y position.
   */
  public void setPosition(float x, float y) {
    this.posX = x;
    this.posY = y;

    updateButtonPosition();
  }

  private void updateButtonPosition() {
    if (scenarioBtn != null) {
      scenarioBtn.setPosition(posX - scenarioBtn.getWidth(), posY);
    }

    if (endlessBtn != null) {
      endlessBtn.setPosition(posX, posY);
    }
  }

  /**
   * Set the {@link Listener} for when the {@link Button}s are pressed.
   *
   * @param listener {@link Listener}&lt;{@link GameType}&gt; : The {@link Listener} to tell.
   */
  public void setListener(Listener<GameType> listener) {
    this.listener = listener;
  }

  private void updateButtonSize() {
    if (scenarioBtn != null) {
      scenarioBtn.setSize(buttonWidth, buttonHeight);
    }

    if (endlessBtn != null) {
      endlessBtn.setSize(buttonWidth, buttonHeight);
    }

    // Update their positions
    setPosition(posX, posY);
  }

  /**
   * Set the scale of the {@link Button}s.
   *
   * @param scale {@code float} : The new scale.
   */
  public void setScale(float scale) {
    float defaultButtonWidth = 473f;
    buttonWidth = scale * defaultButtonWidth;
    float defaultButtonHeight = 144f;
    buttonHeight = scale * defaultButtonHeight;

    updateButtonSize();
  }

  /**
   * Returns the width of the {@link Button}s.
   *
   * @return {@code float} : The width of the {@link Button}s.
   */
  public float getButtonWidth() {
    return buttonWidth;
  }

  /**
   * Returns the height of the {@link Button}s.
   *
   * @return {@code float} : The height of the {@link Button}s.
   */
  public float getButtonHeight() {
    return buttonHeight;
  }

  /**
   * Returns the current {@link GameType} that is selected.
   *
   * @return {@code GameType} : The currently selected {@link GameType}.
   */
  public GameType getCurrentType() {
    return currentType;
  }
}
