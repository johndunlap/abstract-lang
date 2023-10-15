package abs.compiler.lexer;

public enum Precedence {
    LOWEST(1_000),
    HIGHEST(1_000_000);

    private final int precedence;

    Precedence(int precedence) {
        this.precedence = precedence;
    }

    public int getPrecedence() {
        return precedence;
    }
}
