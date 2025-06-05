package app.model.fight.spells;

import app.model.entity.Entity;
import app.model.fight.Spell;

public class Poison extends Spell {
    public Poison(int duration, int amount, int cost) {
        this(duration,duration, amount, cost);
    }
    public Poison(int nbTurnLeft, int duration, int amount, int cost) {
        super(nbTurnLeft, duration, amount, cost);
    }
    @Override
    public void applyEffect(Entity target) {
        target.deal(amount);
    }

    @Override
    public boolean isSelfSpell() {
        return false;
    }
}
