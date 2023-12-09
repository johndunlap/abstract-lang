package abs.compiler.parser.oop.imports;

import static abs.compiler.lexer.Type.IMPORT;
import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.GenericParser;
import abs.compiler.parser.Node;

public class ImportListParser extends GenericParser {
    private final ImportParser importParser;

    public ImportListParser(TokenStream tokenStream, Options options) {
        super(tokenStream, options);
        importParser = new ImportParser(tokenStream, options);
    }

    @Override
    public Node parse(Node parent) {
        ImportListDefinitionNode importListDefinitionNode = new ImportListDefinitionNode(parent);

        // If the next token is not an import, then we have an empty import list
        if (!tokenStream.consumeWhitespace().peek().hasType(IMPORT)) {
            return importListDefinitionNode;
        }

        // Otherwise, we have at least one import
        while (tokenStream.consumeWhitespace().peek().hasType(IMPORT)) {
            // We don't care about the return value because the result is added to the parent node as a child
            importParser.parse(importListDefinitionNode);
        }

        return importListDefinitionNode;
    }
}
