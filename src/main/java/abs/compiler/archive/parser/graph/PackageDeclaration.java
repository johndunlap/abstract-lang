package abs.compiler.archive.parser.graph;

import abs.compiler.archive.parser.ErrorHandler;
import abs.compiler.lexer.TokenStream;
import abs.compiler.model.Namespace;

public class PackageDeclaration {
    /**
     * The namespace identified by the package declaration.
     */
    private Namespace namespace;

    public PackageDeclaration(TokenStream tokenStream, ErrorHandler errorHandler) {
    }

    public Namespace getNamespace() {
        return namespace;
    }
}
