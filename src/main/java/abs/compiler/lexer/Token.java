package abs.compiler.lexer;

import java.util.Objects;

public class Token {
    private Type type;
    private String value;
    private Range range;

    public Token(Type type, Range range, String value) {
        this.value = value;
        this.range = range;
        this.type = type;
    }

    public Token(Type type, Range range, char value) {
        this.value = String.valueOf(value);
        this.range = range;
        this.type = type;
    }

    public Token(Type type, Range range) {
        this.range = range;
        this.type = type;

        if (type.getRepresentation() != null) {
            this.value = type.getRepresentation();
        }
    }

    public Type getType() {
        return type;
    }

    public Range getRange() {
        return range;
    }

    public String getValue() {
        return value;
    }

    public boolean inType(Type... types) {
        for (Type type : types) {
            if (getType().equals(type)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasType(Type type) {
        return getType().equals(type);
    }

    public boolean hasValue(String v) {
        return getValue() != null && getValue().equals(v);
    }

    // This is distinct from toString because toString is used for debugging
    public String toText() {
        if (value != null) {
            return value;
        } if (type.getRepresentation() != null) {
            return type.getRepresentation();
        }

        return type.toString();
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", line=" + (range != null ? range.getStartLine() : "null") +
                ", value='" + filteredValue() + '\'' +
                ", length='" + (value != null ? value.length() : "null") + '\'' +
                ", range='" + (range != null ? range.toString() : "null") + '\'' +
                '}';
    }

    public String toJson() {
        return toJson("");
    }

    public String toJson(String indent) {
        return "{\n" +
            indent + "\t\"value: \"" + filteredValue() + ",\n" +
            indent + "\t\"type: \"" + type + ",\n" +
            indent + "\t\"startLine: \"" + range.getStartLine() + ",\n" +
            indent + "\t\"stopLine: \"" + range.getStopLine() + ",\n" +
            indent + "\t\"startColumn: \"" + range.getStartColumn() + ",\n" +
            indent + "\t\"stopColumn: \"" + range.getStopColumn() + ",\n" +
            indent + "\t\"startPosition: \"" + range.getStartPosition() + ",\n" +
            indent + "\t\"stopPosition: \"" + range.getStopPosition() + ",\n" +
            indent + "}";
    }

    private String filteredValue() {
        if (value == null) {
            return null;
        }

        switch (value) {
            case "\n":
                return "\\n";
            case "\t":
                return "\\t";
            case "\r":
                return  "\\r";
            case "\r\n":
                return  "\\r\\n";
            default:
                return value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type &&
                value.equals(token.value) &&
                range.equals(token.range);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value, range);
    }
}
