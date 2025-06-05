package app.javafx.controller;

import app.javafx.controller.GraphicPlaceState;
import app.model.map.Place;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GraphicPlace extends Circle {

    private Place place;
    private Label label;

    private SimpleObjectProperty<GraphicPlaceState> state = new SimpleObjectProperty<>(GraphicPlaceState.IS_DEFAULT);
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private Color color;

    public GraphicPlace() {
        this.place = null; // Initialisation par défaut
        this.label = new Label("N/A");
    }

    public GraphicPlace(Place place) {
        this.place = place;
        this.label = new Label(Integer.toString(place.getId()));

        label.layoutXProperty().bind(centerXProperty());
        label.layoutYProperty().bind(centerYProperty());

        fillProperty().bind(Bindings.when(state.isEqualTo(GraphicPlaceState.IS_DEFAULT)).then(GraphicPlaceState.IS_DEFAULT.getColor())
                .otherwise(state.get().getColor()));

        strokeProperty().bind(Bindings.when(selected).then(Color.BLACK).otherwise(state.get().getColor()));

        strokeWidthProperty().bind(Bindings.when(state.isEqualTo(GraphicPlaceState.IS_DEFAULT))
                .then(GraphicPlaceState.IS_DEFAULT.getStrokeWidth())
                .otherwise(state.get().getStrokeWidth()));

        setOnMousePressed(this::MousePressed);
        setOnMouseDragged(this::MouseDragged);
    }

    //if (jean vilar){
    //    oui
    //}

    private void MousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            selected.set(true);
            event.consume();
        }
    }

    private void MouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double difX = event.getSceneX() - getCenterX();
            double difY = event.getSceneY() - getCenterY();

            setCenterX(getCenterX() + difX);
            setCenterY(getCenterY() + difY);

            event.consume();
        }
    }


    public GraphicPlaceState getState() {
        return state.get();
    }

    public void setState(GraphicPlaceState state) {
        this.state.set(state);
    }

    public BooleanProperty getSelectedProperty() {
        return selected;
    }

    public Place getPlace() {
        return place;
    }

    public Label getLabel() {
        return label;
    }

    public void setDistanceText(String string){
        this.label.setText(string);
    }
    
    //public void setFill(Color color){
      //  this.color.;
    //}

}
