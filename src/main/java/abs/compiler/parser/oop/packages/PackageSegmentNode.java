package abs.compiler.parser.oop.packages;

import abs.compiler.lexer.Token;
import abs.compiler.parser.AbstractNode;
import abs.compiler.parser.Node;

import java.util.List;

/**
 * Represents a fully qualified package name. This class can be referenced from import statements, class declarations,
 * package declarations, etc.
 */
public class PackageSegmentNode extends AbstractNode {
    private String name;

    public PackageSegmentNode(Node parent, List<Token> tokens) {
        this.parent = parent;
        this.parent.addChild(this);
        this.name = tokens.get(1).getValue();
        this.tokens = tokens;
    }

    public PackageSegmentNode(Node parent, Token token) {
        this.parent = parent;
        this.parent.addChild(this);
        this.name = token.getValue();
        this.tokens.add(token);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
