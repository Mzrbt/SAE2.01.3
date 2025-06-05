package app.javafx.controller;


import app.javafx.model.GraphicPlace;
import app.model.map.Path;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

import static app.javafx.controller.GraphicPlaceState.*;

public class GraphicPath  extends Line {

    private GraphicPlace PlaceDebut;
    private GraphicPlace PlaceFin;
    private Path path;
    private Label distanceLabel;
    private SimpleObjectProperty<GraphicPathState> state = new SimpleObjectProperty<>(GraphicPathState.DEFAULT);


    public GraphicPath(GraphicPlace startPlace, GraphicPlace endPlace, Path path) {
        this.PlaceDebut = startPlace;
        this.PlaceFin = endPlace;
        this.path = path;

        // definition des poitns de debuts et de fin de la Line
        setStartX(PlaceDebut.getCenterX());
        setStartY(PlaceDebut.getCenterY());
        setEndX(PlaceFin.getCenterX());
        setEndY(PlaceFin.getCenterY());

        // la distance a afficher
        distanceLabel.setLayoutX((PlaceDebut.getCenterX() + PlaceFin.getCenterX()) / 2);
        distanceLabel.setLayoutY((PlaceDebut.getCenterY() + PlaceFin.getCenterY()) / 2);

        distanceLabel = new Label(String.valueOf(path.getLength()));
        distanceLabel.setTextAlignment(TextAlignment.CENTER);

        distanceLabel.layoutXProperty().bind(Bindings.divide(Bindings.add(PlaceDebut.layoutXProperty() , PlaceFin.layoutXProperty()), 2));
        distanceLabel.layoutYProperty().bind(Bindings.divide(Bindings.add(PlaceDebut.layoutYProperty() , PlaceFin.layoutYProperty()), 2));



        strokeProperty().bind(Bindings.when(state.isEqualTo(GraphicPathState.DEFAULT)).then(Color.BLACK).otherwise(state.get().getColor()));

        strokeWidthProperty().bind(Bindings.when(state.isEqualTo(GraphicPathState.DEFAULT)).then(2.0).otherwise(state.get().getStrokeWidth()));

        distanceLabel.textFillProperty().bind(Bindings.when(state.isEqualTo(GraphicPathState.DEFAULT)).then(Color.BLACK).otherwise(state.get().getColor()));

        toBack();
    }

    public void setState(GraphicPathState newState) {
        this.state.set(newState);

        //switch (newState) {
        //    case DIJKSTRA_UNVISITED -> GraphicPlace.setFill(Color.LIGHTGRAY);
        //    case DIJKSTRA_CURRENT -> GraphicPlace.setFill(Color.LIGHTBLUE);
        //    case DIJKSTRA_MODIFIED -> GraphicPlace.setFill(Color.ORANGE);
        //}
    }


}
