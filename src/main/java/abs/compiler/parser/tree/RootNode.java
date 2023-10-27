package abs.compiler.parser.tree;

public class RootNode extends AbstractParseNode {
    public RootNode() {
        this.parent = null;
    }

    public RootNode(ParseNode child) {
        this();
        this.children.add(child);
    }

    @Override
    public String toDot() {
        return "digraph {\n" +
                "    node [shape=ellipse];\n" +
                children.get(0).toDot() +
                "}";
    }
}
