package abs;

import abs.compiler.Util;
import abs.compiler.lexer.Position;
import abs.compiler.lexer.Range;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static abs.compiler.Util.coalesce;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UtilTest {
    @Test
    public void testSubstringMethods() throws IOException {
        List<String> inputs = new ArrayList<>() {{
            add("/* This is a multi\n line comment\n*/\n");
            add("This is\na test.");
            add("This is\na test\n of the new substring routine.");
            add("This is\na test\n of the new substring routine.");
            add("/*\nThis is a test\n*/\n");
        }};

        List<String> outputs = new ArrayList<>() {{
            add("\n");
            add("is\na");
            add("the new substring routine");
            add("is\na test\n of");
            add("\nThis is a test\n");
        }};

        List<Range> ranges = new ArrayList<>() {{
            add(new Range(new Position(36,3,3), new Position(37, 4, 1)));
            add(new Range(new Position(6,1,6), new Position(10, 2, 2)));
            add(new Range(new Position(20,3,5), new Position(45, 3, 30)));
            add(new Range(new Position(6,1,6), new Position(19, 3, 4)));
            add(new Range(new Position(3,1,3), new Position(19, 3, 1)));
        }};

        // Make sure that we have the correct number of inputs and outputs
        assertEquals(inputs.size(), outputs.size());
        assertEquals(inputs.size(), ranges.size());

        // Make sure that the expected outputs match the actual outputs
        for (int i = 0; i < inputs.size(); i++) {
            String input = inputs.get(i);
            Range range = ranges.get(i);
            String expectedOutput = outputs.get(i);
            String actualOutput = "";
            String alternateExpectedOutput = "";

            assertNotNull(input);
            assertNotNull(range);
            assertNotNull(expectedOutput);
            assertNotNull(actualOutput);
            assertNotNull(alternateExpectedOutput);

            try {
                actualOutput = Util.substring(input, range);

                // Verify that substrings can be extracted by line/column
                assertEquals(expectedOutput, actualOutput);

                // Verify that substrings extracted by index match the substrings extracted by line/column
                alternateExpectedOutput = input.substring(range.getStartPosition() - 1, range.getStopPosition() - 1);
                assertEquals(alternateExpectedOutput, actualOutput);
            } catch (Throwable e) {
                System.err.printf(
                    "INPUT %d FAILED\n\tInput       :\"%s\"\n\tExpected    :\"%s\"\n\tLine/Column :\"%s\"\n\tIndex       :\"%s\"\n----------------------------------------\n",
                    i,
                    input.replaceAll("(\\r\\n|\\n)", "\\\\n"),
                    expectedOutput.replaceAll("(\\r\\n|\\n)", "\\\\n"),
                    actualOutput.replaceAll("(\\r\\n|\\n)", "\\\\n"),
                    alternateExpectedOutput.replaceAll("(\\r\\n|\\n)", "\\\\n")
                );
                throw e;
            }
        }
    }

    @Test
    public void testCoalesceUtilMethod() {
        assertEquals(5, coalesce(null, 5, 6));
        assertEquals(1, coalesce(1, 5, 6));
        assertEquals("abc", coalesce(null, "abc", "def"));
    }
}
