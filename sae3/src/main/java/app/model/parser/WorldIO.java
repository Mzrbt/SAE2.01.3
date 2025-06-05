package app.model.parser;

import app.model.entity.Monster;
import app.model.exceptions.UnknownPlaceException;
import app.model.map.Path;
import app.model.map.Place;
import app.model.map.World;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static app.model.parser.ParserConfig.*;

public class WorldIO {

    /**
     * Save a world in a .JSON file with the following structures : <br>
     * <br>
     * {
     * "world": string,
     * "places": [
     * {
     * "id": int,
     * "name": string,
     * "description": string,
     * "end": boolean,
     * "start": boolean,
     * "defeat": boolean,
     * "monster": null | {
     * "name": string,
     * "HP": int,
     * "Armor": int,
     * "Attack": int
     * }
     * }
     * ],
     * "paths": [
     * {
     * "from": int,
     * "to": int,
     * "distance": int
     * }
     * ]
     * }
     * Places and paths will be sorted by alphabetical order
     *
     * @param w    World to save
     * @param file A .JSON file
     */
    public static void saveWorld(World w, File file) throws IOException {
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new RuntimeException("Can't create parent directories : %s".formatted(file.getParentFile().toPath().toString()));
            }
        }
        OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath(), StandardOpenOption.CREATE));

        writer.write("{\n");
        writer.write("\t\"world\": \"%s\",\n".formatted(w.getName()));
        writer.write("\t\"places\": [\n");

        List<Place> places = new ArrayList<>(w.getPlaces());
        places.sort(Comparator.comparing(Place::getId));
        for (int i = 0; i < places.size(); i++) {
            writer.write("\t%s%s\n".formatted(places.get(i).asJSON(), (i == places.size() - 1) ? "" : ","));
        }

        writer.write("],\n");

        writer.write("\t\"%s\": [\n".formatted(KEY_PATHS));

        List<Path> paths = new ArrayList<>(w.getPaths());
        paths.sort(Comparator.comparing(p -> p.getFirstPlace().getId()));
        for (int i = 0; i < paths.size(); i++) {
            writer.write("\t%s%s\n".formatted(paths.get(i).asJSON(), (i == paths.size() - 1) ? "" : ","));
        }
        writer.write("\t]\n");
        writer.write("}");

        writer.close();
    }

    public static World loadWorld(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();

        for (String line : reader.lines().toList()) {
            builder.append(line).append("\n");
        }
        reader.close();

        JSONObject json = new JSONParser(builder.toString().trim()).parse();

        World world = new World(json.getString(KEY_WORLD));

        for (Object obj : json.getJSONArray(KEY_PLACES)) {
            if (!(obj instanceof JSONObject)) continue;
            JSONObject place = (JSONObject) obj;

            Place p = new Place(place.getNumber(KEY_PLACE_ID).intValue(),
                    place.getString(KEY_PLACE_NAME),
                    Monster.createFromJSONObject(place.getJSONObject(KEY_PLACE_MONSTER)),
                    place.getString(KEY_PLACE_DESCRIPTION),
                    world,
                    place.getBoolean(KEY_PLACE_START),
                    place.getBoolean(KEY_PLACE_END),
                    place.getBoolean(KEY_PLACE_DEFEAT));

            world.addPlace(p);
        }

        for (Object obj : json.getJSONArray(KEY_PATHS)) {
            if (!(obj instanceof JSONObject)) continue;
            JSONObject place = (JSONObject) obj;

            Place first = world.getPlaceFromId(place.getNumber(KEY_PATH_FIRST_PLACE).intValue());
            Place second = world.getPlaceFromId(place.getNumber(KEY_PATH_SECOND_PLACE).intValue());
            if (first == null || second == null) {
                throw new UnknownPlaceException(first == null ? second.getName() : first.getName());
            }

            world.addPath(new Path(first, second, place.getNumber(KEY_PATH_DISTANCE).intValue()));
        }


        return world;
    }
}
