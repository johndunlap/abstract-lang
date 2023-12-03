package abs.compiler.parser;

import abs.compiler.lexer.Token;
import abs.compiler.lexer.Type;

import java.util.List;

public interface IParser {
    IParser match(Type type) throws ParseErrorException;

    IParser match(Type type, String value) throws ParseErrorException;

    List<Token> tokens();
}
