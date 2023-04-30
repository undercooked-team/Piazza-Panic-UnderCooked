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
import java.nio.file.Paths;

import org.junit.*;
import org.junit.runner.RunWith;

import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.Constants;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class FileControlTests {

	String thisOS;

	@Before
	public void getSystemProperty() {
		// As it is not recommended to change the system properties, I want to ensure
		// the original properties are restored after tests are complete.
		thisOS = System.getProperty("os.name");
	}

	// DATAPATH TESTS {#61e,28}
	// #region
	@Test
	public void getLinuxDataPath() {
		System.setProperty("os.name", "Linux");
		String expLinFP = String.format("%s/data/%s/", System.getProperty("user.dir"), Constants.DATA_FILE);
		String outString = FileControl.getDataPath();
		assertEquals("Linux filepath incorrectly generated", expLinFP, outString);
		restoreSystemProperty();
	}

	@Test
	public void getWindowsDataPath() {
		System.setProperty("os.name", "Windows");
		String expWinFP = String.format("%s/%s/", System.getenv("APPDATA"), Constants.DATA_FILE);
		String outString = FileControl.getDataPath();
		assertEquals("Windows filepath incorrectly generated", expWinFP, outString);
		restoreSystemProperty();
	}

	@Test
	public void getDefaultDataPath() {
		System.setProperty("os.name", "ilabfkli{}1!s#");
		String expDefFP = String.format("/data/%s/", Constants.DATA_FILE);
		String outString = FileControl.getDataPath();
		assertEquals("Default filepath incorrectly generated", expDefFP, outString);
		restoreSystemProperty();
	}
	// #endregion

	@Test
	public void testFormatDir() {
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
	public void testSaveToFile() throws IOException {
		File testDir = new File(FileControl.getDataPath() + "testSaveToFile/");
		File testFile = new File("testFile.json");
		File fulltestPath = new File(testDir.toString() + testFile.toString());
		String testString = "This is a test string";
		String testString2 = "This is a test string 2";

		// Remove fulltestPath if it exists
		if (fulltestPath.exists()) {
			fulltestPath.delete();
		}
		// Remove testDir if it exists
		if (testDir.exists()) {
			testDir.delete();
		}
		// Use method to create testDir and testFile
		FileControl.saveToFile(testDir.toString(), testFile.toString(), testString);
		assertTrue("testDir not created", testDir.exists());
		assertTrue("testFile not created", testFile.exists());
		assertEquals("testFile not created with correct contents", testString,
				new String(Files.readAllBytes(Paths.get(testDir.toString(), testFile.toString())),
						StandardCharsets.UTF_8));

		// Use method to overwrite testFile
		FileControl.saveToFile(testDir.toString(), testFile.toString(), testString2);
		assertEquals("testFile not overwritten with correct contents", testString2,
				new String(Files.readAllBytes(Paths.get(testDir.toString(), testFile.toString())),
						StandardCharsets.UTF_8));

		// Test Cleanup
		fulltestPath.delete();
		testDir.delete();
	}

	@After
	public void restoreSystemProperty() {
		// Restore original system properties
		System.setProperty("os.name", thisOS);
	}
}
