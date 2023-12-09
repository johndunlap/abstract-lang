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
        if (!tokenStream.eatWhitespace().peek().hasType(IMPORT)) {
            return importListDefinitionNode;
        }

        // TODO: This isn't good enough. This stops parsing imports the first time it encounters a broken import
        //  statement rather than recovering, leaving an error node behind, and parsing the next import statement
        // Otherwise, we have at least one import
        while (tokenStream.eatWhitespace().peek().hasType(IMPORT)) {
            // We don't care about the return value because the result is added to the parent node as a child
            importParser.parse(importListDefinitionNode);
        }

        return importListDefinitionNode;
    }
}
