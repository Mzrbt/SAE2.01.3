package app.javafx.controller;

import javafx.scene.input.MouseEvent;

public interface CenterMouseEvent {
    void mousePressed(MouseEvent e);
    void mouseDragged(MouseEvent e);
    void mouseReleased(MouseEvent e);
}