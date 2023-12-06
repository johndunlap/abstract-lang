package abs.compiler.parser;

import abs.compiler.lexer.Token;
import abs.compiler.lexer.Type;

import java.util.List;

public interface Parser<T> {
    T parse();
}
