package abs.compiler.lexer;

import abs.compiler.exception.LexerException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class CharacterStream {
    public final static Integer END_OF_FILE = -1;

    private final LinkedList<Integer> buffer = new LinkedList<>();
    private final InputStream inputStream;
    private int currentIndex;
    private int currentLine;
    private int currentColumn;

    public CharacterStream(String s, LexerOptions options) {
        this(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)), options);
    }

    public CharacterStream(InputStream inputStream, LexerOptions options) {
        this.inputStream = inputStream;
        setOptions(options);

        // Start counting at 1 to make error messages easier to read
        currentIndex = 1;
        currentColumn = 1;
        currentLine = 1;
    }

    public int next() {
        Integer c;

        try {
            // Get the next character
            if (buffer.size() > 0) {
                c = buffer.removeFirst();
            } else {
                int raw = inputStream.read();

                // Skip this Windows specific nonsense
                if ((char)raw == '\r') {
                    raw = inputStream.read();
                }

                // Return if we're at the end of the stream
                if (raw == -1) {
                    return END_OF_FILE;
                }

                c = raw;
            }

            if ((char)c.intValue() == '\n') {
                currentLine++;
                currentColumn =1;
            } else {
                currentColumn++;
            }

            currentIndex++;
        } catch (IOException e) {
            throw new LexerException(e, getCurrentPosition());
        }

        return c;
    }

    public int peek() {
        int c;

        if (buffer.size() > 0) {
            return buffer.getFirst();
        }

        try {
            int raw = inputStream.read();

            // Return if we're at the end of the stream
            if (raw == -1) {
                return END_OF_FILE;
            }

            c = raw;

            // Add the character to the buffer so we don't lose it
            buffer.addLast(c);
        } catch (IOException e) {
            throw new LexerException(e, getCurrentPosition());
        }

        return c;
    }

    /**
     * This method will always return a list with the requested number of character. If the requested number of
     * characters exceeds the number of available characters then null will be used to pad the end of the list.
     * @param num The index of the character which should be returned
     * @return The requested nth character in the stream
     * this should never happen because null will be returned.
     */
    public int peek(int num) {
        try {
            // Read additional characters into the buffer, if necessary
            if (buffer.size() < num + 1) {
                int missing = num + 1 - buffer.size();

                for (int i = 0; i < missing; i++) {
                    Integer raw = inputStream.read();

                    // Translate the -1 to null because it is a Java implementation detail and we don't want code which
                    // uses this method to rely on it
                    if (raw == -1) {
                        raw = null;
                    }

                    // Don't add nulls to the buffer
                    if (raw != null) {
                        buffer.addLast(raw);
                    }
                }
            }

            // Return if we still don't have enough characters in the buffer
            if (buffer.size() < num + 1) {
                return END_OF_FILE;
            }

            // Otherwise, return the requested character
            return buffer.get(num);
        } catch (IOException e) {
            throw new LexerException(e, getCurrentPosition());
        }
    }

    public Position getCurrentPosition() {
        return new Position(currentIndex, currentLine, currentColumn);
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public String toString() {
        return "CharacterStream{" +
                "currentIndex=" + currentIndex +
                ", currentLine=" + currentLine +
                ", currentColumn=" + currentColumn +
                '}';
    }

    protected LexerOptions options;
    protected boolean traceEnabled = false;

    public LexerOptions getOptions() {
        return options;
    }

    public void setOptions(LexerOptions options) {
        this.options = options;

        if (options.isTraceEnabled()) {
            traceEnabled = true;
        }
    }

    protected PrintStream trace(String s) {
        if (options.isTraceEnabled()) {
            options.getTrace().println(s);
        }
        return options.getTrace();
    }

    protected PrintStream debug(String s) {
        if (options.isDebugEnabled()) {
            options.getDebug().println(s);
        }
        return options.getDebug();
    }
}
