package abs.compiler.archive.parser.graph;

import abs.compiler.archive.parser.ErrorHandler;
import abs.compiler.archive.parser.Paradigm;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;

public class ParadigmDeclaration extends GraphObject     {
    private Paradigm paradigm;
    private String between;
    private boolean hasSemicolon = true;

    public ParadigmDeclaration(TokenStream tokenStream, ErrorHandler errorHandler) {
        Token paradigmToken = tokenStream.peek(0);

        // What if there was white space prior to the paradigm keyword
        if (paradigmToken.isType(Type.WHITESPACE)) {
            setBefore(paradigmToken.getValue());
            paradigmToken = tokenStream.eatAndPeek();
        }

        Token whitespaceToken = tokenStream.peek(1);

        if (!whitespaceToken.isType(Type.WHITESPACE)) {
            errorHandler.addExpectationError("whitespace", whitespaceToken);
        } else {
            between = whitespaceToken.getValue();
        }

        Token paradigmNameToken = tokenStream.peek(2);
        Token semicolonToken = tokenStream.peek(3);

        if (paradigmToken.getValue().equals("paradigm")) {
            String name = paradigmNameToken.getValue();

            if (name.equals("oop")) {
                paradigm = Paradigm.OOP;
            } else if (name.equals("procedural")) {
                paradigm = Paradigm.PROCEDURAL;
            } else if (name.equals("functional")) {
                paradigm = Paradigm.FUNCTIONAL;
                } else {
                errorHandler.addFatalError(
                    "Unsupported paradigm: " + name + ". Supported paradigms are: " + Paradigm.getLegalParadigmsAsString(),
                    paradigmNameToken.getRange()
                );
            }

            if (!semicolonToken.getType().equals(Type.SEMICOLON)) {
                errorHandler.addMissingSemicolonError(tokenStream.getPosition());
            }
        } else {
            errorHandler.addFatalExpectationError("paradigm", paradigmToken);
        }
    }

    public Paradigm getParadigm() {
        return paradigm;
    }

    @Override
    public String toString() {
        String s = getBefore() + "paradigm" + between + getParadigm().toString().toLowerCase();

        if (hasSemicolon) {
            s += ';';
        }

        return s;
    }
}
