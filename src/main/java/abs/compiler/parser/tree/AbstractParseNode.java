package abs.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

public class AbstractParseNode implements ParseNode {
    /**
     * Link to the parent node. This will be null for the root node in the tree.
     */
    protected ParseNode parent;

    /**
     * The children of this node. This will be an empty list if this node has no children. This list will never be null.
     */
    protected final List<ParseNode> children = new ArrayList<>();

    @Override
    public ParseNode getParent() {
        return parent;
    }

    @Override
    public List<ParseNode> getChildren() {
        return children;
    }

    @Override
    public void addChild(ParseNode child) {
        children.add(child);
    }

    @Override
    public void setParent(ParseNode parent) {
        this.parent = parent;
    }
}
