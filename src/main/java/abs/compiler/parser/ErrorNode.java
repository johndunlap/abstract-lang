package abs.compiler.parser;

import abs.compiler.lexer.Token;

import java.util.List;

/**
 * When a parser encounters an error, it will return an instance of this class to indicate that parsing has
 * failed.
 */
public class ErrorNode extends AbstractNode {
    private final String message;

    /**
     * Create a new error node with the given message and tokens.
     * @param message The error message
     * @param tokens The tokens that caused the error
     */
    public ErrorNode(String message, List<Token> tokens) {
        this.message = message;
        this.tokens = tokens;
    }

    public String getMessage() {
        return message;
    }
}
