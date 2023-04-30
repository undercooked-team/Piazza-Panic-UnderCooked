package com.undercooked.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.files.SettingsControl;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSettings;
import com.undercooked.game.screen.*;
import com.undercooked.game.station.StationController;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

/**
 * The main game class of the game. This is the class starts the game,
 * creating all other classes that run the game
 */
public class MainGameClass extends Game {

	/** The main screen music of the game. */
	public Music mainScreenMusic;

	/** The game music. */
	public Music gameMusic;

	/** The {@link ScreenController} that controls the {@link Screen}s of the game. */
	public final ScreenController screenController;
	private final AssetManager assetManager;

	/** The {@link AudioManager} which controls audio for the game. */
	public final AudioManager audioManager;

	/** The {@link TextureManager} which controls {@link Texture}s for the game. */
	public final TextureManager textureManager;

	/** The {@link MapManager} which creates the {@link com.undercooked.game.map.Map}s for the game. */
	public final MapManager mapManager;

	/** The {@link AudioSettings} which controls the audio settings from within the game. */
	public final AudioSettings audioSettings;

	/** The {@link SettingsControl} which controls how the settings json file is loaded, modified and stored. */
	public final SettingsControl settingsControl;

	/** The {@link SpriteBatch} to render the {@link Texture}s of the game. */
	public static SpriteBatch batch;

	/** The {@link BitmapFont} to render the text for the game. */
	public static BitmapFont font;

	/** The {@link BitmapFont} to render shapes for the game. */
	public static ShapeRenderer shapeRenderer;

	/**
	 * Constructor for the Game.
	 */
	public MainGameClass() { // SoundStateChecker soundChecker) {
		settingsControl = new SettingsControl("settings.json");
		audioSettings = new AudioSettings(this, settingsControl);
		assetManager = new AssetManager();
		// assetManager.setLoader(TiledMap.class, new TmxMapLoader(new
		// InternalFileHandleResolver()));
		audioManager = new AudioManager(assetManager); // , soundChecker);
		textureManager = new TextureManager(assetManager);
		mapManager = new MapManager(textureManager, audioManager);
		screenController = new ScreenController(this, assetManager);
	}

	/**
	 * Things that are loaded into the game that won't be unloaded until the game is
	 * over.
	 */
	public void load() {
		// Load the settings
		settingsControl.loadData();

		// Load the controls
		InputController.loadControls();
	}

	@Override
	public void create() {

		// Load the game
		load();

		// JsonFormat.main(new String[] {});

		// Load the default assets
		assetManager.finishLoading();

		// Renderers
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		shapeRenderer.setAutoShapeType(true);

		// =============MUSIC=INITIALISATION===========================
		audioSettings.loadVolumes();

		// Camera Initialisation
		CameraController.getCamera(Constants.WORLD_CAMERA_ID);
		CameraController.getCamera(Constants.UI_CAMERA_ID);

		// ===================FONT=INITIALISATION======================
		font = new BitmapFont(Gdx.files.internal("uielements/font.fnt"), Gdx.files.internal("uielements/font.png"),
				false);
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		// ===============GAME=SCREEN=INITIALISATION===========================

		screenController.addScreen(new MainScreen(this), Constants.MAIN_SCREEN_ID);
		screenController.addScreen(new GameScreen(this), Constants.GAME_SCREEN_ID);
		screenController.addScreen(new LeaderboardScreen(this), Constants.LEADERBOARD_SCREEN_ID);
		screenController.addScreen(new PauseScreen(this), Constants.PAUSE_SCREEN_ID);
		screenController.addScreen(new WinScreen(this), Constants.WIN_SCREEN_ID);
		screenController.addScreen(new LossScreen(this), Constants.LOSS_SCREEN_ID);
		screenController.addScreen(new PlayScreen(this), Constants.PLAY_SCREEN_ID);

		screenController.nextScreen(Constants.MAIN_SCREEN_ID);

		// ==============================================================================================================
	}

	@Override
	public void resize(int width, int height) {
		CameraController.getViewport(Constants.WORLD_CAMERA_ID).update(width, height);
		CameraController.getViewport(Constants.UI_CAMERA_ID).update(width, height);
	}

	/**
	 * @return {@link Screen} : The currently selected {@link Screen}.
	 */
	public Screen getScreen() {
		return (Screen) super.getScreen();
	}

	/**
	 * @return {@link AudioManager} : The {@link AudioManager} for the game.
	 */
	public AudioManager getAudioManager() {
		return audioManager;
	}

	/**
	 * @return {@link TextureManager} : The {@link TextureManager} for the game.
	 */
	public TextureManager getTextureManager() {
		return textureManager;
	}

	/**
	 * @return {@link AudioSettings} : The {@link AudioSettings} for the game.
	 */
	public AudioSettings getAudioSettings() {
		return audioSettings;
	}

	/**
	 * @return {@link SpriteBatch} : The {@link SpriteBatch} for the game.
	 */
	public static SpriteBatch getSpriteBatch() {
		return batch;
	}

	/**
	 * @return {@link ShapeRenderer} : The {@link ShapeRenderer} for the game.
	 */
	public static ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		mapManager.unload();
		assetManager.dispose();
		batch.dispose();
		shapeRenderer.dispose();
	}
}
