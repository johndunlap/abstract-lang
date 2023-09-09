package abs.compiler.archive.parser.graph;

import abs.compiler.exception.LexerException;
import abs.compiler.lexer.TokenStream;
import abs.compiler.archive.parser.ErrorHandler;

public class SourceFile {
    /**
     * The paradigm of this source file.
     */
    private ParadigmDeclaration paradigmDeclaration;

    /**
     * The package(namespace) in which this source file was declared.
     */
    private PackageDeclaration packageDeclaration;

    /**
     * Create a new source file instance and attempt to populate it from the token stream.
     * @param tokenStream The source of tokens which will be used
     * @param errorHandler Any errors which may be encountered should be added here
     * @throws LexerException Thrown when something exceptional happens
     */
    public SourceFile(TokenStream tokenStream, ErrorHandler errorHandler) {
        paradigmDeclaration = new ParadigmDeclaration(tokenStream, errorHandler);

        // Don't proceed unless the paradigm statement was successfully parsed
        if (errorHandler.fatalErrorCount() == 0) {
            packageDeclaration = new PackageDeclaration(tokenStream, errorHandler);
        }
    }

    public ParadigmDeclaration getParadigmDeclaration() {
        return paradigmDeclaration;
    }

    public PackageDeclaration getPackageDeclaration() {
        return packageDeclaration;
    }
}
