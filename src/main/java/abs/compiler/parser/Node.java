package abs.compiler.parser;

import abs.compiler.lexer.Token;

import java.util.List;

/**
 * This interface represents an individual node in an abstract syntax tree. This could be a node which was successfully
 * parsed from the token stream or an error node which was created when the token stream could not be parsed.
 */
public interface Node {
    /**
     * This id should be unique within the tree. This is necessary for rendering the tree.
     * @return The unique id of this node.
     */
    String getId();

    /**
     * Render the tree from this node downwards in the DOT language. The output of this method can be used to visualize
     * the tree using Graphviz.
     * @return A string in the DOT language which can be used to visualize this tree.
     */
    String toDot();

    /**
     * Returns a list of all tokens which were used to create this node.
     * @return A list of all tokens which were used to create this node.
     */
    List<Token> getTokens();

    /**
     * Returns a string representation of this node.
     * @return A string representation of this node.
     */
    String toString();
}
