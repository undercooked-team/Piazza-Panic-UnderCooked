package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A value used in a json format to specify a {@link JsonValue} to
 * be formatted by setting the type of the {@link JsonValue} to
 * the {@link #value}.
 */
public class JsonType extends JsonVal<JsonValue.ValueType> {

    /**
     * @param ID {@link String} : The id.
     * @param value {@link JsonValue.ValueType} : The default value to use.
     */
    public JsonType(String ID, JsonValue.ValueType value) {
        super(ID, value);
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
