package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonBool extends JsonVal<Boolean> {
    public JsonBool(String ID, Boolean value) {
        super(ID, value);
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
