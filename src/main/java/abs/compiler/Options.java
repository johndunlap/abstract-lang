package abs.compiler;

import java.io.PrintStream;

public class Options {
    private boolean traceEnabled = false;
    private boolean debugEnabled = false;
    private PrintStream trace;
    private PrintStream debug;
    private boolean includeSpaces = false;
    private boolean includeLineFeed = false;
    private boolean includeTab = false;
    private boolean includeComment = false;

    public boolean isTraceEnabled() {
        return traceEnabled;
    }

    public void setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public PrintStream getTrace() {
        return trace;
    }

    public void setTrace(PrintStream trace) {
        this.trace = trace;
    }

    public PrintStream getDebug() {
        return debug;
    }

    public void setDebug(PrintStream debug) {
        this.debug = debug;
    }
    public boolean isIncludeSpaces() {
        return includeSpaces;
    }

    public void setIncludeSpaces(boolean includeSpaces) {
        this.includeSpaces = includeSpaces;
    }

    public boolean isIncludeLineFeed() {
        return includeLineFeed;
    }

    public void setIncludeLineFeed(boolean includeLineFeed) {
        this.includeLineFeed = includeLineFeed;
    }

    public boolean isIncludeTab() {
        return includeTab;
    }

    public void setIncludeTab(boolean includeTab) {
        this.includeTab = includeTab;
    }

    public boolean isIncludeComment() {
        return includeComment;
    }

    public void setIncludeComment(boolean includeComment) {
        this.includeComment = includeComment;
    }
}
