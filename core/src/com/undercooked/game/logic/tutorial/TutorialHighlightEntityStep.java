package com.undercooked.game.logic.tutorial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;

/**
 * A {@link TutorialStep} for moving to and highlighting an {@link Entity}.
 */
public class TutorialHighlightEntityStep extends TutorialFollowEntityStep {

    /** The {@link Entity} to highlight. */
    protected Entity highlightEntity;

    /** The {@link Texture} to use to highlight the {@link Entity}. */
    protected Texture highlightTexture;

    /**
     * Constructor for the {@link TutorialHighlightEntityStep}.
     * @param text {@link String} : The text to display
     * @param textSpeed {@link float} : The number of characters to add per second.
     * @param highlightEntity {@link Entity} : The {@link Entity} to highlight.
     */
    public TutorialHighlightEntityStep(String text, float textSpeed, Entity highlightEntity) {
        super(text, textSpeed, highlightEntity);
        this.highlightEntity = highlightEntity;
    }

    @Override
    public void load(TextureManager textureManager, String textureGroup) {
        // Load the select box sprite
        textureManager.load(textureGroup, "interactions/select_box.png");
    }

    @Override
    public void postLoad(TextureManager textureManager) {
        super.postLoad(textureManager);
        highlightTexture = textureManager.get("interactions/select_box.png");
    }

    @Override
    public void render(SpriteBatch batch, Entity entity) {
        super.render(batch, entity);
        // If it's not the highlight entity, ignore
        if (entity != highlightEntity) return;

        if (highlightTexture == null) return;
        // Render the highlight texture over the entity's sprite
        Sprite entitySprite = highlightEntity.getSprite();
        batch.setColor(Color.PINK);
        batch.draw(highlightTexture, entity.getX(), entity.getY(), entitySprite.getWidth(), entitySprite.getHeight());
        batch.setColor(Color.WHITE);
    }
}
