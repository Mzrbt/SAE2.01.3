package app.javafx.controller;

import app.ai.world.WorldAnalyzer;
import app.model.map.Place;
import app.model.map.World;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.util.Duration;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class MainController {

    @FXML
    public MenuController menuController;
    @FXML
    public GraphController graphController;
    @FXML
    public MondeController mondeController;
    @FXML
    public DjisktraVisualiseurController djisktraVisualiseurController;


    public ObjectProperty<Place> selectedPlace = new SimpleObjectProperty<>();
    public ObjectProperty<World> actualWorld = new SimpleObjectProperty<>();
    public BooleanProperty dijkstraOn = new SimpleBooleanProperty(false);
    public BooleanProperty worldLoading = new SimpleBooleanProperty(false);
    public List<DijkstraEventListener> dijListeners = new ArrayList<>();

    public void initialize(URL location, ResourceBundle resources) {

        menuController.setMainController(this);
        graphController.setMainController(this);
        mondeController.setMainController(this);
        djisktraVisualiseurController.setMainController(this);

    }

    public void launchDijkstra() {

        Place place = selectedPlace.get();

        if (place != null) {

            dijkstraOn.set(true);

            CompletableFuture<List<Pair<Place, HashMap<Place, Integer>>>> c = new CompletableFuture<>();
            c.completeAsync(() -> {
                return new WorldAnalyzer(actualWorld.get()).dijkstraWithSteps(place);
            }).thenAccept((list) -> {
                Timeline timeline = new Timeline();
                Duration duration = new Duration(250);
                Duration currentTime = Duration.ZERO;

                for (Pair<Place, HashMap<Place, Integer>> step : list) {
                    Place depart = step.getKey();
                    HashMap<Place, Integer> neighbors = step.getValue();

                    timeline.getKeyFrames().add(new KeyFrame(currentTime, e -> {
                        dijListeners.forEach(listener -> listener.beforeLineFrom(depart));
                    }));

                    currentTime = currentTime.add(duration);

                    for (Place voisin : neighbors.keySet()) {
                        int distance = neighbors.get(voisin);
                        Place arrive = depart;
                        Place etape = voisin;

                        timeline.getKeyFrames().add(new KeyFrame(currentTime, e -> {
                            dijListeners.forEach(listener -> listener.beforeNewDistance(arrive, etape));
                        }));
                        currentTime = currentTime.add(duration);


                        timeline.getKeyFrames().add(new KeyFrame(currentTime, e -> {
                            dijListeners.forEach(listener -> listener.newDistance(arrive, etape, distance));
                        }));
                        currentTime = currentTime.add(duration);

                        timeline.getKeyFrames().add(new KeyFrame(currentTime, e -> {
                            dijListeners.forEach(listener -> listener.afterNewDistance(arrive, etape));
                        }));
                        currentTime = currentTime.add(duration);
                    }

                    timeline.getKeyFrames().add(new KeyFrame(currentTime, e -> {
                        dijListeners.forEach(listener -> listener.afterLineFrom(depart));
                    }));
                    currentTime = currentTime.add(duration);
                }

                timeline.getKeyFrames().add(new KeyFrame(currentTime, e -> {
                    dijListeners.forEach(DijkstraEventListener::tearDown);
                    dijkstraOn.set(false);
                }));

                timeline.play();
            });

        }

        //CompletableFuture.completeAsync()

    }
}

