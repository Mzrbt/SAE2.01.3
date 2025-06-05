package app.model.entity;

import app.model.fight.Spell;
import app.model.parser.JSONObject;
import app.model.parser.ParserConfig;
import javafx.beans.property.SimpleStringProperty;

public class Monster extends Entity {
    private final SimpleStringProperty name;

    public Monster() {
        this("", 100, 100, 5, 10);
    }

    public Monster(String name, int maximumHP, int armor, int attack) {
        this(name, maximumHP, maximumHP, armor, attack);
    }

    public Monster(String name, int currentHP, int maximumHP, int armor, int attack) {
        super(currentHP, maximumHP, armor, attack);
        this.name = new SimpleStringProperty(name);
    }

    public static Monster createFromJSONObject(JSONObject json) {
        try {
            int HP = json.getNumber(ParserConfig.KEY_MONSTER_HP).intValue();
            int armor = json.getNumber(ParserConfig.KEY_MONSTER_ARMOR).intValue();
            int attack = json.getNumber(ParserConfig.KEY_MONSTER_ATTACK).intValue();
            String name = json.getString(ParserConfig.KEY_MONSTER_NAME);
            return new Monster(name, HP, HP, armor, attack);
        } catch (Exception e) {
            return null;
        }
    }

    public Monster clone() {
        Monster clone = new Monster(name.get(), currentHP.get(), maximumHP.get(), armor.get(), attack.get());

        for (Spell s : effects) {
            clone.addEffect(s.clone(), false);
        }

        return clone;
    }

    public Object asJSON() {
        return (" {\n" +
                "       \"name\": \"%s\",\n" +
                "        \"HP\": %d,\n" +
                "        \"Armor\": %d,\n" +
                "        \"Attack\": %d\n" +
                "      }").formatted(name.get(), maximumHP.get(), armor.get(), attack.get());
    }
}
