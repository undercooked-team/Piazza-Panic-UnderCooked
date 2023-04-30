package com.undercooked.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * A class for an {@link Entity} that is placed on the {@link Map}'s grid in
 * a {@link MapCell}.
 */
public class MapEntity extends Entity {

    /** The width of the {@link MapEntity} on the {@link Map}. */
    private int width;

    /** The height of the {@link MapEntity} on the {@link Map}. */
    private int height;

    /** The collision of the interaction box. */
    private final Rectangle interactBox;

    /** The asset path to the base's {@link com.badlogic.gdx.graphics.Texture}. */
    protected String basePath;

    /** The id of the {@link MapEntity}. */
    protected String id;

    /**
     * Constructor for the {@link MapEntity}.
     */
    public MapEntity() {
        this.interactBox = new Rectangle();
    }

    @Override
    public void postLoad(TextureManager textureManager) {
        super.postLoad(textureManager);
        updateSpriteSize();
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        interactBox.x = x;
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        interactBox.y = y;
    }

    /**
     * Updates the size of the {@link com.badlogic.gdx.graphics.g2d.Sprite}.
     */
    public void updateSpriteSize() {
        if (this.sprite == null) return;
        this.sprite.setSize(MapManager.gridToPos(width), MapManager.gridToPos(height));
    }

    /**
     * Set the width of the {@link MapEntity} on the {@link Map}.
     * @param width {@code int} : The new width.
     */
    public void setWidth(int width) {
        this.width = Math.max(1,width);
        this.interactBox.width = Math.max(MapManager.gridToPos(1), MapManager.gridToPos(width));
        this.collision.width = interactBox.width;
        updateSpriteSize();
        // this.sprite.setSize(width*MapManager.gridToPos(height), this.sprite.getHeight());
    }

    /**
     * Set the height of the {@link MapEntity} on the {@link Map}.
     * @param height {@code int} : The new height.
     */
    public void setHeight(int height) {
        this.height = Math.max(1,height);
        this.interactBox.height = Math.max(MapManager.gridToPos(1),MapManager.gridToPos(height));
        this.collision.height = interactBox.height;
        updateSpriteSize();
        // this.sprite.setSize(this.sprite.getWidth(), height*MapManager.gridToPos(height));
    }

    /**
     * Set the asset path to the base's {@link com.badlogic.gdx.graphics.Texture}.
     * @param basePath {@link String} : The path to the asset.
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Get the width of the {@link MapEntity} on the {@link Map}.
     * @return {@code int} : The cell width.
     */
    public int getCellWidth() {
        return this.width;
    }

    /**
     * Get the height of the {@link MapEntity} on the {@link Map}.
     * @return {@code int} : The cell height.
     */
    public int getCellHeight() {
        return this.height;
    }

    /**
     * @return {@link String} : The id of the {@link MapEntity}.
     */
    public String getID() {
        return id;
    }

    /**
     * Return whether the {@link Rectangle} is overlapping the
     * interaction collision or not.
     * @param rectangle {@link Rectangle} : The {@link Rectangle} to check.
     * @return {@code boolean} : {@code true} if they are colliding,
     *                           {@code false} if they are not.
     */
    public boolean isInteracting(Rectangle rectangle) {
        return interactBox.overlaps(rectangle);
    }

    /**
     * @return {@link Rectangle} : The interact collision of the {@link MapEntity}.
     */
    public Rectangle getInteractBox() {
        return interactBox;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(sprite, pos.x, pos.y, interactBox.width, interactBox.height);
    }

    @Override
    public void drawDebug(ShapeRenderer shape) {
        super.drawDebug(shape);
        /*shape.setColor(Color.YELLOW);
        shape.rect(interactBox.x,interactBox.y,interactBox.width,interactBox.height);
        shape.setColor(Color.WHITE);*/
    }

    /**
     * Class to be Override by children
     * @param keyID {@link String} : The key's ID.
     * @param inputType {@link InputType} : The type of input of interaction.
     * @return {@link InteractResult} : The result of the {@link InteractionStep}.
     */
    public InteractResult interact(Cook cook, String keyID, InputType inputType) {
        return InteractResult.NONE;
    }
}
