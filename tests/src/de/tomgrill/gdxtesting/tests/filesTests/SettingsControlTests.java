/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tomgrill.gdxtesting.tests.filesTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.files.SettingsControl;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SettingsControlTests {

	static SettingsControl setCon;
	static String settingsFilename = "settingsTest.json";

	@BeforeClass
	public static void setup() {
		setCon = new SettingsControl(settingsFilename);
		setCon.loadData();
	}

	@Test
	public void t11SetMusicVolumeNormalCase() {
		setCon.setMusicVolume(0.3f);
		assertEquals(0.3f, setCon.getMusicVolume(), 0.01f);
	}

	@Test
	public void t12SetMusicVolumeErrCase() {
		assertThrows(IllegalArgumentException.class, () -> {
			setCon.setMusicVolume(-0.01f);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			setCon.setMusicVolume(1.01f);
		});
	}

	@Test
	public void t13SetGameVolumeNormalCase() {
		setCon.setGameVolume(0.8f);
		assertEquals(0.8f, setCon.getGameVolume(), 0.01f);
	}

	@Test
	public void t14SetGameVolumeErrCase() {
		assertThrows(IllegalArgumentException.class, () -> {
			setCon.setGameVolume(-0.01f);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			setCon.setGameVolume(1.01f);
		});
	}

	@Test
	public void t21SaveData() {
		setCon.saveData();
		String fullSettingsPath = FileControl.dirAndName(FileControl.getDataPath(), settingsFilename);
		assertTrue(new File(fullSettingsPath).exists());
		JsonValue expected = new JsonValue(JsonValue.ValueType.object);
		expected.addChild("music_volume", new JsonValue(0.3f));
		expected.addChild("game_volume", new JsonValue(0.8f));
		JsonValue actual = FileControl.loadJsonData(settingsFilename);
		do {
			assertEquals(expected.child.asFloat(), actual.child.asFloat(), 0.01f);
		} while (expected.next != null && actual.next != null);
	}

}
