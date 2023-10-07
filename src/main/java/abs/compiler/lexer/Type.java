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
    DOLLAR("$", 0, false),
    EQ("=", 0, false),
    LPAREN("(", 0, false),
    RPAREN(")", 0, false),
    LBRACE("{", 0, false),
    RBRACE("}", 0, false),
    LBRACKET("[", 0, false),
    RBRACKET("]", 0, false),
    SEMICOLON(";", 0, false),
    COMMA(",", 0, false),
    QUESTION("?", 0, false),
    DQUOTE("\"", 0, false),
    COLON(":", 0, false),
    COLONCOLON("::", 0, false),
    TILDE("~", 0, false),
    TILDEEQ("~=", 0, false),
    ADD("+", 0, false),
    ADDEQ("+=", 0, false),
    ADDADD("++", 0, false),
    SUB("-", 0, false),
    SUBEQ("-=", 0, false),
    SUBSUB("--", 0, false),
    MUL("*", 0, false),
    MULEQ("*=", 0, false),
    DIV("/", 0, false),
    DIVEQ("/=", 0, false),
    MOD("%", 0, false),
    MODEQ("%=", 0, false),
    EQEQ("==", 0, false),
    NOT("!", 0, false),
    NOTEQ("!=", 0, false),
    LT("<", 0, false),
    LTEQ("<=", 0, false),
    GT(">", 0, false),
    GTEQ(">=", 0, false),
    AND("&", 0, false),
    ANDEQ("&=", 0, false),
    OR("|", 0, false),
    OREQ("|=", 0, false),
    ANDAND("&&", 0, false),
    OROR("||", 0, false),
    XOR("^", 0, false),
    XOREQ("^=", 0, false),
    SR(">>", 0, false),
    SREQ(">>=", 0, false),
    SL("<<", 0, false),
    SLEQ("<<=", 0, false),
    PIPE("|>", 0, false),
    COMMENTO("/*", 0, true),
    COMMENTC("*/", 0, true),
    COMMENTL("//", 0, true),
    PERIOD(".", 0, false),
    PERIODEQ(".=", 0, false),
    IDENTIFIER(0, false),
    COMMENT(0, true),
    STRCONST(0, false),
    WHOLE_NUMBER_LITERAL(0, false),
    HEX_LITERAL(0, false),
    FLOAT_LITERAL(0, false),
    WHITESPACE(0, true),
    LF(0, true),
    TAB(0, true),
    EOF(0, false)
    ;

    private final boolean parserIgnore;

    private final int precedence;

    private String representation;

    private static Map<String, Type> lookup = null;

    Type(int precedence, boolean parserIgnore) {
        this.parserIgnore = parserIgnore;
        this.precedence = precedence;
    }

    Type(String representation, int precedence, boolean parserIgnore) {
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
