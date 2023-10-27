package abs.compiler.parser;

import static abs.compiler.lexer.Associativity.LEFT;
import static abs.compiler.lexer.Type.EOF;
import static abs.compiler.lexer.Type.RPAREN;
import static abs.compiler.lexer.Type.WHOLE_NUMBER_LITERAL;
import abs.compiler.lexer.Associativity;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.LexerOptions;
import abs.compiler.lexer.Precedence;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;
import abs.compiler.parser.tree.ParseNode;
import abs.compiler.parser.tree.RootNode;
import abs.compiler.parser.tree.StringNode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PrecedenceClimbingParserV2 extends AbstractParser {
    public PrecedenceClimbingParserV2(ParserOptions options, TokenStream tokenStream) {
        super(options, tokenStream);
    }

    @Override
    public ParseNode parse() {
        return new RootNode(parseExpression(0));
    }

    /**
     * An "atom" is a number or a parenthesized expression.
     *
     * @return The value of the atom
     */
    private ParseNode parseAtom() {
        Token token = peek();
        Type type = token.getType();

        if (type.equals(Type.LPAREN)) {
            next();

            ParseNode node = parseExpression(1);

            if (!peek().getType().equals(RPAREN)) {
                throw new RuntimeException("Expected right parenthesis but found " + type + " instead");
            }

            next();
            return node;
        } else if (type.equals(EOF)) {
            throw new RuntimeException("Source ended unexpectedly ");
        } else if (!type.equals(WHOLE_NUMBER_LITERAL)) {
            throw new RuntimeException("Expected whole number literal but found " + type + " instead");
        } else {
            next();
            return new StringNode(token.getValue());
        }
    }

    private ParseNode parseExpression(int minimumPrecedence) {
        ParseNode lhs = parseAtom();

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

            ParseNode rhs = parseExpression(nextMinimumPrecedence);

            lhs = new StringNode(type.getRepresentation(), lhs, rhs);
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

    public static void main(String[] args) {
        // Merge all command line arguments into a single string
        String input = String.join("", args);

        // Create a character stream from the input string
        LexerOptions o = new LexerOptions();
        CharacterStream characterStream = new CharacterStream(input, o);

        // Create a token stream from the character stream
        TokenStream tokenStream = new TokenStream(characterStream, o);

        // Create a parser from the token stream
        ParserOptions options = new ParserOptions();
        PrecedenceClimbingParserV2 parser = new PrecedenceClimbingParserV2(options, tokenStream);

        // Parse the expression into a tree
        RootNode result = (RootNode) parser.parse();

        // Write the result to a file named tree.dot
        String dot = result.toDot();
        System.out.println(dot);
        toFile(dot, "tree.dot");
    }

    public static void toFile(String contents, String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(contents);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            // Re-throw the exception as a runtime exception
            throw new RuntimeException("An error occurred while writing to the file.", e);
        }
    }
}
