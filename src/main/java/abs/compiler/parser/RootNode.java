package abs.compiler.parser;

public class RootNode extends AbstractNode {

    public RootNode() {
    }

    public RootNode(Node node) {
        this.children = node.getChildren();
        this.parent = node.getParent();
    }

    @Override
    public String toString() {
        return "ROOT";
    }
}
