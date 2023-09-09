package abs.compiler;

import abs.compiler.lexer.Range;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Util {
    /*
     * Extract substrings based on line/column instead of based upon absolute position.
     */
    public static String substring(String input, Range range) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(input));

        //System.out.printf("startLine=%d, startColumn=%d, stopLine=%d, stopColumn=%d\n", startLine, startColumn, stopLine, stopColumn);

        int lineNumber=1;
        String line=null;
        StringBuilder sb = new StringBuilder();
        boolean beginCapture = false;

        while((line=reader.readLine()) != null) {
            if (range.getStartLine() == range.getStopLine()) {
                if (range.getStartLine() == lineNumber) {
                    return line.substring(range.getStartColumn() - 1, range.getStopColumn() - 1);
                }
            } else {
                if (range.getStartLine() == lineNumber) {
                    sb.append(line.substring(range.getStartColumn() - 1)).append("\n");
                    beginCapture = true;
                } else if (range.getStopLine() == lineNumber) {
                    sb.append(line.substring(0, range.getStopColumn() - 1));
                    break;
                } else if (beginCapture) {
                    sb.append(line).append("\n");
                }
            }

            lineNumber++;
        }

        return sb.toString();
    }

    public static List<String> runCommand (String... command) throws IOException {
        Runtime rt = Runtime.getRuntime();

        // Run the command
        Process proc = rt.exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        List<String> lines = new ArrayList<>();
        String s = null;

        // Read the output of the command
        while ((s = stdInput.readLine()) != null) {
            lines.add(s.trim());
        }

        if (lines.size() > 0) {
            int lastIndex = lines.size() - 1;
            String line = lines.get(lastIndex);

            if (line == null || line.trim().equals("")) {
                lines.remove(lastIndex);
            }
        }

        // Return the output
        return lines;
    }

    public static String fromFile(String file) throws IOException {
        return fromFile(new File(file));
    }

    public static String fromFile(File file) throws IOException {
        return Files.readString(Paths.get(file.getPath()), StandardCharsets.UTF_8);
    }

    public static void toFile(String text, String file) throws IOException {
        toFile(text, new File(file));
    }

    public static void toFile(String text, File file) throws IOException {
        Files.writeString(Paths.get(file.getAbsolutePath()), text, StandardCharsets.UTF_8);
    }

    /*
     * TODO: Different way of approaching the substring problem(unfinished)
     */
    public static String substring2(String input, Range range) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();

        //System.out.printf("startLine=%d, startColumn=%d, stopLine=%d, stopColumn=%d\n", startLine, startColumn, stopLine, stopColumn);

        int line = 1;
        int column = 1;
        int c;

        while ((c=inputStream.read()) != -1) {
            if (line == range.getStopLine() && column == range.getStopColumn()) {
                return sb.toString();
            }

            if (line >= range.getStartLine() && column >= range.getStartColumn()) {
                sb.append((char)c);
                System.out.println("OK: " + (char)c);
            } else {
                System.out.println("NO: " + (char)c);
            }

            // Housekeeping
            if ((char)c == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }

        return sb.toString();
    }

    /*
     * Generate a string containing the requested number of tab characters.
     */
    public static String tabs(int d) {
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < d; x++) {
            sb.append('\t');
        }

        return sb.toString();
    }

    public static Object coalesce(Object... values) {
        for (Object value : values) {
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    public static boolean in(Object value, Object... values) {
        for (Object potentialMatch : values) {
            if (potentialMatch.equals(value)) {
                return true;
            }
        }

        return false;
    }
}
