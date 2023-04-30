package com.undercooked.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.undercooked.game.assets.TextureManager;

/**
 * A class for storing the data of a single cell on the {@link Map}.
 */
public class MapCell {
    private boolean collidable;
    private boolean interactable;
    private int x, y;
    private float collisionX, collisionY;
    private float width;
    private float height;
    private boolean base;
    private String belowTilePath;
    /** The floor tile to draw underneath. */
    private Texture belowTile;

    /** The {@link MapEntity} of the {@link MapCell}. */
    protected MapEntity mapEntity;

    /** The {@link Rectangle} collision of the {@link MapCell}. */
    protected Rectangle collision;

    /**
     * Constructor for the {@link MapCell}.
     *
     * @param collidable {@code boolean} : Whether the {@link MapCell} is
     *                                     collidable or not.
     * @param interactable {@code boolean} : Whether the {@link MapCell} is
     *                                       interactable or not.
     * @param base {@code boolean} : Whether the {@link MapCell} should is
     *                               a base or not.
     */
    public MapCell(boolean collidable, boolean interactable, boolean base) {
        this.collidable = collidable;
        this.interactable = interactable;
        this.base = base;
        this.x = 0;
        this.y = 0;
        this.width = 64;
        this.height = 64;
        this.collision = new Rectangle();
        this.collision.setSize(MapManager.gridToPos(1));
    }

    /**
     * Constructor for the {@link MapCell}.
     * Defaults the values to false.
     */
    public MapCell() {
        this(false, false, false);
    }

