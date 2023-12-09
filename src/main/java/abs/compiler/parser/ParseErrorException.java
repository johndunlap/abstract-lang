package abs.compiler.parser;

import abs.compiler.lexer.Token;

import java.util.List;

public class ParseErrorException extends Exception {
    private ErrorNode error;

    public ParseErrorException(ErrorNode error) {
        super(error.getMessage());
        this.error = error;
    }

    public ParseErrorException(ErrorTypeEnum errorType, String message, List<Token> tokens) {
        this(new ErrorNode(errorType, message, tokens));
    }

    public ErrorNode getError() {
        return error;
    }
}
