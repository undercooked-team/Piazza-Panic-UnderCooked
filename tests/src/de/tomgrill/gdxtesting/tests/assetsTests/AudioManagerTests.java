package de.tomgrill.gdxtesting.tests.assetsTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.util.Constants;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AudioManagerTests {

	static AssetManager assetManager;
	static AudioManager audioManager;

	@BeforeClass
	public static void setup() {
		assetManager = new AssetManager();
		audioManager = new AudioManager(assetManager);
		assetManager.finishLoading();
	}

	@Test
	public void t00DefaultAssetsLoaded() {
		assertTrue("Default music has not loaded",
				assetManager.isLoaded(Constants.DEFAULT_MUSIC, Music.class));
		assertTrue("Default sound has not loaded",
				assetManager.isLoaded(Constants.DEFAULT_SOUND, Music.class));

		// assertTrue("Default music has not loaded",
		// audioManager.assetLoaded("<main>:frying.mp3"));
		// assertTrue("Default sound has not loaded",
		// audioManager.assetLoaded("<main>:chopping.mp3"));
	}
}
