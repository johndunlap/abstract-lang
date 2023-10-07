package abs.compiler.lexer;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents the different types of tokens that the lexer can produce. This is an enum for performance
 * reasons. In many languages this will allow parsing to be based on a comparison which is faster than a string match.
 * Also, some tokens cannot be matched based on string comparisons. For example, all integer numbers will have the same
 * token type but different text representations.
 */
public enum Type {
    DOLLAR("$", Precedence.LOWEST, false),
    EQ("=", Precedence.LOWEST, false),
    LPAREN("(", Precedence.LOWEST, false),
    RPAREN(")", Precedence.LOWEST, false),
    LBRACE("{", Precedence.LOWEST, false),
    RBRACE("}", Precedence.LOWEST, false),
    LBRACKET("[", Precedence.LOWEST, false),
    RBRACKET("]", Precedence.LOWEST, false),
    SEMICOLON(";", Precedence.LOWEST, false),
    COMMA(",", Precedence.LOWEST, false),
    QUESTION("?", Precedence.LOWEST, false),
    DQUOTE("\"", Precedence.LOWEST, false),
    COLON(":", Precedence.LOWEST, false),
    COLONCOLON("::", Precedence.LOWEST, false),
    TILDE("~", Precedence.LOWEST, false),
    TILDEEQ("~=", Precedence.LOWEST, false),
    ADD("+", Precedence.LOWEST, false),
    ADDEQ("+=", Precedence.LOWEST, false),
    ADDADD("++", Precedence.LOWEST, false),
    SUB("-", Precedence.LOWEST, false),
    SUBEQ("-=", Precedence.LOWEST, false),
    SUBSUB("--", Precedence.LOWEST, false),
    MUL("*", Precedence.LOWEST, false),
    MULEQ("*=", Precedence.LOWEST, false),
    DIV("/", Precedence.LOWEST, false),
    DIVEQ("/=", Precedence.LOWEST, false),
    MOD("%", Precedence.LOWEST, false),
    MODEQ("%=", Precedence.LOWEST, false),
    EQEQ("==", Precedence.LOWEST, false),
    NOT("!", Precedence.LOWEST, false),
    NOTEQ("!=", Precedence.LOWEST, false),
    LT("<", Precedence.LOWEST, false),
    LTEQ("<=", Precedence.LOWEST, false),
    GT(">", Precedence.LOWEST, false),
    GTEQ(">=", Precedence.LOWEST, false),
    AND("&", Precedence.LOWEST, false),
    ANDEQ("&=", Precedence.LOWEST, false),
    OR("|", Precedence.LOWEST, false),
    OREQ("|=", Precedence.LOWEST, false),
    ANDAND("&&", Precedence.LOWEST, false),
    OROR("||", Precedence.LOWEST, false),
    XOR("^", Precedence.LOWEST, false),
    XOREQ("^=", Precedence.LOWEST, false),
    SR(">>", Precedence.LOWEST, false),
    SREQ(">>=", Precedence.LOWEST, false),
    SL("<<", Precedence.LOWEST, false),
    SLEQ("<<=", Precedence.LOWEST, false),
    PIPE("|>", Precedence.LOWEST, false),
    COMMENTO("/*", Precedence.LOWEST, true),
    COMMENTC("*/", Precedence.LOWEST, true),
    COMMENTL("//", Precedence.LOWEST, true),
    PERIOD(".", Precedence.LOWEST, false),
    PERIODEQ(".=", Precedence.LOWEST, false),
    IDENTIFIER(Precedence.LOWEST, false),
    COMMENT(Precedence.LOWEST, true),
    STRCONST(Precedence.LOWEST, false),
    WHOLE_NUMBER_LITERAL(Precedence.LOWEST, false),
    HEX_LITERAL(Precedence.LOWEST, false),
    FLOAT_LITERAL(Precedence.LOWEST, false),
    WHITESPACE(Precedence.LOWEST, true),
    LF(Precedence.LOWEST, true),
    TAB(Precedence.LOWEST, true),
    EOF(Precedence.LOWEST, false)
    ;

    private final boolean parserIgnore;

    private final Precedence precedence;

    private String representation;

    private static Map<String, Type> lookup = null;

    Type(Precedence precedence, boolean parserIgnore) {
        this.parserIgnore = parserIgnore;
        this.precedence = precedence;
    }

    Type(String representation, Precedence precedence, boolean parserIgnore) {
        this.representation = representation;
        this.parserIgnore = parserIgnore;
        this.precedence = precedence;
    }

    public boolean isParserIgnore() {
        return parserIgnore;
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
