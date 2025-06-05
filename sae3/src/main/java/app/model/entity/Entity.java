package app.model.entity;

import app.model.fight.Spell;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Entity {
    protected SimpleIntegerProperty currentHP;
    protected SimpleIntegerProperty maximumHP;
    protected SimpleIntegerProperty armor;
    protected SimpleIntegerProperty attack;
    protected List<Spell> effects;

    public Entity(int currentHP, int maximumHP, int armor, int attack, List<Spell> effects) {
        this.currentHP = new SimpleIntegerProperty(currentHP);
        this.maximumHP = new SimpleIntegerProperty(maximumHP);
        this.armor = new SimpleIntegerProperty(armor);
        this.attack = new SimpleIntegerProperty(attack);
        this.effects = effects;
    }

    public Entity(int currentHP, int maximumHP, int armor, int attack) {
        this(currentHP, maximumHP, armor, attack, new ArrayList<>());
    }

    public Entity() {
        this(100, 100, 1, 1);
    }

    public void attack(Entity other) {
        int damage = attack.get() - other.armor.get();
        if (damage <= 0) {
            damage = 1;
        }

        other.deal(damage);
    }

    public boolean isAlive() {
        return this.currentHP.get() > 0;
    }

    public boolean isDead() {
        return this.currentHP.get() <= 0;
    }

    /**
     * @param e Effect to add.
     * @return If the target is already affected by the spell, it will return false, true otherwise.
     */
    protected boolean addEffect(Spell e) {
        return addEffect(e, true);
    }

    protected boolean addEffect(Spell e, boolean applyBoost) {
        if (effects.contains(e)) return false;

        this.effects.add(e);

        if (applyBoost && e.isBoost()) {
            e.applyEffect(this);
        }

        return true;
    }

    public void applyEffects() {
        for (int i = 0; i < effects.size(); i++) {
            Spell s = effects.get(i);
            if (s.apply(this)) {
                removeEffect(s);
                i--;
            }
        }
    }

    public void heal(int HP) {
        this.currentHP.set(this.currentHP.get() + HP);

        if (this.currentHP.get() > this.maximumHP.get()) {
            this.currentHP.set(this.maximumHP.get());
        }
    }

    public void deal(int HP) {
        this.currentHP.set(this.currentHP.get() - HP);
    }

    public int getCurrentHP() {
        return currentHP.get();
    }

    public int getMaximumHP() {
        return maximumHP.get();
    }

    public int getArmor() {
        return armor.get();
    }

    public void setArmor(int armor) {
        this.armor.set(armor);
    }

    public int getAttack() {
        return attack.get();
    }

    public void setAttack(int attack) {
        this.attack.set(attack);
    }

    public List<Spell> getEffects() {
        return effects;
    }

    public void removeEffect(Spell effect) {
        this.effects.remove(effect);
    }

    public String toString() {
        return "HP: [%d/%d], ARM: %d, ATT: %d, %s".formatted(currentHP.get(), maximumHP.get(), armor.get(), attack.get(), effects.isEmpty() ? "" : effects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return currentHP.get() == entity.currentHP.get() && maximumHP.get() == entity.maximumHP.get() && armor.get() == entity.armor.get() && attack.get() == entity.attack.get() && Objects.equals(effects, entity.effects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentHP.get(), maximumHP.get(), armor.get(), attack.get(), effects);
    }

    public boolean isAffectedBy(Spell s) {
        return this.effects.contains(s);
    }
}
