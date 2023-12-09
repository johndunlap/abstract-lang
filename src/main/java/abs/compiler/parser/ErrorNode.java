package abs.compiler.parser;

import abs.compiler.lexer.Token;

import javax.lang.model.type.ErrorType;
import java.util.List;

/**
 * When a parser encounters an error, it will return an instance of this class to indicate that parsing has
 * failed.
 */
public class ErrorNode extends AbstractNode {
    private final String message;

    private final ErrorTypeEnum errorType;

    public ErrorNode(ErrorTypeEnum errorType, ParseErrorException e) {
        this.message = e.getMessage();
        this.tokens = e.getError().getTokens();
        this.errorType = errorType;
    }

    /**
     * Create a new error node with the given message and tokens.
     * @param message The error message
     * @param tokens The tokens that caused the error
     */
    public ErrorNode(ErrorTypeEnum errorType, String message, List<Token> tokens) {
        this.message = message;
        this.tokens = tokens;
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public ErrorTypeEnum getErrorType() {
        return errorType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        // TODO: Add line number and column number to error message
        return message;
    }
}
