package abs.compiler.parser.tree;

import abs.compiler.lexer.Token;

import java.util.Collections;
import java.util.List;

/**
 * This class represents an error which occurred during parsing. It is added to the parse
 * tree so that a parse tree can be generated even if there are errors, without losing
 * track of the errors.
 */
public class ErrorNode extends AbstractParseNode {
    /**
     * The error message which was emitted by the parser.
     */
    private final String message;

    /**
     * This is a list because once we encounter an error we have to skip tokens until we reach a recovery point.
     */
    private final List<Token> tokens;

    public ErrorNode(String message, Token token) {
        this(message, Collections.singletonList(token));
    }

    public ErrorNode(String message, List<Token> tokens) {
        this.message = message;
        this.tokens = tokens;
    }

    public String getMessage() {
        return message;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
