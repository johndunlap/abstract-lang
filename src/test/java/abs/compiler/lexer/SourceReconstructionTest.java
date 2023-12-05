package abs.compiler.lexer;

import static abs.compiler.lexer.Type.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import abs.compiler.Options;
import abs.compiler.Util;
import abs.compiler.exception.LexerException;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: When the abs compiler is re-written in abs, this test should examine the source code of the compiler itself
 *
 * This test recursively scans for all abs source files within a directory and considers the behavior of the lexer
 * valid if each of the source file(s) can be reconstructed from the list of tokens output by the lexer. Making sure
 * that the source tokens are correct, in the context of the language, is the responsibility of the parser not the
 * lexer - JDD 2020.04.06
 */
public class SourceReconstructionTest {
    public static Map<Type, Boolean> encounteredTypes = new HashMap<>();
    public static final String BASE_PATH = "src/test/resources/abs/sample";

    @Test
    public void testSourceReconstructionOfAllSourceFiles() throws IOException, LexerException {
        for (File absFile : findAllFilesInDirectory(BASE_PATH)) {
            String absSource = fromFile(absFile);

            // Skip source files which exist, as a reminder, but which haven't been implemented yet
            if (absFile.getAbsolutePath().contains("todo")) {
                continue;
            }

            System.out.println("Lexing: " + absFile.getAbsolutePath());

            Options options = new Options() {{
                setTrace(System.out);
                setTraceEnabled(false);
                setIncludeLineFeed(true);
                setIncludeTab(false);
                setIncludeSpaces(true);
                setIncludeComment(false);
            }};

            CharacterStream characterStream = new CharacterStream(absSource, options);
            TokenStream tokenStream = createTokenStream(characterStream, options);
            StringBuilder sb = new StringBuilder();
            List<Token> tokens = new ArrayList<>();

            Token token;

            // Read in all the tokens from the source file
            while (!(token = tokenStream.next()).getType().equals(EOF)) {
                sb.append(token.getValue());
                tokens.add(token);
            }

            assertEquals(absSource, sb.toString());

            sb = new StringBuilder();

            // Verify the that the token coordinates can be used to reconstruct the source
            for (Token t : tokens) {
                sb.append(Util.substring(absSource, t.getRange()));
            }

            assertEquals(absSource, sb.toString());
            System.out.println("Source reconstruction successful\n");
        }
    }

    /**
     * This method ensures that all token types have been tested for. Without this, it would be difficult to guarantee
     * if the lexer supported all token types. Note that this test will fail unless you run all tests within this class
     * prior to running this test.
     */
// TODO: Uncomment this at a later date when more abs programs have been written
/*
    @AfterClass
    public static void verifyAllTypesTested() {
        int x = 1;
        int expectedSize = values().length;

        for (Type type : Type.values()) {
            assertTrue(format("Failed to test for type(%d of %d, %.2f%%): ", x, expectedSize,((float)x)/((float)expectedSize)*100.0f) + type, encounteredTypes.containsKey(type));
            assertTrue(format("Failed to test for type(%d of %d, %.2f%%): ", x, expectedSize,((float)x)/((float)expectedSize)*100.0f) + type, encounteredTypes.get(type));
            x++;
        }
    }
*/

    private List<File> findAllFilesInDirectory(String directory) throws FileNotFoundException {
        return findAllFilesInDirectory(new File(directory));
    }

    private List<File> findAllFilesInDirectory(File directory) throws FileNotFoundException {
        assertNotNull(directory);

        List<File> files = new ArrayList<>();

        if (!directory.exists() || !directory.isDirectory()) {
            throw new FileNotFoundException("File does not exist or is not a directory: " + directory.getAbsolutePath());
        }

        //noinspection ConstantConditions
        for (File file : directory.listFiles()) {
            assertNotNull(file);

            // Recurse if we find another directory
            if (file.isDirectory()) {
                files.addAll(findAllFilesInDirectory(file));
            }

            if (file.getName().endsWith(".abs")) {
                files.add(file);
            }
        }

        return files;
    }

    private String fromFile(File file) throws IOException {
        assertNotNull(file);

        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file.getAbsolutePath());
        }

        return fromStream(new FileInputStream(file));
    }

    private void toFile(String contents, File file) throws FileNotFoundException {
        assertNotNull(contents);
        assertNotNull(file);

        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file.getAbsolutePath());
        }

        try(FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            byte[] bytes = contents.getBytes();
            bos.write(bytes);
            bos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String fromStream(InputStream is) throws IOException {
        assertNotNull(is);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        boolean fl = true;
        String line;

        while ((line = br.readLine()) != null) {
            if (fl) {
                sb.append(line);
            } else {
                sb.append("\n").append(line);
            }

            fl = false;
        }

        return sb.toString();
    }

    /**
     * This method creates a token stream instance and overrides its next method. This allows the encounteredTypes map
     * to be automatically updated without explicitly updating from within each of the tests.
     * @param characterStream The character stream from which input should be taken
     * @param options the options which should be passed to the token stream
     * @return the requested token stream
     */
    private TokenStream createTokenStream(CharacterStream characterStream, Options options) {
        return new TokenStream(characterStream, options) {
            @Override
            public Token next() throws LexerException {
                Token t = super.next();

                if (!t.getType().equals(EOF)) {
                    encounteredTypes.put(t.getType(), true);
                }

                return t;
            }
        };
    }
}
