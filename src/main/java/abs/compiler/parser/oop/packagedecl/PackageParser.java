package abs.compiler.parser.oop.packagedecl;

import static abs.compiler.lexer.Type.IDENTIFIER;
import static abs.compiler.lexer.Type.PERIOD;
import static abs.compiler.lexer.Type.SEMICOLON;
import abs.ImplementMeException;
import abs.compiler.Options;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.AbstractParser;
import abs.compiler.parser.ErrorNode;
import abs.compiler.parser.Node;

import java.util.ArrayList;
import java.util.List;

public class PackageParser extends AbstractParser {
    public PackageParser(TokenStream tokenStream, Options options) {
        super(tokenStream, options);
    }

    @Override
    public Node parse() {
        // Look ahead at the next token without removing it from the token stream
        Token token = tokenStream.consumeWhitespace().peek();
        List<Token> tokens = new ArrayList<>();

        if (token.hasType(IDENTIFIER) && token.hasValue("package")) {
            tokenStream.consumeWhitespace().next();
            tokens.add(token);

            // Look ahead at the next token without removing it from the token stream
            token = tokenStream.consumeWhitespace().peek();

            if (token.hasType(IDENTIFIER)) {
                tokenStream.consumeWhitespace().next();
                tokens.add(token);

                String packageName = token.getValue();
                PackageDecl rootPackageDecl;

                if (token.getValue() != null || !token.getValue().isEmpty()) {
                    rootPackageDecl = new PackageDecl(packageName);
                } else {
                    // Return an error because we don't have a package name
                    return new ErrorNode("Expected package name but found " + token.toText() + " instead", tokens);
                }

                // Look ahead at the next token without removing it from the token stream
                token = tokenStream.consumeWhitespace().peek();

                PackageDecl previousPackageDecl = rootPackageDecl;

                while (token.hasType(PERIOD)) {
                    tokenStream.consumeWhitespace().next();
                    tokens.add(token);
                    token = tokenStream.consumeWhitespace().peek();
                    tokens.add(token);

                    if (token.hasType(IDENTIFIER)) {
                        tokenStream.consumeWhitespace().next();
                        PackageDecl packageDecl = new PackageDecl(token.getValue());
                        previousPackageDecl.setChild(packageDecl);
                        previousPackageDecl = packageDecl;
                        token = tokenStream.consumeWhitespace().peek();
                    } else {
                        // Return error because we don't have a package name
                        return new ErrorNode("Expected package name but found " + token.toText() + " instead", tokens);
                    }
                }

                tokens.add(token);

                if (token.hasType(SEMICOLON)) {
                    return rootPackageDecl;
                }

                // Return an error because we don't have a semicolon
                return new ErrorNode("Expected \";\" but found " + token.toText() + " instead", tokens);
            }

            tokens.add(token);

            // Return an error because we don't have a package name
            return new ErrorNode("Expected package name but found " + token.toText() + " instead", tokens);
        }

        tokens.add(token);

        // Return an error because we don't have a package declaration
        return new ErrorNode("Expected \"package\" but found " + token.toText() + " instead", tokens);
    }

    @Override
    public void recover(ErrorNode error) {
        throw new ImplementMeException();
    }
}
