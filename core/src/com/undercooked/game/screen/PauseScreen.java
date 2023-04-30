package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSliders;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.SaveLoadGame;

/**
 * A class for the {@link PauseScreen} when the player
 * pauses the {@link GameScreen} as they are playing.
 */
public class PauseScreen extends Screen {

    private Screen displayScreen;
    private final SpriteBatch batch;
    private Stage stage;
    private AudioSliders audioSliders;

    /** The {@link GameScreen} that paused the game. */
    GameScreen gameScreen;

    private boolean saveEnabled;

    /**
     * Constructor for the {@link PauseScreen}.
     * @param game {@link MainGameClass} : The {@link MainGameClass} for the game.
     */
    public PauseScreen(MainGameClass game) {
        super(game);
        this.batch = MainGameClass.batch;
    }

    @Override
    public void load() {
        // Load menu Textures
        TextureManager textureManager = getTextureManager();
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/settings.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/background.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/game/exit.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/game/resume.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/audio2.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/background.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/vButton.jpg");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/vControl.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/background.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/vButton.jpg");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/game/save.png");

        // Create Stage
        stage = new Stage(CameraController.getViewport(Constants.UI_CAMERA_ID));
    }

    @Override
    public void unload() {
        // Unload all Textures
        getTextureManager().unload(Constants.PAUSE_TEXTURE_ID, true);

        // Dispose stage
        stage.dispose();
    }

    /**
     * Set the {@link Screen} to display on the {@link PauseScreen}.
     * @param screen The {@link Screen}.
     */
    @Override
    public void fromScreen(Screen screen) {
        displayScreen = screen;
    }

    /**
     * Set up the visuals of the PauseScreen when loaded.
     */
    @Override
    public void postLoad() {
        // Set up the Stage
        TextureManager textureManager = getTextureManager();

        // Create the Buttons
        Button unpause = new Button(new TextureRegionDrawable(textureManager.get("uielements/game/resume.png")));
        Button menu = new Button(new TextureRegionDrawable(textureManager.get("uielements/game/exit.png")));

        // Set the Button visuals
        unpause.setSize(200,100);
        menu.setSize(200,100);

        // Set the Button positions
        unpause.setPosition(100, 100);
        menu.setPosition(100, 300);

        // Add Button listeners
        unpause.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resumeGame();
            }
        });

        menu.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                quitToMenu();
            }
        });

        // Add the Buttons to the Stage
        stage.addActor(unpause);
        stage.addActor(menu);

        // Only make and add the save button if save is enabled
        if (saveEnabled) {
            // Make the button
            Button save = new Button(new TextureRegionDrawable(textureManager.get("uielements/game/save.png")));

            // Set position and size
            save.setSize(200, 100);
            save.setPosition(100, 200);

            // Add the save's listener
            save.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SaveLoadGame.saveGame(gameScreen.gameLogic);
                    quitToMenu();
                }
            });

            // Add the button to the stage
            stage.addActor(save);
        }

        // Add the audio sliders
        audioSliders = game.getAudioSettings().createAudioSliders(320, 100, stage, textureManager.get("uielements/background.png"), textureManager.get("uielements/vButton.jpg"));

        // Finally, set the Gdx inputProcessor to use the stage
        Gdx.input.setInputProcessor(stage);

    }

    /**
     * Quit to the main menu.
     */
    public void quitToMenu() {
        // Go to the main menu, if still on the pause screen
        if (!getScreenController().onScreen(Constants.PAUSE_SCREEN_ID)) return;
        game.screenController.goToScreen(Constants.MAIN_SCREEN_ID);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // First render the screen below, if there is one.
        if (displayScreen != null) {
            displayScreen.renderScreen(delta);
        } else {
            // Otherwise, just draw a blank screen.
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        // Render the pause menu over the top of the previous screen
        // If escape is pressed, resume
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            resumeGame();
        } else {
            // Otherwise, check button
            stage.act(delta);
        }
        stage.draw();
        batch.begin();
        audioSliders.render(game.batch);
        batch.end();

    }

    /**
     * Resume the game.
     */
    private void resumeGame() {
        if (!getScreenController().onScreen(Constants.PAUSE_SCREEN_ID)) return;
        // Go to the previous screen.
        game.screenController.backScreen();
        // Set the screen to null.
        displayScreen = null;
    }

    /**
     * @param canSave {@code boolean} : {@code true} if the game can be saved,
     *                                  {@code false} if not.
     */
    public void setSaveEnabled(boolean canSave) {
        this.saveEnabled = canSave;
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
