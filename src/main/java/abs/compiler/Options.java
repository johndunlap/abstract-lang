package abs.compiler;

import java.io.PrintStream;

public class Options {
    private boolean traceEnabled = false;
    private boolean debugEnabled = false;
    private PrintStream trace;
    private PrintStream debug;

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
}
