package abs.compiler.lexer;

import static abs.compiler.lexer.Associativity.LEFT;
import static abs.compiler.lexer.Precedence.ONE;
import static abs.compiler.lexer.Precedence.THREE;
import static abs.compiler.lexer.Precedence.TWO;

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
    DOLLAR("$", ONE, LEFT, false),
    EQ("=", ONE, LEFT, false),
    LPAREN("(", ONE, LEFT, false),
    RPAREN(")", ONE, LEFT, false),
    LBRACE("{", ONE, LEFT, false),
    RBRACE("}", ONE, LEFT, false),
    LBRACKET("[", ONE, LEFT, false),
    RBRACKET("]", ONE, LEFT, false),
    SEMICOLON(";", ONE, LEFT, false),
    COMMA(",", ONE, LEFT, false),
    QUESTION("?", ONE, LEFT, false),
    DQUOTE("\"", ONE, LEFT, false),
    COLON(":", ONE, LEFT, false),
    COLONCOLON("::", ONE, LEFT, false),
    TILDE("~", ONE, LEFT, false),
    TILDEEQ("~=", ONE, LEFT, false),
    ADD("+", ONE, LEFT, false),
    ADDEQ("+=", ONE, LEFT, false),
    ADDADD("++", ONE, LEFT, false),
    SUB("-", ONE, LEFT, false),
    SUBEQ("-=", ONE, LEFT, false),
    SUBSUB("--", ONE, LEFT, false),
    MUL("*", TWO, LEFT, false),
    MULEQ("*=", ONE, LEFT, false),
    DIV("/", TWO, LEFT, false),
    DIVEQ("/=", ONE, LEFT, false),
    MOD("%", ONE, LEFT, false),
    MODEQ("%=", ONE, LEFT, false),
    EQEQ("==", ONE, LEFT, false),
    NOT("!", ONE, LEFT, false),
    NOTEQ("!=", ONE, LEFT, false),
    LT("<", ONE, LEFT, false),
    LTEQ("<=", ONE, LEFT, false),
    GT(">", ONE, LEFT, false),
    GTEQ(">=", ONE, LEFT, false),
    AND("&", ONE, LEFT, false),
    ANDEQ("&=", ONE, LEFT, false),
    OR("|", ONE, LEFT, false),
    OREQ("|=", ONE, LEFT, false),
    ANDAND("&&", ONE, LEFT, false),
    OROR("||", ONE, LEFT, false),
    XOR("^", THREE, LEFT, false),
    XOREQ("^=", ONE, LEFT, false),
    SR(">>", ONE, LEFT, false),
    SREQ(">>=", ONE, LEFT, false),
    SL("<<", ONE, LEFT, false),
    SLEQ("<<=", ONE, LEFT, false),
    PIPE("|>", ONE, LEFT, false),
    COMMENTO("/*", ONE, LEFT, true),
    COMMENTC("*/", ONE, LEFT, true),
    COMMENTL("//", ONE, LEFT, true),
    PERIOD(".", ONE, LEFT, false),
    PERIODEQ(".=", ONE, LEFT, false),
    IDENTIFIER(ONE, LEFT, false),
    COMMENT(ONE, LEFT, true),
    STRCONST(ONE, LEFT, false),
    WHOLE_NUMBER_LITERAL(ONE, LEFT, false),
    HEX_LITERAL(ONE, LEFT, false),
    FLOAT_LITERAL(ONE, LEFT, false),
    WHITESPACE(ONE, LEFT, true),
    LF(ONE, LEFT, true),
    TAB(ONE, LEFT, true),
    EOF(ONE, LEFT, false)
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

    public Associativity getAssociativity() {
        return associativity;
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
