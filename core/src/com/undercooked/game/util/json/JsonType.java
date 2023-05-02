package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A value used in a json format to specify a {@link JsonValue} to
 * be formatted by setting the type of the {@link JsonValue} to
 * the {@link #value}.
 */
public class JsonType extends JsonVal<JsonValue.ValueType> {

  /**
   * Constructor for the {@link JsonType}.
   *
   * @param id    {@link String} : The id.
   * @param value {@link JsonValue.ValueType} : The default value to use.
   */
  public JsonType(String id, JsonValue.ValueType value) {
    super(id, value);
  }

  @Override
  public boolean isValue(JsonValue jsonData) {
    return jsonData.type().equals(this.value);
  }

  @Override
  public void setValue(JsonValue jsonData, boolean existsBefore) {
    setType(jsonData);
  }

  @Override
  public JsonValue.ValueType getType() {
    return this.value;
  }
}
