package app.javafx.controller;

import javafx.scene.paint.Color;

public enum GraphicPathState {
    DEFAULT(Color.GRAY, 1),
    HAS_FOCUS(Color.BLUE, 2),
    HAS_FOCUS_MODIFIED(Color.RED, 3);

    private final Color color;
    private final double strokeWidth;

    GraphicPathState(Color c, double s) {
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