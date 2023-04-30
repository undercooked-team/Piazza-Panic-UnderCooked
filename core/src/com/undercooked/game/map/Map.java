package com.undercooked.game.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.MathUtil;

import java.awt.*;

/**
 * The class for the map of the game.
 */
public class Map {
    /** A 2 dimensional {@link Array} of {@link MapCell}s. */
    private final Array<Array<MapCell>> map;

    /** The width of the play area. */
    private final int width;

    /** The width of the full map. */
    private final int fullWidth;

    /** The height of the play area. */
    private final int height;

    /** The height of the full map. */
    private final int fullHeight;

    /** The offset x of the play area on the full map. */
    private int offsetX;

    /** The offset y of the play area on the full map. */
    private int offsetY;

    /**
     * A cell that is used when referring to a cell outside the {@link Map}.
     * <br><br>
     * It is positioned at {@code -fullWidth-1, -fullHeight-1}.
     * */
    public final MapCell outOfBounds = new MapCell(true, false, false);

    /**
     * Constructor for the {@link Map}.
     * <br><br>
     * This constructor sets the play area to be the same size as the
     * full map size.
     *
     * @param width {@code float} : The width of the {@link Map}.
     * @param height {@code float} : The height of the {@link Map}.
     */
    public Map(int width, int height) {
        this(width,height,width,height);
    }

    /**
     * Constructor for the {@link Map}.
     *
     * @param width {@code float} : The play area width of the {@link Map}.
     * @param height {@code float} : The play area height of the {@link Map}.
     * @param fullWidth {@code float} : The full width of the {@link Map}.
     * @param fullHeight {@code float} : The full height of the {@link Map}.
     */
    public Map(int width, int height, int fullWidth, int fullHeight) {
        // Initialise the columns
        map = new Array<>();
        // Then initialise the rows
        /*for (int i = 0 ; i < map.size ; i++) {
            map.add(new Array<MapCell>(fullHeight));
        }*/
        this.width = width;
        this.height = height;
        this.fullWidth = fullWidth;
        this.fullHeight = fullHeight;// Then init the map (so that it's empty)
        this.outOfBounds.setX(-fullWidth-1);
        this.outOfBounds.setY(-fullHeight-1);
        init();
    }

    /**
     * Initialization of the {@link Map}.
     */
    private void init() {
        // Clear the array
        map.clear();
        // Load all the values
        for (int x = 0; x < fullWidth; x++) {
            Array<MapCell> thisCol = new Array<>();
            map.add(thisCol);
            for (int y = 0; y < fullHeight; y++) {
                MapCell newCell = new MapCell();
                thisCol.add(newCell);
                resetCell(newCell);
                newCell.setX(x);
                newCell.setY(y);
            }
        }
    }

    /**
     * Reset a cell at a position.
     * @param x {@code int} : The {@code x} position of the {@link MapCell}.
     * @param y {@code int} : The {@code y} position of the {@link MapCell}.
     */
    public void resetCell(int x, int y) {
        MapCell cell = getCellFull(x,y);
        if (cell != null) {
            resetCell(cell);
        }
    }

    /**
     * Reset a {@link MapCell}.
     *
     * @param cell {@link MapCell} : The {@link MapCell} to reset.
     */
    public void resetCell(MapCell cell) {
        cell.setInteractable(false);
        cell.setCollidable(false);
        cell.setBase(false);
        cell.setMapEntity(null);
        cell.setBelowTile(Constants.DEFAULT_FLOOR_TILE);
    }

    /**
     * Returns whether a position is a valid cell on the full map or not.
     *
     * @param x {@code int} : The {@code x} position.
     * @param y {@code int} : The {@code y} position.
     * @return {@code boolean} : {@code true} if the position is valid,
     *                           {@code false} if not.
     */
    protected boolean validCellFull(int x, int y) {
        if (x < 0 || x >= fullWidth) {
            return false;
        }
        if (y < 0 || y >= fullHeight) {
            return false;
        }
        return true;
    }

    /**
     * Returns whether a position is a valid cell on the play area or not.
     *
     * @param x {@code int} : The {@code x} position.
     * @param y {@code int} : The {@code y} position.
     * @return {@code boolean} : {@code true} if the position is valid,
     *                           {@code false} if not.
     */
    public boolean validCell(int x, int y) {
        if (x < 0 || x >= width) {
            return false;
        }
        if (y < 0 || y >= height) {
            return false;
        }
        return true;
    }

