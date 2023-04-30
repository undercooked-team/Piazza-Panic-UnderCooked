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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.Constants;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileControlTests {

	static String thisOS;
	static File testSaveLoadDir = new File(FileControl.getDataPath() + "testSaveLoad68732/");
	static File testSaveLoadFile = new File("testFile5769.json");
	static File fullSaveLoadFilePath = new File(testSaveLoadDir.toString(), testSaveLoadFile.toString());
	static File fullSaveLoadFilePath2 = new File(FileControl.getDataPath().toString(), testSaveLoadFile.toString());
	static String testString = "This is a test string.";
	static JsonValue testJsonRoot = new JsonValue(JsonValue.ValueType.object);

	@BeforeClass
	public static void getSystemProperty() {
		// As it is not recommended to change the system properties, I want to ensure
		// the original properties are restored after tests are complete.
		thisOS = System.getProperty("os.name");
	}

	@BeforeClass
	public static void initializeTestData() {
		testJsonRoot.addChild("child", new JsonValue(testString));
	}

	// DATAPATH TESTS {#61e,28}
	// #region
	@Test
	public void t11getLinuxDataPath() {
		System.setProperty("os.name", "Linux");
		String expLinFP = String.format("%s/data/%s/", System.getProperty("user.dir"), Constants.DATA_FILE);
		String outString = FileControl.getDataPath();
		assertEquals("Linux filepath incorrectly generated", expLinFP, outString);
		restoreSystemProperty();
	}

	@Test
	public void t12getWindowsDataPath() {
		System.setProperty("os.name", "Windows");
		String expWinFP = String.format("%s/%s/", System.getenv("APPDATA"), Constants.DATA_FILE);
		String outString = FileControl.getDataPath();
		assertEquals("Windows filepath incorrectly generated", expWinFP, outString);
		restoreSystemProperty();
	}

	@Test
	public void t13getDefaultDataPath() {
		System.setProperty("os.name", "ilabfkli{}1!s#");
		String expDefFP = String.format("/data/%s/", Constants.DATA_FILE);
		String outString = FileControl.getDataPath();
		assertEquals("Default filepath incorrectly generated", expDefFP, outString);
		restoreSystemProperty();
	}
	// #endregion

	@Test
	public void t21testFormatDir() {
		String[] testDirs = new String[] {
				"/here/is/my/linux/dir",
				"C:\\here\\is\\my\\windows\\dir",
				"here/is/my/relative/dir",
				"#%^&Random/\\Input&*(6,)"
		};
		String[] endings = new String[] {
				"",
				"\\",
				"/",
				"\\/",
				"//",
				"\\\\",
				"//\\",
				"\\//",
				"\\//\\"
		};

		for (int i = 0; i < testDirs.length; i++) {
			for (int j = 0; j < endings.length; j++) {
				String testDir = testDirs[i] + endings[j];
				String outDir = FileControl.formatDir(testDir);
				String expDir = testDir + (testDir.endsWith("\\") || testDir.endsWith("/") ? "" : "/");
				assertEquals("Directory not formatted correctly", expDir, outDir);
			}
		}
	}

	@Test
	public void t22SaveToFileCreatesDirAndFilesAndWritesCorrectly() throws IOException {
		// Remove fulltestPath if it exists
		fullSaveLoadFilePath.delete();
		testSaveLoadDir.delete();

		// Use method to create testDir and testFile
		FileControl.saveToFile(testSaveLoadDir.toString(), testSaveLoadFile.toString(), testString);
		assertTrue("testDir not created", testSaveLoadDir.exists());
		assertTrue("testFile not created", fullSaveLoadFilePath.exists());

		String readFile = new String(Files.readAllBytes(
				fullSaveLoadFilePath.toPath()), StandardCharsets.UTF_8);
		readFile = readFile.substring(0, readFile.length() - 1);

		assertEquals("testFile not created with correct contents", testString, readFile);
	}

	@Test
	public void t23SaveToFileOverwritesToFileCorrectly() throws IOException {

		// Use method to overwrite testFile
		FileControl.saveToFile(testSaveLoadDir.toString(), testSaveLoadFile.toString(), testString);
		String readFile = new String(Files.readAllBytes(
				fullSaveLoadFilePath.toPath()), StandardCharsets.UTF_8);
		readFile = readFile.substring(0, readFile.length() - 1);

		assertEquals("testFile not overwritten with correct contents", testString, readFile);
	}

	@Test
	public void t24SaveData() throws IOException {

		// Use method to save testFile
		FileControl.saveData(testSaveLoadFile.toString(), testString);
		String readFile = new String(Files.readAllBytes(
				fullSaveLoadFilePath2.toPath()), StandardCharsets.UTF_8);
		readFile = readFile.substring(0, readFile.length() - 1);

		assertEquals("testFile not saved with correct contents", testString, readFile);
	}

	@Test
	public void t31LoadFileNormalCase() throws IOException {

		// Use method to load testFile
		String outString = FileControl.loadFile(testSaveLoadDir.toString(), testSaveLoadFile.toString());
		outString = outString.substring(0, outString.length() - 1);

		assertEquals("testFile not loaded with correct contents", testString, outString);
	}

	@Test
	public void t31LoadFileErrorCase() throws IOException {

		assertEquals("Should fail gracefully when file does not exist", "",
				FileControl.loadFile("some/Random-directory/hey/7218hdwq", "nonexistentfile.txt"));
	}

	@Test
	public void t32LoadData() throws IOException {

		// Use method to load testFile
		String outString = FileControl.loadData(testSaveLoadFile.toString());
		outString = outString.substring(0, outString.length() - 1);

		assertEquals("testFile not loaded with correct contents", testString, outString);
	}

	@Test
	public void t33LoadFileCreatesDirectoryIfFileDoesNotExist() {
		removeTestDirs();

		// Use method to load testFile
		// * Note that the internal = true cannot be tested using unit tests so easily
		// and thus has been omitted from the unit testing
		String outString = FileControl.loadFile(testSaveLoadDir.toString(), testSaveLoadFile.toString(), false);
		outString = outString.substring(0, outString.length() - 1);

		assertTrue("testDir not created", testSaveLoadDir.exists());
		assertTrue("testFile not created", fullSaveLoadFilePath.exists());
		assertEquals("testFile should be empty", "", outString);
	}

	// By this point, the save and load data methods will create and overwrite the
	// file, so there's no need to test for both, from here on out

	@Test
	public void t41SaveJsonFile() {
		FileControl.saveJsonFile(testSaveLoadDir.toString(), testSaveLoadFile.toString(), testJsonRoot);

		assertEquals("Json value was not saved properly", testJsonRoot.toJson(JsonWriter.OutputType.json),
				FileControl.loadFile(testSaveLoadDir.toString(), testSaveLoadFile.toString()).trim());
	}

	@Test
	public void t42SaveJsonData() {
		FileControl.saveJsonData(testSaveLoadFile.toString(), testJsonRoot);

		assertEquals("Json value was not saved properly", testJsonRoot.toJson(JsonWriter.OutputType.json),
				FileControl.loadData(testSaveLoadFile.toString()).trim());
	}

	@Test
	public void t43LoadJsonFile() {
		JsonValue outJson = FileControl.loadJsonFile(testSaveLoadDir.toString(), testSaveLoadFile.toString(), false);

		assertEquals("Json value was not loaded properly", testJsonRoot.toJson(JsonWriter.OutputType.json),
				outJson.toJson(JsonWriter.OutputType.json));
	}

	@Test
	public void t44LoadJsonData() {
		JsonValue outJson = FileControl.loadJsonData(testSaveLoadFile.toString());

		assertEquals("Json value was not loaded properly", testJsonRoot.toJson(JsonWriter.OutputType.json),
				outJson.toJson(JsonWriter.OutputType.json));
	}

	// TODO LoadJsonAsset
	// TODO GetAssetPath

	@AfterClass
	public static void restoreSystemProperty() {
		// Restore original system properties
		System.setProperty("os.name", thisOS);
	}

	@AfterClass
	public static void removeTestDirs() {
		fullSaveLoadFilePath.delete();
		testSaveLoadDir.delete();
		fullSaveLoadFilePath2.delete();
	}
}
