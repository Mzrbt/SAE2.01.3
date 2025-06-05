package app.model.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONParser {
    private int index = 0;
    private final String parse;

    public JSONParser(String parse) {
        this.parse = parse; // parse.trim().replaceAll("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", "");
    }

    public JSONObject parse() {
        if (parse.startsWith("{")) {
            return parseObject(parse);
        }

        throw new IllegalArgumentException("Invalid JSON input");
    }

    private JSONObject parseObject(String json) {
        HashMap<String, Object> jsonObject = new HashMap<>();
        index++;

        while (index < json.length()) {
            char currentChar = json.charAt(index);

            if (currentChar == '}') {
                index++;
                break;
            } else if (currentChar == ',' || currentChar == '{' || currentChar == '\n') {
                index++;
                continue;
            }
            skipWhitespace(json);

            String key = parseString(json);

            skipWhitespace(json);
            index++;
            skipWhitespace(json);

            Object value = parseValue(json);
            jsonObject.put(key, value);

            skipWhitespace(json);
        }

        return new JSONObject(jsonObject);
    }

    private JSONArray parseArray(String json) {
        JSONArray res = new JSONArray();
        index++;

        while (index < json.length()) {
            skipWhitespace(json);
            if (json.charAt(index) == ']') {
                index++;
                break;
            } else if (json.charAt(index) == ',') {
                index++;
                continue;
            }

            JSONObject value = parseObject(json);
            res.add(value);
        }

        return res;
    }

    private Object parseValue(String json) {
        skipWhitespace(json);

        char currentChar = json.charAt(index);
        if (currentChar == '"') {
            return parseString(json);
        } else if (currentChar == '{') {
            return parseObject(json);
        } else if (currentChar == '[') {
            return parseArray(json);
        } else if (currentChar == 't' || currentChar == 'f') {
            return parseBoolean(json);
        } else if (currentChar == 'n') {
            return parseNull(json);
        } else {
            return parseNumber(json);
        }
    }

    private String parseString(String json) {
        StringBuilder result = new StringBuilder();
        index++;

        while (index < json.length()) {
            char currentChar = json.charAt(index);

            if (currentChar == '"') {
                index++;
                break;
            } else if (currentChar == '\\') {
                index++;
                char escapedChar = json.charAt(index);
                switch (escapedChar) {
                    case '"':
                    case '\\':
                    case '/':
                        result.append(escapedChar);
                        break;
                    case 'b':
                        result.append('\b');
                        break;
                    case 'f':
                        result.append('\f');
                        break;
                    case 'n':
                        result.append('\n');
                        break;
                    case 'r':
                        result.append('\r');
                        break;
                    case 't':
                        result.append('\t');
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid escape sequence");
                }
            } else {
                result.append(currentChar);
            }
            index++;
        }

        return result.toString();
    }

    private Number parseNumber(String json) {
        StringBuilder result = new StringBuilder();

        while (index < json.length() && (Character.isDigit(json.charAt(index)) || json.charAt(index) == '.' || json.charAt(index) == '-')) {
            result.append(json.charAt(index));
            index++;
        }

        String numberStr = result.toString();
        if (numberStr.contains(".")) {
            return Double.parseDouble(numberStr);
        } else {
            return Integer.parseInt(numberStr);
        }
    }

    private Boolean parseBoolean(String json) {
        if (json.startsWith("true", index)) {
            index += 4;
            return true;
        } else if (json.startsWith("false", index)) {
            index += 5;
            return false;
        }
        throw new IllegalArgumentException("Invalid boolean value");
    }

    private Object parseNull(String json) {
        if (json.startsWith("null", index)) {
            index += 4;
            return null;
        }
        throw new IllegalArgumentException("Invalid null value");
    }

    private void skipWhitespace(String json) {
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
    }
}