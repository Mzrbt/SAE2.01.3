package app.model.parser;

import java.util.HashMap;

public class JSONObject {
    private final HashMap<String, Object> data;

    public JSONObject() {
        this.data = new HashMap<>();
    }

    public JSONObject(HashMap<String, Object> data) {
        this.data = data;
    }

    public JSONObject put(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public Number getNumber(String key) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return (Number) value;
        }
        throw new ClassCastException("Value associated with key '" + key + "' is not a Number");
    }

    public String getString(String key) {
        Object value = data.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        throw new ClassCastException("Value associated with key '" + key + "' is not a String");
    }

    public Boolean getBoolean(String key) {
        Object value = data.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        throw new ClassCastException("Value associated with key '" + key + "' is not a Boolean");
    }

    public JSONObject getJSONObject(String key) {
        Object value = data.get(key);
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        return null;
    }

    public JSONArray getJSONArray(String key) {
        Object value = data.get(key);
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }
        throw new ClassCastException("Value associated with key '" + key + "' is not a JSONArray");
    }

    public Object get(String key) {
        return data.get(key);
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public boolean isEnum() {
        return data.containsKey("JSON_KEY_IS_ENUM");
    }

    public int size() {
        return data.size();
    }

    @Override
    public String toString() {
        StringBuilder json = new StringBuilder().append("{");
        int size = data.size();
        int count = 0;

        for (var entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            json.append("\"").append(key).append("\":");

            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                json.append(value);
            } else if (value == null) {
                json.append("null");
            } else {
                json.append(value);
            }
            if (++count < size) {
                json.append(",");
            }
        }

        json.append("}");
        return json.toString();
    }
}
