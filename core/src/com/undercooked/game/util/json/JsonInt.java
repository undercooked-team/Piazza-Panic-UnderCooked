package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A value used in a json format to specify a {@link JsonValue}'s value
 * to be formatted as an {@link Integer}.
 */
public class JsonInt extends JsonVal<Integer> {

  /**
   * Constructor for the class that defaults {@code allowNull} to true.
   *
   * @param id    {@link String} : The id.
   * @param value {@link Integer} : The default value to use.
   */
  public JsonInt(String id, Integer value) {
    super(id, value);
  }

  @Override
  public boolean isValue(JsonValue jsonData) {
    return jsonData.isNumber();
  }

  @Override
  public void setValue(JsonValue jsonData, boolean existsBefore) {
    jsonData.set(this.value, null);
  }

  @Override
  public JsonValue.ValueType getType() {
    return JsonValue.ValueType.doubleValue;
  }
}
