package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.undercooked.game.GameType;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.logic.Difficulty;
import com.undercooked.game.logic.EndlessLogic;
import com.undercooked.game.logic.GameLogic;
import com.undercooked.game.logic.ScenarioLogic;
import com.undercooked.game.render.GameRenderer;
import com.undercooked.game.screen.buttons.ModeButton;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.json.JsonFormat;
import com.undercooked.game.util.json.JsonObject;

/**
 * A class for the play {@link Screen} in which the player
 * selects the scenario that they want to play.
 */
public class PlayScreen extends Screen {

  private Stage stage;
  private OrthographicCamera camera;
  private Texture background;
  private Sprite plate;
  private GlyphLayout title;
  private GlyphLayout description;
  private ModeButton modeButton;
  private Button leftCustomerBtn;
  private Button rightCustomerBtn;

  private TextureRegionDrawable easyModeBtnDrawable;
  private Button easyModeBtn;
  private TextureRegionDrawable mediumModeBtnDrawable;
  private Button mediumModeBtn;
  private TextureRegionDrawable hardModeBtnDrawable;
  private Button hardModeBtn;
  private int currentDifficulty;
  private boolean showCustomerNumber;
  private int customerNumber; // The number of requests to serve in the Custom mode
  private GlyphLayout customerNumberText;
  private final float customerNumberScale = 2f;
  private int currentIndex;
  private Array<JsonValue> scenarioArray;

  private final float plateSize = 1000;
  private final float titleScale = 2;
  private final float titlePadding = 200;
  private final float descScale = 1;
  private final float descPadding = 100;
  private final String[] scenarioFiles = {
      "custom",
      "everything",
      "burger_salad",
      "serve_quick",
      "everything_with_powerups"
  };

  /**
   * Constructor for the {@link PlayScreen}.
   *
   * @param game {@link MainGameClass} : The {@link MainGameClass} of the game.
   */
  public PlayScreen(MainGameClass game) {
    super(game);
  }

