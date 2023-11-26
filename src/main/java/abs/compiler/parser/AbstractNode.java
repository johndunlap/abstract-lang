package abs.compiler.parser;

import abs.ImplementMeException;
import abs.compiler.lexer.Token;

import java.util.List;

public abstract class AbstractNode implements Node {
    /**
     * This is the sequence which is used to generate a unique id for each node.
     */
    private static long SEQUENCE = 0;

    /**
     * The id of this node.
     */
    protected final long id = ++SEQUENCE;

    /**
     * The list of tokens that this node represents.
     */
    protected List<Token> tokens;
    
    @Override
    public String getId() {
        return id + "";
    }

    @Override
    public String toDot() {
        throw new ImplementMeException();
    }

    @Override
    public List<Token> getTokens() {
        return tokens;
    }
}
