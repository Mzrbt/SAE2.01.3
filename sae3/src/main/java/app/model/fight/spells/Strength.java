package app.model.fight.spells;

import app.model.entity.Entity;
import app.model.fight.Spell;

public class Strength extends Spell {
    public Strength(int nbTurn,  int amount, int cost) {
        this(nbTurn, nbTurn, amount, cost);
    }
    public Strength(int nbTurnLeft, int duration, int amount, int cost) {
        super(nbTurnLeft, duration, amount, cost);
    }
    @Override
    public void applyEffect(Entity target) {
        target.setAttack(target.getAttack() + amount);
    }

    @Override
    public void endBoost(Entity target) {
        target.setAttack(target.getAttack() - amount);
    }

    @Override
    public boolean isSelfSpell() {
        return true;
    }

    @Override
    public boolean isBoost() {return true;}
}
