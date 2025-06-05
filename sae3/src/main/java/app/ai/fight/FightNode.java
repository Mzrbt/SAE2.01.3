package app.ai.fight;

import app.model.entity.Monster;
import app.model.entity.Player;
import app.model.fight.Spell;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FightNode extends Node<Spell> implements INodeStar<Spell> {
    protected final Player player;
    protected final Monster monster;

    public FightNode(INode<Spell> parent, int cost, Spell path, Player player, Monster monster) {
        super(parent, cost, path);
        this.player = player;
        this.monster = monster;

        /*
         * Affects apply at the start of a turn, so applying them when a node is created before doing the action.
         */
        this.player.applyEffects();
        this.monster.applyEffects();
    }

    @Override
    public boolean isGoal() {
        return monster.isDead();
    }

    @Override
    public boolean isDeadLock() {
        return player.isDead();
    }

    @Override
    public Map<INode<Spell>, Integer> getNeighbors() {
        HashMap<INode<Spell>, Integer> neighbors = new HashMap<>();

        for (Spell s : player.availableSpells()) {
            Player nextPlayer = player.clone();
            Monster nextMonster = monster.clone();

            if (s.isSelfSpell()) {
                if( !nextPlayer.castSpell(s, nextPlayer)) continue;
            } else {
                if (!nextPlayer.castSpell(s, nextMonster)) continue;
            }

            nextMonster.attack(nextPlayer);

            INode<Spell> nextNode = getNode(this, cost, s, nextPlayer, nextMonster);
            neighbors.put(nextNode, 1);
        }

        Player nextPlayer = player.clone();
        Monster nextMonster = monster.clone();
        nextPlayer.attack(nextMonster);

        /*
         * Assert that the monster isn't killed, or we could have a situation where both entities are dead.
         */
        if (nextMonster.isAlive())
            nextMonster.attack(nextPlayer);

        INode<Spell> nextNode = getNode(this, cost, null, nextPlayer, nextMonster);
        neighbors.put(nextNode, 1);

        return neighbors;
    }

    @Override
    public int getHeuristic() {
        return 0;
    }

    protected INode<Spell> getNode(INode<Spell> fightNode, int cost, Spell path, Player nextPlayer, Monster nextMonster) {
        return new FightNode(fightNode, cost, path, nextPlayer, nextMonster);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FightNode fightNode = (FightNode) o;
        return Objects.equals(player, fightNode.player) && Objects.equals(monster, fightNode.monster);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, monster);
    }

    @Override
    public String toString() {
        return "FightNode{" +
                "player=" + player +
                ", monster=" + monster +
                '}';
    }

    public Player getPlayer() { return player;}
}
