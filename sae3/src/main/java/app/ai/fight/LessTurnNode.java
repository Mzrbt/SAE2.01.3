package app.ai.fight;

import app.model.entity.Monster;
import app.model.entity.Player;
import app.model.fight.Spell;

public class LessTurnNode extends FightNode {
    public LessTurnNode(INode<Spell> parent, int cost, Spell path, Player player, Monster monster) {
        super(parent, cost, path, player, monster);
    }

    @Override
    protected INode<Spell> getNode(INode<Spell> parent, int cost, Spell path, Player nextPlayer, Monster nextMonster) {
        return new LessTurnNode(parent, cost + 1, path, nextPlayer, nextMonster);
    }

    @Override
    public int getHeuristic() {
        return cost;
    }
}
