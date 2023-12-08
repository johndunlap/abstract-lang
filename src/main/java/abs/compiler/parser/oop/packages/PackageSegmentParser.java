package abs.compiler.parser.oop.packages;

import static abs.compiler.lexer.Type.IDENTIFIER;
import static abs.compiler.lexer.Type.PERIOD;
import abs.compiler.Options;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;
import abs.compiler.parser.ErrorNode;
import abs.compiler.parser.GenericParser;
import abs.compiler.parser.Node;
import abs.compiler.parser.ParseErrorException;

import java.util.ArrayList;
import java.util.List;

public class PackageSegmentParser extends GenericParser {

    public PackageSegmentParser(TokenStream tokenStream, Options options) {
        super(tokenStream, options);
    }

    public Node parse(Node parent) {
        try {
            List<Token> tokens = match(PERIOD)
                .match(IDENTIFIER)
                .tokens();

            PackageSegmentNode packageSegmentNode = new PackageSegmentNode(parent, tokens);

            // Recursively parse the next package segment if there is one
            if(tokenStream.peek(0).hasType(PERIOD) && tokenStream.peek(1).hasType(IDENTIFIER)) {
                // We don't care about the return value because the result is added to the parent node as a child
                parse(packageSegmentNode);
            }

            return packageSegmentNode;
        } catch (ParseErrorException e) {
            return new ErrorNode(e);
        }
    }
}
