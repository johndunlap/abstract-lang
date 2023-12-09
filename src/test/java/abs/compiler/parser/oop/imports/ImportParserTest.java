package abs.compiler.parser.oop.imports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import abs.compiler.Options;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.AbstractParserTest;
import abs.compiler.parser.ErrorNode;
import abs.compiler.parser.Node;
import abs.compiler.parser.RootNode;
import abs.compiler.parser.oop.packages.PackageSegmentNode;
import org.junit.Test;

public class ImportParserTest extends AbstractParserTest {
    @Test
    public void testParseSingleValidImportStatement() {
        Options options = new Options();
        CharacterStream characterStream = new CharacterStream("import abs.compiler.test.ModuleOne;", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        ImportParser parser = new ImportParser(tokenStream, options);
        Node node = parser.parse(new RootNode());
        assertNotError(node);

        assertEquals(ImportDefinitionNode.class, node.getClass());
        assertEquals(1, node.getChildren().size());
        assertEquals(2, node.getTokens().size());

        PackageSegmentNode packageSegmentNode = ((PackageSegmentNode)node.getChild(0));
        assertEquals("abs", packageSegmentNode.getName());

        packageSegmentNode = ((PackageSegmentNode)packageSegmentNode.getChild(0));
        assertEquals("compiler", packageSegmentNode.getName());

        packageSegmentNode = ((PackageSegmentNode)packageSegmentNode.getChild(0));
        assertEquals("test", packageSegmentNode.getName());

        packageSegmentNode = ((PackageSegmentNode)packageSegmentNode.getChild(0));
        assertEquals("ModuleOne", packageSegmentNode.getName());
    }

    @Test
    public void testParseSingleImportStatementWithInvalidLastSegment() {
        Options options = new Options();
        CharacterStream characterStream = new CharacterStream("import abs.compiler.test.moduleOne;", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        ImportParser parser = new ImportParser(tokenStream, options);
        Node node = parser.parse(new RootNode());
        assertNotError(node);

        assertEquals(ImportDefinitionNode.class, node.getClass());
        assertEquals(1, node.getChildren().size());
        assertEquals(2, node.getTokens().size());

        PackageSegmentNode packageSegmentNode = ((PackageSegmentNode)node.getChild(0));
        assertEquals("abs", packageSegmentNode.getName());

        packageSegmentNode = ((PackageSegmentNode)packageSegmentNode.getChild(0));
        assertEquals("compiler", packageSegmentNode.getName());

        packageSegmentNode = ((PackageSegmentNode)packageSegmentNode.getChild(0));
        assertEquals("test", packageSegmentNode.getName());

        ErrorNode errorNode = ((ErrorNode)packageSegmentNode.getChild(0));
        assertNotNull(errorNode);
    }
}
