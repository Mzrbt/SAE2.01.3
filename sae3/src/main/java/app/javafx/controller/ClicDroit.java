package app.javafx.controller;

import app.javafx.model.GraphicPlace;
import app.model.map.Path;
import app.model.map.Place;
import app.model.map.World;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class ClicDroit implements CenterMouseEvent {

    private final Pane pane;
    private final MainController mainController;

    private GraphicPlace departGraphicPlace = null;
    private Line ligneTemp;


    public ClicDroit(Pane p, MainController mc) {
        this.pane = p;
        this.mainController = mc;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.SECONDARY) {




            for (Node node : pane.getChildren()) {
                if (node instanceof GraphicPlace gp) {
                    //Point2D local = gp.sceneToLocal(e.getSceneX(), e.getSceneY());      gp.contains(local) ||
                    if (gp.contains(e.getSceneX(), e.getSceneY())) {
                        departGraphicPlace = gp;

                        // Créer une ligne temporaire
                        ligneTemp = new Line();
                        ligneTemp.setStartX(gp.getLayoutX());
                        ligneTemp.setStartY(gp.getLayoutY());
                        ligneTemp.setEndX(e.getX());
                        ligneTemp.setEndY(e.getY());
                        //ligneTemp.getStyleClass().add("tempary_line");
                        pane.getChildren().add(ligneTemp);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (ligneTemp != null) {
            ligneTemp.setEndX(e.getX());
            ligneTemp.setEndY(e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (ligneTemp != null) {
            pane.getChildren().remove(ligneTemp);
            ligneTemp = null;

            for (Node node : pane.getChildren()) {
                if (node instanceof GraphicPlace gp && gp != departGraphicPlace) {
                    Point2D local = gp.sceneToLocal(e.getSceneX(), e.getSceneY());
                    if (gp.contains(local)) {
                        // path dans le model
                        Place depart = departGraphicPlace.getPlace();
                        Place arrive = gp.getPlace();

                        // changemnt du world
                        World world = mainController.actualWorld.get();
                        Path newPath = new Path(depart, arrive, 1);
                        world.addPath(newPath);

                        // changmen du graph
                        GraphicPath gpPath = new GraphicPath(departGraphicPlace, gp, newPath);
                        mainController.associationPathGraphicPath.put(newPath, gpPath);
                        pane.getChildren().add(gpPath);
                        break;
                    }
                }
            }
            departGraphicPlace = null;
        }
    }
}
