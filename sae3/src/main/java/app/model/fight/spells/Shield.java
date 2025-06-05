package app.model.fight.spells;

import app.model.entity.Entity;
import app.model.fight.Spell;

public class Shield extends Spell {
    public Shield(int nbTurn,  int amount, int cost) {
        this(nbTurn, nbTurn, amount, cost);
    }
    public Shield(int nbTurnLeft, int duration, int amount, int cost) {
        super(nbTurnLeft, duration, amount, cost);
    }
    @Override
    public void applyEffect(Entity target) {
        target.setArmor(target.getArmor() + amount);
    }

    @Override
    public void endBoost(Entity target) {
        target.setArmor(target.getArmor() - amount);
    }

    @Override
    public boolean isSelfSpell() {
        return true;
    }

    public boolean isBoost() {
        return true;
    }
}
