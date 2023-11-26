package abs.compiler.parser;

import static org.junit.Assert.fail;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.LexerOptions;
import abs.compiler.lexer.TokenStream;

public abstract class AbstractParserTest {
    protected static TokenStream buildTokenStream(String input) {
        LexerOptions o = new LexerOptions();
        CharacterStream characterStream = new CharacterStream(input, o);
        return new TokenStream(characterStream, o);
    }

    protected void assertNotError(Node node) {
        if (node instanceof ErrorNode) {
            fail(((ErrorNode) node).getMessage());
        }
    }
}
