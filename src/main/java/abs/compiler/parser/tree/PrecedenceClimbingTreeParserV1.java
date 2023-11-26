package abs.compiler.parser.tree;

import static abs.compiler.lexer.Associativity.LEFT;
import static abs.compiler.lexer.Type.EOF;
import static abs.compiler.lexer.Type.RPAREN;
import static abs.compiler.lexer.Type.WHOLE_NUMBER_LITERAL;
import abs.compiler.Options;
import abs.compiler.lexer.Associativity;
import abs.compiler.lexer.Precedence;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;

public class PrecedenceClimbingTreeParserV1 extends AbstractTreeParser {
    public PrecedenceClimbingTreeParserV1(Options options, TokenStream tokenStream) {
        super(options, tokenStream);
    }

    @Override
    public TreeNode parse() {
        return new DoubleNode(computeExpression(0));
    }

    /**
     * An "atom" is a number or a parenthesized expression.
     *
     * @return The value of the atom
     */
    private double computeAtom() {
        Token token = peek();
        Type type = token.getType();

        if (type.equals(Type.LPAREN)) {
            next();

            double value = computeExpression(1);

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

    private double computeExpression(int minimumPrecedence) {
        double lhs = computeAtom();

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

            double rhs = computeExpression(nextMinimumPrecedence);

            lhs = computeOperation(type, lhs, rhs);
        }

        return lhs;
    }

    private double computeOperation(Type type, double left, double right) {
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
                return Math.pow(left, right);
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
