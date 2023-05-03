package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undercooked.game.GameType;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSliders;
import com.undercooked.game.audio.Slider;
import com.undercooked.game.logic.Difficulty;
import com.undercooked.game.logic.EndlessLogic;
import com.undercooked.game.logic.GameLogic;
import com.undercooked.game.logic.ScenarioLogic;
import com.undercooked.game.logic.TutorialLogic;
import com.undercooked.game.render.GameRenderer;
import com.undercooked.game.render.TutorialRenderer;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.SaveLoadGame;


/**
 * A class for the main {@link Screen} of the game,
 * which is opened when the game starts.
 */
public class MainScreen extends Screen {

  private final float buttonwidth;
  private final float buttonheight;

  private Texture background;

  private final OrthographicCamera camera;
  private final Viewport viewport;

  private Stage stage;

  private enum State {
    MAIN,
    AUDIO
  }

  private State state;

  private AudioSliders audioSliders;
  private Slider musicSlider;
  private Slider gameSlider;


  /**
   * Constructor for the {@link MainScreen}.
   *
   * @param game {@link MainGameClass} : The {@link MainGameClass} of the game.
   */
  public MainScreen(final MainGameClass game) {
    super(game);
    this.buttonwidth = Constants.V_WIDTH / 3f;
    this.buttonheight = Constants.V_HEIGHT / 6f;

    this.camera = CameraController.getCamera(Constants.UI_CAMERA_ID);
    this.viewport = CameraController.getViewport(Constants.UI_CAMERA_ID);
  }

  @Override
  public void load() {
    TextureManager textureManager = game.getTextureManager();
    textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/vButton.jpg");
    textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/newgame.png");
    textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/MainScreenBackground.jpg");
    textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/leaderboard1.png");
    textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/audio.png");
    textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/background.png");
    textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/game/exit.png");
    textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/game/load.png");
    textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/tutorial.png");

    game.audioManager.loadMusic("audio/music/MainScreenMusic.ogg", Constants.MUSIC_GROUP);
  }

  @Override
  public void unload() {
    game.getTextureManager().unload(Constants.MENU_TEXTURE_ID, true);

    game.audioManager.unloadMusic("audio/music/MainScreenMusic.ogg");
    stage.dispose();
  }

