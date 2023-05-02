package de.tomgrill.gdxtesting.tests.audioTests;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.audio.AudioSettings;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.files.SettingsControl;

import org.junit.runner.RunWith;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AudioSettingsTests {

	static AudioSettings audioSettings;
	static MainGameClass game;
	static SettingsControl settingsControl;
	static String fileDir = FileControl.getDataPath();
	static String fileName = "testSettings5781.json";
	static String fileLoc = FileControl.dirAndName(fileDir, fileName);
	static FileHandle settingsFile = new FileHandle(fileLoc);

	@BeforeClass
	public static void setup() {
		game = new MainGameClass();
		settingsControl = new SettingsControl(fileLoc);
		settingsControl.loadIfNotLoaded();
		audioSettings = new AudioSettings(game, settingsControl);
	}

	@Test
	public void t00MusicVolListener() {
		audioSettings.musicVolListener.tell(0.15f);
		assertEquals("Music volume not set correctly.", 0.15f, audioSettings.getMusicVolume(), 0.01f);
	}

	@Test
	public void t01GameVolListener() {
		audioSettings.gameVolListener.tell(0.95f);
		assertEquals("Game volume not set correctly.", 0.95f, audioSettings.getGameVolume(), 0.01f);
	}

	@Test
	public void t10SaveSettings() {
		audioSettings.saveListener.tell(null);
		assertTrue("Settings file not created.", settingsFile.exists());
		JsonValue settings = FileControl.loadJsonFile(fileDir, fileName, false);
		assertEquals("Music volume not saved correctly.", 0.15f, settings.getFloat("musicVolume"), 0.01f);
		assertEquals("Game volume not saved correctly.", 0.95f, settings.getFloat("gameVolume"), 0.01f);
	}

	@Test
	public void t11LoadSettings() {
		audioSettings.loadListener.tell(null);
		assertEquals("Music volume not loaded correctly.", 0.15f, audioSettings.getMusicVolume(), 0.01f);
		assertEquals("Game volume not loaded correctly.", 0.95f, audioSettings.getGameVolume(), 0.01f);
	}

	@AfterClass
	public static void deleteTestFileDir() {
		settingsFile.delete();
		FileHandle dir = new FileHandle(fileDir);
		dir.deleteDirectory();
	}
}
