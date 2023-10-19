package abs.compiler.parser.tree;

public class LongNode extends AbstractParseNode {
    private final Long longValue;

    public LongNode(Long longValue) {
        this.longValue = longValue;
    }

    public Long getLongValue() {
        return longValue;
    }
}
