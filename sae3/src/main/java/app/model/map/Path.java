package app.model.map;

import app.model.parser.ParserConfig;

public class Path {
    private final int length;

    private final Place firstPlace;
    private final Place secondPlace;

    public Path(int length, Place firstPlace, Place secondPlace) {
        this.length = length;
        this.firstPlace = firstPlace;
        this.secondPlace = secondPlace;
    }

    public Path(Place firstPlace, Place secondPlace, int length) {
        this(length, firstPlace, secondPlace);
    }

    public int getLength() {
        return length;
    }

    public String toString() {
        return "%s to %s".formatted(firstPlace.getName(), secondPlace.getName());
    }

    public Place getFirstPlace() {
        return firstPlace;
    }

    public Place getSecondPlace() {
        return secondPlace;
    }


    public String asJSON() {
        return ("""
                {
                      "%s": %d,
                      "%s": %d,
                      "%s": %d
                    }""").formatted(ParserConfig.KEY_PATH_FIRST_PLACE, firstPlace.getId(), ParserConfig.KEY_PATH_SECOND_PLACE, secondPlace.getId(), ParserConfig.KEY_PATH_DISTANCE, length);
    }
}
