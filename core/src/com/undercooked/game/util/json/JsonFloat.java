package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A value used in a json format to specify a {@link JsonValue}'s value
 * to be formatted as an {@link Float}.
 */
public class JsonFloat extends JsonVal<Float> {

  /**
   * Constructor for the class that defaults {@code allowNull} to true.
   *
   * @param id    {@link String} : The id.
   * @param value {@link Float} : The default value to use.
   */
  public JsonFloat(String id, Float value) {
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
