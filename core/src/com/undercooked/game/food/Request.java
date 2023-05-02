package com.undercooked.game.food;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.TextureManager;

/**
 * The class for a request that a {@link com.undercooked.game.entity.customer.Customer}
 * can make that the player has to serve to them.
 */
public class Request {

  /**
   * The {@link Item} id of the {@link Request}.
   */
  public String itemId;

  /**
   * How much money the {@link Request} is worth.
   */
  private int value;

  /**
   * The time limit on the {@link Request}.
   */
  private float time;

  /**
   * How much reputation will be lost if the {@link Request} is not
   * complete correctly.
   */
  private int reputationThreat;

  /**
   * The instructions of the {@link Request}.
   */
  private final Array<Instruction> instructions;

  /**
   * Constructor for the {@link Request}.
   *
   * @param itemId {@link String} : The id of the {@link Item} requested.
   */
  public Request(String itemId) {
    this.itemId = itemId;
    this.instructions = new Array<>();
    this.time = -1;
  }

  /**
   * Constructor for the {@link Request} from a {@link JsonValue}.
   *
   * @param requestRoot {@link JsonValue} : The Json to load the {@link Request} from.
   */
  public Request(JsonValue requestRoot) {
    this(requestRoot.getString("item_id"));
    deserialise(requestRoot);
  }

  /**
   * Load the {@link Instruction}s.
   *
   * @param textureManager {@link TextureManager} : The {@link TextureManager} to load using.
   * @param textureGroup   {@link String} : The texture group to use.
   */
  public void load(TextureManager textureManager, String textureGroup) {
    for (Instruction instruction : instructions) {
      instruction.load(textureManager, textureGroup);
    }
  }

  /**
   * Post load the {@link Instruction}s.
   *
   * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
   */
  public void postLoad(TextureManager textureManager) {
    for (Instruction instruction : instructions) {
      instruction.postLoad(textureManager);
    }
  }

  /**
   * Create and add an {@link Instruction} to the {@link Request}.
   *
   * @param texturePath {@link String} : The asset path to the instruction's {@link Texture}.
   * @param text        {@link String} : The text of the {@link Instruction}.
   * @return {@link Instruction} : The newly created {@link Instruction}.
   */
  public Instruction addInstruction(String texturePath, String text) {
    Instruction newInstruction = new Instruction();

    newInstruction.texturePath = texturePath;
    newInstruction.text = text;
    instructions.add(newInstruction);

    return newInstruction;
  }

  /**
   * Set the monetary value of the {@link Request}, with a minimum of 0.
   *
   * @param value {@code int} : The value to set the {@link Request} to.
   */
  public void setValue(int value) {
    this.value = Math.max(0, value);
  }

  /**
   * Set the reputation threat of the {@link Request}, with a minimum of 0.
   *
   * @param reputationThreat {@code int} : The reputation threat to set the {@link Request} to.
   */
  public void setReputationThreat(int reputationThreat) {
    this.reputationThreat = Math.max(0, reputationThreat);
  }

  /**
   * Set the time of the {@link Request}.
   * <br><br>
   * If set to &lt;0, the {@link Request} has no timer.
   *
   * @param time {@link float} : The time to set the {@link Request} to.
   */
  public void setTime(float time) {
    this.time = time;
  }

  /**
   * Returns how much money should be given if the {@link Request}
   * is served successfully.
   *
   * @return {@code int} : The value of the {@link Request}.
   */
  public int getValue() {
    return value;
  }

  /**
   * Returns how much reputation should be lost if the {@link Request}
   * is failed to be served.
   *
   * @return {@code int} : The reputation threat of the {@link Request}.
   */
  public int getReputationThreat() {
    return reputationThreat;
  }

  /**
   * Returns the {@link Array} of {@link Instruction}s of the {@link Request}.
   *
   * @return {@link Array}&lt;{@link Instruction}&gt; : The {@link Instruction}s
   *                                  of the {@link Request} in an {@link Array}.
   */
  public Array<Instruction> getInstructions() {
    return instructions;
  }

  /**
   * Returns the time that the {@link Request} should be served in.
   * <br>
   * If < 0, it should have no timer.
   *
   * @return {@code float} : The time of the {@link Request}.
   */
  public float getTime() {
    return this.time;
  }

  /**
   * Serializes the {@link Request} into a {@link JsonValue} so that
   * it can be stored externally.
   *
   * @return {@link JsonValue} : The {@link Request} in Json form.
   */
  public JsonValue serial() {
    JsonValue requestRoot = new JsonValue(JsonValue.ValueType.object);
    requestRoot.addChild("item_id", new JsonValue(itemId));
    requestRoot.addChild("value", new JsonValue(value));
    requestRoot.addChild("time", new JsonValue(time));
    requestRoot.addChild("reputation_threat", new JsonValue(reputationThreat));

    // * Caveat: As instructions is an Array<Instruction>, I cannot create a serial
    // function for it.
    // * meaning, its serialization will be done here.
    JsonValue instructionsRoot = new JsonValue(JsonValue.ValueType.array);
    for (Instruction instruction : instructions) {
      instructionsRoot.addChild(instruction.serial());
    }
    requestRoot.addChild("instructions", instructionsRoot);
    return requestRoot;
  }

  /**
   * Takes a {@link JsonValue} of the {@link Request} and sets the values
   * of the {@link Request} and adds the {@link Instruction}s.
   *
   * @param requestRoot {@link JsonValue} : The Json of the {@link Request}.
   */
  public void deserialise(JsonValue requestRoot) {
    setTime(requestRoot.getFloat("time"));
    setValue(requestRoot.getInt("value"));
    setReputationThreat(requestRoot.getInt("reputation_threat"));

    for (JsonValue instruction : requestRoot.get("instructions")) {
      Instruction newInstruction = new Instruction();
      newInstruction.deserialise(instruction);
      instructions.add(newInstruction);
    }
  }
}
