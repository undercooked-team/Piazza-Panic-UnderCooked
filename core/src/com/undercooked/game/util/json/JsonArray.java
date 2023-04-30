package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonArray extends JsonVal<JsonVal> {
    public JsonArray(String ID, JsonVal valueType) {
        super(ID, valueType);
    }

    @Override
    public boolean isValue(JsonValue jsonData) {
        return jsonData.isArray();
    }

    @Override
    public void setValue(JsonValue jsonData, boolean existsBefore) {
        jsonData.setType(getType());
    }

    @Override
    public void check(JsonValue jsonData, boolean existsBefore) {
        // For all of them, check child
        for (int i = jsonData.size-1; i >= 0 ; i--) {
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
