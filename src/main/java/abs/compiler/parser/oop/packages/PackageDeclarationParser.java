package abs.compiler.parser.oop.packages;

import static abs.compiler.lexer.Type.IDENTIFIER;
import static abs.compiler.lexer.Type.SEMICOLON;
import abs.compiler.Options;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.GenericParser;
import abs.compiler.parser.ErrorNode;
import abs.compiler.parser.Node;

import java.util.ArrayList;
import java.util.List;

public class PackageDeclarationParser extends GenericParser {

    private final PackageSegmentParser packageSegmentParser;

    public PackageDeclarationParser(TokenStream tokenStream, Options options) {
        super(tokenStream, options);
        packageSegmentParser = new PackageSegmentParser(tokenStream, options);
    }

    public Node parse() {
        // Look ahead at the next token without removing it from the token stream
        Token token = tokenStream.consumeWhitespace().peek();
        List<Token> tokens = new ArrayList<>();

        if (token.hasType(IDENTIFIER) && token.hasValue("package")) {
            tokenStream.consumeWhitespace().next();
            tokens.add(token);
            token = tokenStream.consumeWhitespace().peek();

            PackageDeclaration packageDeclaration;

            // Invoke PackageSegmentParser here
            Node node = packageSegmentParser.parse();

            if (node instanceof PackageSegment) {
                packageDeclaration = new PackageDeclaration((PackageSegment) node);

                if (token.hasType(SEMICOLON)) {
                    return packageDeclaration;
                }

                // Return an error because we don't have a semicolon
                return new ErrorNode("Expected \";\" but found " + token.toText() + " instead", tokens);
            } else if (node instanceof ErrorNode) {
                // TODO: This is not returning the correct list of tokens. I need a better solution for capturing
                //  parsed tokens.....
                return node;
            }

            tokens.add(token);

            // Return an error because we don't have a package name
            return new ErrorNode("Expected package name but found " + token.toText() + " instead", tokens);
        }

        tokens.add(token);

        // Return an error because we don't have a package declaration
        return new ErrorNode("Expected \"package\" but found " + token.toText() + " instead", tokens);
    }
}
