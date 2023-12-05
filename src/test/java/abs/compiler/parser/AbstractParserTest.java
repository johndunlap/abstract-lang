package abs.compiler.parser;

import static org.junit.Assert.fail;
import abs.compiler.Options;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.TokenStream;

public abstract class AbstractParserTest {
    protected static TokenStream buildTokenStream(String input, Options options) {
        CharacterStream characterStream = new CharacterStream(input, options);
        return new TokenStream(characterStream, options);
    }

    protected static TokenStream buildTokenStream(String input) {
        Options options = new Options();
        return buildTokenStream(input, options);
    }

    protected void assertNotError(Node node) {
        if (node instanceof ErrorNode) {
            fail(((ErrorNode) node).getMessage());
        }
    }
}
