package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A value in a json format to define how to format a {@link JsonValue} when
 * using the {@link JsonFormat#formatJson(JsonValue, JsonObject, boolean)}.
 *
 * @param <T> The type of value that the {@link JsonVal} has.
 */
public abstract class JsonVal<T> {
  /**
   * The {@link String} id for the left-side key of a {@link JsonValue}.
   */
  protected String id;

  /**
   * The default value for the right-side of a {@link JsonValue}.
   */
  protected T value;

  /**
   * Constructor for the class. Defines the
   * {@link #id} and {@link #value} of the {@link JsonVal}.
   *
   * @param id    {@link String} : The id.
   * @param value {@link T} : The default value to use.
   */
  public JsonVal(String id, T value) {
    if (id == null) {
      id = "";
    }
    this.id = id;
    this.value = value;
  }

  /**
   * Returns the id of the {@link JsonVal}.
   *
   * @return {@link String} : The {@link #id} of the {@link JsonVal}.
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the default value of the {@link JsonVal}.
   *
   * @return {@link T} : The default value of the {@link JsonVal}.
   */
  public T getValue() {
    return value;
  }

  /**
   * Returns whether the {@link JsonValue}'s value matches the
   * type of this {@link JsonVal}.
   *
   * @param jsonData {@link JsonValue} : The {@link JsonValue} to check.
   * @return {@code boolean} : {@code true} if the type is correct,
   *                           {@code false} if it is not.
   */
  public abstract boolean isValue(JsonValue jsonData);

  /**
   * Formats the {@link JsonValue}.
   * <br>
   * Should be defined when there are more steps involved in the formatting
   * of the {@link JsonValue} than just using the {@link #setValue(JsonValue)}
   * function if the type is incorrect.
   *
   * @param jsonData     {@link JsonValue} : The {@link JsonValue} to format.
   * @param existsBefore {@code boolean} : {@code true} if the {@code jsonData} existed
   *                     in a parent {@link JsonValue} or not before the
   *                     function was called,
   *                     {@code false} if not.
   */
  public void format(JsonValue jsonData, boolean existsBefore) {

  }

  /**
   * Sets the value of the {@link JsonValue} to use the default {@link #value}.
   *
   * @param jsonData     {@link JsonValue} : The {@link JsonValue} to set for.
   * @param existsBefore {@code boolean} : {@code true} if the {@code jsonData} existed
   *                     in a parent {@link JsonValue} or not before the
   *                     function was called,
   *                     {@code false} if not.
   */
  public abstract void setValue(JsonValue jsonData, boolean existsBefore);

  /**
   * Sets the value of the {@link JsonValue} to use the default {@link #value}.
   *
   * @param jsonData {@link JsonValue} : The {@link JsonValue} to set for.
   */
  public void setValue(JsonValue jsonData) {
    setValue(jsonData, true);
  }

  /**
   * Sets the type of the {@link JsonValue} to the type defined in {@link #getType()}.
   *
   * @param jsonData {@link JsonValue} : The {@link JsonValue} to set for.
   */
  public void setType(JsonValue jsonData) {
    jsonData.setType(getType());
  }

  /**
   * Returns the {@link JsonValue.ValueType} that the {@link JsonVal} should be.
   *
   * @return {@link JsonValue.ValueType} : The type that the {@link JsonValue} should
   *                                       have for its value.
   */
  public abstract JsonValue.ValueType getType();

  /**
   * Completely overrides the value of a {@link JsonValue} with the type and
   * default {@link #value}.
   *
   * @param jsonData {@link JsonValue} : The {@link JsonValue} to set for.
   */
  public void overrideValue(JsonValue jsonData) {
    // Set the type
    setType(jsonData);
    // And then update the value
    setValue(jsonData);
  }

  /**
   * Adds a new {@link JsonValue} child to the {@link JsonValue} provided
   * using the default {@link #value}.
   *
   * @param root {@link JsonValue} : The {@link JsonValue} to add the child to.
   */
  public void addChild(JsonValue root) {
    // Create the value
    JsonValue newChild = new JsonValue(getType());
    // Set it to use the default value
    setValue(newChild, false);
    // And then add it
    root.addChild(id, newChild);
  }

  /**
   * Checks if the value of the child is correct. If it is not,
   * then it calls the {@link #overrideValue(JsonValue)}.
   * <br>
   * It then calls the {@link #format(JsonValue, boolean)} function.
   *
   * @param jsonData     {@link JsonValue} : The {@link JsonValue} to check.
   * @param existsBefore {@code boolean} : {@code true} if the {@code jsonData} existed
   *                     in a parent {@link JsonValue} or not before the
   *                     function was called,
   *                     {@code false} if not.
   */
  public void check(JsonValue jsonData, boolean existsBefore) {
    // If it does, check if the value type is correct
    if (!isValue(jsonData)) {
      overrideValue(jsonData);
    }
    // If type matches, then just try and call
    // the "format" function, in case it's something
    // like a JsonObject.
    format(jsonData, existsBefore);
  }
}
