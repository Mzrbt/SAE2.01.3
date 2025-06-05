package app.ai.world;

import app.ai.fight.INode;
import app.ai.fight.INodeStar;
import app.ai.fight.Node;
import app.model.map.Place;
import app.model.map.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldNode extends Node<Place> implements INodeStar<Place> {
    public static List<Place> goal = null;
    private Place current;
    private World world;

    public WorldNode(INode<Place> parent, int cost, Place path, Place p, World w) {
        super(parent, cost, path);
        this.current = p;
        this.world = w;
    }

    @Override
    public boolean isGoal() {
        return goal.contains(current);
    }

    @Override
    public boolean isDeadLock() {
        return current.hasMonster() ;
    }

    @Override
    public Map<INode<Place>, Integer> getNeighbors() {
        HashMap<INode<Place>, Integer> res = new HashMap<>();

        world.getPathsFrom(current).forEach((key, value) -> res.put(new WorldNode(this, value + cost, current, key, world), value));

        return res;
    }


    @Override
    public int getHeuristic() {
        return cost;
    }

    @Override
    public String toString() {
        return (path == null) ? "" : path.toString();
    }
}
