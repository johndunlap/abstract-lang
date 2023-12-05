package abs.compiler.lexer;

import abs.compiler.Options;
import abs.compiler.exception.LexerException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CharacterStreamTest {
    @Test
    public void testNextMethodSequentially() throws IOException, LexerException {
        InputStream inputStream = new ByteArrayInputStream("This is a test".getBytes(StandardCharsets.UTF_8));
        CharacterStream characterStream = new CharacterStream("This is a test", new Options());

        int c;

        while ((c = inputStream.read()) != CharacterStream.END_OF_FILE) {
            assertEquals(c, characterStream.next());
        }
    }

    @Test
    public void testNextMethodSequentiallyWithPeek() throws IOException, LexerException {
        InputStream inputStream = new ByteArrayInputStream("This is a test".getBytes(StandardCharsets.UTF_8));
        CharacterStream characterStream = new CharacterStream("This is a test", new Options());

        int c;
        int i = 0;

        while ((c = inputStream.read()) != -1) {
            if (i == 5) {
                assertEquals((char)characterStream.peek(0), 'i');
                assertEquals((char)characterStream.peek(1), 's');
            }

            assertEquals(c, characterStream.next());

            i++;
        }
    }

    @Test
    public void testNextMethod() throws LexerException {
        String sample = "This is\na test of\nthe next() method";
        byte[] bytes = StandardCharsets.UTF_8.encode(sample).array();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        CharacterStream characterStream = new CharacterStream(inputStream, new Options());
        Integer c;
        int expectedColumn = 1;
        int expectedLine = 1;
        int i = 0;

        assertEquals(1, characterStream.getCurrentIndex());

        while ((c = characterStream.next()) != CharacterStream.END_OF_FILE) {
            if (i > sample.length()) {
                fail("Stream returned more characters than were present in the original sample string");
            }

            if (c == 0) {
                assertEquals(i, sample.length());
                break;
            }

            if ((char)c.intValue() == '\n') {
                expectedLine++;
                expectedColumn = 1;
            } else {
                expectedColumn++;
            }

            assertEquals(String.format("Expected %c but found %c", sample.charAt(i), c), (char)c.intValue(), sample.charAt(i));
            i++;

            assertEquals(i + 1, characterStream.getCurrentIndex());
            assertEquals(expectedLine, characterStream.getCurrentLine());
            assertEquals(expectedColumn, characterStream.getCurrentColumn());
        }
    }

    @Test
    public void testNextNumMethod() throws LexerException {
        String sample = "This is\na test of\nthe next(num) method";
        byte[] bytes = StandardCharsets.UTF_8.encode(sample).array();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        CharacterStream characterStream = new CharacterStream(inputStream, new Options());

        List<Integer> expected1 = new ArrayList<>(){{
            add((int)'T');
            add((int)'h');
            add((int)'i');
            add((int)'s');
            add((int)' ');
        }};
        List<Integer> actual1 = new ArrayList<>();

        for (int x = 0; x < 5; x++) {
            actual1.add(characterStream.next());
        }

        assertEquals(6, characterStream.getCurrentIndex());
        assertEquals(1, characterStream.getCurrentLine());
        assertEquals(6, characterStream.getCurrentColumn());

        assertEquals(expected1.size(), actual1.size());

        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }

        List<Integer> expected2 = new ArrayList<>(){{
            add((int)'i');
            add((int)'s');
            add((int)'\n');
            add((int)'a');
            add((int)' ');
            add((int)'t');
            add((int)'e');
            add((int)'s');
            add((int)'t');
        }};
        List<Integer> actual2 = new ArrayList<>();

        for (int x = 0; x < 9; x++) {
            actual2.add(characterStream.next());
        }

        assertEquals(15, characterStream.getCurrentIndex());
        assertEquals(2, characterStream.getCurrentLine());
        assertEquals(7, characterStream.getCurrentColumn());

        assertEquals(expected2.size(), actual2.size());

        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
    }

    @Test
    public void testPeekMethod() throws LexerException {
        String sample = "This is\na test of\nthe peek method";
        byte[] bytes = StandardCharsets.UTF_8.encode(sample).array();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        CharacterStream characterStream = new CharacterStream(inputStream, new Options());

        assertEquals((int)'T', characterStream.peek());
        assertEquals(1, characterStream.getCurrentIndex());
        assertEquals(1, characterStream.getCurrentLine());
        assertEquals(1, characterStream.getCurrentColumn());

        characterStream.next();

        assertEquals((int)'h', characterStream.peek());
        assertEquals(2, characterStream.getCurrentIndex());
        assertEquals(1, characterStream.getCurrentLine());
        assertEquals(2, characterStream.getCurrentColumn());

        for (int x = 0; x < 5; x++) {
            characterStream.next();
        }

        assertEquals((int)'s', characterStream.peek());
        assertEquals(7, characterStream.getCurrentIndex());
        assertEquals(1, characterStream.getCurrentLine());
        assertEquals(7, characterStream.getCurrentColumn());
    }

    @Test
    public void testPeekNumMethod() throws LexerException {
        String sample = "This is\na test of\nthe peek(num) method";
        byte[] bytes = StandardCharsets.UTF_8.encode(sample).array();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        CharacterStream characterStream = new CharacterStream(inputStream, new Options());

        assertEquals(1, characterStream.getCurrentIndex());
        assertEquals(1, characterStream.getCurrentLine());
        assertEquals(1, characterStream.getCurrentColumn());
        assertEquals((int)'T', characterStream.peek(0));
        assertEquals((int)'h', characterStream.peek(1));
        assertEquals((int)'i', characterStream.peek(2));
        assertEquals((int)'s', characterStream.peek(3));

        for (int x = 0; x < 4; x++) {
            characterStream.next();
        }

        assertEquals(5, characterStream.getCurrentIndex());
        assertEquals(1, characterStream.getCurrentLine());
        assertEquals(5, characterStream.getCurrentColumn());
        assertEquals((int)' ', characterStream.peek(0));
        assertEquals((int)'i', characterStream.peek(1));
        assertEquals((int)'s', characterStream.peek(2));
        assertEquals((int)'\n', characterStream.peek(3));
    }
}
