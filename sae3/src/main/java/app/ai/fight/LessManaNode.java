package app.ai.fight;

import app.model.entity.Monster;
import app.model.entity.Player;
import app.model.fight.Spell;

public class LessManaNode extends FightNode {
    public LessManaNode(INode<Spell> parent, int cost, Spell path, Player player, Monster monster) {
        super(parent, cost, path, player, monster);
    }

    @Override
    protected INode<Spell> getNode(INode<Spell> parent, int cost, Spell path, Player nextPlayer, Monster nextMonster) {
        return new LessManaNode(parent, cost + (path == null ? 0 : path.getCost()), path, nextPlayer, nextMonster);
    }

    @Override
    public int getHeuristic() {
        return cost + monster.getCurrentHP();
    }

}
