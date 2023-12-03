package abs.compiler.parser;

import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;

/**
 * Base class for all parsers.
 */
public abstract class AbstractParser implements IParser {
    protected final TokenStream tokenStream;

    protected final Options options;

    public AbstractParser(TokenStream tokenStream, Options options) {
        this.tokenStream = tokenStream;
        this.options = options;
    }
}
