package abs.compiler.parser.tree;

import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.tree.TreeParser;
import abs.compiler.parser.tree.TreeNode;

public abstract class AbstractTreeParser implements TreeParser {
    protected final Options options;

    protected final TokenStream tokenStream;

    public AbstractTreeParser(Options options, TokenStream tokenStream) {
        this.options = options;
        this.tokenStream = tokenStream;
    }

    public TreeNode parse(TokenStream tokenStream, Options options) {
        return parse();
    }

    public abstract TreeNode parse();
}
