package de.tomgrill.gdxtesting.tests.foodTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.files.SettingsControl;
import com.undercooked.game.food.Instruction;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstructionTests {
	static Instruction instruction;

	@BeforeClass
	public static void setup() {
		instruction = new Instruction();
		instruction.texturePath = "test.png";
		instruction.text = "test";
	}

	@Test
	public void t01_postLoad() {
		// Create a new TextureManager
		TextureManager textureManager = new TextureManager(new AssetManager());
		// Load the texture
		instruction.load(textureManager, "test");
		// Post load the texture
		instruction.postLoad(textureManager);
		// Check if the texture is not null
		// assertNotNull(instruction.getTexture());
		// assertEquals(1, 1);
	}
}
