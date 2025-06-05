package app.javafx.controller;


import app.model.map.Path;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

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
        this.distanceLabel = new Label(Integer.toString(path.getLength()));

        startXProperty().bind(startPlace.centerXProperty());
        startYProperty().bind(startPlace.centerYProperty());

        endXProperty().bind(endPlace.centerXProperty());
        endYProperty().bind(endPlace.centerYProperty());

        distanceLabel = new Label(String.valueOf(path.getLength()));
        distanceLabel.setTextAlignment(TextAlignment.CENTER);

        distanceLabel.layoutXProperty().bind(Bindings.divide(Bindings.add(PlaceDebut.centerXProperty(), PlaceFin.centerXProperty()), 2));
        distanceLabel.layoutYProperty().bind(Bindings.divide(Bindings.add(PlaceDebut.centerYProperty(), PlaceFin.centerYProperty()), 2));

        strokeProperty().bind(Bindings.when(state.isEqualTo(GraphicPathState.DEFAULT)).then(Color.BLACK).otherwise(state.get().getColor()));
        strokeWidthProperty().bind(Bindings.when(state.isEqualTo(GraphicPathState.DEFAULT)).then(2.0).otherwise(state.get().getStrokeWidth()));
        distanceLabel.textFillProperty().bind(Bindings.when(state.isEqualTo(GraphicPathState.DEFAULT)).then(Color.BLACK).otherwise(state.get().getColor()));


        strokeProperty().bind(Bindings.when(state.isEqualTo(GraphicPathState.DEFAULT)).then(Color.BLACK).otherwise(state.get().getColor()));

        strokeWidthProperty().bind(Bindings.when(state.isEqualTo(GraphicPathState.DEFAULT)).then(2.0).otherwise(state.get().getStrokeWidth()));

        distanceLabel.textFillProperty().bind(Bindings.when(state.isEqualTo(GraphicPathState.DEFAULT)).then(Color.BLACK).otherwise(state.get().getColor()));

        toBack();
    }

    public Label getLabel() {
        return distanceLabel;
    }
    
    public Path getPath() {
    	return path;
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
