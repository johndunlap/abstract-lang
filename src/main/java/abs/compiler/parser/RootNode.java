package abs.compiler.parser;

public class RootNode extends AbstractNode {
    public String toDot() {
        StringBuilder stringBuilder = new StringBuilder();
        toDot(stringBuilder);
        return stringBuilder.toString();
    }

    @Override
    public void toDot(StringBuilder stringBuilder) {
        stringBuilder.append("digraph {\n")
            .append("    node [shape=ellipse];\n")
            .append("      ")
            .append(id)
            .append(" [label=\"")
            .append(toString())
            .append("\", shape=oval];\n");

        for (Node child : children) {
            stringBuilder.append("      ")
                    .append(id)
                    .append(" -> ")
                    .append(child.getId())
                    .append(";\n");
            child.toDot(stringBuilder);
        }

        stringBuilder.append("}");
    }

    @Override
    public String toString() {
        return "ROOT";
    }
}
