package abs.compiler.archive.parser.graph;

public abstract class GraphObject {
    /**
     * Contains whitespace which appeared prior to the first token of this graph object. The only graph object which
     * needs to concern itself with whitespace which appears after its last token
     */
    protected String before;

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public abstract String toString();
}