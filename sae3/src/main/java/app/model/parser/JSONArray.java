package app.model.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONArray implements Iterable<Object> {
    private final List<Object> data;

    public JSONArray() {
        this.data = new ArrayList<>();
    }

    public JSONArray(List<Object> data) {
        this.data = data;
    }

    public Object get(int index) {
        return data.get(index);
    }

    public int size() {
        return data.size();
    }

    public JSONObject getJSONObject(int index) {
        Object value = data.get(index);
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        throw new ClassCastException("Value at index '" + index + "' is not a JSONObject");
    }

    public JSONArray getJSONArray(int index) {
        Object value = data.get(index);
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }
        throw new ClassCastException("Value at index '" + index + "' is not a JSONArray");
    }

    public String getString(int index) {
        Object value = data.get(index);
        if (value instanceof String) {
            return (String) value;
        }
        throw new ClassCastException("Value at index '" + index + "' is not a String");
    }

    public Number getNumber(int index) {
        Object value = data.get(index);
        if (value instanceof Number) {
            return (Number) value;
        }
        throw new ClassCastException("Value at index '" + index + "' is not a Number");
    }

    @Override
    public String toString() {
        StringBuilder jsonArray = new StringBuilder();
        jsonArray.append("[");

        int count = 0;

        for (Object element : data) {
            if (element instanceof String) {
                jsonArray.append("\"").append(element).append("\"");
            } else if (element instanceof Number || element instanceof Boolean) {
                jsonArray.append(element);
            } else if (element == null) {
                jsonArray.append("null");
            } else {
                jsonArray.append(element);
            }

            if (++count < data.size()) {
                jsonArray.append(",");
            }
        }

        jsonArray.append("]");
        return jsonArray.toString();
    }

    public JSONArray add(Object value) {
        this.data.add(value);
        return this;
    }

    @Override
    public Iterator<Object> iterator() {
        return data.iterator();
    }
}
