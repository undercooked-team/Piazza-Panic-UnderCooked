package com.undercooked.game.map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.entity.cook.CookController;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Items;
import com.undercooked.game.interactions.Interactions;
import com.undercooked.game.station.Station;
import com.undercooked.game.station.StationController;
import com.undercooked.game.station.StationData;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.json.JsonFormat;

/**
 * A class for controlling the loading of a map asset, and the creation
 * of the {@link Map} from the Json data.
 */
public class MapManager {

  /**
   * The {@link TextureManager} to use.
   */
  private final TextureManager textureManager;

  /**
   * The {@link AudioManager} to use.
   */
  private final AudioManager audioManager;

  /**
   * Constructor for the {@link MapManager}.
   *
   * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
   * @param audioManager   {@link AudioManager} : The {@link AudioManager} to use.
   */
  public MapManager(TextureManager textureManager, AudioManager audioManager) {
    this.textureManager = textureManager;
    this.audioManager = audioManager;
  }

  /**
   * Load the map asset at the asset path provided.
   *
   * @param path              {@link String} : The path to tha map asset.
   * @param stationController {@link StationController} : The {@link StationController} to
   *                                                      load {@link Station}s
   *                                                      and {@link StationData} to.
   * @param cookController    {@link CookController} : The {@link CookController} to load
   *                                             {@link com.undercooked.game.entity.cook.Cook}s
   *                                                   to.
   * @param interactions      {@link Interactions} : The {@link Interactions} for the
   *                                                 {@link Station}s to use.
   * @param gameItems         {@link Items} : The {@link Items} of the game.
   * @return {@link Map} : The loaded {@link Map}.
   */
  public Map load(String path, StationController stationController,
                  CookController cookController, Interactions interactions, Items gameItems) {
    // Try loading the Json
    JsonValue root = JsonFormat.formatJson(FileControl.loadJsonAsset(path, "maps"),
            DefaultJson.mapFormat());
    // If it's null, then just load the default map and return that.
    if (root == null) {
      // Make sure this isn't the Default Map, to avoid an infinite loop.
      if (path != Constants.DEFAULT_MAP) {
        return load(Constants.DEFAULT_MAP, stationController,
                cookController, interactions, gameItems);
      } else {
        return null;
      }
    }

    // First clear the stationManager, as it will be used for this Map
    stationController.clear();

    // Convert the Map Json into an actual map
    Map outputMap = mapOfSize(root.getInt("width"), root.getInt("height"), stationController,
            audioManager, interactions, gameItems);

    // Loop through the stations
    for (JsonValue stationData : root.get("stations").iterator()) {
      // Put the whole thing in a try, just in case
      try {
        // Check station ID isn't null
        // If it is, just ignore.
        String stationId = stationData.getString("station_id");
        if (stationId != null) {
          // If stationManager doesn't have this station loaded, then load it
          if (!stationController.hasId(stationId)) {
            stationController.loadStation(stationId);
          }
          // If it's loaded, get the data for the Station
          StationData data = stationController.getStationData(stationId);
          // If station root is null, then ignore.
          // This will happen if the file wasn't found.
          if (data != null) {
            // Initialise the Station
            Station newStation = new Station(data);
            setupStation(outputMap, stationData, stationController,
                    newStation, data, interactions, audioManager, gameItems);
          }
        }
      } catch (GdxRuntimeException e) {
        e.printStackTrace();
      }
    }

    // Give the map to the CookController so that it can load the cooks
    cookController.loadCooksIntoMap(root, gameItems, outputMap, textureManager, true);

    return outputMap;
  }

