package app.ai.fight;


import app.model.entity.Monster;
import app.model.entity.Player;
import app.model.fight.Spell;

import java.util.*;

public class Solver<T> {

    public INode<T> AStar(INodeStar<T> start) {
        HashMap<INode<T>, Integer> cost = new HashMap<>();
        Comparator<INode<T>> comparator = Comparator.comparingInt(n -> cost.getOrDefault(n, Integer.MAX_VALUE) + ((INodeStar<T>) n).getHeuristic());
        PriorityQueue<INode<T>> queue = new PriorityQueue<>(comparator);
        Set<INode<T>> visited = new HashSet<>();

        queue.add(start);
        cost.put(start, 0);

        while (!queue.isEmpty()) {
            INode<T> current = queue.poll();

            if (current.isGoal()) {
                return current;
            }

            visited.add(current);

            for (Map.Entry<INode<T>, Integer> entry : current.getNeighbors().entrySet()) {
                INode<T> next = entry.getKey();
                int nextCost = current.getCost() + entry.getValue();

                if (!visited.contains(next) && !next.isDeadLock() && (!cost.containsKey(next) || cost.get(next) > nextCost)) {
                    cost.put(next, next.getCost());
                    queue.add(next);
                }
            }
        }

        return null;
    }

    public static boolean playerCanBeat(Player p, Monster m) {
        Solver<Spell> solv = new Solver<>();
        LessTurnNode start = new LessTurnNode(null, 0, null, p, m);

        return solv.AStar(start) != null;
    }
}
