package abs.compiler.archive.parser;

import abs.compiler.lexer.Position;
import abs.compiler.lexer.Range;

public class ParseError {
    private String message;
    private Range range;
    private Position position;
    private boolean fatal = false;

    public ParseError(String message, Range range) {
        this.message = message;
        this.range = range;
    }

    public ParseError(String message, Range range, boolean fatal) {
        this.message = message;
        this.range = range;
        this.fatal = fatal;
    }

    public ParseError(String message, Position position) {
        this.message = message;
        this.position = position;
    }

    public ParseError(String message, Position position, boolean fatal) {
        this.message = message;
        this.position = position;
        this.fatal = fatal;
    }

    public ParseError(String message) {
        this.message = message;
    }

    public ParseError(String message, boolean fatal) {
        this.message = message;
        this.fatal = fatal;
    }

    public String getMessage() {
        return message;
    }

    public Range getRange() {
        return range;
    }

    public Position getPosition() {
        return position;
    }

    public boolean getFatal() {
        return fatal;
    }

    public void setFatal(boolean fatal) {
        this.fatal = fatal;
    }

    @Override
    public String toString() {
        if (range != null) {
            return "ParseError{" +
                "message='" + message + '\'' +
                ", range=" + range +
                '}';
        } else if (position != null) {
            return "ParseError{" +
                "message='" + message + '\'' +
                ", position=" + position +
                '}';
        } else {
            return "ParseError{" +
                "message='" + message + '\'' +
                '}';
        }
    }
}
