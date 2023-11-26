package abs.compiler.parser.tree;

public class StringNode extends AbstractTreeNode {
    private final String value;

    public StringNode(String value) {
        this.value = value;
    }
    public StringNode(String value, TreeNode... children) {
        this(value);

        for (TreeNode node : children) {
            addChild(node);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
