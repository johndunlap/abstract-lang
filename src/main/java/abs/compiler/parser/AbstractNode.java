package abs.compiler.parser;

import abs.ImplementMeException;
import abs.compiler.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNode implements Node {
    /**
     * This is the sequence which is used to generate a unique id for each node.
     */
    private static long SEQUENCE = 0;

    /**
     * The id of this node. Note that this approach is not thread safe but it should be fine provided it is only used
     * by the toDot() method.
     */
    protected final long id = ++SEQUENCE;

    /**
     * The list of tokens that this node represents.
     */
    protected List<Token> tokens;

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
    public String toDot() {
        throw new ImplementMeException();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public abstract String toString();
}
