package abs.compiler.exception;

import abs.compiler.lexer.Position;
import abs.compiler.lexer.Range;

public class IllegalStateException extends LexerException {
    public IllegalStateException(Exception e, Range range) {
        super(e, range);
    }

    public IllegalStateException(String s, Range range) {
        super(s, range);
    }

    public IllegalStateException(Exception e, Position position) {
        super(e, position);
    }

    public IllegalStateException(String s, Position position) {
        super(s, position);
    }
}
