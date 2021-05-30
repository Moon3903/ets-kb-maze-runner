package kb;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        if (o1.getFCost() < o2.getFCost()) {
            return 1;
        }
        return 0;
    }
}
