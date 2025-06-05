package app.javafx.controller;

import app.Main;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class GraphController {


    @FXML public Pane pane;

    private MainController mainController;

    public void setMainController(MainController controller){
        this.mainController = controller;
    }

}
