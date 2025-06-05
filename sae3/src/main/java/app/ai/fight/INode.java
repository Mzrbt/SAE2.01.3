package app.ai.fight;

import java.util.List;
import java.util.Map;

/**
 *
 * @param <T> Represent the object for going from one node to another.
 *
 */
public interface INode<T> {
    /**
     *
     * @return True if it's the goal we are looking for.
     */
    boolean isGoal();

    /**
     *
     * @return False if we can't reach the goal from this point
     */
    boolean isDeadLock();

    /**
     *
     * @return A map of &lt;destination, distance> from a given node.
     */
    Map<INode<T>, Integer> getNeighbors();

    List<INode<T>> rebuildPath();
    T getPath();
    INode<T> getParent();
    int getCost();
}
