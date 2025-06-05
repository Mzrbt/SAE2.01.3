package app.ai.fight;

public interface INodeStar<T> extends INode<T> {
    int getHeuristic();
}
