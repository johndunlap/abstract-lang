package abs.compiler.parser.oop.packages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.AbstractParserTest;
import abs.compiler.parser.Node;
import org.junit.Test;

public class PackageDeclarationParserTest extends AbstractParserTest {
    @Test
    public void testPackageDecl() {
        Options options = new Options();
        TokenStream tokenStream = buildTokenStream("package abs.compiler.parser.oop.packages;");
        PackageDeclarationParser parser = new PackageDeclarationParser(tokenStream, options);
        Node node = parser.parse();

        assertNotError(node);

        PackageDeclaration packageDeclaration = (PackageDeclaration) node;

/*
        assertEquals("abs", packageDeclaration.getName());
        assertEquals("compiler", packageDeclaration.getChild().getName());
        assertEquals("parser", packageDeclaration.getChild().getChild().getName());
        assertEquals("oop", packageDeclaration.getChild().getChild().getChild().getName());
        assertEquals("packages", packageDeclaration.getChild().getChild().getChild().getChild().getName());
        assertNull(packageDeclaration.getChild().getChild().getChild().getChild().getChild());
*/
    }
}