  /**
   * Set up a {@link Station} on the {@link Map} using its {@link JsonValue}.
   *
   * @param map               {@link Map} : The {@link Map} to add the {@link Station} to.
   * @param jsonData          {@link JsonValue} : The Json of the {@link Station}.
   * @param stationController {@link StationController} : The {@link StationController} to use
   *                                                      for the {@link Station} and
   *                                                      {@link StationData}.
   * @param newStation        {@link Station} : The new {@link Station}, without loading its Json
   *                                            data onto it.
   * @param stationData       {@link StationData} : The data of the {@link Station}.
   * @param interactions      {@link Interactions} : The {@link Interactions} for the
   *                                                 {@link Station} to get interactions from.
   * @param audioManager      {@link AudioManager} : The {@link AudioManager} to use.
   * @param gameItems         {@link Items} : The {@link Items} of the game.
   * @return {@link Array}&lt;{@link Entity}&gt; : The entities removed from the {@link Map}
   *                                               by adding the {@link Station}.
   */
  public static Array<Entity> setupStation(Map map, JsonValue jsonData,
                                           StationController stationController,
                                           Station newStation, StationData stationData,
                                           Interactions interactions, AudioManager audioManager,
                                           Items gameItems) {
    newStation.makeInteractionController(audioManager, gameItems);
    newStation.setInteractions(interactions);

    // Check if there is a custom base
    String basePath = jsonData.getString("base_texture");
    if (basePath != null) {
      // If there is, then set the newStation to use it
      newStation.setBasePath(basePath);
    }
    // Add it to the station manager
    stationController.addStation(newStation);

    // Check if it has a custom has_collision tag
    if (jsonData.has("has_collision")) {
      // If it does, then set the station to use it
      newStation.hasCollision = jsonData.getBoolean("has_collision");
    } else {
      // If it doesn't, get it from the station data
      newStation.hasCollision = stationData.isCollidable();
    }

    // Check if it has a custom price
    if (jsonData.has("price")) {
      // If it does, then set the station to use it
      newStation.setPrice(jsonData.getInt("price"));
      // If not, the station will use the StationData,
      // which it does when give the StationData
    }

    // Add it to the map
    Array<Entity> removedEntities = map.addMapEntity(newStation,
            jsonData.getInt("x"),
            jsonData.getInt("y"),
            stationData.getFloorTile(),
            newStation.hasCollision);

    // If it does have collision
    if (newStation.hasCollision) {
      // Then set up the collision values
      newStation.collision.setWidth(stationData.getCollisionWidth());
      newStation.collision.setHeight(stationData.getCollisionHeight());
      newStation.offsetX = stationData.getCollisionOffsetX();
      newStation.offsetY = stationData.getCollisionOffsetY();
    } else {
      // If it doesn't, just set the collision to the size of the
      // station
      newStation.collision.setWidth(gridToPos(stationData.getWidth()));
      newStation.collision.setHeight(gridToPos(stationData.getHeight()));
    }
    return removedEntities;
  }

  /**
   * Unloads the map and renderer, if there is one of either.
   */
  public void unload() {

  }

  /**
   * Creates a new counter {@link Station} and returns it, loading it as needed.
   *
   * @param stationController {@link StationController} : The {@link StationController} to use
   *                                                      for the {@link Station} and
   *                                                      {@link StationData}.
   * @param audioManager      {@link AudioManager} : The {@link AudioManager} to use.
   * @param interactions      {@link Interactions} : The {@link Interactions} for the
   *                                                 {@link Station} to
   *                                                 get interactions from.
   * @param gameItems         {@link Items} : The {@link Items} of the game.
   * @return {@link Station} : The newly created counter.
   */
  public static Station newCounter(StationController stationController,
                                   AudioManager audioManager, Interactions interactions,
                                   Items gameItems) {
    // If the counter isn't loaded, then try to load it
    if (!stationController.hasId("<main>:counter")) {
      StationData loadedData = stationController.loadStation("<main>:counter");
      if (loadedData == null) {
        // If it doesn't load, throw an error
        throw new RuntimeException("Counter could not load.");
      }
    }
    // Create the station
    Station newCounter = new Station(stationController.getStationData("<main>:counter"));
    newCounter.makeInteractionController(audioManager, gameItems);
    newCounter.setInteractions(interactions);
    // Add it to the station manager
    stationController.addStation(newCounter);

    // Finally, return the counter
    return newCounter;
  }

