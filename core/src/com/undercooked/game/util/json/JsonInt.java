package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonInt extends JsonVal<Integer> {
    public JsonInt(String ID, Integer value) {
        super(ID, value);
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
