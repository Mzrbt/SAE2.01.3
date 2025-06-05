package app.javafx.controller;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ClicGauche implements CenterMouseEvent{

    private Point2D pointDebut;
    private Pane pane;

    public ClicGauche(Pane pane) {
        this.pane = pane;
    }

    public void mousePressed(MouseEvent e) {
        pointDebut = new Point2D(e.getSceneX(), e.getSceneY());
        pane.setCursor(Cursor.MOVE);
    }

    public void mouseDragged(MouseEvent e) {
        double difX = e.getSceneX() - pointDebut.getX();
        double difY = e.getSceneY() - pointDebut.getY();
        pane.translateXProperty().set(pane.getTranslateX() + difX);
        pane.translateYProperty().set(pane.getTranslateY() + difY);
        pointDebut = new Point2D(e.getSceneX(), e.getSceneY());
    }

    public void mouseReleased(MouseEvent e) {
        pane.setCursor(Cursor.DEFAULT);
        // sauvegarde du deplacement dy panel
        // TODO
    }
}
