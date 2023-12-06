package abs.compiler.parser;

public interface Parser {

    /**
     * Parse the current token stream into a {@link Node}. Created nodes should be added to the parent node.
     * @param parent The parent node to which the created node should be added.
     * @return The child node which was created by this parser.
     */
    Node parse(Node parent);
}