    /**
     * Returns the {@link MapCell} at the position, defaulting to
     * allowing getting the {@link #outOfBounds} {@link MapCell}.
     * @param x {@code int} : The {@code x} position of the {@link MapCell}.
     * @param y {@code int} : The {@code y} position of the {@link MapCell}.
     * @return {@link MapCell} : The {@link MapCell} at the position.
     */
    public MapCell getCellFull(int x, int y) {
        return getCellFull(x, y, true);
    }

    /**
     * Returns the {@link MapCell} at the position.
     * <br><br>
     * If trying to find a {@link MapCell} that's not in the valid area,
     * it will return either {@link #outOfBounds} or {@code null} depending
     * on the value of {@code allowOutOfBounds}.
     *
     * @param x {@code int} : The {@code x} position of the {@link MapCell}.
     * @param y {@code int} : The {@code y} position of the {@link MapCell}.
     * @param allowOutOfBounds {@code boolean} : If out of bounds, returns
     *                                           {@link #outOfBounds} if {@code true} or
     *                                           {@code null} if {@code false}.
     * @return {@link MapCell} : The {@link MapCell} at the position.
     */
    public MapCell getCellFull(int x, int y, boolean allowOutOfBounds) {
        // Make sure it's a valid cell position
        // If not, then return null
        if (!validCellFull(x,y)) {
            if (allowOutOfBounds) {
                return outOfBounds;
            }
            return null;
        }

        // If it's a valid cell, then return the cell
        return map.get(x).get(y);
    }

    /**
     * Returns the {@link MapCell} at the position inside the play area,
     * defaulting to  allowing getting the {@link #outOfBounds} {@link MapCell}.
     * @param x {@code int} : The {@code x} position of the {@link MapCell}.
     * @param y {@code int} : The {@code y} position of the {@link MapCell}.
     * @return {@link MapCell} : The {@link MapCell} at the position.
     */
    public MapCell getCell(int x, int y) {
        return getCell(x, y, true);
    }

    /**
     * Returns the {@link MapCell} at the position inside the play area.
     * <br><br>
     * If trying to find a {@link MapCell} that's not in the valid area,
     * it will return either {@link #outOfBounds} or {@code null} depending
     * on the value of {@code allowOutOfBounds}.
     *
     * @param x {@code int} : The {@code x} position of the {@link MapCell}.
     * @param y {@code int} : The {@code y} position of the {@link MapCell}.
     * @param allowOutOfBounds {@code boolean} : If out of bounds, returns
     *                                           {@link #outOfBounds} if {@code true} or
     *                                           {@code null} if {@code false}.
     * @return {@link MapCell} : The {@link MapCell} at the position.
     */
    public MapCell getCell(int x, int y, boolean allowOutOfBounds) {
        if (!validCell(x,y)) {
            if (allowOutOfBounds) {
                return outOfBounds;
            }
            return null;
        }
        return getCellFull(x+offsetX,y+offsetY, allowOutOfBounds);
    }

    /**
     * Set the x offset of the play area.
     * <br><br>
     * Limited in the range of 0 to the full map width minus the
     * play area width.
     *
     * @param offsetX {@code int} : The new {@code x} offset of the play area.
     */
    public void setOffsetX(int offsetX) {
        this.offsetX = Math.max(Math.min(offsetX, fullWidth-width), 0);
    }

    /**
     * Set the y offset of the play area.
     * <br><br>
     * Limited in the range of 0 to the full map height minus the
     * play area height.
     *
     * @param offsetY {@code int} : The new {@code y} offset of the play area.
     */
    public void setOffsetY(int offsetY) {
        this.offsetY = Math.max(Math.min(offsetY, fullHeight-height), 0);
    }

