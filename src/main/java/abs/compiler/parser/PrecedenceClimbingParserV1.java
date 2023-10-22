package abs.compiler.parser;

import static abs.compiler.lexer.Associativity.LEFT;
import static abs.compiler.lexer.Type.EOF;
import static abs.compiler.lexer.Type.RPAREN;
import static abs.compiler.lexer.Type.WHOLE_NUMBER_LITERAL;
import abs.compiler.lexer.Associativity;
import abs.compiler.lexer.Precedence;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;
import abs.compiler.parser.tree.LongNode;
import abs.compiler.parser.tree.ParseNode;

public class PrecedenceClimbingParserV1 extends AbstractParser {
    public PrecedenceClimbingParserV1(ParserOptions options, TokenStream tokenStream) {
        super(options, tokenStream);
    }

    @Override
    public ParseNode parse() {
        return new LongNode(computeExpression(0));
    }

    private long computeAtom() {
        Token token = peek();
        Type type = token.getType();

        if (type.equals(Type.LPAREN)) {
            next();

            long value = computeExpression(1);

            if (!peek().getType().equals(RPAREN)) {
                throw new RuntimeException("Expected right parenthesis but found " + type + " instead");
            }

            next();
            return value;
        } else if (type.equals(EOF)) {
            throw new RuntimeException("Source ended unexpectedly ");
        } else if (!type.equals(WHOLE_NUMBER_LITERAL)) {
            throw new RuntimeException("Expected whole number literal but found " + type + " instead");
        } else {
            next();
            return Long.parseLong(token.getValue());
        }
    }

    private long computeExpression(int minimumPrecedence) {
        long lhs = computeAtom();

        while (true) {
            Token token = peek();
            Type type = token.getType();
            Precedence precedence = type.getPrecedence();
            Associativity associativity = type.getAssociativity();

            boolean abort = type.equals(EOF) ||
                type.in(WHOLE_NUMBER_LITERAL, RPAREN) ||
                type.getPrecedence().getPrecedence() < minimumPrecedence;

            if (abort) {
                break;
            }

            int nextMinimumPrecedence = precedence.getPrecedence();

            if (associativity.equals(LEFT)) {
                nextMinimumPrecedence += 1;
            }

            next();

            long rhs = computeExpression(nextMinimumPrecedence);

            lhs = computeOperation(type, lhs, rhs);
        }

        return lhs;
    }

    private long computeOperation(Type type, long left, long right) {
        switch (type) {
            case MUL:
                return left * right;
            case DIV:
                return left / right;
            case ADD:
                return left + right;
            case SUB:
                return left - right;
            case XOR:
                return (long) Math.pow((double)left, (double)right);
            default:
                throw new RuntimeException("Unsupported operation " + type + " found");
        }
    }

    private void ignoreWhitespace() {
        while (tokenStream.peek().getType().isIgnore()) {
            tokenStream.next();
        }
    }

    private Token peek() {
        ignoreWhitespace();
        return tokenStream.peek();
    }

    private Token next() {
        ignoreWhitespace();
        return tokenStream.next();
    }
}
