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
    DOLLAR("$", false),
    EQ("=", false),
    LPAREN("(", false),
    RPAREN(")", false),
    LBRACE("{", false),
    RBRACE("}", false),
    LBRACKET("[", false),
    RBRACKET("]", false),
    SEMICOLON(";", false),
    COMMA(",", false),
    QUESTION("?", false),
    DQUOTE("\"", false),
    COLON(":", false),
    COLONCOLON("::", false),
    TILDE("~", false),
    TILDEEQ("~=", false),
    ADD("+", false),
    ADDEQ("+=", false),
    ADDADD("++", false),
    SUB("-", false),
    SUBEQ("-=", false),
    SUBSUB("--", false),
    MUL("*", false),
    MULEQ("*=", false),
    DIV("/", false),
    DIVEQ("/=", false),
    MOD("%", false),
    MODEQ("%=", false),
    EQEQ("==", false),
    NOT("!", false),
    NOTEQ("!=", false),
    LT("<", false),
    LTEQ("<=", false),
    GT(">", false),
    GTEQ(">=", false),
    AND("&", false),
    ANDEQ("&=", false),
    OR("|", false),
    OREQ("|=", false),
    ANDAND("&&", false),
    OROR("||", false),
    XOR("^", false),
    XOREQ("^=", false),
    SR(">>", false),
    SREQ(">>=", false),
    SL("<<", false),
    SLEQ("<<=", false),
    PIPE("|>", false),
    COMMENTO("/*", true),
    COMMENTC("*/", true),
    COMMENTL("//", true),
    PERIOD(".", false),
    PERIODEQ(".=", false),
    IDENTIFIER(false),
    COMMENT(true),
    STRCONST(false),
    WHOLE_NUMBER_LITERAL(false),
    HEX_LITERAL(false),
    FLOAT_LITERAL(false),
    WHITESPACE(true),
    LF(true),
    TAB(true),
    EOF(false)
    ;

    private final boolean parserIgnore;

    private String representation;

    private static Map<String, Type> lookup = null;

    Type(boolean parserIgnore) {
        this.parserIgnore = parserIgnore;
    }

    Type(String representation, boolean parserIgnore) {
        this.representation = representation;
        this.parserIgnore = parserIgnore;
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
