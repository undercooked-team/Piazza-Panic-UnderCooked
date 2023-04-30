package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

public class JsonObject extends JsonVal<Array<JsonVal>> {

    public JsonObject(String ID) {
        super(ID, new Array<JsonVal>());
    }
    public JsonObject() {
        this("");
    }

    public void addValue(JsonVal jsonVal) {
        value.add(jsonVal);
    }
    public void addValue(String ID, JsonVal jsonVal) {
        jsonVal.ID = ID;
        value.add(jsonVal);
    }

    public Array<JsonVal> getValues() {
        return value;
    }
    public JsonVal getValue(String ID) {
        for (JsonVal val : value) {
            if (val.ID.equals(ID)) {
                return val;
            }
        }
        return null;
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
