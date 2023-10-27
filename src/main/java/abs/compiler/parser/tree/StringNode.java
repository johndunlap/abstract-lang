package abs.compiler.parser.tree;

public class StringNode extends AbstractParseNode {
    private final String value;

    public StringNode(String value) {
        this.value = value;
    }
    public StringNode(String value, ParseNode... children) {
        this(value);

        for (ParseNode node : children) {
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
