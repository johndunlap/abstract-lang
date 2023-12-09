package abs.compiler.parser.oop.packages;

import static abs.compiler.lexer.Type.IDENTIFIER;
import static abs.compiler.lexer.Type.PERIOD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import abs.compiler.Options;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.AbstractParserTest;
import abs.compiler.parser.Node;
import abs.compiler.parser.RootNode;
import org.junit.Test;

public class PackageSegmentParserTest extends AbstractParserTest {
    @Test
    public void testPackageSegmentParserWithValidInput() {
        Options options = new Options();
        CharacterStream characterStream = new CharacterStream(".compiler.parser.oop.Packages", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        PackageSegmentParser parser = new PackageSegmentParser(tokenStream, options);
        Node node = parser.parse(new RootNode());

        assertNotError(node);

        PackageSegmentNode packageSegment = (PackageSegmentNode) node;
        assertEquals(1, packageSegment.getChildren().size());
        assertEquals(2, packageSegment.getTokens().size());
        assertEquals("compiler", packageSegment.getName());
        assertEquals(PERIOD, packageSegment.getTokens().get(0).getType());
        assertEquals(".", packageSegment.getTokens().get(0).getValue());
        assertEquals(IDENTIFIER, packageSegment.getTokens().get(1).getType());
        assertEquals("compiler", packageSegment.getTokens().get(1).getValue());

        packageSegment = (PackageSegmentNode) packageSegment.getChild(0);
        assertEquals(1, packageSegment.getChildren().size());
        assertEquals(2, packageSegment.getTokens().size());
        assertEquals("parser", packageSegment.getName());
        assertEquals(PERIOD, packageSegment.getTokens().get(0).getType());
        assertEquals(".", packageSegment.getTokens().get(0).getValue());
        assertEquals(IDENTIFIER, packageSegment.getTokens().get(1).getType());
        assertEquals("parser", packageSegment.getTokens().get(1).getValue());

        packageSegment = (PackageSegmentNode) packageSegment.getChild(0);
        assertEquals(1, packageSegment.getChildren().size());
        assertEquals(2, packageSegment.getTokens().size());
        assertEquals("oop", packageSegment.getName());
        assertEquals(PERIOD, packageSegment.getTokens().get(0).getType());
        assertEquals(".", packageSegment.getTokens().get(0).getValue());
        assertEquals(IDENTIFIER, packageSegment.getTokens().get(1).getType());
        assertEquals("oop", packageSegment.getTokens().get(1).getValue());

        packageSegment = (PackageSegmentNode) packageSegment.getChild(0);
        assertEquals(0, packageSegment.getChildren().size());
        assertEquals(2, packageSegment.getTokens().size());
        assertEquals("Packages", packageSegment.getName());
        assertEquals(PERIOD, packageSegment.getTokens().get(0).getType());
        assertEquals(".", packageSegment.getTokens().get(0).getValue());
        assertEquals(IDENTIFIER, packageSegment.getTokens().get(1).getType());
        assertEquals("Packages", packageSegment.getTokens().get(1).getValue());

        assertTrue(packageSegment.getChildren().isEmpty());
    }
}
