package com.undercooked.game.interactions;

import com.badlogic.gdx.Gdx;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Items;
import com.undercooked.game.station.Station;
import com.undercooked.game.util.Listener;

/**
 * The class for controlling the variables of the {@link InteractionStep}.
 */
public class InteractionInstance {
    // Holds the variables needed by the StationInteractControl to give to the InteractionSteps.
    // This makes it so only 1 InteractionStep has to exist at one point for each Interaction.

    /** The station to update */
    public final Station station;

    /** The {@link StationInteractControl} instance to use. */
    public final StationInteractControl interactControl;

    /** The items within the game. Useful for Interactions. */
    public Items gameItems;

    /** The elapsed time for the Interaction */
    public float elapsedTime;
    /** The {@link AudioManager} to use to play the sound */
    private final AudioManager audioManager;
    /** The result of the last delta check, the time since the last frame. */
    private float lastDeltaCheck;

    /**
     * @param station {@link Station} : The {@link Station} that holds this interaction instance.
     * @param interactControl {@link StationInteractControl} : The {@link StationInteractControl} that controls
     *                                                         the interaction data.
     * @param audioManager {@link AudioManager} : The {@link AudioManager} to use.
     * @param gameItems {@link Items} : The {@link Items} of the game.
     */
    public InteractionInstance(Station station, StationInteractControl interactControl, AudioManager audioManager, Items gameItems) {
        this.station = station;
        this.gameItems = gameItems;
        this.interactControl = interactControl;
        this.audioManager = audioManager;
        this.elapsedTime = 0;
        this.lastDeltaCheck = 0;
    }

    /**
     * Updates the current {@link Station} interaction.
     */
    public void updateInteractions() {
        // Then update the station
        station.updateStationInteractions();
    }

    /**
     * Plays a sound using {@link com.badlogic.gdx.audio.Music}.
     * @param soundPath {@link String} : The asset path to the sound.
     */
    public void playSound(String soundPath) {
        if (soundPath != null) {
            //if (!audioManager.musicIsPlaying(soundID)) {
            audioManager.getMusicAsset(soundPath).play();
        }
    }

    /**
     * Stops a sound using {@link com.badlogic.gdx.audio.Music}.
     * @param soundPath {@link String} : The asset path to the sound.
     */
    public void stopSound(String soundPath) {
        audioManager.getMusicAsset(soundPath).stop();
    }

    /**
     * Resets the variables of the {@link InteractionInstance}.
     */
    public void reset() {
        elapsedTime = 0;
    }

    /**
     * Updates the delta variable.
     */
    public void updateDelta() {
        this.lastDeltaCheck = Gdx.graphics.getDeltaTime();
    }

    /**
     * @return {@code float} : The time since the last frame.
     */
    public float getDelta() {
        return this.lastDeltaCheck;
    }
}
