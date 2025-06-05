package app.javafx.controller;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.awt.event.WindowEvent;
import javafx.scene.control.TextField;
import java.io.File;

public class MondeController {


    private MainController mainController;

    @FXML
    private TextField nbPlace;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }



    @FXML
    public void initialize() {
        // Ajouter un ChangeListener pour n'accepter que des entiers
        nbPlace.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Vérifie si la nouvelle valeur contient uniquement des chiffres
                nbPlace.setText(oldValue); // Rétablit l'ancienne valeur si ce n'est pas un entier
            }
        });
    }
}