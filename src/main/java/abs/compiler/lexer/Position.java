package abs.compiler.lexer;

import java.util.Objects;

public class Position {
    private int index;
    private int line;
    private int column;

    public Position(int index, int line, int column) {
        this.index = index;
        this.line = line;
        this.column = column;
    }

    public int getIndex() {
        return index;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return index == position.index &&
                line == position.line &&
                column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, line, column);
    }

    @Override
    public String toString() {
        return "Position{" +
                "index=" + index +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
