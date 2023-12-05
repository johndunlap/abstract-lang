package abs.compiler.parser;

import abs.compiler.lexer.Token;
import abs.compiler.lexer.Type;

import java.util.List;

public interface Parser {
    Parser match(Type type) throws ParseErrorException;

    Parser match(Type type, String value) throws ParseErrorException;
}
