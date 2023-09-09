package abs.compiler.exception;

import abs.compiler.lexer.Position;
import abs.compiler.lexer.Range;

public class AbsException extends RuntimeException {
    private Range range;
    private Position position;

    /**
     * This is necessary in cases where positional information is not available.
     */
    public AbsException(String message) {
        super(message);
    }

    public AbsException(Exception e, Range range) {
        super(e);
        this.range = range;
    }

    public AbsException(String s, Range range) {
        super(s);
        this.range = range;
    }

    public AbsException(Exception e, Position position) {
        super(e);
        this.position = position;
    }

    public AbsException(String s, Position position) {
        super(s);
        this.position = position;
    }

    public Range getRange() {
        return range;
    }

    public Position getPosition() {
        return position;
    }
}
