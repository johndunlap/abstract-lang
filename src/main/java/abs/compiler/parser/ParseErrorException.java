package abs.compiler.parser;

import java.util.List;

public class ParseErrorException extends Exception {
    private List<ErrorNode> errors;

    public ParseErrorException(String message) {
        super(message);
    }

    public ParseErrorException(List<ErrorNode> errors) {
        this.errors = errors;
    }

    public ParseErrorException(ErrorNode error) {
        this.errors = List.of(error);
    }

    public List<ErrorNode> getErrors() {
        return errors;
    }
}
