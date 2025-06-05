package app.javafx.controller;

import app.model.map.Place;

/**
 * Listener interface for receiving events during the execution of Dijkstra's algorithm.
 * This allows for visualization or logging of the algorithm's key steps.
 */
public interface DijkstraEventListener {

    /**
     * Called before Dijkstra's algorithm starts.
     * Typically used to initialize the state of the graphical components,
     * such as setting the displayed distances of all nodes to infinity.
     *
     * @param place The starting node from which Dijkstra's algorithm will run.
     */
    void setup(Place place);

    /**
     * Called before examining and updating the distances from the specified node
     * to its adjacent nodes.
     *
     * @param place The node from which distances to neighbors are being considered.
     */
    void beforeLineFrom(Place place);

    /**
     * Called just before evaluating a possible new distance to reach the 'to' node from the 'from' node.
     * This may be used to visually indicate the edge being evaluated or to log the operation.
     *
     * @param from The current node being used as the base for distance updates.
     * @param to The target node whose distance is being updated.
     */
    void beforeNewDistance(Place from, Place to);

    /**
     * Called when a new shortest distance to a node is found and is about to be recorded.
     * This can be used to update visual labels or state in a GUI.
     *
     * @param from The source node leading to the update.
     * @param to The destination node whose distance is being updated.
     * @param distance The new shortest distance found to reach the destination node.
     */
    void newDistance(Place from, Place to, int distance);

    /**
     * Called right after a distance update for a given node has been made.
     * Can be used to finalize visual feedback or internal state changes.
     *
     * @param from The node from which the update was made.
     * @param to The node whose distance has just been updated.
     */
    void afterNewDistance(Place from, Place to);

    /**
     * Called after all distance updates from the current node have been completed.
     * Useful for marking finalizing animations.
     *
     * @param current The node for which all neighbor distances have been processed.
     */
    void afterLineFrom(Place current);

    /**
     * Called after Dijkstra's algorithm has completed.
     * Used for cleanup or resetting UI states.
     */
    void tearDown();
}
