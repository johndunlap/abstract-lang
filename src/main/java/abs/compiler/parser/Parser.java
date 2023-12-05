package abs.compiler.parser;

import abs.compiler.Options;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens = new ArrayList<>();

    private final TokenStream tokenStream;

    private Options options;

    public Parser(TokenStream tokenStream, Options options) {
        this.tokenStream = tokenStream;
        this.options = options;
    }

    public Parser match(Type type) throws ParseErrorException {
        Token peek = tokenStream.consumeWhitespace().peek();

        if (peek.hasType(type)) {
            tokens.add(tokenStream.next());
            return this;
        }

        throw new ParseErrorException("Expected " + type + " but got " + peek.getType());
    }

    public Parser match(Type type, String value) throws ParseErrorException {
        Token peek = tokenStream.consumeWhitespace().peek();

        if (peek.hasType(type) && peek.getValue().equals(value)) {
            tokens.add(tokenStream.next());
            return this;
        }

        throw new ParseErrorException("Expected " + type + " with value " + value + " but got " + peek.getType() + " with value " + peek.getValue());
    }

    public List<Token> tokens() {
        return tokens;
    }
}
