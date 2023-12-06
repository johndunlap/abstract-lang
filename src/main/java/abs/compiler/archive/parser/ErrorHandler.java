package abs.compiler.archive.parser;

import abs.compiler.lexer.Position;
import abs.compiler.lexer.Range;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.Type;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class ErrorHandler {
    private List<ParseError> errors = new LinkedList<>();
    private List<ParseError> fatalErrors = new LinkedList<>();

    public void addError(ParseError error) {
        errors.add(error);

        // Track fatal errors separately
        if (error.getFatal()) {
            fatalErrors.add(error);
        }
    }

    public void printErrors() {
        printErrors(System.err);
    }

    public void printErrors(PrintStream printStream) {
        for (ParseError error : errors) {
            printStream.println(error.toString());
        }
    }

    public List<ParseError> getErrors() {
        return errors;
    }

    public int errorCount() {
        return errors.size();
    }

    public int fatalErrorCount() {
        return fatalErrors.size();
    }

    public void addError(String message) {
        addError(new ParseError(message));
    }

    public void addFatalError(String message) {
        addError(new ParseError(message, true));
    }

    public void addError(String message, Range range) {
        addError(new ParseError(message, range));
    }

    public void addFatalError(String message, Range range) {
        addError(new ParseError(message, range, true));
    }

    public void addError(String message, Position position) {
        addError(new ParseError(message, position));
    }

    public void addFatalError(String message, Position position) {
        addError(new ParseError(message, position, true));
    }

    public void addEofError(Position position) {
        addFatalError("Unexpected end of file", position);
    }

    public void addEofError(Range range) {
        addFatalError("Unexpected end of file", new Position(range.getStartPosition(), range.getStartLine(), range.getStartColumn()));
    }

    public void addMissingIdentifierError(Token actual) {
        addMissingIdentifierError(actual, false);
    }

    protected void addFatalMissingIdentifierError(Token actual) {
        addMissingIdentifierError(actual, true);
    }

    protected void addMissingIdentifierError(Token actual, boolean fatal) {
        addError(new ParseError(String.format("Expected identifier but found \"%s\" instead", actual.toText()), fatal));
    }

    public void addMissingSemicolonError(Position position) {
        addMissingSemicolonError(position, false);
    }

    public void addFatalMissingSemicolonError(Position position) {
        addMissingSemicolonError(position, true);
    }

    protected void addMissingSemicolonError(Position position, boolean fatal) {
        addError(new ParseError("Missing semicolon", position, fatal));
    }

    public void addFatalUnsupportedParadigmError(OldDoNotUseParadigm paradigm, Position position) {
        addFatalError(
            "Unsupported paradigm: " + paradigm + ". Supported paradigms are: " + OldDoNotUseParadigm.getLegalParadigmsAsString(),
            position
        );
    }

    public void addFatalExpectationError(String expected, Token actual) {
        if (actual.getType().equals(Type.EOF)) {
            addEofError(actual.getRange());
        } else {
            addExpectationError(expected, actual.toText(), actual.getRange(), true);
        }
    }

    public void addExpectationError(String expected, Token actual) {
        addExpectationError(expected, actual.toText(), actual.getRange(), false);
    }

    protected void addExpectationError(String expected, String actual, Range range, boolean fatal) {
        addError(new ParseError(String.format("Expected \"%s\" but found \"%s\" instead", expected, actual), range, fatal));
    }
}
