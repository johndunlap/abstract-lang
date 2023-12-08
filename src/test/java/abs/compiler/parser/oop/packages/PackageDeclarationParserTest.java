package abs.compiler.parser.oop.packages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import abs.compiler.Options;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.AbstractParserTest;
import abs.compiler.parser.Node;
import abs.compiler.parser.RootNode;
import org.junit.Test;

public class PackageDeclarationParserTest extends AbstractParserTest {
    @Test
    public void testPackageDecl() {
        Options options = new Options();
        CharacterStream characterStream = new CharacterStream("package abs.compiler.parser.oop.packages;", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        PackageDeclarationParser parser = new PackageDeclarationParser(tokenStream, options);
        Node node = parser.parse(new RootNode());

        assertNotError(node);

        PackageDeclarationNode packageDeclaration = (PackageDeclarationNode) node;

        assertEquals("abs", ((PackageSegmentNode)packageDeclaration.getChild(0)).getName());
/*
        assertEquals("compiler", packageDeclaration.getChild(0).getChild(0).getName());
        assertEquals("parser", packageDeclaration.getChild().getChild().getName());
        assertEquals("oop", packageDeclaration.getChild().getChild().getChild().getName());
        assertEquals("packages", packageDeclaration.getChild().getChild().getChild().getChild().getName());
        assertNull(packageDeclaration.getChild().getChild().getChild().getChild().getChild());
*/
    }
}
