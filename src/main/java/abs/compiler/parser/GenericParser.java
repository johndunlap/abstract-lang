package abs.compiler.parser;

import abs.ImplementMeException;
import abs.compiler.Options;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;

import java.util.ArrayList;
import java.util.List;

// TODO: Make this class abstract
public class GenericParser implements Parser {

    protected final TokenStream tokenStream;

    protected final Options options;

    public GenericParser(TokenStream tokenStream, Options options) {
        this.tokenStream = tokenStream;
        this.options = options;
    }

    public Token matchSingle(Type type) throws ParseErrorException {
        return new TokenParser(tokenStream, options).match(type).tokens().get(0);
    }

    public TokenParser match(Type type) throws ParseErrorException {
        return new TokenParser(tokenStream, options).match(type);
    }

    public TokenParser match(Type type, String value) throws ParseErrorException {
        return new TokenParser(tokenStream, options).match(type, value);
    }

    // TODO: This method should be deleted when ths class becomes abstract
    @Override
    public Node parse(Node parent) {
        throw new ImplementMeException();
    }

    /**
     * The purpose of this class is to capture a list of tokens from a single parsing operation.
     */
    public static class TokenParser {
        private final TokenStream tokenStream;

        private final Options options;

        private final List<Token> tokens = new ArrayList<>();

        public TokenParser(TokenStream tokenStream, Options options) {
            this.tokenStream = tokenStream;
            this.options = options;
        }

        public TokenParser match(Type type) throws ParseErrorException {
            Token peek = tokenStream.consumeWhitespace().peek();

            if (peek.hasType(type)) {
                tokens.add(tokenStream.next());
                return this;
            }

            throw new ParseErrorException("Expected " + type + " but got " + peek.getType(), tokens);
        }

        public TokenParser match(Type type, String value) throws ParseErrorException {
            Token peek = tokenStream.consumeWhitespace().peek();

            if (peek.hasType(type) && peek.getValue().equals(value)) {
                tokens.add(tokenStream.next());
                return this;
            }

            throw new ParseErrorException("Expected " + type + " with value " + value + " but got " + peek.getType() + " with value " + peek.getValue(), tokens);
        }

        public List<Token> tokens() {
            return tokens;
        }
    }
}
