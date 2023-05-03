package de.tomgrill.gdxtesting.tests.foodTests;

import de.tomgrill.gdxtesting.GdxTestRunner;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Instruction;
import com.undercooked.game.food.Request;

import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RequestTests {
  static Request request;
  static String itemId = "<main>:lettuce.png";
  static Instruction instruction1;
  static Instruction instruction2;
  static Instruction[] instructions;
  static JsonValue requestRoot;
  static AssetManager assetManager;
  static TextureManager textureManager;

  @BeforeClass
  public static void setupRequest() {
    assetManager = new AssetManager();
    textureManager = new TextureManager(assetManager);

    instruction1 = new Instruction();
    instruction1.text = "Here's some lettuce.";
    instruction1.texturePath = "<main>:item/lettuce.png";

    instruction2 = new Instruction();
    instruction2.text = "Here's a patty.";
    instruction2.texturePath = "<main>:item/patty.png";

    instructions = new Instruction[] { instruction1, instruction2 };

    request = new Request(itemId);
    request.setReputationThreat(4);
    request.setTime(0.5f);
    request.setValue(99);
    request.addInstruction(instruction1.texturePath, instruction1.text);
    request.addInstruction(instruction2.texturePath, instruction2.text);
    requestRoot = new JsonValue(JsonValue.ValueType.object);

    requestRoot.addChild("item_id", new JsonValue(itemId));
    requestRoot.addChild("value", new JsonValue(99));
    requestRoot.addChild("time", new JsonValue(0.5f));
    requestRoot.addChild("reputation_threat", new JsonValue(4));

    JsonValue instructionsRoot = new JsonValue(JsonValue.ValueType.array);
    for (Instruction instruction : instructions) {
      instructionsRoot.addChild(instruction.serial());
    }
    requestRoot.addChild("instructions", instructionsRoot);
  }

  @Test
  public void t00_constructor() {
    assertEquals(itemId, request.itemId);
    assertEquals(4, request.getReputationThreat());
    assertEquals(0.5f, request.getTime(), 0.01f);
    assertEquals(99, request.getValue());
    assertEquals(2, request.getInstructions().size);
  }

  @Test
  public void t10_load_postLoad() {
    request.load(textureManager, "group1");
    assetManager.finishLoading();
    request.postLoad(textureManager);
    assertNotNull("Texture for instruction 1 was not loaded.", request.getInstructions().get(0).getTexture());
    assertNotNull("Texture for instruction 2 was not loaded.", request.getInstructions().get(1).getTexture());
  }

  @Test
  public void t20_serial() {
    assertEquals("Serialised json does not match expected.", requestRoot.toString(), request.serial().toString());
  }

  @Test
  public void t21_constructorFromJson() {
    Request requestFromJson = new Request(requestRoot);
    assertEquals("Request wasn't deserialised properly: itemId's don't match.", request.itemId,
        requestFromJson.itemId);
    assertEquals("Request wasn't deserialised properly: reputationThreat's don't match.",
        request.getReputationThreat(),
        requestFromJson.getReputationThreat());
    assertEquals("Request wasn't deserialised properly: time's don't match.", request.getTime(),
        requestFromJson.getTime(), 0.01f);
    assertEquals("Request wasn't deserialised properly: value's don't match.", request.getValue(),
        requestFromJson.getValue());
    assertEquals("Request wasn't deserialised properly: instructions' sizes don't match.",
        request.getInstructions().size, requestFromJson.getInstructions().size);
    for (int i = 0; i < request.getInstructions().size; i++) {
      assertEquals("Request wasn't deserialised properly: instruction " + i + " texturePaths don't match.",
          request.getInstructions().get(i).texturePath,
          requestFromJson.getInstructions().get(i).texturePath);
      assertEquals("Request wasn't deserialised properly: instruction " + i + " texts don't match.",
          request.getInstructions().get(i).text, requestFromJson.getInstructions().get(i).text);
    }
  }

}
