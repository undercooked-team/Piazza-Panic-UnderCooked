package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A value used in a json format to specify a {@link JsonValue}'s value
 * to be formatted as a {@link String}.
 */
public class JsonString extends JsonVal<String> {
  /**
   * If the default {@link #value} should be allowed to be {@code null} or not.
   */
  boolean allowNull;

  /**
   * Constructor for the class that defaults {@code allowNull} to true.
   *
   * @param id    {@link String} : The id.
   * @param value {@link String} : The default value to use.
   */
  public JsonString(String id, String value) {
    this(id, value, true);
  }

  /**
   * If {@code allowNull} is false and {@code value} is {@code null},
   * it will instead become {@code ""}.
   *
   * @param id        {@link String} : The id.
   * @param value     {@link JsonValue.ValueType} : The default value to use.
   * @param allowNull {@code boolean} : Whether the value of a {@link JsonValue}
   *                  should be allowed to be {@code null} or not.
   */
  public JsonString(String id, String value, boolean allowNull) {
    super(id, value);
    this.allowNull = allowNull;
    // If not allowed null, and default value is null, then
    // instead change it to an empty string.
    if (!allowNull && value == null) {
      this.value = "";
    }
  }

  @Override
  public boolean isValue(JsonValue jsonData) {
    return jsonData.isString() || (allowNull && jsonData.isNull());
  }

  @Override
  public void setValue(JsonValue jsonData, boolean existsBefore) {
    // If null is allowed, then leave it alone
    if (allowNull && jsonData.isNull()) {
      return;
    }
    jsonData.set(this.value);
  }

  @Override
  public JsonValue.ValueType getType() {
    return JsonValue.ValueType.stringValue;
  }
}
