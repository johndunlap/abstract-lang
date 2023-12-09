package abs.compiler.parser.oop.packages;

import static abs.compiler.lexer.Type.IDENTIFIER;
import static abs.compiler.lexer.Type.PACKAGE;
import static abs.compiler.lexer.Type.SEMICOLON;
import static abs.compiler.parser.ErrorTypeEnum.LEXICAL;
import abs.compiler.Options;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.GenericParser;
import abs.compiler.parser.ErrorNode;
import abs.compiler.parser.Node;
import abs.compiler.parser.ParseErrorException;

import java.util.ArrayList;
import java.util.List;

public class PackageDeclarationParser extends GenericParser {

    private final PackageSegmentParser packageSegmentParser;

    public PackageDeclarationParser(TokenStream tokenStream, Options options) {
        super(tokenStream, options);
        packageSegmentParser = new PackageSegmentParser(tokenStream, options);
    }

    @Override
    public Node parse(Node parent) {
        try {
            List<Token> tokens = match(PACKAGE)
                    .match(IDENTIFIER)
                    .tokens();

            PackageDeclarationNode packageDeclarationNode = new PackageDeclarationNode(parent, tokens);
            packageDeclarationNode.addToken(tokens.get(0));
            Node packageSegment = packageDeclarationNode.getChild(0);

            // We don't care about the return value because the result is added to the parent node as a child
            packageSegmentParser.parse(packageSegment);

            return packageDeclarationNode;
        } catch (ParseErrorException e) {
            return new ErrorNode(LEXICAL, e);
        }
    }
}
