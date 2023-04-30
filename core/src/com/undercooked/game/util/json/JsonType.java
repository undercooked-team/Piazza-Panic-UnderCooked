package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonType extends JsonVal<JsonValue.ValueType> {
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
