package abs.compiler.parser;

import abs.compiler.archive.parser.ParseError;
import abs.compiler.lexer.Position;
import abs.compiler.lexer.Range;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParseErrorTest {
    @Test
    public void testStringRangeConstructorAndRangeMessageGetters() {
        Range range = new Range(new Position(1, 1, 1), new Position(17, 1, 17));
        String message = "This is an error";
        ParseError parseError = new ParseError(message, range);
        assertEquals(range, parseError.getRange());
        assertEquals(message, parseError.getMessage());
    }
    @Test
    public void testStringRangeBooleanConstructorAndRangeMessageGetters() {
        Range range = new Range(new Position(1, 1, 1), new Position(17, 1, 17));
        String message = "This is an error";

        ParseError parseError = new ParseError(message, range, false);
        assertEquals(range, parseError.getRange());
        assertEquals(message, parseError.getMessage());
        assertFalse(parseError.getFatal());

        parseError = new ParseError(message, range, true);
        assertEquals(range, parseError.getRange());
        assertEquals(message, parseError.getMessage());
        assertTrue(parseError.getFatal());
    }

    @Test
    public void testStringStringPositionConstructorAndPositionMessageGetters() {
        Position position = new Position(1, 1, 1);
        String message = "This is an error";
        ParseError parseError = new ParseError(message, position);
        assertEquals(position, parseError.getPosition());
        assertEquals(message, parseError.getMessage());
    }

    @Test
    public void testStringStringPositionBooleanConstructorAndPositionMessageGetters() {
        Position position = new Position(1, 1, 1);
        String message = "This is an error";

        ParseError parseError = new ParseError(message, position, false);
        assertEquals(position, parseError.getPosition());
        assertEquals(message, parseError.getMessage());
        assertFalse(message, parseError.getFatal());

        parseError = new ParseError(message, position, true);
        assertEquals(position, parseError.getPosition());
        assertEquals(message, parseError.getMessage());
        assertTrue(message, parseError.getFatal());
    }

    @Test
    public void testStringConstructorAndMessageGetter() {
        String message = "This is an error";
        ParseError parseError = new ParseError(message);
        assertEquals(message, parseError.getMessage());
    }

    @Test
    public void testStringConstructorBooleanAndMessageGetter() {
        String message = "This is an error";

        ParseError parseError = new ParseError(message, false);
        assertEquals(message, parseError.getMessage());
        assertFalse(parseError.getFatal());

        parseError = new ParseError(message, true);
        assertEquals(message, parseError.getMessage());
        assertTrue(parseError.getFatal());
    }

    @Test
    public void testFatalGetterSetter() {
        ParseError parseError = new ParseError("This is a test");

        assertFalse(parseError.getFatal());
        parseError.setFatal(true);
        assertTrue(parseError.getFatal());
        parseError.setFatal(false);
        assertFalse(parseError.getFatal());
    }

    @Test
    public void testToString() {
        Range range = new Range(new Position(1, 1, 1), new Position(17, 1, 17));
        String message = "This is an error";
        ParseError parseError = new ParseError(message, range);
        String expected = "ParseError{message='This is an error', range=Range{startColumn=1, stopColumn=17, startLine=1, stopLine=1, startPosition=1, stopPosition=17, length=16}}";
        assertEquals(expected, parseError.toString());
    }
}
