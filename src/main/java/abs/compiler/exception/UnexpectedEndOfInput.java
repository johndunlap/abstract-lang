package abs.compiler.exception;

import abs.compiler.lexer.Position;
import abs.compiler.lexer.Range;

public class UnexpectedEndOfInput extends LexerException {
    public UnexpectedEndOfInput(Exception e, Range range) {
        super(e, range);
    }

    public UnexpectedEndOfInput(String s, Range range) {
        super(s, range);
    }

    public UnexpectedEndOfInput(Exception e, Position position) {
        super(e, position);
    }

    public UnexpectedEndOfInput(String s, Position position) {
        super(s, position);
    }

    public UnexpectedEndOfInput(Position position) {
        this(String.format("Unexpected end of input at index %d", position.getIndex()), position);
    }
}
