package com.undercooked.game.audio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.files.SettingsControl;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;

/**
 * A class for controlling the audio of the game,
 * so that the music and game volumes is automatically
 * adjusted when the {@link AudioSliders} are changed.
 */
public class AudioSettings {

    private float musicVolume;
    private float gameVolume;
    private final MainGameClass game;
    private final SettingsControl settingsControl;

    /**
     * Constructor for the {@link AudioSettings}.
     *
     * @param game {@link MainGameClass} : The {@link MainGameClass} of the game.
     * @param settingsControl {@link SettingsControl} : The {@link SettingsControl} for loading
     *                                                  the volumes in the settings json.
     */
    public AudioSettings(MainGameClass game, SettingsControl settingsControl) {
        this.game = game;
        this.settingsControl = settingsControl;
    }

    /**
     * The music volume listener, for when the music volume is changed
     * by a {@link Slider}.
     */
    public final Listener<Float> musicVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            setMusicVol(value);
        }
    };

    /**
     * The game volume listener, for when the game sounds volume is changed
     * by a {@link Slider}.
     */
    public final Listener<Float> gameVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            setGameVol(value);
        }
    };

    /**
     * The save listener, for when a {@link Slider} has had a value changed,
     * and therefore needs to be saved.
     */
    public final Listener<Float> saveListener = new Listener<Float>() {
        @Override
        public void tell(Float change) {
            // Only save if there was a change
            if (change == 0) return;
            saveVolumes();
        }
    };

    /**
     * Creates the game's {@link AudioSliders} using the provided values, with
     * the {@link Listener}s for updating and saving the volumes already added.
     *
     * @param x {@code float} : The x position to place it at.
     * @param y {@code float} : The y position to place it at.
     * @param stage {@link Stage} : The {@link Stage} to add the {@link Slider}s to.
     * @param backText {@link Texture} : The {@link Texture} to show behind the {@link Slider}s.
     * @param buttonTex {@link Texture} : The {@link Texture} for the {@link Slider} button.
     * @return {@link AudioSliders} : The {@link AudioSliders}.
     */
    public AudioSliders createAudioSliders(float x, float y, Stage stage, Texture backText, Texture buttonTex) {
        AudioSliders audioSliders = new AudioSliders(x,y,300,200, backText);
        audioSliders.setSliderWidth(0.18F);

        Slider musicSlider = audioSliders.addSlider(musicVolListener, buttonTex);
        musicSlider.addToStage(stage);
        musicSlider.setValue(getMusicVolume());
        musicSlider.addReleaseListener(saveListener);

        Slider gameSlider = audioSliders.addSlider(gameVolListener, buttonTex);
        gameSlider.addToStage(stage);
        gameSlider.setValue(getGameVolume());
        gameSlider.addReleaseListener(saveListener);

        return audioSliders;
    }

    /**
     * Set the volume of a {@link com.badlogic.gdx.audio.Music} volume group.
     *
     * @param volume {@link float} : The volume to set the music to.
     * @param audioGroup {@link String} : The audio group to set it for.
     */
    public void setMusicVolume(float volume, String audioGroup) {
        game.audioManager.setMusicVolume(volume, audioGroup);
    }

    /**
     *  Set the volume of the default {@link com.badlogic.gdx.audio.Music} volume group.
     *
     * @param volume {@link float} : The volume to set the {@link com.badlogic.gdx.audio.Music}
     *                               to.
     */
    public void setMusicVolume(float volume) {
        setMusicVolume(volume, "default");
    }

    /**
     * Set the volume of the game music.
     *
     * @param volume {@code float} : The volume to set the music to.
     */
    public void setMusicVol(float volume) {
        musicVolume = volume;
        setMusicVolume(volume, Constants.MUSIC_GROUP);
    }

    /**
     * Set the volume of the game sounds.
     *
     * @param volume {@code float} : The volume to set the music to.
     */
    public void setGameVol(float volume) {
        gameVolume = volume;
        setMusicVolume(volume, Constants.GAME_GROUP);
        setSoundVolume(volume, Constants.GAME_GROUP);
    }

    /**
     * Save the updated volumes to the settings json.
     */
    public void saveVolumes() {
        if (settingsControl == null) return;
        settingsControl.setMusicVolume(musicVolume);
        settingsControl.setGameVolume(gameVolume);
        settingsControl.saveData();
    }

    /**
     * Load the volumes from the settings json.
     */
    public void loadVolumes() {
        if (settingsControl == null) return;
        settingsControl.loadIfNotLoaded();
        setMusicVol(settingsControl.getMusicVolume());
        setGameVol(settingsControl.getGameVolume());
    }

    /**
     * Set the volume of a {@link com.badlogic.gdx.audio.Sound} volume group.
     *
     * @param volume {@link float} : The volume to set the sounds to.
     * @param audioGroup {@link String} : The audio group to set it for.
     */
    public void setSoundVolume(float volume, String audioGroup) {
        game.audioManager.setSoundVolume(volume, audioGroup);
    }

    /**
     *  Set the volume of the default {@link com.badlogic.gdx.audio.Sound} volume group.
     *
     * @param volume {@link float} : The volume to set the {@link com.badlogic.gdx.audio.Sound}
     *                               to.
     */
    public void setSoundVolume(float volume) {
        setSoundVolume(volume, "default");
    }

    /**
     * @return {@code float} : The music volume.
     */
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * @return {@code float} : The game sound's volume.
     */
    public float getGameVolume() {
        return gameVolume;
    }

}
