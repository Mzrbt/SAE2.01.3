package app.model.fight;

import app.model.entity.Entity;
import app.model.entity.Monster;
import app.model.entity.Player;

public class Fight {
    private final Player player;
    private final Monster monster;

    public Fight(Player p, Monster m) {
        this.player = p;
        this.monster = m;
    }

    public Entity turn(Spell e) {
        if (e == null) {
            player.attack(monster);
        } else {
            player.castSpell(e, e.isSelfSpell() ? player : monster);
        }

        if (!monster.isAlive()) {
            return player;
        }

        monster.attack(player);

        if (!player.isAlive()) return monster;
        return null;
    }

    public Entity newTurn() {
        player.applyEffects();
        monster.applyEffects();

        if (!monster.isAlive()) {
            return player;
        }

        if (!player.isAlive()) return monster;

        return null;
    }

    public Monster getMonster() {
        return monster;
    }

    public boolean playerWin() {
        return !monster.isAlive();
    }
}
