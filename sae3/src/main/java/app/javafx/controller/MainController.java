package app.javafx.controller;

import app.ai.world.WorldAnalyzer;
import app.model.map.Path;
import app.model.map.Place;
import app.model.map.World;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class MainController implements  DijkstraEventListener, Initializable{

    @FXML
    public MenuController menuController;
    @FXML
    public GraphController graphController;
    @FXML
    public MondeController mondeController;
    @FXML
    public DjisktraVisualiseurController djisktraVisualiseurController;

    public ObjectProperty<Place> selectedPlace = new SimpleObjectProperty<>(null);
    public ObjectProperty<World> actualWorld = new SimpleObjectProperty<>(null);
    public BooleanProperty dijkstraOn = new SimpleBooleanProperty(false);
    public BooleanProperty worldLoading = new SimpleBooleanProperty(false);
    public List<DijkstraEventListener> dijListeners = new ArrayList<>();

    private final HashMap<Place, GraphicPlace> associationPlaceGraphicPlace = new HashMap<>();
    public final HashMap<Path, GraphicPath> associationPathGraphicPath = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        menuController.setMainController(this);
        graphController.setMainController(this);
        mondeController.setMainController(this);
        djisktraVisualiseurController.setMainController(this);

        graphController.pane.setStyle("-fx-background-color: grey;");
        
        World w = new World("Monde");
        actualWorld.set(w);
        
        Place pl = new Place(2, "kf", null);
        Place pl2 = new Place(3, "kf", null);
        Path p1 = new Path(pl, pl2, 10);
        
        GraphicPlace gp = new GraphicPlace(pl, 50, 100);
        GraphicPlace gp2 = new GraphicPlace(pl2, 50, 100);
        GraphicPath gpa = new GraphicPath(gp, gp2, p1);
        
        actualWorld.get().addPlace(pl);
        actualWorld.get().addPlace(pl2);
        actualWorld.get().addPath(p1);
        
		/*
		 * actualWorld.get().addPlace(pl); actualWorld.get().addPlace(pl2);
		 * 
		 * graphController.pane.getChildren().add(gp);
		 * graphController.pane.getChildren().add(gp2);
		 */
        
        reload(graphController.pane);
    }


    public void launchDijkstra() {

        Place place = actualWorld.get().getPlaceFromId(1);

        //Place place = selectedPlace.get();
        System.out.println("eu ba on est la 2");

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
                    // R.I.P. Grafiqpass
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

        } else {
            System.out.println("place est null");
        }

        //CompletableFuture.completeAsync()

    }



    public void reload(Pane pane) {
        associationPlaceGraphicPlace.clear();
        associationPathGraphicPath.clear();
        pane.getChildren().clear();

        // creer les graphic place
        for (Place place : actualWorld.get().getPlaces()) {
            GraphicPlace graphicPlace = new GraphicPlace(place, 10, 20);
            graphicPlace.setCenterX(100 + Math.random() * 600);
            graphicPlace.setCenterY(100 + Math.random() * 400);
            graphicPlace.setMainController(this);
            associationPlaceGraphicPlace.put(place, graphicPlace);
            pane.getChildren().add(graphicPlace);
            pane.getChildren().add(graphicPlace.getLabel());
        }

        // creer les graphic path
        for (Path path : actualWorld.get().getPaths()) {
            Place depart = path.getFirstPlace();
            Place arrive = path.getSecondPlace();
            GraphicPath graphicPath = new GraphicPath(associationPlaceGraphicPlace.get(depart), associationPlaceGraphicPlace.get(arrive), path);
            associationPathGraphicPath.put(path, graphicPath);
            pane.getChildren().add(graphicPath);
            pane.getChildren().add(graphicPath.getLabel());
        }
    }

    @Override
    public void setup(Place place) {
        for (Place p : associationPlaceGraphicPlace.keySet()) {
            GraphicPlace gp = associationPlaceGraphicPlace.get(p);
            gp.setState(GraphicPlaceState.DIJKSTRA_UNVISITED);
            gp.setDistanceText("∞");

        }

        GraphicPlace startGraphic = associationPlaceGraphicPlace.get(place);
        if (startGraphic != null) {
            startGraphic.setState(GraphicPlaceState.DIJKSTRA_CURRENT);
            startGraphic.setDistanceText("0");
        }
    }

    @Override
    public void beforeLineFrom(Place place) {

    }

    @Override
    public void beforeNewDistance(Place from, Place to) {

    }

    @Override
    public void newDistance(Place debut, Place arrive, int distance) {
        GraphicPlace gpTo = associationPlaceGraphicPlace.get(arrive);
        if (gpTo != null) {
            gpTo.setState(GraphicPlaceState.DIJKSTRA_MODIFIED);
            gpTo.setDistanceText(String.valueOf((int) distance));
        }

        Path path = actualWorld.get().getPathBetween(debut, arrive);
        if (path != null) {
            GraphicPath graphicPath = associationPathGraphicPath.get(path);
            if (graphicPath != null) {
                graphicPath.setState(GraphicPathState.HAS_FOCUS_MODIFIED);
            }
        }
    }

    @Override
    public void afterNewDistance(Place from, Place to) {

    }

    @Override
    public void afterLineFrom(Place current) {

    }

    @Override
    public void tearDown() {

    }
    
    public void selectGraphicPlace(GraphicPlace selectedGp) {
        for (GraphicPlace gp : associationPlaceGraphicPlace.values()) {
            gp.getSelectedProperty().set(gp == selectedGp);
        }
    }

}

