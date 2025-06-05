package app.javafx.controller;

import app.model.map.World;
import javafx.fxml.FXML;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import app.ai.world.WorldGenerator;

import javafx.scene.control.CheckBox;
import java.util.concurrent.CompletableFuture;


public class MondeController {

    private MainController mainController;

    @FXML
    private TextField nbPlace;
    @FXML
    private Slider sliderDepart, sliderVictoire, sliderDefaite, sliderNeutre;

    @FXML
    private Label labelDepart, labelVictoire, labelDefaite, labelNeutre;

    @FXML
    private Button generateButton;

    @FXML
    private CheckBox checkboxAI;

    @FXML
    private ProgressBar progressBar;

    private final IntegerProperty totalPlaces = new SimpleIntegerProperty(100);

    public void setMainController(MainController mainController) {
        this.mainController = mainController;

        nbPlace.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                nbPlace.setText(oldValue);
            }
        });

        generateButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            double sum = sliderDepart.getValue() + sliderVictoire.getValue() + sliderDefaite.getValue();
            int places = nbPlace.getText().isEmpty() ? 0 : Integer.parseInt(nbPlace.getText());
            return sum > 100 || places <= 2;
        }, sliderDepart.valueProperty(), sliderVictoire.valueProperty(), sliderDefaite.valueProperty(), nbPlace.textProperty()));

        progressBar.setVisible(false);

        // Ajouter des listeners pour recalculer les valeurs
        sliderDepart.valueProperty().addListener((obs, oldVal, newVal) -> updateSliders());
        sliderVictoire.valueProperty().addListener((obs, oldVal, newVal) -> updateSliders());
        sliderDefaite.valueProperty().addListener((obs, oldVal, newVal) -> updateSliders());

        // Lier les labels aux sliders
        bindLabelToSliderWithPaths(sliderDepart, labelDepart);
        bindLabelToSliderWithPaths(sliderVictoire, labelVictoire);
        bindLabelToSliderWithPaths(sliderDefaite, labelDefaite);
        bindLabelToSliderWithPaths(sliderNeutre, labelNeutre);

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
        return (places * (places - 1)) / 2;
    }

    private void updateSliders() {
        // Calculer la somme des sliders actifs
        double sum = sliderDepart.getValue() + sliderVictoire.getValue() + sliderDefaite.getValue();

        // Ajuster le slider neutre
        if (sum <= 100) {
            sliderNeutre.setValue(100 - sum);
            sliderNeutre.setStyle("");
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

    @FXML
    private void generateWorld() {
        int places = Integer.parseInt(nbPlace.getText());
        double startPercentage = sliderDepart.getValue() / 100.0;
        double defeatPercentage = sliderDefaite.getValue() / 100.0;

        WorldGenerator generator = WorldGenerator.builder()
                .name("Mon Monde")
                .nbPlace(places)
                .percentageStartPoint(startPercentage)
                .percentageDefeatPoint(defeatPercentage)
                .withAIGeneration(checkboxAI.isSelected())
                .build();

        if (checkboxAI.isSelected()) {
            // Si une génération avec AI est activée
            progressBar.setVisible(true);
            progressBar.setProgress(0);

            CompletableFuture.runAsync(() -> {
                // Lancement de la génération du monde
                World world = generator.generate(place -> {
                    // Mise à jour de la barre de progression dans le thread UI
                    javafx.application.Platform.runLater(() -> {
                        double progress = (double) place.getId() / places;
                        progressBar.setProgress(progress);
                    });
                });

                // Mise à jour dans le thread UI une fois terminé
                javafx.application.Platform.runLater(() -> {
                    progressBar.setVisible(false);
                    mainController.actualWorld.set(world);
                    mainController.reload(mainController.graphController.pane);
                    System.out.println("Monde généré avec succès !");
                });
            });

        } else {
            // Génération sans AI
            World world = generator.generate(place -> {});
            mainController.actualWorld.set(world);
            mainController.reload(mainController.graphController.pane);
            System.out.println("Monde généré avec succès !");
        }


    }
}