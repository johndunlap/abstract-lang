package abs.compiler.parser.oop.imports;

import static abs.compiler.lexer.Type.IDENTIFIER;
import static abs.compiler.lexer.Type.IMPORT;
import static abs.compiler.parser.ErrorTypeEnum.LEXICAL;
import abs.compiler.Options;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.ErrorNode;
import abs.compiler.parser.ErrorTypeEnum;
import abs.compiler.parser.GenericParser;
import abs.compiler.parser.Node;
import abs.compiler.parser.ParseErrorException;
import abs.compiler.parser.oop.packages.PackageSegmentParser;

import java.util.List;

public class ImportParser extends GenericParser {
    private final PackageSegmentParser packageSegmentParser;

    public ImportParser(TokenStream tokenStream, Options options) {
        super(tokenStream, options);
        packageSegmentParser = new PackageSegmentParser(tokenStream, options);
    }

    @Override
    public Node parse(Node parent) {
        try {
            List<Token> tokens = match(IMPORT)
                .match(IDENTIFIER)
                .tokens();

            ImportDefinitionNode importDefinitionNode = new ImportDefinitionNode(parent, tokens);
            Node packageSegment = importDefinitionNode.getChild(0);

            // We don't care about the return value because the result is added to the parent node as a child
            packageSegmentParser.parse(packageSegment);

            return importDefinitionNode;
        } catch (ParseErrorException e) {
            return new ErrorNode(LEXICAL, e);
        }
    }
}
