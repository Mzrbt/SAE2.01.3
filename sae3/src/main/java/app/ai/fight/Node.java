package app.ai.fight;

import java.util.ArrayList;
import java.util.List;

public abstract class Node<T> implements INode<T> {
    protected int cost;
    protected INode<T> parent;
    protected T path;

    protected Node(INode<T> parent, int cost, T path) {
        this.parent = parent;
        this.cost = cost;
        this.path = path;
    }

    public List<INode<T>> rebuildPath() {
        List<INode<T>> res = new ArrayList<>();
        INode<T> current = this;

        while (current != null) {
            res.add(current);
            current = current.getParent();
        }

        for (int i = 0; i < res.size(); i++) {
            res.add(i, res.remove(res.size() - 1));
        }

        return res;
    }


    public INode<T> getParent() {
        return parent;
    }

    public int getCost() {
        return cost;
    }

    public T getPath() {
        return path;
    }

}
