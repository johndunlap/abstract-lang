package abs.compiler.archive.parser.graph;

import abs.compiler.archive.parser.ErrorHandler;
import abs.compiler.archive.parser.OldDoNotUseParadigm;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;

public class OldDoNotUseParadigmDeclaration extends GraphObject     {
    private OldDoNotUseParadigm paradigm;
    private String between;
    private boolean hasSemicolon = true;

    public OldDoNotUseParadigmDeclaration(TokenStream tokenStream, ErrorHandler errorHandler) {
        Token paradigmToken = tokenStream.peek(0);

        // What if there was white space prior to the paradigm keyword
        if (paradigmToken.hasType(Type.WHITESPACE)) {
            setBefore(paradigmToken.getValue());
            paradigmToken = tokenStream.eatAndPeek();
        }

        Token whitespaceToken = tokenStream.peek(1);

        if (!whitespaceToken.hasType(Type.WHITESPACE)) {
            errorHandler.addExpectationError("whitespace", whitespaceToken);
        } else {
            between = whitespaceToken.getValue();
        }

        Token paradigmNameToken = tokenStream.peek(2);
        Token semicolonToken = tokenStream.peek(3);

        if (paradigmToken.getValue().equals("paradigm")) {
            String name = paradigmNameToken.getValue();

            if (name.equals("oop")) {
                paradigm = OldDoNotUseParadigm.OOP;
            } else if (name.equals("procedural")) {
                paradigm = OldDoNotUseParadigm.PROCEDURAL;
            } else if (name.equals("functional")) {
                paradigm = OldDoNotUseParadigm.FUNCTIONAL;
                } else {
                errorHandler.addFatalError(
                    "Unsupported paradigm: " + name + ". Supported paradigms are: " + OldDoNotUseParadigm.getLegalParadigmsAsString(),
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

    public OldDoNotUseParadigm getParadigm() {
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
