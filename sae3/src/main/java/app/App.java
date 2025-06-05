package app;

import app.javafx.controller.MainController;
import app.javafx.controller.MenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.sound.sampled.Control;
import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    public static MainController controller;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("interface.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root, 840, 600);
        stage.setScene(scene);

        stage.show();
    }

    public static void main (String[] args){
        launch();
    }
}