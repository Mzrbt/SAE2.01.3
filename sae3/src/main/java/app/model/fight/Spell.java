package app.model.fight;

import app.model.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class Spell {
    protected final int duration;
    protected int nbTurnLeft;
    protected int amount;
    protected int cost;

    public Spell(int nbTurnLeft, int duration, int amount, int cost) {
        if (duration <= 0) throw new RuntimeException("Spell must last at least one turn.");
        if (cost <= 0) throw new RuntimeException("A spell can't be cast for free or negative mana.");

        this.nbTurnLeft = nbTurnLeft;
        this.duration = duration;
        this.amount = amount;
        this.cost = cost;
    }

    public Spell(int duration, int amount, int cost) {
        this(duration, duration, amount, cost);
    }

    /**
     * Apply the specific effect from the spell, which should be damage, heal or boost.
     *
     * @return If the spell is over.
     */
    public boolean apply(Entity target) {
        if (target == null)
            throw new RuntimeException("No target designed.");

        if (!isBoost()) applyEffect(target);

        nbTurnLeft--;

        if (nbTurnLeft <= 0) {
            if (isBoost()) {
                endBoost(target);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass().equals(this.getClass());
    }

    @Override
    public int hashCode() {
        return this.getClass().toString().hashCode();
    }

    public abstract void applyEffect(Entity target);

    public abstract boolean isSelfSpell();

    public int getCost() {
        return cost;
    }

    @Override
    public Spell clone() {
        for (Constructor<?> c : this.getClass().getConstructors()) {
            if (c.getParameterCount() == 4) {
                try {
                    return (Spell) c.newInstance(nbTurnLeft, duration, amount, cost);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        throw new RuntimeException("%s has no constructor with 4 parameters".formatted(this.getClass().getSimpleName()));
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "duration=" + duration +
                ", nbTurnLeft=" + nbTurnLeft +
                ", amount=" + amount +
                ", cost=" + cost +
                '}';
    }

    public int getAmount() {
        return amount;
    }

    /**
     *
     * @return If the spell is a boost which mean those effects should only be applied once.
     */
    public boolean isBoost() {
        return false;
    }

    /**
     * Not abstract to avoid redefinition in every spell class, only spell whose {@link app.model.fight.Spell#isBoost()} return true should override this function.
     *
     * @param target The entity affected by the spell.
     */
    protected void endBoost(Entity target) { }
}
