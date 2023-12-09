package abs.compiler.parser;

import static abs.compiler.parser.paradigms.ParadigmEnum.OOP;
import abs.ImplementMeException;
import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.oop.OopRootParser;
import abs.compiler.parser.paradigms.ParadigmDeclarationNode;
import abs.compiler.parser.paradigms.ParadigmParser;

public class RootParser extends GenericParser {
    private final ParadigmParser paradigmParser;

    public RootParser(TokenStream tokenStream, Options options) {
        super(tokenStream, options);
        this.paradigmParser = new ParadigmParser(tokenStream, options);
    }

    public RootNode parse() {
        return (RootNode) parse(null);
    }

    @Override
    public Node parse(Node parent) {
        Node rootNode = new RootNode();

        Node paradigmNode = paradigmParser.parse(parent);
        rootNode.addChild(paradigmNode);

        if (paradigmNode instanceof ErrorNode) {
            if (options.getDefaultParadigm() != null) {
                // This is necessary for the unit tests to pass because is incremented by the error node which
                // was discarded
                AbstractNode.SEQUENCE--;

                // Leave the token stream empty because the paradigm wasn't taken from the token stream.
                paradigmNode = new ParadigmDeclarationNode(options.getDefaultParadigm());
                paradigmNode.setParent(rootNode);
                rootNode.getChildren().set(0, paradigmNode);
            } else {
                // It's not possible to parse the rest of the token stream without a paradigm declaration.
                return rootNode;
            }
        }

        ParadigmDeclarationNode paradigmDeclaration = (ParadigmDeclarationNode) paradigmNode;

        if (paradigmDeclaration.getParadigm() == OOP) {
            // Treat the remainder of the token stream as OOP
            OopRootParser oopRootParser = new OopRootParser(tokenStream, options);
            oopRootParser.parse(rootNode);
        } else {
            // TODO: As a general rule we shouldn't throw exceptions but, in practice, this shouldn't happen. Even so,
            //  this should be replaced with an error node.
            throw new ImplementMeException();
        }

        return rootNode;
    }
}
