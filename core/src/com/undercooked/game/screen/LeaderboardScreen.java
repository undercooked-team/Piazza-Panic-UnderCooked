package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.undercooked.game.GameType;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.screen.buttons.ModeButton;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.leaderboard.Leaderboard;
import com.undercooked.game.util.leaderboard.LeaderboardController;
import com.undercooked.game.util.leaderboard.LeaderboardEntry;

/**
 * The class for displaying the {@link Leaderboard}s of the game.
 */
public class LeaderboardScreen extends Screen {

  private Texture backgroundTexture;
  private Texture lineTexture;
  private Texture leaderboardTexture;
  private OrthographicCamera camera;
  private FitViewport viewport;
  private Leaderboard leaderboard;
  private Array<LeaderboardEntry> leaderboardData;
  private GameType currentLeaderboardType;
  private int currentIndex;
  private Stage stage;
  private ModeButton modeButton;
  private Array<String> leaderboardIds;
  private GlyphLayout leaderboardNameDisplay;
  private int firstScore;
  private String scoreText;
  private static final int SCORES_AT_ONCE = 5;

  /**
   * Constructor for leaderboard screen.
   *
   * @param game {@link MainGameClass} : The {@link MainGameClass} of the game.
   */
  public LeaderboardScreen(MainGameClass game) {
    super(game);
  }

  @Override
  public void load() {
    TextureManager textureManager = game.getTextureManager();
    textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/MainScreenBackground.jpg");
    textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/LeaderBoard.png");
    textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/line.jpg");
    textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/arrow_left.png");
    textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/arrow_right.png");
    textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/scenario.png");
    textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/scenario_off.png");
    textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/endless.png");
    textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/endless_off.png");
    textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/exittomenu.png");

    game.audioManager.loadMusic("audio/music/MainScreenMusic.ogg", Constants.MUSIC_GROUP);

    leaderboardNameDisplay = new GlyphLayout(game.font, "");

    // Load the leaderboard
    LeaderboardController.loadLeaderboard();

    camera = CameraController.getCamera(Constants.UI_CAMERA_ID);
    viewport = CameraController.getViewport(Constants.UI_CAMERA_ID);

    // Create the stage
    stage = new Stage(viewport, game.batch);

    // Create the mode button
    modeButton = new ModeButton(textureManager);
    modeButton.load(Constants.LEADERBOARD_TEXTURE_ID);

    // And go to the scenario leaderboard by default
    currentLeaderboardType = null;
    goToLeaderboardType(GameType.SCENARIO);
  }

  @Override
  public void postLoad() {
    getAudioManager().postLoad();
  }

  @Override
  public void unload() {
    game.getTextureManager().unload(Constants.LEADERBOARD_TEXTURE_ID, true);

    game.audioManager.unloadMusic("audio/music/MainScreenMusic.ogg");

    currentLeaderboardType = null;
    currentIndex = 0;
    leaderboard = null;
    leaderboardData = null;
    leaderboardNameDisplay = null;

    stage.dispose();
    stage = null;
    modeButton = null;

    // Unload the leaderboard
    LeaderboardController.unloadLeaderboard();
  }