    /**
     * Removes all instances of a {@link MapEntity} from
     * the {@link Map}.
     * @param entity The {@link MapEntity} to remove.
     * @return {@code boolean}: {@code True} if the entity was on the map,
     *                          {@code False} if it was not.
     */
    public boolean removeEntity(MapEntity entity) {
        boolean found = false;
        MapCell cornerCell = null;
        // Loop through all cells, left to right, bottom to top
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                MapCell thisCell = getCellFull(x,y);
                // If the current cell isn't empty.
                if (thisCell != null) {
                    // Check if the entity in this cell == entity.
                    // If it does, then set that cell to null
                    if (thisCell.mapEntity == entity){
                        resetCell(x, y);
                    }
                }
            }
        }
        return found;
    }

    /**
     * Add a {@link MapEntity} to the full map.
     *
     * @param entity {@link MapEntity} : The {@link MapEntity} to add.
     * @param x {@code int} : The {@code x} position to place the {@link MapEntity} at.
     * @param y {@code int} : The {@code y} position to place the {@link MapEntity} at.
     * @param floorTile {@link String} : The asset path to the floor tile {@link com.badlogic.gdx.graphics.Texture}
     *                                   to use. If null, no floor {@link com.badlogic.gdx.graphics.Texture}.
     * @return {@link Array<Entity>} : An {@link Array} of all {@link MapEntity} that were removed
     *                                 from the {@link Map}.
     */
    public Array<Entity> addFullMapEntity(MapEntity entity, int x, int y, String floorTile) {
        return addFullMapEntity(entity, x, y, floorTile, false);
    }

    /**
     * Add a {@link MapEntity} to the full map.
     *
     * @param entity {@link MapEntity} : The {@link MapEntity} to add.
     * @param x {@code int} : The {@code x} position to place the {@link MapEntity} at.
     * @param y {@code int} : The {@code y} position to place the {@link MapEntity} at.
     * @param floorTile {@link String} : The asset path to the floor tile {@link com.badlogic.gdx.graphics.Texture}
     *                                   to use. If null, no floor {@link com.badlogic.gdx.graphics.Texture}.
     * @param hasCollision {@code boolean} : Whether the {@link MapCell}s that the {@link MapEntity} take up
     *                                    will or will not have collision.
     * @return {@link Array<Entity>} : An {@link Array} of all {@link MapEntity} that were removed
     *                                 from the {@link Map}.
     */
    public Array<Entity> addFullMapEntity(MapEntity entity, int x, int y, String floorTile, boolean hasCollision) {
        Array<Entity> removedEntities = new Array<>();
        // And now add the entity
        for (int i = x + entity.getCellWidth()-1 ; i >= x ; i--) {
            for (int j = y + entity.getCellHeight()-1 ; j >= y ; j--) {
                // If it's a valid cell
                if (validCellFull(i,j)) {
                    MapCell cellToReplace = getCellFull(i, j, false);
                    // If there's already a station here, remove it completely.
                    boolean hadCollision = cellToReplace.isCollidable();
                    if (cellToReplace.getMapEntity() != null) {
                        // Only add it for removal if it hasn't been already
                        MapEntity entityToRemove = getCellFull(i, j).getMapEntity();
                        removeEntity(entityToRemove);
                        // Add entity to removed entities, if it's not there already
                        if (!removedEntities.contains(entityToRemove, true)) removedEntities.add(entityToRemove);
                    }
                    cellToReplace.setCollidable(hasCollision);
                    cellToReplace.setInteractable(true);
                    cellToReplace.setBase(false);
                    cellToReplace.setMapEntity(entity);
                    cellToReplace.setBelowTile(floorTile);
                    // If there is a station above this...
                    if (validCellFull(i,j+1)) {
                        MapCell aboveCell = getCellFull(i,j+1, false);
                        if (!aboveCell.isBase()) {
                            // And the previous cell had collision...
                            if (hadCollision) {
                                // Then set this one to collidable
                                cellToReplace.setCollidable(true);
                            }
                        }
                    }
                }
            }

            // Below it, if it's outside the map, go to the next one
            if (!validCellFull(i, y-1)) continue;

            // Otherwise, if there isn't a MapCell already, place a cupboard
            // It is not in the loop above, as only the lowest y needs it below
            MapCell cellBelow = getCellFull(i, y-1, false);
            if (cellBelow != null) {
                if (cellBelow.getMapEntity() == null) {
                    // If y-1 is valid, and basePath is not null
                    if (entity.basePath != null) {
                        // Make a cupboard cell below and add it to the map
                        cellBelow.setCollidable(true);
                        cellBelow.setInteractable(false);
                        cellBelow.setBase(true);
                        cellBelow.setMapEntity(new MapEntity());
                        cellBelow.getMapEntity().setTexture(entity.basePath);
                        cellBelow.setBelowTile(null);
                        cellBelow.setWidth(1);
                        cellBelow.setHeight(1);
                    }
                } else {
                    // If not...
                    // Update the cell below, if it is a base
                    if (cellBelow.isBase()) {
                        // If the entity has a base path, then replace it
                        if (entity.basePath != null) {
                            cellBelow.getMapEntity().setTexture(entity.basePath);
                        } else {
                            // Add entity to removed entities, if it's not there already
                            if (!removedEntities.contains(cellBelow.mapEntity, true)) removedEntities.add(cellBelow.mapEntity);
                            // If it doesn't have a base path, remove the base
                            resetCell(cellBelow);
                        }
                    } else {
                        // If it's not a base below, then set if it has collision
                        // depending on if it has a base or not

                        // TODO: Fix below problem
                        // Note that this has a problem when a station with collision has a station placed above it
                        // that does not have a base, as it will remove the collision of the station below.
                        // E.g: Placing a bin directly above another bin.
                        cellBelow.setCollidable(entity.basePath != null);
                    }
                }
            }
        }
        return removedEntities;
    }

    /**
     * Add a {@link MapEntity} to the play area.
     *
     * @param entity {@link MapEntity} : The {@link MapEntity} to add.
     * @param x {@code int} : The {@code x} position to place the {@link MapEntity} at.
     * @param y {@code int} : The {@code y} position to place the {@link MapEntity} at.
     * @param floorTile {@link String} : The asset path to the floor tile {@link com.badlogic.gdx.graphics.Texture}
     *                                   to use. If null, no floor {@link com.badlogic.gdx.graphics.Texture}.
     * @return {@link Array<Entity>} : An {@link Array} of all {@link MapEntity} that were removed
     *                                 from the {@link Map}.
     */
    public Array<Entity> addMapEntity(MapEntity entity, int x, int y, String floorTile) {
        return addMapEntity(entity, x, y, floorTile, false);
    }

    /**
     * Add a {@link MapEntity} to the play area.
     *
     * @param entity {@link MapEntity} : The {@link MapEntity} to add.
     * @param x {@code int} : The {@code x} position to place the {@link MapEntity} at.
     * @param y {@code int} : The {@code y} position to place the {@link MapEntity} at.
     * @param floorTile {@link String} : The asset path to the floor tile {@link com.badlogic.gdx.graphics.Texture}
     *                                   to use. If null, no floor {@link com.badlogic.gdx.graphics.Texture}.
     * @param hasCollision {@code boolean} : Whether the {@link MapCell}s that the {@link MapEntity} take up
     *                                    will or will not have collision.
     * @return {@link Array<Entity>} : An {@link Array} of all {@link MapEntity} that were removed
     *                                 from the {@link Map}.
     */
    public Array<Entity> addMapEntity(MapEntity entity, int x, int y, String floorTile, boolean hasCollision) {
        // Make sure the range is valid
        // (x, x+entity.width, y, y+entity.height all in range)
        // Make sure that width and height are > 0
        if (entity.getCellWidth() <= 0 || entity.getCellHeight() <= 0) {
            return null;
        }
        // Make sure that the cell is valid
        if (!(validCell(x,y) && validCell(x+entity.getCellWidth()-1,y+entity.getCellHeight()-1))) {
            // If it's not, then return as it can't be added.
            return null;
        }

        // Add it to the map
        return addFullMapEntity(entity, x+offsetX, y+offsetY, floorTile, hasCollision);
    }

    /**
     * Randomly gets a cell in the range provided, given that it's NOT of the type
     * {@code mapCellType}.
     *
     * @param x {@code int} : Left-most x
     * @param y {@code int} : Bottom-most x
     * @param width {@code int} : The width
     * @param height {@code int} : The height
     * @param mapCellType {@code int} : The {@link MapCellType} to ignore.
     * @return {@link MapCell} : A random {@link MapCell} in the range provided that
     *                           matches the {@code mapCellType} condition.
     */
    public MapCell randomOpenCellRange(int x, int y, int width, int height, MapCellType mapCellType) {
        // Make an array of open cells
        Array<MapCell> openCells = openCellsRangeFull(x, y, width, height, mapCellType);
        // If the array is empty, return nothing
        if (openCells.size == 0) return null;

        // Otherwise return a random cell
        return openCells.random();
    }

    /**
     * Get any random open cell from the play area.
     * @return {@link MapCell} : The random {@link MapCell}.
     */
    public MapCell randomOpenCell() {
        return randomOpenCell(MapCellType.ANY);
    }

    /**
     * Get any random open cell from the play area that doesn't
     * match the {@link MapCellType}.
     * @param mapCellType {@link MapCellType} : The {@link MapCellType} to ignore.
     * @return {@link MapCell} : The random {@link MapCell}.
     */
    public MapCell randomOpenCell(MapCellType mapCellType) {
        return randomOpenCellRange(offsetX, offsetY, width-1, height-1, mapCellType);
    }

    /**
     * Get any random open cell from the full map.
     * @return {@link MapCell} : The random {@link MapCell}.
     */
    public MapCell randomOpenCellFull() {
        return randomOpenCellFull(MapCellType.ANY);
    }

    /**
     * Get any random open cell from the full map that doesn't
     * match the {@link MapCellType}.
     * @param mapCellType {@link MapCellType} : The {@link MapCellType} to ignore.
     * @return {@link MapCell} : The random {@link MapCell}.
     */
    public MapCell randomOpenCellFull(MapCellType mapCellType) {
        return randomOpenCellRange(0,0,fullWidth,fullHeight, mapCellType);
    }

    /**
     * Returns all open cells from the play area.
     * @return {@link Array<MapCell>} : An {@link Array} of the open {@link MapCell}s.
     */
    public Array<MapCell> openCells() {
        return openCells(MapCellType.ANY);
    }

    /**
     * Returns all open cells from the play area that don't
     * match the {@link MapCellType}.
     * @param mapCellType {@link MapCellType} : The {@link MapCellType} to ignore.
     * @return {@link MapCell} : The random {@link MapCell}.
     */
    public Array<MapCell> openCells(MapCellType mapCellType) {
        return openCellsRangeFull(offsetX, offsetY, width-1, height-1, mapCellType);
    }

    /**
     * Returns all open cells from the full map.
     * @return {@link Array<MapCell>} : An {@link Array} of the open {@link MapCell}s.
     */
    public Array<MapCell> openCellsFull() {
        return openCells(MapCellType.ANY);
    }

    /**
     * Returns all open cells from the full map that don't
     * match the {@link MapCellType}.
     * @param mapCellType {@link MapCellType} : The {@link MapCellType} to ignore.
     * @return {@link MapCell} : The random {@link MapCell}.
     */
    public Array<MapCell> openCellsFull(MapCellType mapCellType) {
        return openCellsRangeFull(0, 0, fullWidth, fullHeight, mapCellType);
    }

    /**
     * Returns all the open cells in the range, given that it's NOT of the type
     * {@code mapCellType}.
     *
     * @param x {@code int} : Left-most x
     * @param y {@code int} : Bottom-most x
     * @param width {@code int} : The width
     * @param height {@code int} : The height
     * @param mapCellType {@code int} : The {@link MapCellType} to ignore.
     * @return {@link Array<MapCell>} : An {@link Array} of all {@link MapCell}s
     *                                  in the range provided that match the
     *                                  {@code mapCellType} condition.
     */
    public Array<MapCell> openCellsRangeFull(int x, int y, int width, int height, MapCellType mapCellType) {// Make an array of open cells
        Array<MapCell> openCells = new Array<>();
        // Loop through the locations, and add them to the array if they're open cells
        // (Those being cells with no collision)
        for (int i = x ; i < x + width ; i++) {
            for (int j = y ; j < y + height ; j++) {
                MapCell thisCell = getCellFull(i,j);
                switch (mapCellType) {
                    case COLLIDABLE:
                        if (!thisCell.isCollidable()) {
                            openCells.add(thisCell);
                        }
                        break;
                    case INTERACTABLE:
                        if (!thisCell.isInteractable()) {
                            openCells.add(thisCell);
                        }
                        break;
                    case ANY:
                        if (!thisCell.isCollidable() && !thisCell.isInteractable() && !thisCell.isBase()) {
                            openCells.add(thisCell);
                        }
                        break;
                }
            }
        }
        // Return the array
        return openCells;
    }

    /**
     * The different types of cells on the map.
     */
    public enum MapCellType {
        /** A {@link MapCell} that is anything but empty. */
        ANY,

        /** A {@link MapCell} that has collision. */
        COLLIDABLE,

        /** A {@link MapCell} that is interactable. */
        INTERACTABLE
    }

    /**
     * Returns the {@link MapCell} that the {@code collision} {@link Rectangle}
     * is overlapping.
     *
     * @param collision {@link Rectangle} : The collision to check against the {@link Map}.
     * @param returnClosest {@code boolean} : If {@code true}, return the closest {@link MapCell}
     *                                          to the {@code collision},
     *                                        otherwise just return the first {@link MapCell} found,
     *                                          if any.
     * @param mapCellType {@link MapCellType} : The {@link MapCellType} to check collision for.
     * @return {@link MapCell} : The {@link MapCell} found, or {@code null} if none found.
     */
    public MapCell getCollision(Rectangle collision, boolean returnClosest, MapCellType mapCellType) {
        // Get the range of cell X to cellY
        int cellX    = MapManager.posToGridFloor(collision.x);
        int cellXMax = MapManager.posToGridFloor(collision.x + collision.width);

        int cellY    = MapManager.posToGridFloor(collision.y);
        int cellYMax = MapManager.posToGridFloor(collision.y + collision.height);

        Array<Point> validCells = null;
        if (returnClosest) {
            validCells = new Array();
        }

        // Loop through all the cells in the range
        for (int x = cellX ; x <= cellXMax ; x++) {
            for (int y = cellY ; y <= cellYMax ; y++) {
                // Check if it's a valid cell
                if (!validCellFull(x, y)) {
                    // If it's not valid, return that it's colliding
                    if (!returnClosest) {
                        return outOfBounds;
                    }
                    validCells.add(new Point(-width, -height));
                }

                // Get the cell.
                MapCell cell = getCellFull(x, y);
                if (cell == null) {
                    continue;
                }
                // Make sure it has a map entity
                if (cell.mapEntity == null) {
                    continue;
                }
                switch (mapCellType) {
                    case COLLIDABLE:
                        // If it's not a collidable, then skip
                        if (!cell.isCollidable()) {
                            continue;
                        }
                        // If they're not colliding, then skip
                        if (!cell.mapEntity.isColliding(collision)) {
                            continue;
                        }
                        break;
                    case INTERACTABLE:
                        // If it's not an interactable, then skip.
                        if (!cell.isInteractable()) {
                            continue;
                        }
                        // If they're not interacting, then skip
                        if (!cell.mapEntity.isInteracting(collision)) {
                            continue;
                        }
                }
                // Otherwise, if the above succeeds, return the MapCell
                if (!returnClosest) {
                    return cell;
                }
                validCells.add(new Point(x,y));
            }
        }

        // If requested to return the closest...
        if (returnClosest) {
            // If there are none found, then return null.
            if (validCells.size == 0) {
                return null;
            }
            // If there's only one, return that
            if (validCells.size == 1) {
                return getCellFull(validCells.get(0).x, validCells.get(0).y);
            }

            // Go through the points in the array, and find the closest one.
            Point firstPoint = validCells.get(0);
            MapCell closestCell = getCellFull(firstPoint.x, firstPoint.y, true);
            double closestDist = MathUtil.distanceBetweenRectangles(collision, closestCell.getCollision());
            for (int i = 1 ; i < validCells.size ; i++) {
                Point currentPoint = validCells.get(i);
                MapCell currentCell = getCellFull(currentPoint.x, currentPoint.y, true);
                double currentDist = MathUtil.distanceBetweenRectangles(collision, currentCell.getCollision());
                if (currentDist < closestDist) {
                    closestDist = currentDist;
                    closestCell = currentCell;
                }
            }

            // Return the cell at that location
            return closestCell;
        }

        // If none of the above returns, then return false as they're not colliding
        return null;
    }

    /**
     * Returns whether an {@link Entity} is colliding with a
     * {@link MapCellType#COLLIDABLE} {@link MapCell} or not.
     * @param entity {@link Entity} : The {@link Entity} to check.
     * @return {@code boolean} : {@code true} if the {@link Entity} is colliding
     *                              with a {@link MapCell},
     *                           {@code false} if not.
     */
    public boolean checkCollision(Entity entity) {
        return checkCollision(entity.collision);
    }

    /**
     * Returns whether an {@link Entity} would be colliding with a
     * {@link MapCellType#COLLIDABLE} {@link MapCell} at the specified position or not.
     * @param entity {@link Entity} : The {@link Entity} to check.
     * @return {@code boolean} : {@code true} if the {@link Entity} would be colliding
     *                              with a {@link MapCell},
     *                           {@code false} if not.
     */
    public boolean checkCollision(Entity entity, float x, float y) {
        return checkCollision(new Rectangle(x, y, entity.collision.width, entity.collision.height));
    }

    /**
     * Returns whether an {@link Rectangle} is colliding with a
     * {@link MapCellType#COLLIDABLE} {@link MapCell} or not.
     * @param rectangle {@link Entity} : The {@link Rectangle} to check.
     * @return {@code boolean} : {@code true} if the {@link Rectangle} is colliding
     *                              with a {@link MapCell},
     *                           {@code false} if not.
     */
    public boolean checkCollision(Rectangle rectangle) {
        return checkCollision(rectangle, MapCellType.COLLIDABLE);
    }

    /**
     * Returns whether an {@link Rectangle} is colliding with a
     * {@link MapCell} or not.
     * @param rectangle {@link Entity} : The {@link Rectangle} to check.
     * @param mapCellType {@link MapCellType} : The {@link MapCellType} to check for.
     * @return {@code boolean} : {@code true} if the {@link Rectangle} is colliding
     *                              with a {@link MapCell},
     *                           {@code false} if not.
     */
    public boolean checkCollision(Rectangle rectangle, MapCellType mapCellType) {
        MapCell collidingCell = getCollision(rectangle, false, mapCellType);

        // If it's null, then return false
        if (collidingCell == null) {
            return false;
        }

        // Custom checks
        if (mapCellType != MapCellType.COLLIDABLE && collidingCell == outOfBounds) {
            return false;
        }

        // Return true
        return true;
    }

    /**
     * Draw the floor tiles.
     *
     * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
     */
    public void drawGround (SpriteBatch batch) {
        // Draw a tile for every cell of the map.
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                getCellFull(x,y).drawBelow(batch);
            }
        }
    }

    /**
     * Draw the shape debug.
     *
     * @param shape {@link ShapeRenderer} : The {@link ShapeRenderer} to use.
     */
    public void drawDebug(ShapeRenderer shape) {
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                shape.setColor(Color.SKY);
                if ((x >= offsetX && x < width + offsetX) &&
                        (y >= offsetY && y < height + offsetY)) {
                    shape.setColor(Color.GREEN);
                }
                shape.rect(MapManager.gridToPos(x), MapManager.gridToPos(y), 64, 64);
            }
        }
    }

    /**
     * Loads all the {@link com.badlogic.gdx.graphics.Texture}s of the
     * {@link MapEntity}s.
     * @param textureManager The {@link TextureManager} to load using.
     */
    public void loadAll(TextureManager textureManager, String textureID) {
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                MapCell thisCell = getCellFull(x,y);
                if (thisCell != null) {
                    thisCell.load(textureManager, textureID);
                }
            }
        }
    }

    /**
     * Loads just the floor tiles of the MapCells
     * @param textureManager {@link TextureManager} : The {@link TextureManager}
     *                                                to use.
     */
    public void loadFloor(TextureManager textureManager, String textureID) {
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                MapCell thisCell = getCellFull(x,y);
                if (thisCell != null) {
                    thisCell.loadFloor(textureManager, textureID);
                }
            }
        }
    }

    /**
     * Update the floor sprites of all the {@link MapCell}s.
     * @param textureManager {@link TextureManager} : The {@link TextureManager}
     *                                                to use.
     */
    public void postLoad(TextureManager textureManager) {
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                MapCell thisCell = getCellFull(x,y);
                if (thisCell != null) {
                    thisCell.updateBelowTile(textureManager);
                }
            }
        }
    }

    // Draw function
    /**
     * @return {@link Array<MapEntity>} : All of the {@link MapEntity}s from the
     *                                    {@link MapCell}s.
     */
    public Array<MapEntity> getAllEntities() {
        Array<MapEntity> entities = new Array<>();
        // Loop through all the cells
        for (int x = 0 ; x < fullWidth ; x++) {
            for (int y = 0 ; y < fullHeight ; y++) {
                // Get the current cell
                MapCell thisCell = getCellFull(x,y);
                // Make sure it's not null
                if (thisCell != null) {
                    // Get the entity of the cell
                    MapEntity thisEntity = thisCell.mapEntity;
                    // If it's not null...
                    if (thisEntity != null) {
                        // and it's not already in the array...
                        if (!entities.contains(thisEntity, true)) {
                            // then add it to the array.
                            entities.add(thisEntity);
                        }
                    }
                }
            }
        }
        // Return all the entities.
        return entities;
    }

    /**
     * Get a {@link MapCellType#COLLIDABLE} {@link MapCell} that an
     * {@link Entity} is colliding with, if they are colliding with one.
     * <br><br>
     * Just returns the first {@link MapCell} found to be colliding.
     *
     * @param entity {@link Entity} : The {@link Entity} to check.
     * @return {@link MapCell} : The {@link MapCell} they are colliding with,
     *                           or {@code null} if they are not colliding with any.
     */
    public MapCell getCollision(Entity entity) {
        return getCollision(entity.collision);
    }

    /**
     * Get a {@link MapCellType#COLLIDABLE} {@link MapCell} that an
     * {@link Entity} is colliding with if they were are the position
     * specified, if they are colliding with one.
     * <br><br>
     * Returns the first {@link MapCell} found to be colliding.
     *
     * @param entity {@link Entity} : The {@link Entity} to check.
     * @param x {@link float} : The {@code x} to check the collision at.
     * @param y {@link float} : The {@code y} to check the collision at.
     * @return {@link MapCell} : The {@link MapCell} they are colliding with,
     *                           or {@code null} if they are not colliding with any
     */
    public MapCell getCollision(Entity entity, float x, float y) {
        return getCollision(new Rectangle(x, y, entity.collision.width, entity.collision.height));
    }

    /**
     * Get a {@link MapCellType#COLLIDABLE} {@link MapCell} that an
     * {@link Rectangle} is colliding with, if they are colliding with one.
     * <br><br>
     * Returns the first {@link MapCell} found to be colliding.
     *
     * @param rectangle {@link Rectangle} : The {@link Rectangle} to check.
     * @return {@link MapCell} : The {@link MapCell} they are colliding with,
     *                           or {@code null} if they are not colliding with any.
     */
    public MapCell getCollision(Rectangle rectangle) {
        return getCollision(rectangle, MapCellType.COLLIDABLE);
    }

    /**
     * Get a {@link MapCellType#COLLIDABLE} {@link MapCell} that an
     * {@link Rectangle} is colliding with, if they are colliding with one.
     *
     * @param rectangle {@link Rectangle} : The {@link Rectangle} to check.
     * @param returnClosest {@code boolean} : Whether the closest {@link MapCell} to
     *                                        the {@link Rectangle} should be returned
     *                                        or not.
     * @return {@link MapCell} : The {@link MapCell} they are colliding with,
     *                           or {@code null} if they are not colliding with any.
     */
    public MapCell getCollision(Rectangle rectangle, boolean returnClosest) {
        return getCollision(rectangle, returnClosest, MapCellType.COLLIDABLE);
    }

    /**
     * Get a {@link MapCellType#COLLIDABLE} {@link MapCell} that an
     * {@link Rectangle} is colliding with, if they are colliding with one.
     * <br><br>
     * Returns the first {@link MapCell} found to be colliding.
     *
     * @param rectangle {@link Rectangle} : The {@link Rectangle} to check.
     * @param mapCellType {@link MapCellType} : The {@link MapCellType} to check for.
     * @return {@link MapCell} : The {@link MapCell} they are colliding with,
     *                           or {@code null} if they are not colliding with any.
     */
    public MapCell getCollision(Rectangle rectangle, MapCellType mapCellType) {
        return getCollision(rectangle, false, mapCellType);
    }

    /**
     * @return {@code int} : The width of the play area.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return {@code int} : The height of the play area.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return {@code int} : The width of the full map.
     */
    public int getFullWidth() {
        return fullWidth;
    }

    /**
     * @return {@code int} : The height of the full map.
     */
    public int getFullHeight() {
        return fullHeight;
    }

    /**
     * @return {@code int} : The {@code x} offset of the play area.
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * @return {@code int} : The {@code y} offset of the play area.
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     * Unloads all of the {@link com.badlogic.gdx.graphics.Texture}s of the
     * {@link MapEntity}s, and then clears the map.
     */
    public void dispose() {
        // Loop through the cells
        for (int x = 0 ; x < width ; x++) {
            for (int y = 0 ; y < height ; y++) {
                // Get the current cell
                MapCell thisCell = getCell(x,y);
                // Make sure it's not nothing
                if (thisCell != null) {
                    // If it's not nothing, then unload
                    // thisCell.dispose();
                }
            }
        }
    }
}
