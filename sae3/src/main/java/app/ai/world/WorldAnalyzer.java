package app.ai.world;

import app.ai.fight.INode;
import app.ai.fight.Solver;
import app.model.map.Place;
import app.model.map.World;
import javafx.util.Pair;

import java.util.*;

public class WorldAnalyzer {

    private World w;

    public WorldAnalyzer(World w) {
        this.w = w;
    }

    public boolean isConnex() {
        Set<Place> visited = new HashSet<>();
        List<Place> queue = new ArrayList<>();
        queue.add(w.getPlaces().get(0));

        while (!queue.isEmpty()) {
            Place current = queue.remove(0);

            visited.add(current);

            for (Place p : w.getPathsFrom(current).keySet()) {
                if (p.isEnd()) return true;

                if (!queue.contains(p) && !p.isDefeat() && !visited.contains(p)) {
                    queue.add(p);
                }
            }
        }

        return false;
    }
    public List<Pair<Place, HashMap<Place, Integer>>> dijkstraWithSteps(Place start) {
        List<Pair<Place, HashMap<Place, Integer>>> steps = new ArrayList<>();
        Map<Place, Integer> distances = new HashMap<>();
        Set<Place> visited = new HashSet<>();
        PriorityQueue<Pair<Place, Integer>> queue = new PriorityQueue<>(Comparator.comparingInt(pd -> pd.getValue()));

        for (Place place : w.getPlaces()) {
            distances.put(place, Integer.MAX_VALUE);
        }

        distances.put(start, 0);
        queue.add(new Pair<>(start, 0));


        while (!queue.isEmpty()) {
            Pair<Place, Integer> current = queue.poll();
            Place currentPlace = current.getKey();

            if (visited.contains(currentPlace)) continue;
            visited.add(currentPlace);

            HashMap<Place, Integer> newState = new HashMap<>();

            for (Map.Entry<Place, Integer> neighborEntry : w.getPathsFrom(currentPlace).entrySet()) {
                Place neighbor = neighborEntry.getKey();
                int pathLength = neighborEntry.getValue();

                int newDist = distances.get(currentPlace) + pathLength;
                if (newDist < distances.get(neighbor)) {
                    newState.put(neighbor, newDist);
                    distances.put(neighbor, newDist);
                    queue.add(new Pair<>(neighbor, newDist));
                }
            }

            steps.add(new Pair<>(currentPlace, newState));
        }

        return steps;
    }

    public int lessDistanceToReach(Place from, Place goal) {
        Solver<Place> s = new Solver<>();

        WorldNode.goal = new ArrayList<>();
        WorldNode.goal.add(goal);
        WorldNode worldNode = new WorldNode(null, 0, null, from, w);

        INode<Place> end = s.AStar(worldNode);

        return end.getCost();
    }
}
