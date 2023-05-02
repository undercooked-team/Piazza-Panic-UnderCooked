package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A value used in a json format to specify a {@link JsonValue}'s value
 * to be formatted as an {@link Boolean}.
 */
public class JsonBool extends JsonVal<Boolean> {

  /**
   * Constructor for the class that defaults {@code allowNull} to true.
   *
   * @param id    {@link String} : The id.
   * @param value {@link Boolean} : The default value to use.
   */
  public JsonBool(String id, Boolean value) {
    super(id, value);
  }

  @Override
  public boolean isValue(JsonValue jsonData) {
    return jsonData.isBoolean();
  }

  @Override
  public void setValue(JsonValue jsonData, boolean existsBefore) {
    jsonData.set(this.value);
  }

  @Override
  public JsonValue.ValueType getType() {
    return JsonValue.ValueType.booleanValue;
  }
}
