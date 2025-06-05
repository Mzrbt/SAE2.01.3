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
    private CheckBox ismonster;

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

        // Lier la propriété `selectedPlace` du MainController au contrôleur actuel
        mainController.selectedPlace.addListener((observable, oldPlace, newPlace) -> setSelectedPlace(newPlace));

        // Initialiser la place actuellement sélectionnée (si elle existe)
        setSelectedPlace(mainController.selectedPlace.get());
    }

    public void setSelectedPlace(Place place) {
        this.selectedPlace = place;

        if (place != null) {
            // Liaison des propriétés de l'objet Place avec l'interface
            id.textProperty().bind(Bindings.convert(place.nameProperty()));
            nomLieu.textProperty().bindBidirectional(place.nameProperty());
            descriptionLieu.textProperty().bindBidirectional(place.descriptionProperty());
            debut.selectedProperty().bindBidirectional(place.isStartProperty());
            fin.selectedProperty().bindBidirectional(place.isEndProperty());
            defaite.selectedProperty().bindBidirectional(place.isDefeatProperty());

            ismonster.selectedProperty().bindBidirectional((Property<Boolean>) place.monsterProperty().isNotNull());
            monsterName.disableProperty().bind(ismonster.selectedProperty().not());
            monsterHP.disableProperty().bind(ismonster.selectedProperty().not());
            monsterArmor.disableProperty().bind(ismonster.selectedProperty().not());
            monsterAttack.disableProperty().bind(ismonster.selectedProperty().not());
        }else {
            // Réinitialiser les champs si aucun lieu n'est sélectionné
            id.textProperty().unbind();
            nomLieu.textProperty().unbind();
            descriptionLieu.textProperty().unbind();
            debut.selectedProperty().unbind();
            fin.selectedProperty().unbind();
            defaite.selectedProperty().unbind();
            ismonster.selectedProperty().unbind();
        }

        // Désactiver le bouton Dijkstra si aucune place n'est sélectionnée
        if (selectedPlace != null) {
            launchDijkstra.disableProperty().bind(Bindings.isNull((ObservableObjectValue<?>) selectedPlace));
        } else {
            launchDijkstra.setDisable(true);
        }
    }
}