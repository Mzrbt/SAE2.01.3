package app.model.map;

import app.model.exceptions.UnknownPlaceException;

import java.util.*;

public class World {
    private final String name;
    private final List<Place> places;
    private final Set<Path> paths;

    private final HashMap<Place, HashMap<Place, Integer>> pathCache;

    public World(String name) {
        this.name = name;
        places = new ArrayList<>();
        paths = new HashSet<>();
        pathCache = new HashMap<>();
    }

    public List<Place> getPlaces() {
        return places;
    }

    public Set<Path> getPaths() {
        return paths;
    }

    public void addPlace(Place p) {
        if (places.contains(p)) {
            return;
        }

        this.places.add(p);
    }

    public void addPath(Path path) throws UnknownPlaceException {
        Place[] destinations = {path.getFirstPlace(), path.getSecondPlace()};

        Arrays.stream(destinations).forEach(place -> {
            if (!places.contains(place)) {
                throw new UnknownPlaceException(String.format("%s does not exist in %s.", place.getName(), this.name));
            }
        });

        if (getPathBetween(path.getFirstPlace(), path.getSecondPlace()) != null) {
            throw new RuntimeException(String.format("Path between %s and %s already exist.", path.getFirstPlace(), path.getSecondPlace()));
        }

        this.paths.add(path);

        for (Place p : destinations) {
            pathCache.remove(p);
        }
    }

    public String getName() {
        return name;
    }

    public Place getPlaceFromName(String name) {
        Optional<Place> optionalPlace = places.stream().filter(p -> p.getName().equals(name)).findFirst();
        return optionalPlace.orElse(null);
    }

    public Place getPlaceFromId(int id) {
        Optional<Place> optionalPlace = places.stream().filter(p -> p.getId() == id).findFirst();
        return optionalPlace.orElse(null);
    }

    public Map<Place, Integer> getPathsFrom(Place place) {
        if (pathCache.containsKey(place)) {
            return pathCache.get(place);
        }

        HashMap<Place, Integer> res = new HashMap<>();

        for (Path p : paths) {
            if (p.getFirstPlace() == place || p.getSecondPlace() == place) {
                res.put((place == p.getFirstPlace()) ? p.getSecondPlace() : p.getFirstPlace(), p.getLength());
            }
        }

        pathCache.put(place, res);
        return res;
    }

    public Path getPathBetween(Place p, Place key) {
        for (Path path : getPaths()) {
            if (path.getFirstPlace() == p && path.getSecondPlace() == key || path.getFirstPlace() == key && path.getSecondPlace() == p) {
                return path;
            }
        }

        return null;
    }
}
