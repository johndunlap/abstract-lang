package abs.compiler.exception;

import abs.compiler.lexer.Position;
import abs.compiler.lexer.Range;

public class LexerException extends AbsException {
    private Range range;

    public LexerException(Exception e, Range range) {
        super(e, range);
    }
    public LexerException(String s, Range range) {
        super(s, range);
    }

    public LexerException(Exception e, Position position) {
        super(e, position);
    }
    public LexerException(String s, Position position) {
        super(s, position);
    }
}
