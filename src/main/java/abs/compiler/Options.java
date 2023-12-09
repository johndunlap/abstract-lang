package abs.compiler;

import abs.compiler.parser.paradigms.ParadigmEnum;

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
    
    private ParadigmEnum defaultParadigm = ParadigmEnum.OOP;

    public boolean isTraceEnabled() {
        return traceEnabled;
    }

    public Options setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
        return this;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public Options setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        return this;
    }

    public PrintStream getTrace() {
        return trace;
    }

    public Options setTrace(PrintStream trace) {
        this.trace = trace;
        return this;
    }

    public PrintStream getDebug() {
        return debug;
    }

    public Options setDebug(PrintStream debug) {
        this.debug = debug;
        return this;
    }

    public boolean isIncludeSpaces() {
        return includeSpaces;
    }

    public Options setIncludeSpaces(boolean includeSpaces) {
        this.includeSpaces = includeSpaces;
        return this;
    }

    public boolean isIncludeLineFeed() {
        return includeLineFeed;
    }

    public Options setIncludeLineFeed(boolean includeLineFeed) {
        this.includeLineFeed = includeLineFeed;
        return this;
    }

    public boolean isIncludeTab() {
        return includeTab;
    }

    public Options setIncludeTab(boolean includeTab) {
        this.includeTab = includeTab;
        return this;
    }

    public boolean isIncludeComment() {
        return includeComment;
    }

    public Options setIncludeComment(boolean includeComment) {
        this.includeComment = includeComment;
        return this;
    }

    public ParadigmEnum getDefaultParadigm() {
        return defaultParadigm;
    }

    public Options setDefaultParadigm(ParadigmEnum defaultParadigm) {
        this.defaultParadigm = defaultParadigm;
        return this;
    }
}
