package de.tomgrill.gdxtesting.tests.foodTests;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.files.SettingsControl;
import com.undercooked.game.food.Instruction;
import com.undercooked.game.util.json.JsonVal;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstructionTests {
	static Instruction instruction;
	static AssetManager assetManager;
	static TextureManager textureManager;
	static String texturePath = "<main>:item/lettuce.png";
	static String text = "This is some lettuce.";
	static JsonValue instructionRoot;

	@BeforeClass
	public static void setup() {
		instruction = new Instruction();
		assetManager = new AssetManager();
		textureManager = new TextureManager(assetManager);
		instruction.texturePath = texturePath;
		instruction.text = text;

		instructionRoot = new JsonValue(JsonValue.ValueType.object);
		instructionRoot.addChild("texture_path", new JsonValue(texturePath));
		instructionRoot.addChild("text", new JsonValue(text));
	}

	@Test
	public void t00_postLoad() {
		instruction.load(textureManager, "items");
		assetManager.finishLoading();
		instruction.postLoad(textureManager);
		assertNotNull("Instruction texture didn't load.", instruction.getTexture());
	}

	@Test
	public void t10_serial() {
		JsonValue jsonActual = instruction.serial();
		assertEquals("Instruction was not serialised properly.", instructionRoot.toString(), jsonActual.toString());
	}

	@Test
	public void t20_deserialise() {
		Instruction newInstruction = new Instruction();
		newInstruction.deserialise(instructionRoot);

		boolean areTheyEqual = instruction.text == newInstruction.text
				&& instruction.texturePath == newInstruction.texturePath;

		assertTrue("Instruction was not deserialised properly.", areTheyEqual);
	}
}
