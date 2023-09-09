package abs.compiler.exception;

import abs.compiler.lexer.Position;
import abs.compiler.lexer.Range;

public class IllegalCharacterException extends LexerException {
    public IllegalCharacterException(Exception e, Range range) {
        super(e, range);
    }

    public IllegalCharacterException(String s, Range range) {
        super(s, range);
    }

    public IllegalCharacterException(Exception e, Position position) {
        super(e, position);
    }

    public IllegalCharacterException(String s, Position position) {
        super(s, position);
    }

    public IllegalCharacterException(char c, Position position) {
        this(
            String.format("Illegal character \"%c\" encountered on line %d column %d position %d",
                c,
                position.getLine(),
                position.getColumn(),
                position.getIndex()
            ),
            position
        );
    }

    public IllegalCharacterException(char c, Position position, String message) {
        this(
            String.format("Illegal character \"%c\" encountered on line %d column %d position %d: %s",
                c,
                position.getLine(),
                position.getColumn(),
                position.getIndex(),
                message
            ),
            position
        );
    }
}
