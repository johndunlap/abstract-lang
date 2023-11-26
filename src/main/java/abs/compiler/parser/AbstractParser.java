package abs.compiler.parser;

import static abs.compiler.lexer.Type.WHITESPACE;
import abs.compiler.Options;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;

/**
 * Base class for all parsers.
 */
public abstract class AbstractParser implements Parser {
    protected final TokenStream tokenStream;

    protected final Options options;

    public AbstractParser(TokenStream tokenStream, Options options) {
        this.tokenStream = tokenStream;
        this.options = options;
    }

    /**
     * Returns the next token in the token stream which isn't a whitespace token.
     * @return The next token in the token stream which isn't a whitespace token.
     */
    protected Token next() {
        Token token = tokenStream.next();

        // Skip over any whitespace tokens
        while (token.hasType(WHITESPACE)) {
            token = tokenStream.next();
        }

        return token;
    }

    /**
     * Consumes all whitespace tokens and peeks at the next token in the token stream.
     * @return The next token in the token stream which isn't a whitespace token.
     */
    protected Token peek() {
        Token token = tokenStream.peek();

        // Skip over any whitespace tokens
        while (token.hasType(WHITESPACE)) {
            tokenStream.next();
            token = tokenStream.peek();
        }

        return token;
    }
}
