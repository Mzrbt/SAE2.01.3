package app.javafx.controller;

import app.model.parser.WorldIO;
import app.model.map.World;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static app.model.parser.WorldIO.loadWorld;

public class MenuController {

    @FXML
    private Button loadButton;

    @FXML
    private Button saveButton;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        loadButton.disableProperty().bind(Bindings.or(
                mainController.dijkstraOn,
                mainController.worldLoading
        ));
        saveButton.disableProperty().bind(Bindings.or(
                mainController.dijkstraOn,
                mainController.worldLoading
        ));
    }

    @FXML
    public void handleLoadWorld() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json"));
        fileChooser.setTitle("Charger un monde");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                // Charger le monde depuis le fichier en utilisant WorldIO
                World world = WorldIO.loadWorld(Files.newInputStream(file.toPath()));
                mainController.actualWorld.set(world); // Met à jour la propriété actualWorld
            } catch (IOException e) {
                e.printStackTrace();
                // Gérer les erreurs de lecture du fichier
            } catch (Exception e) {
                e.printStackTrace();
                // Gérer les erreurs de formatage ou autres exceptions
            }
        }
    }

    @FXML
    public void handleSaveWorld() {
        if (mainController.actualWorld.get() == null) {
            System.out.println("Aucun monde à sauvegarder !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json"));
        fileChooser.setTitle("Sauvegarder le monde");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                // Sauvegarder le monde actuel dans le fichier sélectionné
                WorldIO.saveWorld(mainController.actualWorld.get(), file);
                System.out.println("Monde sauvegardé avec succès !");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la sauvegarde du monde.");
            }
        }
    }

}