package abs.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AbstractParseNode implements ParseNode {
    /**
     * Link to the parent node. This will be null for the root node in the tree.
     */
    protected ParseNode parent;

    /**
     * The children of this node. This will be an empty list if this node has no children. This list will never be null.
     */
    protected final List<ParseNode> children = new ArrayList<>();

    protected final String id = 'a' + UUID.randomUUID()
        .toString()
        .replaceAll("-", "");

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

    @Override
    public String getId() {
        // dot tree nodes cannot have a name which begins with a number
        return id;
    }

    public String toDot() {
        // Build the dot node for this node
        StringBuilder dot = new StringBuilder("      ")
            .append(id)
            .append(" [label=\"")
            .append(toString())
            .append("\", shape=circle];\n");

        // Add dot edges to each child node
        for (ParseNode child : children) {
            dot.append("      ")
                .append(id)
                .append(" -> ")
                .append(child.getId())
                .append(";\n");

            dot.append(child.toDot());
        }

        return dot.toString();
    }
}
