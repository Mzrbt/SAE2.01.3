package app.javafx.controller;

import app.model.map.Place;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableObjectValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class DjisktraVisualiseurController {

    @FXML
    private TextField id;

    @FXML
    private TextField nomLieu;

    @FXML
    private TextArea descriptionLieu;

    @FXML
    private ToggleButton debut;

    @FXML
    private ToggleButton fin;

    @FXML
    private ToggleButton defaite;

    @FXML
    private CheckBox isMonster;

    @FXML
    private HBox monsterName;

    @FXML
    private HBox monsterHP;

    @FXML
    private HBox monsterArmor;

    @FXML
    private HBox monsterAttack;

    @FXML
    private Button launchDijkstra;

    private Place selectedPlace;

    private MainController mainController;

    /**
     * Définit le contrôleur principal pour permettre l'accès à ses données.
     *
     * @param mainController l'instance de MainController
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;

        mainController.selectedPlace.addListener((observable, oldPlace, newPlace) -> setSelectedPlace(newPlace));

        setSelectedPlace(mainController.selectedPlace.get());
    }

    public void setSelectedPlace(Place place) {
        this.selectedPlace = place;

        if (place != null) {
            id.textProperty().bindBidirectional((place.nameProperty()));
            nomLieu.textProperty().bindBidirectional(place.nameProperty());
            descriptionLieu.textProperty().bindBidirectional(place.descriptionProperty());
            debut.selectedProperty().bindBidirectional(place.isStartProperty());
            fin.selectedProperty().bindBidirectional(place.isEndProperty());
            defaite.selectedProperty().bindBidirectional(place.isDefeatProperty());

            isMonster.selectedProperty().bindBidirectional((Property<Boolean>) place.monsterProperty().isNotNull());
            monsterName.disableProperty().bind(isMonster.selectedProperty().not());
            monsterHP.disableProperty().bind(isMonster.selectedProperty().not());
            monsterArmor.disableProperty().bind(isMonster.selectedProperty().not());
            monsterAttack.disableProperty().bind(isMonster.selectedProperty().not());
        }else {
            id.textProperty().unbind();
            nomLieu.textProperty().unbind();
            descriptionLieu.textProperty().unbind();
            debut.selectedProperty().unbind();
            fin.selectedProperty().unbind();
            defaite.selectedProperty().unbind();
            isMonster.selectedProperty().unbind();
        }

        if (selectedPlace != null) {
            launchDijkstra.disableProperty().bind(Bindings.isNull((ObservableObjectValue<?>) selectedPlace));
        } else {
            launchDijkstra.setDisable(true);
        }
    }
}