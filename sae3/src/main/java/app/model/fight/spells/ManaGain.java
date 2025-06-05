package app.model.fight.spells;

import app.model.entity.Entity;
import app.model.entity.Player;
import app.model.fight.Spell;

public class ManaGain extends Spell {
    public ManaGain(int duration, int amount, int cost) {
        this(duration,duration, amount, cost);
    }
    public ManaGain(int nbTurnLeft, int duration, int amount, int cost) {
        super(nbTurnLeft, duration, amount, cost);
    }

    @Override
    public void applyEffect(Entity target) {
        Player p = ((Player)target);
        p.setCurrentMana(p.getCurrentMana() + amount);

        if (p.getCurrentMana() > p.getMaximumMana())
            p.setCurrentMana(p.getMaximumMana());
    }

    @Override
    public boolean isSelfSpell() {
        return true;
    }
}
