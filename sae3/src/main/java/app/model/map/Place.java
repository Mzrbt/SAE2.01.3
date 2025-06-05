package app.model.map;

import app.model.entity.Monster;
import app.model.parser.ParserConfig;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Map;

public class Place {
    private final int id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty description;
    private final SimpleBooleanProperty isEnd;
    private final SimpleBooleanProperty isStart;
    private final SimpleBooleanProperty isDefeat;
    private final World world;
    private final SimpleObjectProperty<Monster> monster;

    public Place(int id, String name, Monster monster, World world) {
        this(id, name, monster, "", world, false, false, false);
    }

    public Place(int id, String name, Monster monster, String description, World world, boolean isStart, boolean isEnd, boolean isDefeat) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.world = world;
        this.monster = new SimpleObjectProperty<>(monster);
        this.isEnd = new SimpleBooleanProperty(isEnd);
        this.isStart = new SimpleBooleanProperty(isStart);
        this.isDefeat = new SimpleBooleanProperty(isDefeat);
    }

    public Place(int id, String name, World world) {
        this(id, name, null, world);
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public void setDescription(String description) {
        descriptionProperty().set(description);
    }

    public String getName() {
        return name.get();
    }

    public String toString() {
        return String.format("%s\t%s\t%s", name.get(), monster, description.get());
    }

    public Monster getMonster() {
        return monster.get();
    }

    public boolean hasMonster() {
        return monster != null;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description.get();
    }

    public Map<Place, Integer> getPaths() {
        return world.getPathsFrom(this);
    }

    public String asJSON() {
        return ("""
                {
                      "%s": %d,
                      "%s": "%s",
                      "%s": "%s",
                      "%s": %b,
                      "%s": %b,
                      "%s": %b,
                      "%s": %s
                    }""").formatted(ParserConfig.KEY_PLACE_ID, id,
                ParserConfig.KEY_PLACE_NAME, name.get(),
                ParserConfig.KEY_PLACE_DESCRIPTION, description.get(),
                ParserConfig.KEY_PLACE_START, isStart.get(),
                ParserConfig.KEY_PLACE_END, isEnd.get(),
                ParserConfig.KEY_PLACE_DEFEAT, isDefeat.get(),
                ParserConfig.KEY_PLACE_MONSTER, (monster.get() == null) ? "null" : monster.get().asJSON());
    }

    public boolean isDefeat() {
        return isDefeat.get();
    }

    public boolean isEnd() {
        return isEnd.get();
    }

    public boolean isStart() {
        return isStart.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleBooleanProperty isEndProperty() {
        return isEnd;
    }

    public SimpleBooleanProperty isStartProperty() {
        return isStart;
    }

    public SimpleBooleanProperty isDefeatProperty() {
        return isDefeat;
    }

    public SimpleObjectProperty<Monster> monsterProperty() {return monster;}
}
