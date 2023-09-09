package abs.compiler.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import abs.compiler.archive.parser.ErrorHandler;
import abs.compiler.archive.parser.ParseError;
import abs.compiler.lexer.Position;
import abs.compiler.lexer.Range;
import org.junit.Test;

public class ErrorHandlerTest {
    @Test
    public void testNullConstructorErrorsGetterAndErrorCountMethods() {
        ErrorHandler errorHandler = new ErrorHandler();
        assertNotNull(errorHandler.getErrors());
        assertEquals(0, errorHandler.errorCount());
        assertEquals(0, errorHandler.fatalErrorCount());
    }

    @Test
    public void testStringAddErrorMethod() {
        ErrorHandler errorHandler = new ErrorHandler();
        String message = "This is an error";
        errorHandler.addError(message);
        assertEquals(1, errorHandler.errorCount());
        assertEquals(message, errorHandler.getErrors().get(0).getMessage());
        assertEquals(0, errorHandler.fatalErrorCount());
    }

    @Test
    public void testStringAddFatalErrorMethod() {
        ErrorHandler errorHandler = new ErrorHandler();

        assertEquals(0, errorHandler.fatalErrorCount());
        String message = "This is an error";
        errorHandler.addFatalError(message);
        assertEquals(1, errorHandler.errorCount());
        assertEquals(message, errorHandler.getErrors().get(0).getMessage());
        assertTrue(errorHandler.getErrors().get(0).getFatal());
        assertEquals(1, errorHandler.fatalErrorCount());
    }

    @Test
    public void testStringRangeAddErrorMethod() {
        ErrorHandler errorHandler = new ErrorHandler();
        String message = "This is an error";
        Range range = new Range(new Position(1, 1, 1), new Position(17, 1, 17));
        errorHandler.addError(message, range);
        assertEquals(1, errorHandler.errorCount());
        assertEquals(message, errorHandler.getErrors().get(0).getMessage());
        assertEquals(range, errorHandler.getErrors().get(0).getRange());
        assertEquals(0, errorHandler.fatalErrorCount());
    }

    @Test
    public void testStringRangeAddFatalErrorMethod() {
        ErrorHandler errorHandler = new ErrorHandler();
        String message = "This is an error";
        Range range = new Range(new Position(1, 1, 1), new Position(17, 1, 17));
        errorHandler.addFatalError(message, range);
        assertEquals(1, errorHandler.errorCount());
        assertEquals(message, errorHandler.getErrors().get(0).getMessage());
        assertEquals(range, errorHandler.getErrors().get(0).getRange());
        assertTrue(errorHandler.getErrors().get(0).getFatal());
        assertEquals(1, errorHandler.fatalErrorCount());
    }

    @Test
    public void testStringPositionAddErrorMethod() {
        ErrorHandler errorHandler = new ErrorHandler();
        String message = "This is an error";
        Position position = new Position(1, 1, 1);
        errorHandler.addError(message, position);
        assertEquals(1, errorHandler.errorCount());
        assertEquals(message, errorHandler.getErrors().get(0).getMessage());
        assertEquals(position, errorHandler.getErrors().get(0).getPosition());
        assertEquals(0, errorHandler.fatalErrorCount());
    }

    @Test
    public void testStringPositionAddFatalErrorMethod() {
        ErrorHandler errorHandler = new ErrorHandler();
        String message = "This is an error";
        Position position = new Position(1, 1, 1);
        errorHandler.addFatalError(message, position);
        assertEquals(1, errorHandler.errorCount());
        assertEquals(message, errorHandler.getErrors().get(0).getMessage());
        assertEquals(position, errorHandler.getErrors().get(0).getPosition());
        assertTrue(errorHandler.getErrors().get(0).getFatal());
        assertEquals(1, errorHandler.fatalErrorCount());
    }

    @Test
    public void testParseErrorAddErrorMethod() {
        ErrorHandler errorHandler = new ErrorHandler();
        ParseError parseError = new ParseError("This is an error");
        errorHandler.addError(parseError);
        assertEquals(1, errorHandler.errorCount());
        assertEquals(parseError, errorHandler.getErrors().get(0));
        assertEquals(0, errorHandler.fatalErrorCount());
    }
}
