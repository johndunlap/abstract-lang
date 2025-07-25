package abs.compiler.parser.paradigms;

import abs.compiler.lexer.Token;
import abs.compiler.parser.AbstractNode;

import java.util.List;

public class ParadigmDeclarationNode extends AbstractNode {
    private ParadigmEnum paradigm;

    public ParadigmDeclarationNode(ParadigmEnum paradigm) {
        this.paradigm = paradigm;
    }

    public ParadigmDeclarationNode(List<Token> tokens) {
        this.paradigm = ParadigmEnum.findByName(tokens.get(1).getValue());
        this.tokens = tokens;
    }

    public ParadigmEnum getParadigm() {
        return paradigm;
    }

    public void setParadigm(ParadigmEnum paradigm) {
        this.paradigm = paradigm;
    }

    @Override
    public String toString() {
        return "PARADIGM " + paradigm.name();
    }
}
