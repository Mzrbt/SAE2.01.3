package app.javafx.controller;

import app.model.map.Place;
import app.model.map.World;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    public MenuController menuController;
    @FXML
    public GraphController graphController;
    @FXML public MondeController mondeController;
    @FXML public DjisktraVisualiseurController djisktraVisualiseurController;


    public ObjectProperty<Place> selectedPlace = new SimpleObjectProperty<>();
    public ObjectProperty<World> actualWorld = new SimpleObjectProperty<>();
    public BooleanProperty dijkstraOn = new SimpleBooleanProperty(false);
    public BooleanProperty worldLoading = new SimpleBooleanProperty(false);

    public void initialize(URL location, ResourceBundle resources) {

        menuController.setMainController(this);
        graphController.setMainController(this);
        mondeController.setMainController(this);
        djisktraVisualiseurController.setMainController(this);

    }


}
