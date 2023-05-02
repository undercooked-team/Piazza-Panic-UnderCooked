package de.tomgrill.gdxtesting.tests.assetsTests;

import static org.junit.Assert.*;

import com.badlogic.gdx.audio.Sound;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.Constants;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AudioManagerTests {

	static AssetManager assetManager;
	static AudioManager audioManager;
	static Music defaultMusic;
	static Sound defaultSound;

	@BeforeClass
	public static void setup() {
		assetManager = new AssetManager();
		audioManager = new AudioManager(assetManager);
		assetManager.finishLoading();
		defaultMusic = assetManager.get(Constants.DEFAULT_MUSIC, Music.class);
		defaultSound = assetManager.get(Constants.DEFAULT_SOUND, Sound.class);
	}

	@Test
	public void t00DefaultAssetsLoaded() {
		assertTrue("Default music has not loaded",
				assetManager.isLoaded(Constants.DEFAULT_MUSIC, Music.class));
		assertTrue("Default sound has not loaded",
				assetManager.isLoaded(Constants.DEFAULT_SOUND, Sound.class));
	}

	@Test
	public void t10TestAssetLoaded() {
		/*assertTrue("Should be true as it is a default music",
				audioManager.assetLoaded("<main>:frying.mp3"));
		assertTrue("Should be true as it is a default music",
				audioManager.assetLoaded("<main>:chopping.mp3"));
		*/
	}

	@Test
	public void t20getMusicNormalCase() {
		Music music = audioManager.getMusic(Constants.DEFAULT_MUSIC);
		assertEquals("Should be true as it is a default music", defaultMusic, music);
		Sound sound = audioManager.getSound(Constants.DEFAULT_SOUND);
		assertEquals("Should be true as it is a default sound", defaultSound, sound);
	}

	@Test
	public void t21getMusicErrorCases() {
		Music music = audioManager.getMusic(null);
		assertEquals("Should be defaultMusic as this is a null input", defaultMusic, music);
		Music music2 = audioManager.getMusic("");
		assertEquals("Should be defaultMusic as this is a blank string input", defaultMusic, music2);
		Music music3 = audioManager.getMusic("error");
		assertEquals("Should be defaultMusic as this is an invalid input", defaultMusic, music3);
	}

	@Test
	public void t30getMusicAssetNormalCase() {
		Music music = audioManager.getMusicAsset("<main>:frying.mp3");
		assertEquals("Should be true as it is a default music", defaultMusic, music);
		Sound sound = audioManager.getSoundAsset("<main>:chopping.mp3");
		assertEquals("Should be true as it is a default sound", defaultSound, sound);
	}

	@Test
	public void t33getMusicAssetErrorCases() {
		Music music = audioManager.getMusic(null);
		assertEquals("Should be defaultMusic as this is a null input", defaultMusic, music);
		Music music2 = audioManager.getMusic("");
		assertEquals("Should be defaultMusic as this is a blank string input", defaultMusic, music2);
		Music music3 = audioManager.getMusic("error");
		assertEquals("Should be defaultMusic as this is an invalid input", defaultMusic, music3);
	}

	@Test
	public void t40loadMusicAsset() {
		assertNotEquals("Did not load music", defaultMusic,
				audioManager.loadMusicAsset("<main>:cash-register-opening.mp3", ""));
	}

	@Test
	public void t41unloadMusicNormalCase() {
		String musicPath = FileControl.toPath("<main>:cash-register-opening.mp3", "sounds");
		audioManager.unloadMusic(musicPath);
		assertFalse("Music is still loaded when it should've unloaded",
				assetManager.isLoaded(musicPath));
	}

	@Test
	public void t42unloadMusicErrorCases() {
		// Try to unload a music that is not loaded
		String musicPath = FileControl.toPath("<main>:cash-register-opening.mp3", "sounds");
		// Ensure that the functions below don't crash the program
		audioManager.unloadMusic(musicPath);
		audioManager.unloadMusic("I don't exist. Should not crash program.");
	}

	@Test
	public void t43loadMusicAssetErrorCases() {
		// Ensure that the default sound is loaded first, otherwise something has gone
		// wrong in testing
		assertTrue(
				"Something in testing has gone wrong. Default sound is not loaded when nothing should've unloaded it.",
				assetManager.isLoaded(Constants.DEFAULT_SOUND, Sound.class));

		// Unload the default music asset first, since we're testing that the default
		// music gets loaded by the erroneous inputs
		audioManager.unloadMusic(Constants.DEFAULT_SOUND);
		assertTrue("Should succeed loading the default sound", audioManager.loadMusicAsset(null, ""));
		assetManager.finishLoading();
		assertTrue("Default sound should be present as you can't load a null input",
				assetManager.isLoaded(Constants.DEFAULT_SOUND, Sound.class));

		// ! FIXME: Below tests are not working
		// audioManager.unloadMusic(Constants.DEFAULT_SOUND);
		// assertTrue("Should succeed loading the default sound",
		// audioManager.loadMusicAsset("", ""));
		// assetManager.finishLoading();
		// assertTrue("Default sound should be present as you can't load a blank string
		// input, which is an invalid input",
		// assetManager.isLoaded(Constants.DEFAULT_SOUND, Music.class));

		// audioManager.unloadMusic(Constants.DEFAULT_SOUND);
		// assertTrue("Should succeed loading the default sound",
		// audioManager.loadMusicAsset("error-Im not a music :D", ""));
		// assetManager.finishLoading();
		// assertTrue("Default sound should be present as the input is not a valid path
		// to music",
		// assetManager.isLoaded(Constants.DEFAULT_SOUND, Music.class));
	}

	@Test
	public void t50updateMusicVolumes() {
		audioManager.loadMusicAsset("main>:cash-register-opening.mp3", "");
		audioManager.setMusicVolume(0.2f, "");
		audioManager.updateMusicVolumes("");
		assertEquals("The audio volume should've updated.", 0.2f,
				audioManager.getMusicAsset("main>:cash-register-opening.mp3").getVolume());
	}
}
