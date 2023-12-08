package abs.compiler.parser;

import static org.junit.Assert.assertEquals;
import abs.compiler.Options;
import abs.compiler.Util;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.TokenStream;
import org.junit.Test;

public class RootParserTest extends AbstractParserTest {
    @Test
    public void testRootParserToDotMethodWithValidSyntax() {
        Options options = new Options();
        CharacterStream characterStream = new CharacterStream("paradigm oop; package abs.compiler.test;", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        RootParser parser = new RootParser(tokenStream, options);
        Node node = parser.parse(null);

        assertNotError(node);

        String expected = "digraph {\n" +
                "    node [shape=ellipse];\n" +
                "      1 [label=\"ROOT\", shape=oval];\n" +
                "      1 -> 2;\n" +
                "      2 [label=\"PARADIGM OOP\", shape=oval];\n" +
                "      1 -> 3;\n" +
                "      3 [label=\"OOP ROOT\", shape=oval];\n" +
                "      3 -> 4;\n" +
                "      4 [label=\"PACKAGE\", shape=oval];\n" +
                "      4 -> 5;\n" +
                "      5 [label=\"abs\", shape=oval];\n" +
                "      5 -> 6;\n" +
                "      6 [label=\"compiler\", shape=oval];\n" +
                "      6 -> 7;\n" +
                "      7 [label=\"test\", shape=oval];\n" +
                "}";

        String actual = ((RootNode)node).toDot();
        assertEquals(expected, actual);
    }
}
