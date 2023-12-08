package abs.compiler.parser;

import abs.ImplementMeException;
import abs.compiler.lexer.Token;
import abs.compiler.parser.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNode implements Node {
    /**
     * This is the sequence which is used to generate a unique id for each node.
     */
    public static long SEQUENCE = 0;

    /**
     * The id of this node. Note that this approach is not thread safe but it should be fine provided it is only used
     * by the toDot() method.
     */
    protected final long id = ++SEQUENCE;

    /**
     * The list of tokens that this node represents.
     */
    protected List<Token> tokens = new ArrayList<>();

    /**
     * The parent of this node.
     */
    protected Node parent;

    /**
     * The children of this node.
     */
    protected List<Node> children = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getChild(int index) {
        return children.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addChild(Node child) {
        children.add(child);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Node> getChildren() {
        return children;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getId() {
        return String.valueOf(id);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * {@inheritDoc}
     */
    public void addToken(Token token) {
        tokens.add(token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toDot(StringBuilder stringBuilder) {
        // Build the dot node for this node
        stringBuilder.append("      ")
                .append(id)
                .append(" [label=\"")
                .append(toString())
                .append("\", shape=oval];\n");

        // Add dot edges to each child node
        for (Node child : children) {
            stringBuilder.append("      ")
                    .append(id)
                    .append(" -> ")
                    .append(child.getId())
                    .append(";\n");

            child.toDot(stringBuilder);
        }
    }
}
