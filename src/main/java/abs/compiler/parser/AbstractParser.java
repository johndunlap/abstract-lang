package abs.compiler.parser;

import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.tree.TreeNode;

public abstract class AbstractParser implements Parser {
    protected final Options options;

    protected final TokenStream tokenStream;

    public AbstractParser(Options options, TokenStream tokenStream) {
        this.options = options;
        this.tokenStream = tokenStream;
    }

    public TreeNode parse(TokenStream tokenStream, Options options) {
        return parse();
    }

    public abstract TreeNode parse();
}
