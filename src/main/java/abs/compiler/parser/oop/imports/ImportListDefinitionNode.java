package abs.compiler.parser.oop.imports;

import abs.compiler.parser.AbstractNode;
import abs.compiler.parser.Node;

public class ImportListDefinitionNode extends AbstractNode {

    public ImportListDefinitionNode(Node parent) {
        setParent(parent);
        parent.addChild(this);
    }

    @Override
    public String toString() {
        return "IMPORT LIST";
    }
}
