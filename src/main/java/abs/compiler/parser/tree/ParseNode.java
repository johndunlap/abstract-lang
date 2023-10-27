package abs.compiler.parser.tree;

import java.util.List;

public interface ParseNode {
    /**
     * Guaranteed to be non-null unless this is the root node.
     *
     * @return The parent node or null if this is the root node.
     */
    ParseNode getParent();

    /**
     * Returns a list of all children of this node. This list is
     * guaranteed to be non-null when this node has no children.
     *
     * @return A list of all children of this node.
     */
    List<ParseNode> getChildren();

    /**
     * Adds a child to this node.
     *
     * @param child The child to add.
     */
    void addChild(ParseNode child);

    /**
     * Sets the parent of this node.
     *
     * @param parent The parent of this node.
     */
    void setParent(ParseNode parent);

    /**
     * This id should be unique within the tree.
     * @return The id of this node.
     */
    String getId();

    /**
     * Returns a string in the DOT language which can be used to visualize this tree.
     * @return A string in the DOT language which can be used to visualize this tree.
     */
    String toDot();

    /**
     * Returns a string representation of this node.
     * @return A string representation of this node.
     */
    String toString();
}
