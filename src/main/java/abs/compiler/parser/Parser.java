package abs.compiler.parser;

import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.tree.ParseNode;

public interface Parser {
    ParseNode parse(TokenStream tokenStream, ParserOptions options);
}
