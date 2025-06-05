package app.javafx.controller;

import javafx.fxml.FXML;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class MondeController {

    private MainController mainController;

    @FXML
    private TextField nbPlace;
    @FXML
    private Slider sliderDepart, sliderVictoire, sliderDefaite, sliderNeutre;

    @FXML
    private Label labelDepart, labelVictoire, labelDefaite, labelNeutre;

    private final IntegerProperty totalPlaces = new SimpleIntegerProperty(100);

    public void setMainController(MainController mainController) {
        this.mainController = mainController;

        nbPlace.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Vérifie si la nouvelle valeur contient uniquement des chiffres
                nbPlace.setText(oldValue); // Rétablit l'ancienne valeur si ce n'est pas un entier
            }
        });

        // Ajouter des listeners pour recalculer les valeurs
        sliderDepart.valueProperty().addListener((obs, oldVal, newVal) -> updateSliders());
        sliderVictoire.valueProperty().addListener((obs, oldVal, newVal) -> updateSliders());
        sliderDefaite.valueProperty().addListener((obs, oldVal, newVal) -> updateSliders());

        // Lier les labels aux sliders
        bindLabelToSliderWithPaths(sliderDepart, labelDepart);
        bindLabelToSliderWithPaths(sliderVictoire, labelVictoire);
        bindLabelToSliderWithPaths(sliderDefaite, labelDefaite);
        bindLabelToSliderWithPaths(sliderNeutre, labelNeutre);

        // Initialiser les sliders
        updateSliders();
    }

    private void bindLabelToSliderWithPaths(Slider slider, Label label) {
        label.textProperty().bind(Bindings.createStringBinding(() -> {
            int percentage = (int) slider.getValue();
            int estimatedPlaces = (int) (percentage / 100.0 * totalPlaces.get());
            int totalPaths = calculatePaths(estimatedPlaces);
            return percentage + "% (" + totalPaths + " chemins)";
        }, slider.valueProperty(), totalPlaces));
    }

    private int calculatePaths(int places) {
        return (places * (places - 1)) / 2; // Formule pour la somme des entiers de 1 à (places - 1)
    }

    private void updateSliders() {
        // Calculer la somme des sliders actifs
        double sum = sliderDepart.getValue() + sliderVictoire.getValue() + sliderDefaite.getValue();

        // Ajuster le slider neutre
        if (sum <= 100) {
            sliderNeutre.setValue(100 - sum);
            sliderNeutre.setStyle(""); // Réinitialiser le style
            labelNeutre.setStyle("");
        } else {
            sliderNeutre.setValue(0);
            // Appliquer le style rouge si la somme dépasse 100
            String errorStyle = "-fx-text-fill: red; -fx-control-inner-background: red;";
            sliderDepart.setStyle(errorStyle);
            sliderVictoire.setStyle(errorStyle);
            sliderDefaite.setStyle(errorStyle);
            labelDepart.setStyle("-fx-text-fill: red;");
            labelVictoire.setStyle("-fx-text-fill: red;");
            labelDefaite.setStyle("-fx-text-fill: red;");
        }

        // Réinitialiser les styles si la somme est correcte
        if (sum <= 100) {
            String defaultStyle = "";
            sliderDepart.setStyle(defaultStyle);
            sliderVictoire.setStyle(defaultStyle);
            sliderDefaite.setStyle(defaultStyle);
            labelDepart.setStyle(defaultStyle);
            labelVictoire.setStyle(defaultStyle);
            labelDefaite.setStyle(defaultStyle);
        }
    }
}