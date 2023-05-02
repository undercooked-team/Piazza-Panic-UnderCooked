package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A value used in a json format to specify a {@link JsonValue}'s value
 * to be formatted as an {@link JsonValue.ValueType#array} of a type of
 * {@link JsonVal}.
 *
 * @see JsonOr
 */
public class JsonArray extends JsonVal<JsonVal<?>> {

  /**
   * Constructor for the class that defaults {@code allowNull} to true.
   *
   * @param id        {@link String} : The id.
   * @param valueType {@link Boolean} : The value type of the array contents.
   */
  public JsonArray(String id, JsonVal<?> valueType) {
    super(id, valueType);
  }

  @Override
  public boolean isValue(JsonValue jsonData) {
    return jsonData.isArray();
  }

  @Override
  public void setValue(JsonValue jsonData, boolean existsBefore) {
    jsonData.setType(getType());
  }

  /**
   * Make sure that it's an {@link JsonValue.ValueType#array} and, if it
   * is, make sure that all of the {@code jsonData}'s children are also
   * correct.
   */
  @Override
  public void check(JsonValue jsonData, boolean existsBefore) {
    // Make sure that the jsonData is an array
    if (!isValue(jsonData)) {
      // If it's not, make it an array
      setType(jsonData);
      // Clear it of all of its children
      for (JsonValue jsonChild : jsonData) {
        jsonChild.remove();
      }
      // And stop, as the array will be empty
      return;
    }
    // For all of them, check child
    for (int i = jsonData.size - 1; i >= 0; i--) {
      JsonValue val = jsonData.get(i);
      if (!this.value.isValue(val)) {
        // If it's not valid, remove it
        overrideValue(jsonData.get(i));
      } else {
        // Otherwise, if it's valid, check the child
        this.value.check(jsonData.get(i), existsBefore);
      }
    }
  }

  @Override
  public JsonValue.ValueType getType() {
    return JsonValue.ValueType.array;
  }
}
