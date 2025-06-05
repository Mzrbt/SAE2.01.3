package app.javafx.controller;

import app.model.map.Place;
import app.model.map.World;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MainController {

    public ObjectProperty<Place> selectedPlace = new SimpleObjectProperty<>();
    public ObjectProperty<World> actualWorld = new SimpleObjectProperty<>();
    public BooleanProperty dijkstraOn = new SimpleBooleanProperty(false);
    public BooleanProperty worldLoading = new SimpleBooleanProperty(false);



}
