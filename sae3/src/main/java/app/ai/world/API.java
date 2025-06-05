package app.ai.world;

import app.model.map.Place;
import app.model.parser.JSONArray;
import app.model.parser.JSONObject;
import app.model.parser.JSONParser;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class API {
    private final static String API_KEY = ""; /* TODO Change to your own token */

    public static void main(String[] args) {
        String urlString = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;
        String text = "Tu es en train d'écrire le texte d'un lieu pour un livre dont vous êtes le héros, ce lieu est un lieu de défaite où tu viens de te faire avaler par un ver. Fais une description courte d'environ 100 mots";

        System.out.println(getAPIResponse(urlString, text));
    }

    public static void setText(Place p) {
        String urlString = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;
        String type = "";

        if (p.isDefeat()) type = "Ce lieu est un lieu de défaite.";
        else {
            if (p.isStart()) type = "Ce lieu est un lieu de départ.";
            else if (p.isEnd()) {
                type = "Ce lieu est un lieu de victoire.";
            } else {
                type = "Ce lieu est un lieu de passage, par exemple : un village, un chemin, une foret..";
            }
        }

        String text = "Tu es en train d'écrire le texte d'un lieu pour un livre dont vous êtes le héros %s. %s Fais une description courte d'environ 100 mots".formatted(type, p.getMonster() == null ? "" : "Ce lieu possède un combat");

        JSONObject res = getAPIResponse(urlString, text);

        p.setName(res.getString("nom"));
        p.setDescription(res.getString("text du lieu"));
    }

    public static JSONObject getAPIResponse(String URL, String text) {
        JSONObject content = new JSONObject();

        content.put("contents", new JSONArray().add(
                new JSONObject().put("parts", new JSONArray().add(
                        new JSONObject().put("text", text)
                ))
        ));

        JSONObject config = new JSONObject().put("maxOutputTokens", 200);
        JSONObject schema = new JSONObject()
                .put("type", "object")
                .put("properties", new JSONObject()
                        .put("nom", new JSONObject().put("type", "string"))
                        .put("text du lieu", new JSONObject().put("type", "string"))
                )
                .put("required", new JSONArray().add("nom").add("text du lieu"));

        config.put("responseMimeType", "application/json");
        config.put("responseSchema", schema);

        content.put("generationConfig", config);
        HttpURLConnection conn = null;
        try {
            URL url = new URL(URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = content.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    JSONObject json = new JSONParser(responseBody).parse();
                    String responseText = json.getJSONArray("candidates").getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");
                    return new JSONParser(responseText).parse();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("Error : Request failed with response code: " + responseCode + "\n" + conn.getResponseMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return null;
    }
}
