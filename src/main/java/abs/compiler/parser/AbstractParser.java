package abs.compiler.parser;

import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.tree.ParseNode;

public abstract class AbstractParser implements Parser {
    protected final ParserOptions options;

    protected final TokenStream tokenStream;

    public AbstractParser(ParserOptions options, TokenStream tokenStream) {
        this.options = options;
        this.tokenStream = tokenStream;
    }

    public ParseNode parse(TokenStream tokenStream, ParserOptions options) {
        return parse();
    }

    public abstract ParseNode parse();
}
