package abs.compiler.parser.tree;

public class DoubleNode extends AbstractTreeNode {
    private final Double value;

    public DoubleNode(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
