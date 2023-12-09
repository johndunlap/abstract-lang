package abs.compiler.parser.oop;

import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.GenericParser;
import abs.compiler.parser.Node;
import abs.compiler.parser.oop.imports.ImportListParser;
import abs.compiler.parser.oop.packages.PackageDeclarationParser;

public class OopRootParser extends GenericParser {
    public OopRootParser(TokenStream tokenStream, Options options) {
        super(tokenStream, options);
    }

    @Override
    public Node parse(Node parent) {
        // The parent node should always be an instance of RootNode
        Node rootNode = new OopRootNode(parent);

        // The paradigm declaration has already been parsed by the root parser, so we can proceed with parsing the
        // package declaration here
        Node packageDeclarationNode = new PackageDeclarationParser(tokenStream, options).parse(rootNode);

        // Parse the import list. The import list should be a single node that contains a list of import nodes.
        Node importListNode = new ImportListParser(tokenStream, options).parse(rootNode);

        // TODO: Parse the class declaration. At least initially, we will only support a single class
        //  declaration per file.

        // TODO: Parse variable declaration list. The variable declaration list should be a single node that contains a
        //  list of variable declarations. For the time being, variables must be declared BEFORE functions.

        // TODO: Parse function declaration list. The function declaration list should be a single node that contains a
        //  list of function declarations. For the time being, functions must be declared AFTER variables.

        return rootNode;
    }
}
