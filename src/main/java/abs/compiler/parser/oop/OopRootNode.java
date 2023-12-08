package abs.compiler.parser.oop;

import abs.compiler.parser.AbstractNode;
import abs.compiler.parser.Node;
import abs.compiler.parser.RootNode;

public class OopRootNode extends AbstractNode {

    public OopRootNode(Node parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    @Override
    public String toString() {
        return "OOP ROOT";
    }
}
