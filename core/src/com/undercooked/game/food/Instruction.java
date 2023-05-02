package com.undercooked.game.food;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.TextureManager;

/**
 * The class for one {@link Instruction} of a {@link Request}, which
 * is used to instruct the player on what to do.
 */
public class Instruction {
  /**
   * The {@link Texture} to show.
   */
  private Texture texture;

  /**
   * The path for the {@link #texture}.
   */
  public String texturePath;

  /**
   * The text to display.
   */
  public String text;

  /**
   * Post loading of the {@link Instruction} to set the
   * {@link Texture}.
   *
   * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
   */
  public void postLoad(TextureManager textureManager) {
    texture = textureManager.getAsset(texturePath);
  }

  /**
   * Loading of the {@link Instruction} to tell the {@link TextureManager}
   * to load the {@link #texturePath}.
   *
   * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
   * @param textureGroup   {@link String} : The group to load the {@link #texturePath} to.
   */
  public void load(TextureManager textureManager, String textureGroup) {
    textureManager.loadAsset(textureGroup, texturePath, "textures");
  }

  /**
   * Returns the {@link Texture} to display with the text.
   *
   * @return {@link Texture} : The {@link #texture}
   */
  public Texture getTexture() {
    return texture;
  }

  /**
   * Converts the {@link Instruction} into a {@link JsonValue} to
   * be stored in memory.
   *
   * @return {@link JsonValue} : The {@link Instruction} in Json format.
   */
  public JsonValue serial() {
    JsonValue instructionRoot = new JsonValue(JsonValue.ValueType.object);
    instructionRoot.addChild("texture_path", new JsonValue(texturePath));
    instructionRoot.addChild("text", new JsonValue(text));
    return instructionRoot;
  }

  /**
   * Takes a {@link JsonValue} and sets the values of the
   * {@link Instruction}.
   *
   * @param instructionRoot {@link JsonValue} : The {@link JsonValue} to load.
   */
  public void deserialise(JsonValue instructionRoot) {
    texturePath = instructionRoot.getString("texture_path");
    text = instructionRoot.getString("text");
  }
}
