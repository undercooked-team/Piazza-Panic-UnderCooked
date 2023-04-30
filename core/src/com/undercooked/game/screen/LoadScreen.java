package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

/**
 * The {@link Screen} used whilst changing between two different
 * {@link Screen}s to load the next {@link Screen}.
 */
public class LoadScreen extends Screen {
    private Screen previous;
    private Screen next;
    private final AssetManager assetManager;
    private final MainGameClass game;
    private final OrthographicCamera camera;
    private long lastLoad;

    /**
     * Constructor for the {@link LoadScreen}.
     *
     * @param assetManager {@link AssetManager} : The {@link AssetManager} to load.
     * @param game {@link MainGameClass} : The game's main class.
     */
    public LoadScreen(AssetManager assetManager, MainGameClass game) {
        super(game);
        this.assetManager = assetManager;
        this.game = game;
        this.camera = CameraController.getCamera(Constants.UI_CAMERA_ID);
    }

    @Override
    public void load() {
        // Remove the input processor
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void unload() {

    }

    @Override
    public void show() {

    }

    /**
     * Updating the {@link LoadScreen}.
     * @param moveToNext {@link boolean} : Whether the game should move to the next
     *                                     {@link Screen} if the game has finished
     *                                     loading, or not.
     */
    public void update(boolean moveToNext) {

        // Update the AssetManager for a short bit before moving on
        while (TimeUtils.timeSinceMillis(lastLoad) <= 100
                && !assetManager.isFinished()) {
            // Try to load, but don't crash if it fails
            try {
                assetManager.update();
            } catch (GdxRuntimeException e) {
                System.out.println(e);
            }
        }

        lastLoad = TimeUtils.millis();

        // Check if the AssetManager is finished
        if (assetManager.isFinished() && moveToNext) {
            // Post load function for the Screen
            next.postLoad();
            // Then swap to the screen that was loading
            System.out.println("Swapped to Screen " + next);
            game.setScreen(next);
            // Once loaded, tell the Screen what screen loaded it
            next.fromScreen(previous);
        }

    }

    @Override
    public void render(float delta) {

        // Update the screen
        update(true);

        // Render the previous screen underneath (if there is one)
        if (previous != null) {
            previous.renderScreen(delta);
        }

        // Render the loading bar
        renderBar();

    }

    /**
     * Renders the loading bar at the bottom left of the window.
     */
    public void renderBar() {
        // Then render a loading bar
        // Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ShapeRenderer shape = game.shapeRenderer;
        shape.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        // Draw a rectangle with the shapeRenderer, at the bottom of the screen
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.WHITE);
        shape.rect(20,20,200,50);
        shape.setColor(Color.GREEN);
        shape.rect(30,30,180*assetManager.getProgress(),30);
        shape.setColor(Color.WHITE);
        shape.end();
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

    /**
     * Sets the {@link #previous} and {@link #next} variables.
     *
     * @param previous {@link Screen} : The {@link Screen} that is just being changed from.
     * @param next {@link Screen} : The {@link Screen} that is being changed to.
     */
    public void setScreens(Screen previous, Screen next) {
        this.previous = previous;
        this.next = next;
    }

    /**
     * Start loading.
     */
    public void start() {
        // Make the game change to this screen
        game.setScreen(this);

        // Set the load time
        lastLoad = TimeUtils.millis();

        // Then update once before moving on to the next frame
        // This means that if it's done in one go, then it can just go
        // straight to the screen rather than wait a frame
        update(false);

        // Render the bar, if it's not finished updating yet
        if (!assetManager.isFinished()) {
            renderBar();
        }
    }
}
