package com.undercooked.game.station;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Items;
import com.undercooked.game.interactions.Interactions;
import com.undercooked.game.logic.GameLogic;
import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.json.JsonFormat;

/**
 * Deals with the loading and storage of all {@link Station}s
 * and their {@link StationData} within the game.
 */
public class StationController {

  /**
   * An {@link Array} of the {@link Station}s that are in the
   * game.
   */
  public Array<Station> stations;
  /**
   * An {@link ObjectMap} that links a
   * {@link Station}'s id to the {@link StationData} that it
   * has loaded.
   */
  public ObjectMap<String, StationData> stationData;

  /**
   * Constructor for the {@link StationController}.
   */
  public StationController() {
    this.stations = new Array<>();
    this.stationData = new ObjectMap<>();
  }

  /**
   * A function that calls the {@link Station#update(float)} function
   * for all {@link Station}s that are stored by the {@link #stations}
   * {@link Array}.
   *
   * @param delta             {@code float} : The time since the last frame.
   * @param powerUpMultiplier {@code float} : The multiplier from power ups.
   */
  public void update(float delta, float powerUpMultiplier) {
    // Update all the stations.
    for (Station station : stations) {
      station.update(delta, powerUpMultiplier);
    }
  }

  /**
   * Calls the {@link Station#updateInteractions()} and
   * {@link Station#updateStationInteractions()} functions for
   * all of the {@link Station}s stored in the {@link #stations}
   * {@link Array}.
   * <br>
   * This sets up their interactions.
   */
  public void updateStationInteractions() {
    // Update all interactions for the stations.
    for (Station station : stations) {
      station.updateInteractions();
      station.updateStationInteractions();
    }
  }

  /**
   * Loads the {@link StationData} from the asset path provided, adds it
   * to the {@link StationController}'s {@link StationData} and then returns
   * it.
   *
   * @param stationPath {@link String} : The path to the station asset.
   * @return {@link StationData} : The {@link StationData} at that location,
   *                               or {@code null} if it wasn't loaded successfully.
   */
  public StationData loadStation(String stationPath) {
    StationData loadedData = loadStationPath(stationPath);
    // Add it to the array, if it's not null.
    if (loadedData != null) {
      stationData.put(stationPath, loadedData);
    }
    return loadedData;
  }

  /**
   * Loads the {@link StationData} from the asset path provided and returns it.
   *
   * @param stationPath {@link String} : The path to the station asset.
   * @return {@link StationData} : The {@link StationData} at that location,
   *                               or {@code null} if it wasn't loaded successfully.
   */
  public StationData loadStationPath(String stationPath) {
    if (stationData.containsKey(stationPath)) {
      return stationData.get(stationPath);
    }
    // Try to load this single station path
    // Read the file data
    JsonValue stationRoot = JsonFormat.formatJson(
            FileControl.loadJsonAsset(stationPath, "stations"),
            DefaultJson.stationFormat());

    // If it's not null...
    if (stationRoot != null) {
      // Load the data
      StationData data = new StationData(stationPath);
      // data.setPath(file.path());
      data.setTexturePath(stationRoot.getString("texture_path"));
      data.setWidth(stationRoot.getInt("width"));
      data.setHeight(stationRoot.getInt("height"));
      data.setDefaultBase(stationRoot.getString("default_base"));
      data.setFloorTile(stationRoot.getString("floor_tile"));
      data.setCollidable(stationRoot.getBoolean("has_collision"));

      // For collision width and height, if they are <= 0, then default to
      // grid size of the width and height
      float stationWidth = stationRoot.getFloat("collision_width");
      float stationHeight = stationRoot.getFloat("collision_height");

      data.setCollisionWidth(
              stationWidth > 0 ? stationWidth : MapManager.gridToPos(data.getWidth())
      );
      data.setCollisionHeight(
              stationHeight > 0 ? stationHeight : MapManager.gridToPos(data.getHeight())
      );

      data.setCollisionOffsetX(stationRoot.getFloat("collision_offset_x"));
      data.setCollisionOffsetY(stationRoot.getFloat("collision_offset_y"));

      data.setHoldCount(stationRoot.getInt("holds"));

      data.setPrice(stationRoot.getInt("price"));
      // Then add it to the stations list
      return data;
    }
    return null;
  }

  /**
   * Returns the {@link StationData} mapped to the id.
   *
   * @param stationId {@link String} : The id of the {@link Station}.
   * @return {@link StationData} : The data of the {@link Station}.
   */
  public StationData getStationData(String stationId) {
    return stationData.get(stationId);
  }


