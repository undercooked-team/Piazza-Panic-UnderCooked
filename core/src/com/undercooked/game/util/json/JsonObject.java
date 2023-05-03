package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

/**
 * A value used in a json format to specify a {@link JsonValue} to
 * have its value formatted as another {@link JsonValue}.
 */
public class JsonObject extends JsonVal<Array<JsonVal<?>>> {

  /**
   * Constructor for the {@link JsonObject}.
   *
   * @param id {@link String} : The id.
   */
  public JsonObject(String id) {
    super(id, new Array<JsonVal<?>>());
    if (id == null) {
      id = "";
    }
  }

  /**
   * Constructor for the {@link JsonObject}.
   * <br><br>
   * Defaults to using a blank ID, as not all {@link JsonValue}s need an
   * id, but all {@link JsonValue} children do.
   */
  public JsonObject() {
    this("");
  }

  /**
   * Adds a {@link JsonVal} to be formatted in the {@link JsonValue}.
   *
   * @param jsonVal {@link JsonVal} : The {@link JsonVal} to add.
   */
  public void addValue(JsonVal<?> jsonVal) {
    value.add(jsonVal);
  }

  /**
   * Returns an {@link Array} of all the {@link JsonVal} children.
   *
   * @return {@link Array}&lt;{@link JsonVal}&gt; : Returns the list of {@link JsonVal} that
   *                                                are in the {@link JsonObject}.
   */
  public Array<JsonVal<?>> getValues() {
    return value;
  }

  @Override
  public boolean isValue(JsonValue jsonData) {
    return jsonData.isObject();
  }

  @Override
  public void setValue(JsonValue jsonData, boolean existsBefore) {
    // Format all the values within
    JsonFormat.formatJson(jsonData, this, existsBefore);
  }

  @Override
  public void format(JsonValue jsonData, boolean existsBefore) {
    // Only continue if it existed before
    if (existsBefore) {
      // Try to format
      setValue(jsonData, true);
    }
  }

  @Override
  public JsonValue.ValueType getType() {
    return JsonValue.ValueType.object;
  }
}
