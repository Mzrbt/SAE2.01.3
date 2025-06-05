package app.model.fight.spells;

import app.model.entity.Entity;
import app.model.fight.Spell;

public class Heal extends Spell {
    public Heal(int nbTurn,  int amount, int cost) {
        this(nbTurn, nbTurn, amount, cost);
    }
    public Heal(int nbTurnLeft, int duration, int amount, int cost) {
        super(nbTurnLeft, duration, amount, cost);
    }

    @Override
    public void applyEffect(Entity target) {
        target.heal(amount);
    }

    @Override
    public boolean isSelfSpell() {
        return true;
    }
}
