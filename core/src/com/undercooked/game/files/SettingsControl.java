package com.undercooked.game.files;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.json.JsonFormat;

/**
 * A class to control the settings of the game.
 */
public class SettingsControl {

    /** loaded = True if this class has loaded its data. False otherwise. */
    private boolean loaded;
    /** Location of settings file. */
    private final String fileLoc;
    /** json containing the settings. */
    private JsonValue settingsData;

    /**
     * Constructor for the {@link SettingsControl}.
     *
     * @param fileLoc {@link String} : The file location of the settings json.
     */
    public SettingsControl(String fileLoc) {
        this.fileLoc = fileLoc;
        this.loaded = false;
    }

    /**
     * Loads the setting data from the file location.
     */
    public void loadData() {
        // it's now loaded
        loaded = true;
        // Try to load the file
        settingsData = FileControl.loadJsonData(fileLoc);
        // If it failed to load...
        if (settingsData == null) {
            // Make new json and save it
            settingsData = new JsonValue(JsonValue.ValueType.object);
            JsonFormat.formatJson(settingsData, DefaultJson.settingsFormat());
            // And save the data
            saveData();
        } else {
            // Otherwise just format the json
            JsonFormat.formatJson(settingsData, DefaultJson.settingsFormat());
        }
    }

    /**
     * Unloads the settings data.
     */
    private void unload() {
        // Forget the json
        settingsData = null;
        // and it's not loaded
        loaded = false;
    }

    /**
     * Loads the settings data if it hasn't yet been loaded.
     */
    public void loadIfNotLoaded() {
        // Only load if it's not loaded.
        if (loaded)
            return;
        loadData();
    }

    /**
     * Saves the settings data.
     */
    public void saveData() {
        // Only save if it's loaded
        if (!loaded)
            return;
        FileControl.saveJsonData(fileLoc, settingsData);
    }

    /**
     * Sets a {@code float} value within the settings data.
     *
     * @param key {@link String} : The key to set.
     * @param val {@code float} : The value.
     */
    private void setFloatValue(String key, float val) {
        // If the json is not null
        if (settingsData == null)
            return;

        // Validate val is inbetween 0 and 1 inclusive
        if (val < 0 || val > 1)
            throw new IllegalArgumentException("Value must be between 0 and 1 inclusive.");

        // Then set the value
        settingsData.get(key).set(val, key);
    }

    /**
     * Set the music volume of the game.
     *
     * @param volume {@link float} : The volume to set the music to.
     */
    public void setMusicVolume(float volume) {
        setFloatValue("music_volume", volume);
    }

    /**
     * Set the game sounds volume.
     *
     * @param volume {@link float} : The volume to set the game to.
     */
    public void setGameVolume(float volume) {
        setFloatValue("game_volume", volume);
    }

    /**
     * @return {@code float} : The volume of the music in the settings data.
     */
    public float getMusicVolume() {
        return settingsData.getFloat("music_volume");
    }

    /**
     * @return {@code float} : The volume of the game sounds in the settings data.
     */
    public float getGameVolume() {
        return settingsData.getFloat("game_volume");
    }
}
