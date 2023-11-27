package abs.compiler.parser.oop.packagedecl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.AbstractParserTest;
import abs.compiler.parser.Node;
import org.junit.Test;

public class PackageDeclParserTest extends AbstractParserTest {
    @Test
    public void testPackageDecl() {
        Options options = new Options();
        TokenStream tokenStream = buildTokenStream("package abs.compiler.parser.oop.packagedecl;");
        PackageParser parser = new PackageParser(tokenStream, options);
        Node node = parser.parse();

        assertNotError(node);

        PackageDecl packageDecl = (PackageDecl) node;

        assertEquals("abs", packageDecl.getName());
        assertEquals("compiler", packageDecl.getChild().getName());
        assertEquals("parser", packageDecl.getChild().getChild().getName());
        assertEquals("oop", packageDecl.getChild().getChild().getChild().getName());
        assertEquals("packagedecl", packageDecl.getChild().getChild().getChild().getChild().getName());
        assertNull(packageDecl.getChild().getChild().getChild().getChild().getChild());
    }
}