  /**
   * Creates a {@link Map} of size width x height.
   *
   * @param width             {@code int} : The width of the {@link Map}.
   * @param height            {@code int} : The height of the {@link Map}.
   * @param stationController {@link StationController} : The {@link StationController} to use
   *                                                      for the {@link Station} and
   *                                                      {@link StationData}.
   * @param audioManager      {@link AudioManager} : The {@link AudioManager} to use.
   * @param interactions      {@link Interactions} : The {@link Interactions} for the
   *                                                 {@link Station} to get interactions from.
   * @param gameItems         {@link Items} : The {@link Items} of the game.
   * @return {@link Map} : A {@link Map} of size width x height.
   */
  public static Map mapOfSize(int width, int height, StationController stationController,
                              AudioManager audioManager, Interactions interactions,
                              Items gameItems) {
    // The map size is the area that the players can run around in.
    // Therefore, height += 2, for top and bottom counters, and then
    // width has a few more added, primarily on the left.
    int fullWidth = width + 4;
    int fullHeight = height + 1;
    Map returnMap = new Map(width, height, fullWidth, fullHeight);
    int offsetX = 4;
    int offsetY = 1;
    returnMap.setOffsetX(offsetX);
    returnMap.setOffsetY(offsetY);

    // Add the counter border
    // X entities
    for (int i = 0; i < width; i++) {
      returnMap.addMapEntity(newCounter(stationController,
              audioManager, interactions, gameItems), i, 0, null);
      returnMap.addMapEntity(newCounter(stationController,
              audioManager, interactions, gameItems), i, height - 1, null);
    }
    // Y entities
    for (int j = 1; j < height - 1; j++) {
      returnMap.addMapEntity(newCounter(stationController,
              audioManager, interactions, gameItems), 0, j, null);
      returnMap.addMapEntity(newCounter(stationController,
              audioManager, interactions, gameItems), width - 1, j, null);
    }

    // Leftmost wall
    for (int j = 1; j < fullHeight; j++) {
      returnMap.addFullMapEntity(newCounter(stationController,
              audioManager, interactions, gameItems), 0, j, null);
    }

    // Add 2 at the top at x=1, x=2
    returnMap.addFullMapEntity(newCounter(stationController,
            audioManager, interactions, gameItems), 1, fullHeight - 1, null);
    returnMap.addFullMapEntity(newCounter(stationController,
            audioManager, interactions, gameItems), 2, fullHeight - 1, null);
    returnMap.addFullMapEntity(newCounter(stationController,
            audioManager, interactions, gameItems), 3, fullHeight - 1, null);

    // Add the customer floor tiles
    for (int i = 2; i <= 3; i++) {
      for (int j = 0; j < fullHeight - 2; j++) {
        MapCell thisCell = returnMap.getCellFull(i, j);
        if (j == 0) {
          thisCell.setBelowTile("<main>:floor/customer_tile_bot.png");
        } else if (j == fullHeight - 3) {
          thisCell.setBelowTile("<main>:floor/customer_tile_top.png");
        } else {
          thisCell.setBelowTile("<main>:floor/customer_tile_mid.png");
        }
      }
    }

    return returnMap;
  }

  /**
   * Converts a grid position into a pixel position value.
   *
   * @param gridPos {@code float} : The position on the {@link Map} grid.
   * @return {@code float} : The pixel position of the grid position.
   */
  public static float gridToPos(float gridPos) {
    return gridPos * 64F;
  }

  /**
   * Converts a pixel position into a grid position value,
   * and floors it to be an int.
   *
   * @param pos {@code float} : The pixel position.
   * @return {@code int} : The grid position of the pixel position, floored.
   */
  public static int posToGridFloor(float pos) {
    return (int) (pos / 64F);
  }

  /**
   * Converts a pixel position into a grid position value.
   *
   * @param pos {@code float} : The pixel position.
   * @return {@code float} : The grid position of the pixel position.
   */
  public static float posToGrid(float pos) {
    return (pos / 64F);
  }

}
