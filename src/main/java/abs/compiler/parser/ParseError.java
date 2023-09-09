package abs.compiler.parser;

import abs.compiler.lexer.Token;

public class ParseError {
    private final String message;
    private final Token first;
    private final Token last;

    public ParseError(String message, Token first, Token last) {
        this.message = message;
        this.first = first;
        this.last = last;
    }

    public String getMessage() {
        return message;
    }

    public Token getFirst() {
        return first;
    }

    public Token getLast() {
        return last;
    }
}