  @Override
  public void show() {
    final TextureManager textureManager = game.getTextureManager();

    // Update the main screen music variable
    game.mainScreenMusic = game.audioManager.getMusic("audio/music/MainScreenMusic.ogg");
    ScreenUtils.clear(0, 0, 0, 0);
    backgroundTexture = textureManager.get("uielements/MainScreenBackground.jpg");
    leaderboardTexture = textureManager.get("uielements/LeaderBoard.png");
    lineTexture = textureManager.get("uielements/line.jpg");
    game.font.getData().setScale(2.5F);

    // Create the buttons
    final Button leftBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/arrow_left.png")
    ));
    final Button rightBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/arrow_right.png")
    ));
    final Button menuBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/exittomenu.png")
    ));

    modeButton.postLoad();
    modeButton.update();

    // Set their position and sizes
    leftBtn.setSize(128, 128);
    leftBtn.setPosition(32, Constants.V_HEIGHT / 2 - leftBtn.getHeight());

    rightBtn.setSize(128, 128);
    rightBtn.setPosition(Constants.V_WIDTH - rightBtn.getWidth() - 32,
            Constants.V_HEIGHT / 2f - rightBtn.getHeight() / 2);

    menuBtn.setSize(473, 144);
    menuBtn.setPosition(16, Constants.V_HEIGHT - menuBtn.getHeight() - 16);

    // Add ClickListeners to the buttons
    leftBtn.addListener(new ClickListener() {
      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        setIndex(currentIndex - 1);
      }
    });

    rightBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        setIndex(currentIndex + 1);
      }
    });

    modeButton.setListener(new Listener<GameType>() {
      @Override
      public void tell(GameType value) {
        goToLeaderboardType(value);
      }
    });

    modeButton.setPosition(Constants.V_WIDTH / 2, 72);

    menuBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        changeScreenToMain();
      }
    });

    // Add the buttons to the stage
    stage.addActor(leftBtn);
    stage.addActor(rightBtn);
    stage.addActor(menuBtn);
    modeButton.addToStage(stage);

    // Add a scroll listener to go up / down on the scores
    stage.addListener(new InputListener() {
      @Override
      public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
        // Scroll the leaderboard
        scrollLeaderboard(amountY);

        return super.scrolled(event, x, y, amountX, amountY);
      }
    });

    // Update the name text
    updateNameText();

    // Set the input processor
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float delta) {
    // TODO Auto-generated method stub
    ScreenUtils.clear(0, 0, 0, 0);
    game.batch.setProjectionMatrix(camera.combined);
    float gameResolutionX = Constants.V_WIDTH;
    float gameResolutionY = Constants.V_HEIGHT;

    final float lineX = gameResolutionX / 10.0f;
    final float lineY = gameResolutionY / 10.0f;
    final float boxWidth = 8 * lineX;
    final float boxHeight = 8 * lineY;
    final float entryhi = 7 * lineY;
    final float topentry = lineY + entryhi;
    final float eachentryhi = entryhi / 7;

    // Play the menu music
    game.mainScreenMusic.play();

    game.batch.begin();
    game.batch.draw(backgroundTexture, 0, 0, gameResolutionX, gameResolutionY);
    game.batch.draw(leaderboardTexture, lineX, lineY, boxWidth, boxHeight);
    game.batch.draw(lineTexture, lineX, lineY + eachentryhi,
            boxWidth, gameResolutionY / 100.0f);
    game.batch.draw(lineTexture, lineX, lineY + 2 * eachentryhi,
            boxWidth, gameResolutionY / 100.0f);
    game.batch.draw(lineTexture, lineX, lineY + 3 * eachentryhi,
            boxWidth, gameResolutionY / 100.0f);
    game.batch.draw(lineTexture, lineX, lineY + 4 * eachentryhi,
            boxWidth, gameResolutionY / 100.0f);
    game.batch.draw(lineTexture, lineX, lineY + 5 * eachentryhi,
            boxWidth, gameResolutionY / 100.0f);
    game.batch.draw(lineTexture, lineX, lineY + 6 * eachentryhi,
            boxWidth, gameResolutionY / 100.0f);
    game.font.setColor(Color.BLACK);

    game.font.draw(game.batch, leaderboardNameDisplay,
            Constants.V_WIDTH / 2f - (leaderboardNameDisplay.width / 2),
            17 * gameResolutionY / 20.0f);
    game.font.draw(game.batch, "Name",
            4 * gameResolutionX / 20.0f, 15.5f * gameResolutionY / 20.0f);
    game.font.draw(game.batch, scoreText,
            12 * gameResolutionX / 20.0f + 40, 15.5f * gameResolutionY / 20.0f);

    // If the leaderboard isn't null...
    if (leaderboardData != null) {
      int leaderboardIndex = 1;
      for (int scoreno = firstScore;
           scoreno < Math.min(leaderboardData.size, firstScore + SCORES_AT_ONCE);
           scoreno++) {
        LeaderboardEntry thisEntry = leaderboardData.get(scoreno);
        float ycord = topentry - leaderboardIndex * eachentryhi - 0.3f * eachentryhi;
        String name = thisEntry.name;
        String stringScore = LeaderboardController.scoreToString(currentLeaderboardType,
                thisEntry.score);
        game.font.draw(game.batch, scoreno + 1 + ". " + name, 3 * gameResolutionX / 20.0f, ycord);
        game.font.draw(game.batch, stringScore, 11 * gameResolutionX / 20.0f + 100, ycord);
        leaderboardIndex++;
      }
    }

    game.font.setColor(Color.WHITE);

    game.batch.end();
    stage.draw();

    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
      changeScreenToMain();
    } else {
      stage.act();
    }
  }

  /**
   * Go to the {@link Leaderboard}s for a certain {@link GameType}.
   *
   * @param gameType {@link GameType} : The {@link GameType} to show the
   *                 {@link Leaderboard}s for.
   */
  public void goToLeaderboardType(GameType gameType) {
    // If it's already that leaderboard, just return
    if (gameType == currentLeaderboardType) {
      return;
    }

    // Update the textures for the buttons
    modeButton.setCurrentType(gameType);
    modeButton.update();

    // Update the current type
    currentLeaderboardType = gameType;

    // Update the visual
    switch (gameType) {
      case SCENARIO:
        scoreText = "Time";
        break;
      case ENDLESS:
        scoreText = "Served";
        break;
      default:
        scoreText = "";
    }

    // Otherwise, get the ids for the leaderboards of this type
    updateIds();

    setIndex(0);
  }

  /**
   * Update the IDs to the currently displayued {@link GameType}.
   */
  private void updateIds() {
    leaderboardIds = LeaderboardController.getIds(currentLeaderboardType);
  }

  /**
   * Scroll the leaderboard by an amount of {@link LeaderboardEntry}.
   *
   * @param amountY {@link float} : The number of {@link LeaderboardEntry}s to scroll past.
   */
  private void scrollLeaderboard(float amountY) {
    if (leaderboardData == null) return;
    // Add the scroll
    firstScore += Math.signum(amountY);
    // Clamp it between 0 and the number of possible
    // Allows one scroll below the lowest to show that there's
    // no more to scroll through.
    firstScore = Math.max(0, Math.min(firstScore, leaderboardData.size - (SCORES_AT_ONCE - 1)));
  }

  /**
   * Set the currently viewed {@link Leaderboard} by the index it is in
   * the leaderboard IDs for the current {@link GameType}.
   *
   * @param index {@link int} : The index of the {@link Leaderboard}.
   */
  public void setIndex(int index) {
    // If leaderboardIDs is null, return
    if (leaderboardIds == null) {
      return;
    }
    // If it's empty, just ignore
    if (leaderboardIds.size == 0) {
      leaderboard = null;
      leaderboardData = null;
      updateNameText();
      return;
    }

    // Otherwise, make sure it's in the range
    while (index < 0) {
      index += leaderboardIds.size;
    }
    index %= leaderboardIds.size;

    // And update the currentIndex
    currentIndex = index;

    // Then set it to use the id
    if (leaderboardIds.size <= 0) {
      return;
    }
    showLeaderboard(currentLeaderboardType, leaderboardIds.get(index));
  }

  /**
   * Show the {@link Leaderboard} of the {@link GameType} by its id.
   *
   * @param leaderboardType {@link GameType} : The {@link GameType} of the {@link Leaderboard}.
   * @param id    {@link String} : The id of the {@link Leaderboard}.
   */
  protected void showLeaderboard(GameType leaderboardType, String id) {
    // Get the leaderboard
    leaderboard = LeaderboardController.getLeaderboard(leaderboardType, id);
    // If it's null, set leaderboard data to null
    if (leaderboard == null) {
      leaderboardData = null;
      leaderboardNameDisplay.setText(game.font, "");
      return;
    }
    // Set the leaderboardData
    leaderboardData = leaderboard.copyLeaderboard();

    // Update the name text
    updateNameText();

    firstScore = 0;
  }

  /**
   * Show the {@link Leaderboard} of the id, of the current
   * {@link GameType}.
   *
   * @param id {@link String} : The id of the {@link Leaderboard} to show.
   */
  public void showLeaderboard(String id) {
    showLeaderboard(currentLeaderboardType, id);
  }

  /**
   * Update the displayed {@link Leaderboard} name text at the top of the
   * leaderboard display.
   */
  protected void updateNameText() {
    game.font.setColor(Color.BLACK);
    if (leaderboard == null) {
      leaderboardNameDisplay.setText(game.font, "No Leaderboards Available");
      game.font.setColor(Color.WHITE);
      return;
    }
    if (leaderboard.name != null) {
      leaderboardNameDisplay.setText(game.font, leaderboard.name);
    } else {
      leaderboardNameDisplay.setText(game.font, "");
    }
    game.font.setColor(Color.WHITE);
  }

  /**
   * Add data to leaderboard.
   *
   * @param leaderboardType           {@link GameType} : The leaderboard {@link GameType}.
   * @param id              {@link String} : The id of the {@link Leaderboard}.
   * @param leaderboardName {@link String} : The name of the {@link Leaderboard} to set it to if
   *                        it does not have one already.
   * @param name            {@link String} : The name of the {@link LeaderboardEntry}.
   * @param score           {@code float} : The score of the {@link LeaderboardEntry}.
   */
  public void addLeaderBoardData(GameType leaderboardType, String id,
                                 String leaderboardName, String name, float score) {
    // Only continue if it's loaded
    if (!LeaderboardController.isLoaded()) {
      return;
    }
    // Add it to the leaderboard
    LeaderboardController.addEntry(leaderboardType, id, leaderboardName, name, score);
    // And save the leaderboard
    LeaderboardController.saveLeaderboard();
    // If the currently displayed leaderboard is the same, update the IDs.
    updateIds();
  }

  /**
   * Change screen back to main menu screen.
   */
  public void changeScreenToMain() {
    if (game.gameMusic != null) {
      game.gameMusic.pause();
    }
    if (getScreenController().onScreen(Constants.MAIN_SCREEN_ID)) {
      return;
    }
    game.screenController.goToScreen(Constants.MAIN_SCREEN_ID);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
  }

  @Override
  public void pause() {
    // TODO Auto-generated method stub

  }

  @Override
  public void resume() {
    // TODO Auto-generated method stub

  }

  public void hide() {
    // TODO Auto-generated method stub

  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub

  }
}
