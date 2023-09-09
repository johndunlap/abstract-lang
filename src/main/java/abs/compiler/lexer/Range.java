package abs.compiler.lexer;

import java.util.Objects;

public class Range {
    private Position start;
    private Position stop;

    public Range(Position start, Position stop) {
        this.start = start;
        this.stop = stop;

        if (start.getIndex() > stop.getIndex()) {
            throw new RuntimeException(String.format("startPosition(%d) cannot be greater or equal to stopPosition(%d)", getStartPosition(), getStopPosition()));
        } if (start.getLine() > stop.getLine()) {
            throw new RuntimeException(String.format("startLine(%d) cannot be greater than stopLine(%d)", getStartLine(), getStopLine()));
        } if (start.getColumn() >= stop.getColumn() && start.getLine() == stop.getLine()) {
            throw new RuntimeException(String.format("startColumn(%d) cannot be greater than or equal to stopColumn(%d)", getStartColumn(), getStopColumn()));
        }
    }

    public int getStartColumn() {
        return start.getColumn();
    }

    public int getStopColumn() {
        return stop.getColumn();
    }

    public int getStartLine() {
        return start.getLine();
    }

    public int getStopLine() {
        return stop.getLine();
    }

    public int getStartPosition() {
        return start.getIndex();
    }

    public int getStopPosition() {
        return stop.getIndex();
    }

    @Override
    public String toString() {
        return "Range{" +
            "startColumn=" + start.getColumn() +
            ", stopColumn=" + stop.getColumn() +
            ", startLine=" + start.getLine() +
            ", stopLine=" + stop.getLine() +
            ", startPosition=" + start.getIndex() +
            ", stopPosition=" + stop.getIndex() +
            ", length=" + (stop.getIndex() - start.getIndex()) +
            '}';
    }

    public String toJson() {
        return toJson("");
    }

    public String toJson(String indent) {
        return "{\n" +
            indent + "\t\"startLine: \"" + start.getLine() + ",\n" +
            indent + "\t\"stopLine\": " + stop.getLine() + ",\n" +
            indent + "\t\"stopColumn\": " + stop.getColumn() + ",\n" +
            indent + "\t\"stopColumn\": " + stop.getColumn() + ",\n" +
            indent + "\t\"startPosition\": " + start.getIndex() + ",\n" +
            indent + "\t\"stopPosition\": " + stop.getIndex() + "\n" +
            indent + "\t\"length\": " + (stop.getIndex() - start.getIndex()) + "\n" +
            indent + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return start.equals(range.start) &&
                stop.equals(range.stop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, stop);
    }
}