  @Override
  public void load() {
    TextureManager textureManager = getTextureManager();
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/MainScreenBackground.jpg");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/exittomenu.png");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/playte.png");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/newgame.png");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/arrow_left.png");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/arrow_right.png");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/difficulty/easy_off.png");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/difficulty/easy_on.png");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/difficulty/medium_off.png");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/difficulty/medium_on.png");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/difficulty/hard_off.png");
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/difficulty/hard_on.png");

    // TEMPORARY
    textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/scenario.png");

    game.audioManager.loadMusic("audio/music/MainScreenMusic.ogg", Constants.MUSIC_GROUP);

    camera = CameraController.getCamera(Constants.UI_CAMERA_ID);

    // Create the stage
    stage = new Stage(CameraController.getViewport(Constants.UI_CAMERA_ID), game.batch);

    // Create the Mode Button
    modeButton = new ModeButton(textureManager);
    modeButton.load(Constants.PLAY_TEXTURE_ID);

    title = new GlyphLayout();
    description = new GlyphLayout();

    customerNumberText = new GlyphLayout();

    scenarioArray = new Array<>();

    // Load the game's Scenario's
    loadScenarios();

  }

  /**
   * Load the scenario data from file paths in the {@link #scenarioFiles} array.
   */
  private void loadScenarios() {
    JsonObject format = DefaultJson.scenarioFormat();
    // Load all the files in the array
    for (String filePath : scenarioFiles) {
      JsonValue root = FileControl.loadJsonFile("game/scenarios", filePath, true);
      JsonFormat.formatJson(root, format);
      scenarioArray.add(root);

      root.addChild("id", new JsonValue("<main>:" + filePath));
    }
  }

  @Override
  public void unload() {
    TextureManager textureManager = getTextureManager();
    textureManager.unload(Constants.PLAY_TEXTURE_ID, true);

    game.audioManager.unloadMusic("audio/music/MainScreenMusic.ogg");

    stage.dispose();
    stage = null;
    modeButton = null;

    leftCustomerBtn = null;
    rightCustomerBtn = null;

    background = null;
    plate = null;

    title = null;
    description = null;

    scenarioArray = null;
  }

  @Override
  public void postLoad() {

    final TextureManager textureManager = getTextureManager();

    // Textures
    background = textureManager.get("uielements/MainScreenBackground.jpg");
    plate = new Sprite(textureManager.get("uielements/playte.png"));

    // Set up plate sprite
    plate.setSize(plateSize, plateSize);
    plate.setPosition(
            Constants.V_WIDTH / 2f - plate.getWidth() / 2f,
            Constants.V_HEIGHT / 2f - plate.getHeight() / 2f
    );

    // Make the buttons
    final Button menuBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/exittomenu.png")
    ));
    final Button leftBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/arrow_left.png")
    ));
    final Button rightBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/arrow_right.png")
    ));
    final Button playBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/newgame.png")
    ));

    easyModeBtnDrawable = new TextureRegionDrawable();
    mediumModeBtnDrawable = new TextureRegionDrawable();
    hardModeBtnDrawable = new TextureRegionDrawable();

    easyModeBtn = new Button(easyModeBtnDrawable);
    mediumModeBtn = new Button(mediumModeBtnDrawable);
    hardModeBtn = new Button(hardModeBtnDrawable);


    leftCustomerBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/arrow_left.png")
    ));
    rightCustomerBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/arrow_right.png")
    ));

    // Set the position and size of the buttons
    menuBtn.setSize(473, 144);
    menuBtn.setPosition(16, Constants.V_HEIGHT - menuBtn.getHeight() - 16);

    leftBtn.setSize(128, 128);
    leftBtn.setPosition(32, Constants.V_HEIGHT / 2f - leftBtn.getHeight()/2f);

    rightBtn.setSize(128, 128);
    rightBtn.setPosition(
            Constants.V_WIDTH - rightBtn.getWidth() - 32,
            Constants.V_HEIGHT / 2f - rightBtn.getHeight() / 2
    );

    playBtn.setSize(473, 144);
    playBtn.setPosition(Constants.V_WIDTH / 2f - playBtn.getWidth() / 2f, 0);

    float difficultyWidth = 270.28f;
    float difficultyHeight = 82.28f;
    easyModeBtn.setSize(difficultyWidth, difficultyHeight);
    mediumModeBtn.setSize(difficultyWidth, difficultyHeight);
    hardModeBtn.setSize(difficultyWidth, difficultyHeight);

    float customerBtnSize = plateSize * 0.1f;
    leftCustomerBtn.setSize(customerBtnSize, customerBtnSize);
    leftCustomerBtn.setPosition(
            plate.getX() + 20,
            plate.getY() + plate.getHeight() / 2f - leftCustomerBtn.getHeight()
    );

    rightCustomerBtn.setSize(customerBtnSize, customerBtnSize);
    rightCustomerBtn.setPosition(
            plate.getX() + plate.getWidth() - rightCustomerBtn.getWidth() - 20,
            plate.getY() + plate.getHeight() / 2f - rightCustomerBtn.getHeight()
    );

    // Add the listeners to the buttons
    menuBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Go to main menu
        backScreen();
      }
    });

    leftBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Go back one index
        changeIndex(-1);
      }
    });

    rightBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Go forward one index
        changeIndex(1);
      }
    });

    playBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        startGame();
      }
    });

    leftCustomerBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        customerNumber = Math.max(1, customerNumber - 1);
        updateCustomerText();
      }
    });

    rightCustomerBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        customerNumber += 1;
        updateCustomerText();
      }
    });

    easyModeBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        setDifficulty(Difficulty.EASY);
      }
    });

    mediumModeBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        setDifficulty(Difficulty.MEDIUM);
      }
    });

    hardModeBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        setDifficulty(Difficulty.HARD);
      }
    });

    // Set up the mode button
    modeButton.postLoad();
    modeButton.update();
    modeButton.setScale(0.8f);

    modeButton.setPosition(Constants.V_WIDTH / 2f,
            Constants.V_HEIGHT - modeButton.getButtonHeight());

    modeButton.setListener(new Listener<GameType>() {
      @Override
      public void tell(GameType value) {
        setIndex(currentIndex);
      }
    });

    // Add the buttons to the stage
    stage.addActor(menuBtn);
    stage.addActor(leftBtn);
    stage.addActor(rightBtn);
    stage.addActor(playBtn);
    stage.addActor(leftCustomerBtn);
    stage.addActor(rightCustomerBtn);
    stage.addActor(easyModeBtn);
    stage.addActor(mediumModeBtn);
    stage.addActor(hardModeBtn);
    modeButton.addToStage(stage);

    // Set the difficulty
    setDifficulty(Difficulty.MEDIUM);

    // Then show index 0
    setIndex(0);
  }

  @Override
  public void show() {
    // Set the input processor
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float v) {

    // Clear the screen
    ScreenUtils.clear(0, 0, 0, 0);
    game.batch.setProjectionMatrix(camera.combined);
    game.mainScreenMusic.play();

    game.batch.begin();
    game.batch.draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
    plate.draw(game.batch);
    game.font.getData().setScale(titleScale);
    float titleY = Constants.V_HEIGHT / 2f + (0.7f * plate.getHeight() / 2f);
    game.font.draw(game.batch, title, Constants.V_WIDTH / 2f - title.width / 2f, titleY);
    game.font.getData().setScale(descScale);
    game.font.draw(game.batch, description, Constants.V_WIDTH / 2f - description.width / 2f,
            titleY - title.height - descPadding);

    if (showCustomerNumber) {
      game.font.getData().setScale(customerNumberScale);
      game.font.draw(game.batch, customerNumberText,
              Constants.V_WIDTH / 2f - customerNumberText.width / 2f,
              Constants.V_HEIGHT / 2f - customerNumberText.height / 2f);
      game.font.getData().setScale(1f);
    }
    game.batch.end();

    stage.draw();
    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
      backScreen();
    } else {
      stage.act();
    }

  }

  /**
   * Set the currently selected difficulty of the game.
   *
   * @param difficulty {@code int} : The currently selected difficulty.
   */
  private void setDifficulty(int difficulty) {
    this.currentDifficulty = difficulty;
    TextureManager textureManager = getTextureManager();

    float difficultyPosX = Constants.V_WIDTH / 2f;
    float difficultyPosY = 200;
    easyModeBtn.setPosition(
            difficultyPosX - mediumModeBtn.getWidth() / 2f - easyModeBtn.getWidth(),
            difficultyPosY
    );
    mediumModeBtn.setPosition(difficultyPosX - mediumModeBtn.getWidth() / 2f, difficultyPosY);
    hardModeBtn.setPosition(difficultyPosX + mediumModeBtn.getWidth() / 2f, difficultyPosY);
    switch (currentDifficulty) {
      case Difficulty.EASY:
        // Easy is active
        easyModeBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/difficulty/easy_on.png")
        ));
        mediumModeBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/difficulty/medium_off.png")
        ));
        hardModeBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/difficulty/hard_off.png")
        ));

        easyModeBtn.setPosition(easyModeBtn.getX(), easyModeBtn.getY() + 30);
        break;
      case Difficulty.MEDIUM:
        // Easy is active
        easyModeBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/difficulty/easy_off.png")
        ));
        mediumModeBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/difficulty/medium_on.png")
        ));
        hardModeBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/difficulty/hard_off.png")
        ));

        mediumModeBtn.setPosition(mediumModeBtn.getX(), mediumModeBtn.getY() + 30);
        break;
      case Difficulty.HARD:
        // Easy is active
        easyModeBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/difficulty/easy_off.png")
        ));
        mediumModeBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/difficulty/medium_off.png")
        ));
        hardModeBtnDrawable.setRegion(new TextureRegion(
                textureManager.get("uielements/difficulty/hard_on.png")
        ));

        hardModeBtn.setPosition(hardModeBtn.getX(), hardModeBtn.getY() + 30);
        break;
      default:
        break;
    }
  }

  /**
   * Dry the currently displayed index of the scenarios.
   *
   * @param index     {@code int} : The index to set it to show.
   * @param changeVal {@code int} : The index to change by if on {@link GameType#ENDLESS} and
   *                  {@link Constants#CUSTOM_SCENARIO_ID}.
   */
  private void setIndex(int index, int changeVal) {

    // If it's not valid, don't do anything
    if (index < 0 || index >= scenarioArray.size) {
      return;
    }
    // If it's the custom scenario, and the game type is currently endless, then
    // move +1 or -1
    // This is because the custom type only supports scenario mode, and the "everything"
    // scenario can be used instead for endless.
    if (scenarioArray.get(index).getString("id").equals(Constants.CUSTOM_SCENARIO_ID)
            && modeButton.getCurrentType() == GameType.ENDLESS) {
      if (changeVal < 0) {
        changeIndex(changeVal - 1);
      } else {
        changeIndex(changeVal + 1);
      }
      return;
    }
    // If it's valid, update the current index and show the scenario data
    currentIndex = index;
    showScenarioData(scenarioArray.get(index));
  }

  /**
   * Dry the currently displayed index of the scenarios.
   *
   * @param index {@code int} : The index to set it to show.
   */
  private void setIndex(int index) {
    setIndex(index, 0);
  }

  /**
   * Change the currently displayed index of the scenarios.
   *
   * @param change {@code int} : How many indexes to change it by.
   */
  private void changeIndex(int change) {
    // Update the index number
    int newIndex = currentIndex + change;
    while (newIndex < 0) {
      newIndex += scenarioArray.size;
    }
    newIndex %= scenarioArray.size;

    // Then set the new index
    setIndex(newIndex, change);
  }

  /**
   * Show the data for a scenario's {@link JsonValue}.
   *
   * @param scenarioData {@link JsonValue} : The scenario data to show.
   */
  private void showScenarioData(JsonValue scenarioData) {
    game.font.getData().setScale(titleScale);
    title.setText(game.font, scenarioData.getString("name"),
            Color.BLACK, plateSize - titlePadding, Align.left, true);

    game.font.getData().setScale(descScale);
    description.setText(game.font, scenarioData.getString("description"),
            Color.BLACK, plateSize - descPadding, Align.left, true);
    game.font.getData().setScale(1f);

    // If it's the custom scenario, then show the customer buttons. Otherwise, hide them.
    if (scenarioData.getString("id").equals(Constants.CUSTOM_SCENARIO_ID)) {
      customerNumber = 3;
      showCustomerNumber = true;
      updateCustomerText();
    } else {
      showCustomerNumber = false;
    }

    leftCustomerBtn.setDisabled(!showCustomerNumber);
    leftCustomerBtn.setVisible(showCustomerNumber);
    rightCustomerBtn.setDisabled(!showCustomerNumber);
    rightCustomerBtn.setVisible(showCustomerNumber);
  }

  /**
   * Updat the {@link #customerNumberText}'s text.
   */
  private void updateCustomerText() {
    game.font.getData().setScale(customerNumberScale);
    customerNumberText.setText(game.font, Integer.toString(customerNumber));
    game.font.getData().setScale(1f);
  }

  /**
   * Start the game.
   */
  private void startGame() {
    // Make sure the currentIndex is valid
    if (currentIndex < 0 || currentIndex >= scenarioArray.size) {
      return;
    }
    // Make sure it's on the play screen
    if (!getScreenController().onScreen(Constants.PLAY_SCREEN_ID)) {
      return;
    }
    // Turn off the game music if it's on.
    if (game.mainScreenMusic != null) {
      game.mainScreenMusic.stop();
    }
    GameScreen gameScreen = (GameScreen) game.screenController.getScreen(Constants.GAME_SCREEN_ID);
    // gameScreen.setGameLogic(new ScenarioLogic(gameScreen, textureManager, getAudioManager()));
    GameLogic gameLogic;
    GameRenderer gameRenderer;
    JsonValue currentData = scenarioArray.get(currentIndex);
    switch (modeButton.getCurrentType()) {
      case SCENARIO:
        ScenarioLogic scenario = new ScenarioLogic(gameScreen, getTextureManager(),
                                                   getAudioManager());
        // If it's custom, set id and leaderboard name to custom values
        if (currentData.getString("id").equals(Constants.CUSTOM_SCENARIO_ID)) {
          scenario.setLeaderboardId(String.format("%s-%d", currentData.getString("id"),
                  customerNumber));
          scenario.setLeaderboardName(String.format("Custom - %d Customers", customerNumber));
          scenario.setRequestTarget(customerNumber);
        }
        gameLogic = scenario;
        gameRenderer = new GameRenderer();
        break;
      case ENDLESS:
        EndlessLogic endless = new EndlessLogic(gameScreen, getTextureManager(), getAudioManager());
        gameLogic = endless;
        gameRenderer = new GameRenderer();
        break;
      default:
        // If it reaches here, it's invalid.
        return;
    }

    gameLogic.setId(currentData.getString("id"));
    gameLogic.setDifficulty(currentDifficulty);

    gameScreen.setGameLogic(gameLogic);
    gameScreen.setGameRenderer(gameRenderer);

    // Move to the game screen
    game.screenController.goToScreen(Constants.GAME_SCREEN_ID);
  }

  /**
   * Go back to the previous {@link Screen} if still on this {@link Screen}.
   */
  public void backScreen() {
    if (!getScreenController().onScreen(Constants.PLAY_SCREEN_ID)) {
      return;
    }
    game.screenController.backScreen();
  }

  @Override
  public void resize(int i, int i1) {

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
