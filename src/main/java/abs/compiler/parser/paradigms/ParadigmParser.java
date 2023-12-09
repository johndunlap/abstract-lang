package abs.compiler.parser.paradigms;

import static abs.compiler.Util.coalesce;
import static abs.compiler.lexer.Type.IDENTIFIER;
import static abs.compiler.lexer.Type.PARADIGM;
import static abs.compiler.lexer.Type.SEMICOLON;
import abs.compiler.Options;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.GenericParser;
import abs.compiler.parser.ErrorNode;
import abs.compiler.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for parsing the paradigm declaration which may appear at the beginning of an ABS file. If no paradigm is
 * declared, the default paradigm is assumed.
 */
public class ParadigmParser extends GenericParser {
    public ParadigmParser(TokenStream tokenStream, Options options) {
        super(tokenStream, options);
    }

    @Override
    public Node parse(Node parent) {
        // Look ahead at the next token without removing it from the token stream
        Token token = tokenStream.eatWhitespace().peek();
        List<Token> tokens = new ArrayList<>();

        // If the next token is an identifier, and it has the value "paradigm", then we have a paradigm declaration
        if (token.hasType(PARADIGM)) {
            // Remove the "paradigm" token from the token stream
            tokenStream.eatWhitespace().next();
            tokens.add(token);

            // Look ahead at the next token without removing it from the token stream
            token = tokenStream.eatWhitespace().peek();

            // If the next token is an identifier, then we need to check it to see if it is a valid paradigm name
            if (token.hasType(IDENTIFIER)) {
                // Attempt to resolve the identifier to a paradigm name
                ParadigmEnum paradigm = ParadigmEnum.findByName(token.getValue());

                // Was a valid paradigm name found?
                if (paradigm != null) {
                    tokenStream.eatWhitespace().next();
                    tokens.add(token);

                    // Look ahead at the next token without removing it from the token stream
                    token = tokenStream.eatWhitespace().peek();

                    // If the next token is a semicolon, then we have a valid paradigm declaration
                    if (token.hasType(SEMICOLON)) {
                        // Remove the semicolon token from the token stream
                        tokenStream.eatWhitespace().next();
                        tokens.add(token);

                        // Return the paradigm declaration node
                        return new ParadigmDeclarationNode(tokens);
                    }

                    tokens.add(token);

                    // Return a parse error because a semicolon was not found
                    return new ErrorNode("Expected \";\" but found \"" + coalesce(token.getValue(), token.getType().name()) + "\" instead", tokens);
                }

                tokens.add(token);

                // Return a parse error because a valid paradigm name was not found
                return new ErrorNode("\"" + token.getValue() + "\" is not a valid paradigm", tokens);
            }

            tokens.add(token);

            // Return a parse error because an identifier was not found
            return new ErrorNode("Expected identifier but found \"" + token.getValue() + "\" instead", tokens);
        }

        // Add the token to the list of tokens that caused the error despite it not being
        // removed from the token stream
        tokens.add(token);

        // Return a parse error if no paradigm declaration was found
        return new ErrorNode("Expected \"paradigm\" but found \"" + token.getValue() + "\" instead", tokens);
    }
}
