package abs.compiler.parser;

import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.tree.RootNode;

public class OopV1Parser extends AbstractParser {
    public OopV1Parser(ParserOptions options, TokenStream tokenStream) {
        super(options, tokenStream);
    }

    @Override
    public RootNode parse() {
        return new RootNode();
    }
}
