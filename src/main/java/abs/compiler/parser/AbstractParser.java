package abs.compiler.parser;

import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.tree.ParseNode;

public abstract class AbstractParser implements Parser {
    protected final Options options;

    protected final TokenStream tokenStream;

    public AbstractParser(Options options, TokenStream tokenStream) {
        this.options = options;
        this.tokenStream = tokenStream;
    }

    public ParseNode parse(TokenStream tokenStream, Options options) {
        return parse();
    }

    public abstract ParseNode parse();
}
