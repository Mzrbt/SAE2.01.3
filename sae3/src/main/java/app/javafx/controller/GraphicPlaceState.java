package app.javafx.controller;

import javafx.scene.paint.Color;

public enum GraphicPlaceState {
    IS_DEFAULT(Color.LIGHTGRAY, 2),
    IS_END(Color.RED, 3),
    IS_START(Color.GREEN, 3),
    IS_DEFEAT(Color.DARKRED, 3),
    DIJKSTRA_OVER(Color.DARKGREEN, 3),
    DIJKSTRA_VISITED(Color.BLUE, 2),
    DIJKSTRA_UNVISITED(Color.GRAY, 1),
    DIJKSTRA_CURRENT(Color.ORANGE, 4),
    DIJKSTRA_MODIFIED(Color.YELLOW, 3);

    private final Color color;
    private final double strokeWidth;

    GraphicPlaceState(Color c, double s) {
        this.color = c;
        this.strokeWidth = s;
    }

    public Color getColor() {
        return color;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }
}