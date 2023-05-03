package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A value used in a json format to specify a {@link JsonValue} to
 * be formatted to at least one of the {@link JsonVal}s provided.
 * <br>
 * If two {@link JsonVal}s of the same type are provided, it will
 * only use the first to format it.
 */
public class JsonOr extends JsonVal<JsonVal<?>[]> {

  /**
   * Constructor for the class that defaults {@code allowNull} to true.
   *
   * @param id    {@link String} : The id.
   * @param value {@link JsonVal}[] : The different {@link JsonVal}s to
   *                                  allow.
   */
  public JsonOr(String id, JsonVal<?>[] value) {
    super(id, value);
  }

  /**
   * Returns if the {@link JsonValue} has the same type of
   * at least one {@link JsonVal}.
   */
  @Override
  public boolean isValue(JsonValue jsonData) {
    // Make sure at least one value matches
    for (JsonVal<?> jsonVal : this.value) {
      if (jsonVal.isValue(jsonData)) {
        return true;
      }
    }
    // Otherwise return false.
    return false;
  }

  /**
   * Sets to null type, as {@link JsonOr} has no type.
   */
  @Override
  public void setValue(JsonValue jsonData, boolean existsBefore) {
    // Just set it to null if it tries to set here
    jsonData.setType(JsonValue.ValueType.nullValue);
  }

  /**
   * Returns the type of the first {@link JsonVal} in the {@link JsonOr}.
   * <br><br>
   * If there are no {@link JsonVal}s set, then it will be a null value.
   */
  @Override
  public JsonValue.ValueType getType() {
    // If no or, return null
    if (this.value.length == 0) {
      return JsonValue.ValueType.nullValue;
    }
    // Otherwise, return the type of the first or
    return this.value[0].getType();
  }

  /**
   * Adds the child to the {@code root} using the first {@link JsonVal}
   * in the {@link JsonOr}.
   * <br><br>
   * If there are no {@link JsonVal}s set, then it will be a null value.
   */
  @Override
  public void addChild(JsonValue root) {
    // If no or, add a null value
    if (this.value.length == 0) {
      // Create the value
      JsonValue newChild = new JsonValue(JsonValue.ValueType.nullValue);
      // And then add it
      root.addChild(id, newChild);
      return;
    }
    // Otherwise, add the first or as the child
    this.value[0].addChild(root);
  }

  /**
   * Checks that the {@link JsonValue} is the same value as at least one of the
   * {@link JsonVal}s, and will instead call their {@link JsonVal#check(JsonValue, boolean)}
   * function.
   * <br><br>
   * If the type does not match any, it will default to the first {@link JsonVal} in the
   * {@link JsonOr}.
   * <br><br>
   * If there are no {@link JsonVal}s set, then it will be a null value.
   */
  @Override
  public void check(JsonValue jsonData, boolean existsBefore) {
    // Check if this has at least one or
    if (this.value.length == 0) {
      // If it doesn't, just set it
      setValue(jsonData, existsBefore);
      // and then stop here
      return;
    }
    // When checking the child, check for a single valid one
    int valid = -1;
    for (int i = 0; i < this.value.length; i++) {
      JsonVal<?> jsonVal = this.value[i];
      // Check if it's valid
      if (jsonVal.isValue(jsonData)) {
        // If it is, break from the loop, and set the
        // valid index
        valid = i;
        break;
      }
    }
    // If it's invalid, just set it to check the first child
    if (valid < 0) {
      this.value[0].check(jsonData, existsBefore);
      return;
    }
    // Otherwise, if it's valid, then use the valid child to set the value
    this.value[valid].check(jsonData, existsBefore);
  }
}
