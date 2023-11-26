package abs.compiler.parser;

public interface Parser {
    /**
     * Parses the token stream and returns a node representing the object which was parsed.
     * @return The node representing the object which was parsed or an error node if parsing failed.
     */
    Node parse();

    /**
     * Recovers from an error in the parser. Generally speaking, recovery consists of skipping tokens until the token
     * stream is in a state where parsing can resume. The error node which was returned by the parser will be left
     * behind in the abstract syntax tree.
     * @param error The error which was encountered by the parser.
     */
    void recover(ErrorNode error);
}
