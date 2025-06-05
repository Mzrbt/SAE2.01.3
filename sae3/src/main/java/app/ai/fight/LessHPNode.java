package app.ai.fight;

import app.model.entity.Monster;
import app.model.entity.Player;
import app.model.fight.Spell;
import app.model.fight.spells.Shield;

public class LessHPNode  extends FightNode {
    public LessHPNode(INode<Spell> parent, int cost, Spell path, Player player, Monster monster) {
        super(parent, cost, path, player, monster);
        if (monster.isDead()) super.cost--;
    }

    @Override
    protected INode<Spell> getNode(INode<Spell> parent, int cost, Spell path, Player nextPlayer, Monster nextMonster) {
        int damage = monster.getAttack() - (player.getArmor() + ((path instanceof Shield) ? (path).getAmount() : 0)) ;
        damage = (damage <= 0) ? 1 : damage;
        return new LessHPNode(parent, cost + damage, path, nextPlayer, nextMonster);
    }

    @Override
    public int getHeuristic() {
        return player.getMaximumHP() - player.getCurrentHP();
    }
}
