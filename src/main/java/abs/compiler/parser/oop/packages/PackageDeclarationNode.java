package abs.compiler.parser.oop.packages;

import abs.compiler.lexer.Token;
import abs.compiler.parser.AbstractNode;
import abs.compiler.parser.Node;

import java.util.List;

public class PackageDeclarationNode extends AbstractNode {
    private String name;

    public PackageDeclarationNode(Node parent, List<Token> tokens) {
        this.parent = parent;

        // We don't care about the return value because the constructor adds itself to the parent node as a child
        new PackageSegmentNode(this, tokens.get(1));
    }

    @Override
    public String toString() {
        return "PACKAGE";
    }
}
