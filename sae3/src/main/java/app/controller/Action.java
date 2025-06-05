package app.controller;

public enum Action {
    MOVE("Move to another place"), QUIT("Exit the game");

    public String getDescription() {
        return description;
    }

    private final String description;

    Action(String desc){
        this.description = desc;
    }
}
