package abs.compiler.lexer;

import static abs.compiler.lexer.Associativity.LEFT;
import static abs.compiler.lexer.Precedence.LOWEST;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents the different types of tokens that the lexer can produce. This is an enum for performance
 * reasons. In many languages this will allow parsing to be based on a comparison which is faster than a string match.
 * Also, some tokens cannot be matched based on string comparisons. For example, all integer numbers will have the same
 * token type but different text representations.
 *
 * TODO: Many of these token types have the wrong precedence and associativity. For some of them, it is not applicable
 *   and for others it is just wrong and needs to be fixed.
 */
public enum Type {
    DOLLAR("$", LOWEST, LEFT, false),
    EQ("=", LOWEST, LEFT, false),
    LPAREN("(", LOWEST, LEFT, false),
    RPAREN(")", LOWEST, LEFT, false),
    LBRACE("{", LOWEST, LEFT, false),
    RBRACE("}", LOWEST, LEFT, false),
    LBRACKET("[", LOWEST, LEFT, false),
    RBRACKET("]", LOWEST, LEFT, false),
    SEMICOLON(";", LOWEST, LEFT, false),
    COMMA(",", LOWEST, LEFT, false),
    QUESTION("?", LOWEST, LEFT, false),
    DQUOTE("\"", LOWEST, LEFT, false),
    COLON(":", LOWEST, LEFT, false),
    COLONCOLON("::", LOWEST, LEFT, false),
    TILDE("~", LOWEST, LEFT, false),
    TILDEEQ("~=", LOWEST, LEFT, false),
    ADD("+", LOWEST, LEFT, false),
    ADDEQ("+=", LOWEST, LEFT, false),
    ADDADD("++", LOWEST, LEFT, false),
    SUB("-", LOWEST, LEFT, false),
    SUBEQ("-=", LOWEST, LEFT, false),
    SUBSUB("--", LOWEST, LEFT, false),
    MUL("*", LOWEST, LEFT, false),
    MULEQ("*=", LOWEST, LEFT, false),
    DIV("/", LOWEST, LEFT, false),
    DIVEQ("/=", LOWEST, LEFT, false),
    MOD("%", LOWEST, LEFT, false),
    MODEQ("%=", LOWEST, LEFT, false),
    EQEQ("==", LOWEST, LEFT, false),
    NOT("!", LOWEST, LEFT, false),
    NOTEQ("!=", LOWEST, LEFT, false),
    LT("<", LOWEST, LEFT, false),
    LTEQ("<=", LOWEST, LEFT, false),
    GT(">", LOWEST, LEFT, false),
    GTEQ(">=", LOWEST, LEFT, false),
    AND("&", LOWEST, LEFT, false),
    ANDEQ("&=", LOWEST, LEFT, false),
    OR("|", LOWEST, LEFT, false),
    OREQ("|=", LOWEST, LEFT, false),
    ANDAND("&&", LOWEST, LEFT, false),
    OROR("||", LOWEST, LEFT, false),
    XOR("^", LOWEST, LEFT, false),
    XOREQ("^=", LOWEST, LEFT, false),
    SR(">>", LOWEST, LEFT, false),
    SREQ(">>=", LOWEST, LEFT, false),
    SL("<<", LOWEST, LEFT, false),
    SLEQ("<<=", LOWEST, LEFT, false),
    PIPE("|>", LOWEST, LEFT, false),
    COMMENTO("/*", LOWEST, LEFT, true),
    COMMENTC("*/", LOWEST, LEFT, true),
    COMMENTL("//", LOWEST, LEFT, true),
    PERIOD(".", LOWEST, LEFT, false),
    PERIODEQ(".=", LOWEST, LEFT, false),
    IDENTIFIER(LOWEST, LEFT, false),
    COMMENT(LOWEST, LEFT, true),
    STRCONST(LOWEST, LEFT, false),
    WHOLE_NUMBER_LITERAL(LOWEST, LEFT, false),
    HEX_LITERAL(LOWEST, LEFT, false),
    FLOAT_LITERAL(LOWEST, LEFT, false),
    WHITESPACE(LOWEST, LEFT, true),
    LF(LOWEST, LEFT, true),
    TAB(LOWEST, LEFT, true),
    EOF(LOWEST, LEFT, false)
    ;

    private final boolean ignore;

    private final Precedence precedence;
    
    private final Associativity associativity;

    private String representation;

    private static Map<String, Type> lookup = null;

    Type(Precedence precedence, Associativity associativity, boolean ignore) {
        this.ignore = ignore;
        this.precedence = precedence;
        this.associativity = associativity;
    }

    Type(String representation, Precedence precedence, Associativity associativity, boolean ignore) {
        this.representation = representation;
        this.ignore = ignore;
        this.precedence = precedence;
        this.associativity = associativity;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public String getRepresentation() {
        return this.representation;
    }

    public Precedence getPrecedence() {
        return precedence;
    }

    public static Type lookup(String word) {
        if (lookup == null) {
            lookup = new HashMap<>();

            for (Type t : Type.values()) {
                // Don't bother caching types which don't have a representation
                if (t.getRepresentation() == null) {
                    continue;
                }

                lookup.put(t.getRepresentation(), t);
            }
        }

        return lookup.get(word);
    }

    public static Type lookup(char c) {
        return lookup("" + c);
    }

    public static Type lookup(int c) {
        return lookup((char)c);
    }

    public static Type coalesce(Type ...types) {
        for (Type t : types) {
            if (t != null) {
                return t;
            }
        }

        return null;
    }

    public boolean in(Type ...types) {
        for (Type t : types) {
            if (t == this) {
                return true;
            }
        }

        return false;
    }

    // This is a convenience method for printing out LaTex formatted table rows
    public static void main(String[] args) {
        int i = 1;

        HashMap<String, Boolean> dedup = new HashMap<>();

        for (Type t : Type.values()) {
/*
            if (t.isReserved) {
                System.out.print(t.getRepresentation().toLowerCase() + " & ");

                if (i % 8 == 0) {
                    System.out.println("\\\\");
                }
                i++;
            }
*/
            String r = t.getRepresentation();

            if (r != null) {
                for (String c : r.split("(?!^)")) {
                    dedup.put(c, true);
                }
            }
            i++;
        }

        i = 1;
        for (String s:dedup.keySet()) {
            System.out.print("case '" + s + "': ");

            if (i % 4 == 0) {
                System.out.println();
            }

            i++;
        }
    }
}
