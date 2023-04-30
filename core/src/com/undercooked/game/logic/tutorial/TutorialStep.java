package com.undercooked.game.logic.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.util.Listener;

/**
 * A class for a step during the {@link com.undercooked.game.logic.TutorialLogic}.
 */
public class TutorialStep {

    /** The x position of the camera. */
    protected float x;

    /** The y position of the camera. */
    protected float y;

    /** The current character of the text. */
    protected int textIndex;

    /** The time since the last text. */
    protected float sinceLastText;

    /** The number of characters per second. */
    protected float textSpeed;

    /** The text to display. */
    protected String text;

    /** The text currently being displayed. */
    protected String displayText;

    /** Whether the step should display text or not. */
    protected boolean hasText;

    /** Whether inputs for the {@link com.undercooked.game.entity.cook.Cook}s should be processed for this step or not. */
    protected boolean processInput;

    /** Whether the text is skippable or not. */
    protected boolean skippable;

    /** The {@link Listener} to call when the step is over. */
    protected Listener<TutorialStep> endListener;

    /** If the tutorial step has started or not.. */
    private boolean started;

    /**
     * Constructor for the {@link TutorialStep}.
     * @param text {@link String} : The text to display
     * @param textSpeed {@link float} : The number of characters to add per second.
     */
    public TutorialStep(String text, float textSpeed) {
        this.textIndex = 0;
        this.text = text;
        this.displayText = "";
        this.sinceLastText = 0f;
        this.textSpeed = 1f / textSpeed;
        this.skippable = true;
        this.hasText = true;
        this.x = -1;
        this.y = -1;
    }

    /**
     * Constructor for the {@link TutorialStep}.
     * @param text {@link String} : The text to display
     * @param textSpeed {@link float} : The number of characters to add per second.
     * @param x {@code float} : The x position of the camera.
     * @param y {@code float} : The y position of the camera.
     */
    public TutorialStep(String text, float textSpeed, float x, float y) {
        this(text, textSpeed);
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor for the {@link TutorialStep}.
     * @param text {@link String} : The text to display
     * @param textSpeed {@link float} : The number of characters to add per second.
     * @param processInput {@code boolean} : Whether inputs should or should not be
     *                                       processed for this step.
     */
    public TutorialStep(String text, float textSpeed, boolean processInput) {
        this(text, textSpeed, -1, -1, processInput);
    }

    /**
     * Constructor for the {@link TutorialStep}.
     * @param text {@link String} : The text to display
     * @param textSpeed {@link float} : The number of characters to add per second.
     * @param x {@code float} : The x position of the camera.
     * @param y {@code float} : The y position of the camera.
     * @param processInput {@code boolean} : Whether inputs should or should not be
     *                                       processed for this step.
     */
    public TutorialStep(String text, float textSpeed, float x, float y, boolean processInput) {
        this(text, textSpeed, x, y);
        this.processInput = processInput;
    }

    /**
     * Function that is called when the {@link TutorialStep} is
     * first started.
     */
    public void start() {
        started = true;
    }

    /**
     * Update the step.
     *
     * @param delta {@code float} : The time since the last frame.
     */
    public void update(float delta) {
        // Update display text
        if (textIndex < text.length()) {
            sinceLastText += delta;
            while (sinceLastText >= textSpeed) {
                sinceLastText -= textSpeed;
                displayText += text.charAt(textIndex);
                textIndex++;
                if (textIndex >= text.length()) {
                    break;
                }
            }
        }
    }

    /**
     * Called when the step is finished. Should tell the
     * {@link #endListener} that the {@link TutorialStep} is
     * finished.
     */
    public void finished() {
        endListener.tell(this);
    }

    /**
     * Set to load any needed {@link com.badlogic.gdx.graphics.Texture}s
     * using the {@link TextureManager}.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     */
    public void load(TextureManager textureManager) {
        load(textureManager, "default");
    }

    /**
     * Set to load any needed {@link com.badlogic.gdx.graphics.Texture}s
     * using the {@link TextureManager} to the texture group.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     * @param textureGroup {@link String} : The texture group to use.
     */
    public void load(TextureManager textureManager, String textureGroup) {

    }

    /**
     * Post load the {@link TutorialStep}.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     */
    public void postLoad(TextureManager textureManager) {

    }

    /**
     * Unload the {@link TutorialStep}.
     */
    public void unload() {

    }

    /**
     * Set the text speed of the {@link TutorialStep}.
     * @param textSpeed {@code float} : The new text speed.
     */
    public void setTextSpeed(float textSpeed) {
        this.textSpeed = textSpeed;
    }

    /**
     * Set the if the {@link TutorialStep} is skippable or not.
     * @param skippable {@code boolean} : Whether the {@link TutorialStep} should be
     *                                    skippable or not.
     */
    public void setCanSkip(boolean skippable) {
        this.skippable = skippable;
    }

    /**
     * Set the if the {@link TutorialStep} has text or not.
     * @param hasText {@code boolean} : Whether the {@link TutorialStep} should
     *                                  have text or not.
     */
    public void setHasText(boolean hasText) {
        this.hasText = hasText;
    }

    /**
     * Set the end listener of the {@link TutorialStep}.
     * @param endListener {@code float} : The {@link Listener} to call when the
     *                                    {@link TutorialStep} has finished.
     */
    public void setEndListener(Listener<TutorialStep> endListener) {
        this.endListener = endListener;
    }

    /**
     * @return {@code float} : The x position that the camera should be at.
     */
    public float getX() {
        return x;
    }

    /**
     * @return {@code float} : The y position that the camera should be at.
     */
    public float getY() {
        return y;
    }

    /**
     * @return {@link String} : The current text to display.
     */
    public String getDisplayText() {
        return displayText;
    }

    /**
     * Render an {@link Entity} for the {@link TutorialStep}.
     *
     * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
     * @param entity {@link Entity} : The {@link Entity} to draw for.
     */
    public void render(SpriteBatch batch, Entity entity) {

    }

    /**
     * Render an {@link Entity} for the {@link TutorialStep}.
     *
     * @param shape {@link SpriteBatch} : The {@link ShapeRenderer} to use.
     * @param entity {@link Entity} : The {@link Entity} to draw for.
     */
    public void render(ShapeRenderer shape, Entity entity) {

    }

    /**
     * @return {@code boolean} : {@code true} when inputs should be processed,
     *                           {@code false} when they should not.
     */
    public boolean getProcessInput() {
        return processInput;
    }

    /**
     * @return {@code boolean} : {@code true} when the text is skippable,
     *                           {@code false} when it is not.
     */
    public boolean canSkip() {
        return skippable;
    }

    /**
     * @return {@code boolean} : {@code true} is the {@link TutorialStep} has text,
     *                           {@code false} when it does not.
     */
    public boolean hasText() {
        return hasText;
    }
}