  /**
   * Add a {@link Station} to the {@link StationController}.
   *
   * @param station {@link Station} : The {@link Station} to add.
   */
  public void addStation(Station station) {
    // Only add if it's not contained already
    if (!stations.contains(station, true)) {
      stations.add(station);
    }
  }

  /**
   * Remove a {@link Station} from the {@link StationController}.
   *
   * @param station {@link Station} : The {@link Station} to remove.
   */
  public void removeStation(Station station) {
    stations.removeValue(station, true);
  }

  /**
   * Reset all {@link Station}s.
   */
  public void reset() {
    // Reset all stations
    for (Station station : stations) {
      station.reset();
    }
  }

  /**
   * Stop all {@link Station}s.
   */
  public void stopAll() {
    // Stop all stations
    for (Station station : stations) {
      station.stop();
    }
  }

  /**
   * Clear all of the {@link Station}s.
   */
  public void clear() {
    // Clear the stations
    stations.clear();
  }

  /**
   * Clear all of the {@link Station}s and {@link StationData}.
   */
  public void dispose() {
    // Clear
    clear();

    // And remove all StationData
    stationData.clear();
  }

  /**
   * Returns whether the {@link StationController} has the {@link StationData}
   * for the station id assigned or not.
   *
   * @param stationId {@link String} : The id of the {@link Station}.
   * @return {@code boolean} : {@code true} if the station data loaded has the station id,
   *                           {@code false} if it does not.
   */
  public boolean hasId(String stationId) {
    return stationData.containsKey(stationId);
  }

  /**
   * Converts all of the {@link Station}s in the game into a {@link JsonValue}
   * array, only if they need to be, to be stored externally.
   *
   * @param map {@link Map} : The {@link Map} of the game.
   * @return {@link JsonValue} : The {@link Station}s in a Json array form.
   */
  public JsonValue serializeStations(Map map) {
    // JsonValue stationsRoot = new JsonValue(JsonValue.ValueType.object);
    JsonValue stationsArrayRoot = new JsonValue(JsonValue.ValueType.array);
    // stationsRoot.addChild("stations", stationsArrayRoot);

    for (Station station : stations) {
      JsonValue stationData = station.serial(map);
      if (stationData == null) {
        continue;
      }
      stationsArrayRoot.addChild(stationData);
    }

    // return stationsRoot;
    return stationsArrayRoot;
  }

  /**
   * Takes a {@link JsonValue} of the {@link Station}s that was serialized,
   * and loads all of the {@link Station}s back into the game.
   *
   * @param logic        {@link GameLogic} : The {@link GameLogic} to load to.
   * @param jsonValue    {@link JsonValue} : The station Json data.
   * @param audioManager {@link AudioManager} : The {@link AudioManager} to load to.
   * @param interactions {@link Interactions} : The {@link Interactions} for the {@link Station}s
   *                     to use.
   * @param items        {@link Items} : The {@link Items} to load the {@link Item}s the
   *                                     {@link Station} is holding to.
   * @param map          {@link Map} : The {@link Map} to add the {@link Station} to.
   */
  public void deserializeStations(GameLogic logic, JsonValue jsonValue,
                                  AudioManager audioManager, Interactions interactions,
                                  Items items, Map map) {
    // For each station
    for (JsonValue stationRoot : jsonValue) {
      StationData data = loadStationPath(stationRoot.getString("station_id"));
      // If it's not null
      if (data != null) {
        // Create a new station
        Station station = new Station(data);
        final Array<Entity> removedEntities = MapManager.setupStation(map, stationRoot,
                this, station, data, interactions, audioManager, items);
        // Deserialize it
        station.setDisabled(stationRoot.getBoolean("disabled"));
        JsonValue itemsArray = stationRoot.get("items");
        for (JsonValue item : itemsArray) {
          Item thisItem = items.addItemAsset(item.asString());
          if (thisItem != null) {
            station.items.add(thisItem);
          }
        }

        // Add it to the map, and the game renderer
        logic.getGameRenderer().addEntity(station);
        // Remove the Station entities from this station controller
        for (Entity removedEntity : removedEntities) {
          if (removedEntity.getClass().equals(Station.class)) {
            removeStation((Station) removedEntity);
          }
        }
        // And remove the entities it removed from the game renderer
        logic.getGameRenderer().removeEntities(removedEntities);

        // Update station interactions
        station.updateInteractions();
        station.updateStationInteractions();
      }
    }
  }
}
