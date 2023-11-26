package abs.compiler.parser.tree;

import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;

public class OopV1TreeParser extends AbstractTreeParser {
    public OopV1TreeParser(Options options, TokenStream tokenStream) {
        super(options, tokenStream);
    }

    @Override
    public RootNode parse() {
        return new RootNode();
    }
}
