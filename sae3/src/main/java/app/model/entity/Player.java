package app.model.entity;

import app.model.fight.Spell;
import app.model.fight.spells.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Player extends Entity {
    private final static int POWER_PER_WIN = 10;
    private final int maximumMana;
    private int currentMana;
    private final List<Spell> spells;

    public Player(int currentHP, int maximumHP, int armor, int attack, int currentMana, int maximumMana, Spell... spells) {
        super(currentHP, maximumHP, armor, attack);
        this.currentMana = currentMana;
        this.maximumMana = maximumMana;
        this.spells = Arrays.asList(spells);
    }
    public Player(int currentHP, int maximumHP, int armor, int attack, int currentMana, int maximumMana) {
        this(currentHP, maximumHP, armor, attack, currentMana, maximumMana, new Heal(4, 10, 30),
                new Poison(4, 20, 25),
                new ManaGain(4, 20, 30),
                new Strength(2, 10, 30),
                new Shield(3, 10, 25));
    }

    public Player(int currentHP, int maximumHP, int armor, int attack, int mana) {
        this(currentHP, maximumHP, armor, attack, mana, mana);
    }

    public Player() {
        this(100,100,10,10,100);
    }


    public int getMaximumMana() {
        return maximumMana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public List<Spell> availableSpells() {
        return spells.stream().filter(spell -> spell.getCost() <= currentMana).toList();
    }

    /**
     *
     * @param spell Spell to launch.
     * @param target Entity targeted.
     * @return If the spell effect has been added. If the target is already affected by the spell, it will return false, true otherwise.
     */
    public boolean castSpell(Spell spell, Entity target) {
        if (spell.getCost() > this.currentMana)
            throw new RuntimeException("Can't cast %s".formatted(spell.getClass().getSimpleName()));

        if (target.addEffect(spell.clone())) {
            this.currentMana -= spell.getCost();
            return true;
        }

        return false;
    }

    public String toString() {
        return super.toString() + ", MAN: " + currentMana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return super.equals(o) && maximumMana == player.maximumMana && currentMana == player.currentMana && Objects.equals(spells, player.spells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximumMana, currentMana, spells);
    }

    public Player clone() {
        Spell[] newSpells = new Spell[spells.size()];

        for (int i = 0; i < spells.size(); i++) {
            newSpells[i] = spells.get(i).clone();
        }

        Player clone = new Player(currentHP.get(), maximumHP.get(), armor.get(), attack.get(), currentMana, maximumMana, newSpells);

        for (Spell s : effects) {
            clone.addEffect(s.clone(), false);
        }

        return clone;
    }
}