    /**
     * Set the {@link MapCell} to be collidable or not.
     * @param collidable {@code boolean} : Whether the {@link MapCell} should
     *                                     be collidable or not.
     */
    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    /**
     * Set the {@link MapCell} to be interactable or not.
     * @param interactable {@code boolean} : Whether the {@link MapCell} should
     *                                be interactable or not.
     */
    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }

    /**
     * Set the {@link MapCell} to be a base or not.
     * @param base {@code boolean} : Whether the {@link MapCell} should
     *                                be a base or not.
     */
    public void setBase(boolean base) {
        this.base = base;
    }

    /**
     * Set the {@link MapEntity} of the {@link MapCell}.
     * @param mapEntity {@link MapEntity} : The {@link MapEntity} to set the
     *                                      {@link MapCell} to use.
     */
    public void setMapEntity(MapEntity mapEntity) {
        this.mapEntity = mapEntity;
        if (mapEntity != null) {
            mapEntity.setX(this.collisionX);
            mapEntity.setY(this.collisionY);
            mapEntity.setWidth(this.width);
            mapEntity.setHeight(this.height);
        }
    }

    /**
     * Set the x position of the {@link MapCell}.
     * @param x {@code int} : The new x position.
     */
    public void setX(int x) {
        this.x = x;
        setCollisionX(MapManager.gridToPos(x));
    }

    /**
     * Set the collision x position of the {@link MapCell}.
     * @param x {@code int} : The new x position.
     */
    protected void setCollisionX(float x) {
        if (this.mapEntity != null) {
            mapEntity.setX(x);
        }
        this.collision.setX(x);
        this.collisionX = x;
    }

    /**
     * Set the y position of the {@link MapCell}.
     * @param y {@code int} : The new y position.
     */
    public void setY(int y) {
        this.y = y;
        setCollisionY(MapManager.gridToPos(y));
    }

    /**
     * Set the collision y position of the {@link MapCell}.
     * @param y {@code int} : The new y position.
     */
    protected void setCollisionY(float y) {
        if (this.mapEntity != null) {
            mapEntity.setY(y);
        }
        this.collision.setY(y);
        this.collisionY = y;
    }

    /**
     * Set the width of the {@link MapCell}.
     * @param width {@code int} : The new width.
     */
    public void setWidth(int width) {
        float newWidth = MapManager.gridToPos(width);
        if (this.mapEntity != null) {
            mapEntity.setWidth(width);
        }
        this.width = newWidth;
    }

    /**
     * Set the height of the {@link MapCell}.
     * @param height {@code int} : The new height.
     */
    public void setHeight(int height) {
        float newHeight = MapManager.gridToPos(height);
        if (this.mapEntity != null) {
            mapEntity.setHeight(height);
        }
        this.height = newHeight;
    }

    /**
     * @return {@link int} : The x position of the {@link MapCell}.
     */
    public int getX() {
        return x;
    }

    /**
     * @return {@link int} : The collision x position of the {@link MapCell}.
     */
    public float getCollisionX() {
        return collisionX;
    }

    /**
     * @return {@link int} : The y position of the {@link MapCell}.
     */
    public int getY() {
        return y;
    }

    /**
     * @return {@link int} : The collision y position of the {@link MapCell}.
     */
    public float getCollisionY() {
        return collisionY;
    }

    /**
     * Update the {@link Texture} for the tile below the {@link MapEntity}.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to get
     *                                                the {@link Texture} from.
     */
    public void updateBelowTile(TextureManager textureManager) {
        if (belowTilePath == null) {
            this.belowTile = null;
            return;
        }
        this.belowTile = textureManager.getAsset(belowTilePath);
    }

    /**
     * Set the asset path of the tile drawn below the {@link MapEntity}.
     * <br><br>
     * If set to {@code null}, it won't draw any tile.
     * @param texturePath {@link String} : The path to the asset.
     */
    public void setBelowTile(String texturePath) {
        this.belowTilePath = texturePath;
    }

    /**
     * Set the {@link TextureManager} to load the {@link Texture} of the
     * tile drawn below the {@link MapEntity} and load the {@link MapEntity} too.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     * @param textureGroup {@link String} : The texture group to load to.
     */
    public void load(TextureManager textureManager, String textureGroup) {
        // Load the floor tile
        loadFloor(textureManager, textureGroup);
        // And then load the map entity
        mapEntity.load(textureManager, textureGroup);
    }

    /**
     * Set the {@link TextureManager} to load the {@link Texture} of the
     * tile drawn below the {@link MapEntity}.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     * @param textureGroup {@link String} : The texture group to load to.
     */
    public void loadFloor(TextureManager textureManager, String textureGroup) {
        textureManager.loadAsset(textureGroup, belowTilePath, "textures");
    }

    /**
     * Unload the {@link Texture} of the below tile, and also the {@link MapEntity}.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     */
    public void unload(TextureManager textureManager) {
        // Unload the floor tile
        textureManager.unloadTexture(belowTilePath);
        // And unload the map entity
        mapEntity.unload(textureManager);
    }

    /**
     * @return {@code boolean} : {@code true} if the {@link MapCell} is collidable,
     *                           {@code false} if not.
     */
    public boolean isCollidable() {
        return collidable;
    }

    /**
     * @return {@code boolean} : {@code true} if the {@link MapCell} is interactable,
     *                           {@code false} if not.
     */
    public boolean isInteractable() {
        return interactable;
    }

    /**
     * @return {@code boolean} : {@code true} if the {@link MapCell} is a base,
     *                           {@code false} if not.
     */
    public boolean isBase() {
        return base;
    }

    /**
     * @return {@link MapEntity} : The {@link MapEntity} of the {@link MapCell},
     *                             or {@code null} if there isn't one.
     */
    public MapEntity getMapEntity() {
        return mapEntity;
    }

    /**
     * Draw the {@link Texture} of the below tile.
     * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
     */
    public void drawBelow(SpriteBatch batch) {
        if (belowTile != null) {
            batch.draw(belowTile, collisionX, collisionY, width, height);
        }
    }

    /**
     * Draw the {@link Texture} of the below tile, and the {@link MapEntity} over it.
     * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
     */
    public void draw(SpriteBatch batch) {
        drawBelow(batch);
        mapEntity.draw(batch);
    }

    /**
     * @return {@link Rectangle} : The collision of the {@link MapEntity} if there is one,
     *                             or the collision of the {@link MapCell} if there isn't.
     */
    public Rectangle getCollision() {
        if (mapEntity == null) {
            return getCellCollision();
        }
        return mapEntity.collision;
    }

    /**
     * @return {@link Rectangle} : The collision of the {@link MapCell}.
     */
    public Rectangle getCellCollision() {
        return this.collision;
    }
}