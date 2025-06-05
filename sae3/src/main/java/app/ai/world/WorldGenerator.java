package app.ai.world;

import app.model.entity.Monster;
import app.model.entity.Player;
import app.model.map.Path;
import app.model.map.Place;
import app.model.map.World;

import javax.security.auth.callback.Callback;
import java.util.*;
import java.util.function.Consumer;

public class WorldGenerator {
    private final String name;
    private final int nbPlace;
    private final Player player;
    private final double percentageEndPoint;
    private final double percentageStartPoint;
    private final double percentageDefeatPoint;
    private final double percentageMonster;
    private final double maximumPathPerPlace;
    private final double pathCoverage;
    private final boolean withAIGeneration;

    private WorldGenerator(Builder builder) {
        this.name = builder.name;
        this.nbPlace = builder.nbPlace;
        this.player = builder.player;
        this.percentageEndPoint = builder.percentageEndPoint;
        this.percentageStartPoint = builder.percentageStartPoint;
        this.percentageDefeatPoint = builder.percentageDefeatPoint;
        this.maximumPathPerPlace = builder.maximumPathPerPlace;
        this.percentageMonster = builder.percentageMonster;
        this.pathCoverage = builder.pathCoverage;
        this.withAIGeneration = builder.withAIGeneration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private int nbPlace;
        private Player player;
        private double percentageEndPoint = 0.1f;
        private double percentageStartPoint = 0.1f;
        private double percentageDefeatPoint = 0.1f;
        private double percentageMonster = 0.55f;
        private double maximumPathPerPlace = 1000;
        private double pathCoverage = 0.5f;
        private boolean withAIGeneration = false;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nbPlace(int nbPlace) {
            this.nbPlace = nbPlace;
            return this;
        }

        public Builder withAIGeneration(boolean withAIGeneration) {
            this.withAIGeneration = withAIGeneration;
            return this;
        }

        public Builder player(Player player) {
            this.player = player;
            return this;
        }

        public Builder percentageEndPoint(double percentageEndPoint) {
            this.percentageEndPoint = percentageEndPoint;
            return this;
        }

        public Builder percentageMonster(double percentageMonster) {
            this.percentageMonster = percentageMonster;
            return this;
        }

        public Builder percentageStartPoint(double percentageStartPoint) {
            this.percentageStartPoint = percentageStartPoint;
            return this;
        }

        public Builder percentageDefeatPoint(double percentageDefeatPoint) {
            this.percentageDefeatPoint = percentageDefeatPoint;
            return this;
        }

        public Builder maximumPathPerPlace(double maximumPathPerPlace) {
            this.maximumPathPerPlace = maximumPathPerPlace;
            return this;
        }

        public Builder pathCoverage(double pathCoverage) {
            this.pathCoverage = pathCoverage;
            return this;
        }

        public WorldGenerator build() {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Le nom ne peut pas être vide");
            }
            if (nbPlace <= 2) {
                throw new IllegalArgumentException("Le nombre de places doit être supérieur à 2");
            }

            if (isIncorrect(percentageDefeatPoint) || isIncorrect(percentageEndPoint) || isIncorrect(percentageStartPoint)
                    || isIncorrect(percentageDefeatPoint + percentageEndPoint + percentageStartPoint)) {
                throw new IllegalArgumentException("Le pourcentage ne doit pas excéder 100%");
            }
            return new WorldGenerator(this);
        }

        public boolean isIncorrect(double percentage) {
            return percentage < 0 || percentage > 1;
        }
    }

    public Monster randomMonster() {
        Random random = new Random();
        return new Monster("", random.nextInt(50, 150), random.nextInt(5, 20), random.nextInt(5, 20));
    }

    /**
     * @param callback Function called when a place's text and description have been generated if the AI generation
     *                 option is enable. Useful to display a progression on a UI.
     * @return The world generated.
     */
    public World generate(Consumer<Place> callback) {
        World world = new World(name);
        Random random = new Random();

        List<Place> places = new ArrayList<>();
        for (int i = 0; i < nbPlace; i++) {

            Place place = new Place(i, "Place " + i, (random.nextDouble() < percentageMonster) ? randomMonster() : null, world);
            world.addPlace(place);
            places.add(place);

            System.out.println(place.getId() + " - " + place.getMonster());
        }

        Collections.shuffle(places);

        int nbStart = (int) Math.max(1, Math.round(nbPlace * percentageStartPoint));
        int nbEnd = (int) Math.max(1, Math.round(nbPlace * percentageEndPoint));
        int nbDefeat = (int) Math.round(nbPlace * percentageDefeatPoint);

        for (int i = 0; i < nbStart && i < places.size(); i++) {
            places.get(i).isStartProperty().set(true);
        }

        for (int i = nbStart; i < nbStart + nbEnd && i < places.size(); i++) {
            places.get(i).isEndProperty().set(true);
        }

        for (int i = nbStart + nbEnd; i < nbStart + nbEnd + nbDefeat && i < places.size(); i++) {
            places.get(i).isDefeatProperty().set(true);
        }

        Set<String> connected = new HashSet<>();
        for (int i = 0; i < places.size(); i++) {
            Place p1 = places.get(i);
            int connections = 0;

            for (int j = i + 1; j < places.size(); j++) {
                Place p2 = places.get(j);

                String key = p1.getId() + "-" + p2.getId();

                if (connected.contains(key)) continue;
                if (connections >= maximumPathPerPlace) break;

                if (random.nextFloat() < pathCoverage) {
                    int length = 1 + random.nextInt(10);
                    Path path = new Path(length, p1, p2);
                    world.addPath(path);
                    connected.add(key);
                    connections++;
                }
            }
        }

        if (withAIGeneration) {
            for (Place p : places) {
                API.setText(p);
                callback.accept(p);
            }
        }

        return world;
    }


}