  @Override
  public void postLoad() {
    // AudioManager post load
    game.audioManager.postLoad();

    state = State.MAIN;

    game.mainScreenMusic = game.audioManager.getMusic("audio/music/MainScreenMusic.ogg");

    final TextureManager textureManager = game.getTextureManager();
    final Texture startBtnTex = textureManager.get("uielements/newgame.png");
    background = textureManager.get("uielements/MainScreenBackground.jpg");
    final Texture leaderboardBtnTex = textureManager.get("uielements/leaderboard1.png");
    final Texture audioBtnTex = textureManager.get("uielements/audio.png");
    final Texture audioEdit = textureManager.get("uielements/background.png");
    final Texture exitBtnTex = textureManager.get("uielements/game/exit.png");
    final Texture sliderButtonTex = textureManager.get("uielements/vButton.jpg");

    final Button startBtn = new Button(new TextureRegionDrawable(startBtnTex));
    final Button leaderboardBtn = new Button(new TextureRegionDrawable(leaderboardBtnTex));
    final Button audioBtn = new Button(new TextureRegionDrawable(audioBtnTex));
    final Button exitBtn = new Button(new TextureRegionDrawable(exitBtnTex));

    final Button loadGameBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/game/load.png")
    ));
    final Button tutorialBtn = new Button(new TextureRegionDrawable(
            textureManager.get("uielements/tutorial.png")
    ));

    leaderboardBtn.setSize(buttonwidth, buttonheight);
    audioBtn.setSize(buttonwidth, buttonheight);
    exitBtn.setSize(buttonwidth, buttonheight);
    startBtn.setSize(buttonwidth, buttonheight);
    loadGameBtn.setSize(buttonwidth, buttonheight);
    tutorialBtn.setSize(buttonwidth, buttonheight);

    startBtn.setPosition(Constants.V_WIDTH / 10.0f,
            4 * Constants.V_HEIGHT / 5.0f - buttonheight / 2);
    leaderboardBtn.setPosition(Constants.V_WIDTH / 10.0f,
            3 * Constants.V_HEIGHT / 5.0f - buttonheight / 2);
    audioBtn.setPosition(Constants.V_WIDTH / 10.0f,
            2 * Constants.V_HEIGHT / 5.0f - buttonheight / 2);
    exitBtn.setPosition(Constants.V_WIDTH / 10.0f,
            Constants.V_HEIGHT / 5.0f - buttonheight / 2);
    loadGameBtn.setPosition(Constants.V_WIDTH / 10.0f + startBtn.getWidth() + 50,
            4 * Constants.V_HEIGHT / 5.0f - buttonheight / 2);
    tutorialBtn.setPosition(Constants.V_WIDTH / 10.0f + startBtn.getWidth() + 50,
            3 * Constants.V_HEIGHT / 5.0f - buttonheight / 2);

    audioBtn.addListener(new ClickListener() {
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (state == State.MAIN) {
          state = State.AUDIO;
        } else {
          state = State.MAIN;
        }
        super.touchUp(event, x, y, pointer, button);
      }
    });
    startBtn.addListener(new ClickListener() {
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        // Go to play screen, if still on the main screen
        if (!getScreenController().onScreen(Constants.MAIN_SCREEN_ID)) {
          return;
        }
        getScreenController().nextScreen(Constants.PLAY_SCREEN_ID);
      }
    });
    leaderboardBtn.addListener(new ClickListener() {
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        if (!game.screenController.onScreen(Constants.MAIN_SCREEN_ID)) {
          return;
        }
        getScreenController().nextScreen(Constants.LEADERBOARD_SCREEN_ID);
      }
    });
    exitBtn.addListener(new ClickListener() {
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        Gdx.app.exit();
        super.touchUp(event, x, y, pointer, button);
      }
    });
    loadGameBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (!game.screenController.onScreen(Constants.MAIN_SCREEN_ID)) {
          return;
        }
        loadGame();
      }
    });
    tutorialBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (!game.screenController.onScreen(Constants.MAIN_SCREEN_ID)) {
          return;
        }
        GameScreen gameScreen = (GameScreen) (
                getScreenController().getScreen(Constants.GAME_SCREEN_ID)
        );
        TutorialLogic tutorialLogic = new TutorialLogic(gameScreen, textureManager,
                                                        getAudioManager());
        TutorialRenderer tutorialRenderer = new TutorialRenderer(tutorialLogic);

        gameScreen.setGameLogic(tutorialLogic);
        gameScreen.setGameRenderer(tutorialRenderer);

        tutorialLogic.setDifficulty(Difficulty.EASY);

        game.screenController.goToScreen(Constants.GAME_SCREEN_ID);
      }
    });

    stage = new Stage(viewport, game.batch);

    stage.addActor(startBtn);
    stage.addActor(leaderboardBtn);
    stage.addActor(audioBtn);
    stage.addActor(exitBtn);
    stage.addActor(loadGameBtn);
    stage.addActor(tutorialBtn);

    audioSliders = game.getAudioSettings().createAudioSliders(audioBtn.getX() + 650,
            audioBtn.getY() - 10, stage, audioEdit, sliderButtonTex);

    musicSlider = audioSliders.getSlider(0);
    musicSlider.setTouchable(Touchable.disabled);

    gameSlider = audioSliders.getSlider(1);
    gameSlider.setTouchable(Touchable.disabled);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float delta) {
    // TODO Auto-generated method stub
    ScreenUtils.clear(0, 0, 0, 0);
    game.batch.setProjectionMatrix(camera.combined);
    game.mainScreenMusic.play();

    game.batch.begin();
    game.batch.draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
    game.batch.end();
    stage.act();
    stage.draw();
    changeScreen(state);

    if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
      state = State.MAIN;
    }
  }

  /**
   * Change screen to specified state.
   *
   * @param state {@link State} : The {@link State} to set the screen to.
   */
  public void changeScreen(State state) {
    if (state == State.AUDIO) {

      musicSlider.setTouchable(Touchable.enabled);
      gameSlider.setTouchable(Touchable.enabled);

      game.batch.begin();

      audioSliders.render(game.batch);

      game.batch.end();
    } else {
      musicSlider.setTouchable(Touchable.disabled);
      gameSlider.setTouchable(Touchable.disabled);
    }
  }

  /**
   * Load the game if there is data from a saved game to load.
   */
  public void loadGame() {
    JsonValue saveData = SaveLoadGame.loadGameJson();
    if (saveData == null) {
      return;
    }
    GameType gameType = GameType.valueOf(saveData.getString("game_type"));
    GameScreen gameScreen = (GameScreen) game.screenController.getScreen(Constants.GAME_SCREEN_ID);
    GameLogic gameLogic;
    GameRenderer gameRenderer;
    switch (gameType) {
      case SCENARIO:
        gameLogic = new ScenarioLogic(gameScreen, getTextureManager(), getAudioManager());
        // If it's custom, set id and leaderboard name to custom values
        gameRenderer = new GameRenderer();
        break;
      case ENDLESS:
        gameLogic = new EndlessLogic(gameScreen, getTextureManager(), getAudioManager());
        gameRenderer = new GameRenderer();
        break;
      default:
        // If it reaches here, it's invalid.
        return;
    }

    gameLogic.setId(saveData.getString("scenario_id"));
    gameLogic.setDifficulty(Difficulty.asInt(saveData.getString("difficulty")));

    gameScreen.setGameLogic(gameLogic);
    gameScreen.setGameRenderer(gameRenderer);

    gameLogic.resetOnLoad = false;

    // Move to the game screen
    game.screenController.goToScreen(Constants.GAME_SCREEN_ID);

    // Load the game
    SaveLoadGame.loadGame(gameLogic, saveData);
  }

  @Override
  public void resize(int width, int height) {
    // TODO Auto-generated method stub
  }

  @Override
  public void pause() {
    // TODO Auto-generated method stub

  }

  @Override
  public void resume() {
    // TODO Auto-generated method stub

  }

  @Override
  public void hide() {
    // TODO Auto-generated method stub

  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub
  }
}
