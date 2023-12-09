package abs.compiler.parser;

import static org.junit.Assert.assertEquals;
import abs.compiler.Options;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.oop.OopRootNode;
import abs.compiler.parser.paradigms.ParadigmDeclarationNode;
import org.junit.Before;
import org.junit.Test;

public class RootParserTest extends AbstractParserTest {

    public static final String EXPECTED_DOT_GRAPH1 = "digraph {\n" +
        "    node [shape=ellipse];\n" +
        "      null [label=\"ROOT\", shape=oval];\n" +
        "      null -> 1;\n" +
        "      1 [label=\"PARADIGM OOP\", shape=oval];\n" +
        "      null -> 2;\n" +
        "      2 [label=\"OOP ROOT\", shape=oval];\n" +
        "      2 -> 3;\n" +
        "      3 [label=\"PACKAGE\", shape=oval];\n" +
        "      3 -> 4;\n" +
        "      4 [label=\"abs\", shape=oval];\n" +
        "      4 -> 5;\n" +
        "      5 [label=\"compiler\", shape=oval];\n" +
        "      5 -> 6;\n" +
        "      6 [label=\"Test\", shape=oval];\n" +
        "      2 -> 7;\n" +
        "      7 [label=\"IMPORT LIST\", shape=oval];\n" +
        "}";

    @Before
    public void before() {
        // Without this, the test will fail because other tests will have incremented the sequence.
        AbstractNode.SEQUENCE = 0;
    }

    @Test
    public void testRootParserToDotMethodWithValidSyntax() {
        Options options = new Options();
        CharacterStream characterStream = new CharacterStream("paradigm oop; package abs.compiler.Test;", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        RootParser parser = new RootParser(tokenStream, options);
        Node node = parser.parse(null);

        assertNotError(node);

        String actual = ((RootNode)node).toDot();
        assertEquals(EXPECTED_DOT_GRAPH1, actual);
    }

    @Test
    public void testMissingParadigmDeclarationWithOopDefaultParadigm() {
        Options options = new Options();
        CharacterStream characterStream = new CharacterStream("package abs.compiler.Test;", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        RootParser parser = new RootParser(tokenStream, options);
        Node node = parser.parse(null);

        assertNotError(node);

        assertEquals(2, node.getChildren().size());
        assertEquals(ParadigmDeclarationNode.class, node.getChildren().get(0).getClass());
        assertEquals(OopRootNode.class, node.getChildren().get(1).getClass());

        // Verify that the dot graph is the same with or without the paradigm declaration.
        String actual = ((RootNode)node).toDot();
        assertEquals(EXPECTED_DOT_GRAPH1, actual);
    }

    @Test
    public void testMissingParadigmDeclarationWithoutOopDefaultParadigm() {
        Options options = new Options();
        options.setDefaultParadigm(null);

        CharacterStream characterStream = new CharacterStream("package abs.compiler.Test;", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        RootParser parser = new RootParser(tokenStream, options);
        Node node = parser.parse(null);

        assertNotError(node);

        assertEquals(1, node.getChildren().size());
        assertEquals(ErrorNode.class, node.getChildren().get(0).getClass());
    }
}
