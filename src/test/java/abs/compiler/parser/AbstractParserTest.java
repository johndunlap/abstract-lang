package abs.compiler.parser;

import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.LexerOptions;
import abs.compiler.lexer.TokenStream;

public abstract class AbstractParserTest {
    protected static TokenStream buildTokenStream(String input) {
        LexerOptions o = new LexerOptions();
        CharacterStream characterStream = new CharacterStream(input, o);
        return new TokenStream(characterStream, o);
    }
}
