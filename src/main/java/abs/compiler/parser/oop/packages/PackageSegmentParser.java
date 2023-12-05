package abs.compiler.parser.oop.packages;

import abs.compiler.Options;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;
import abs.compiler.parser.AbstractParser;
import abs.compiler.parser.Node;
import abs.compiler.parser.ParseErrorException;
import abs.compiler.parser.Parser;

import java.util.List;

public class PackageSegmentParser extends AbstractParser {

    public PackageSegmentParser(TokenStream tokenStream, Options options) {
        super(tokenStream, options);
    }

    public Node parse() {
/*
        if (token.hasType(IDENTIFIER)) {
            tokenStream.consumeWhitespace().next();
            tokens.add(token);

            String packageName = token.getValue();
            PackageDeclaration rootPackageDeclaration;

            if (token.getValue() != null || !token.getValue().isEmpty()) {
                rootPackageDeclaration = new PackageDeclaration(packageName);
            } else {
                // Return an error because we don't have a package name
                return new ErrorNode("Expected package name but found " + token.toText() + " instead", tokens);
            }

            // Look ahead at the next token without removing it from the token stream
            token = tokenStream.consumeWhitespace().peek();

            PackageDeclaration previousPackageDeclaration = rootPackageDeclaration;

            while (token.hasType(PERIOD)) {
                tokenStream.consumeWhitespace().next();
                tokens.add(token);
                token = tokenStream.consumeWhitespace().peek();
                tokens.add(token);

                if (token.hasType(IDENTIFIER)) {
                    tokenStream.consumeWhitespace().next();
                    PackageSegment packageSegment = new PackageSegment(token.getValue());
                    previousPackageDeclaration.setPackageName(packageDeclaration);
                    previousPackageDeclaration = packageDeclaration;
                    token = tokenStream.consumeWhitespace().peek();
                } else {
                    // Return error because we don't have a package name
                    return new ErrorNode("Expected package name but found " + token.toText() + " instead", tokens);
                }
            }

            tokens.add(token);
        }

*/
        return null;
    }
}
