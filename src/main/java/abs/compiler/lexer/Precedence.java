package abs.compiler.lexer;

public enum Precedence {
    LOWEST(0);

    private final int precedence;

    Precedence(int precedence) {
        this.precedence = precedence;
    }

    public int getPrecedence() {
        return precedence;
    }
}
