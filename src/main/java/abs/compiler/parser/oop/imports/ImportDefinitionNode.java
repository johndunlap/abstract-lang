package abs.compiler.parser.oop.imports;

import abs.compiler.lexer.Token;
import abs.compiler.parser.AbstractNode;
import abs.compiler.parser.Node;
import abs.compiler.parser.oop.packages.PackageSegmentNode;

import java.util.List;

public class ImportDefinitionNode extends AbstractNode {
    public ImportDefinitionNode(Node parent, List<Token> tokens) {
        setParent(parent);
        parent.addChild(this);
        this.tokens = tokens;

        // We don't care about the return value because the constructor adds itself to the parent node as a child
        new PackageSegmentNode(this, tokens.get(1));

    }

    @Override
    public String toString() {
        return "IMPORT";
    }
}
