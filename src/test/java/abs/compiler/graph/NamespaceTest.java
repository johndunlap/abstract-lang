package abs.compiler.graph;

import abs.compiler.model.Namespace;
import abs.compiler.parser.AbstractParserTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NamespaceTest extends AbstractParserTest {
    @Test
    public void testToStringMethodWithStringNamespaceConstructor() {
        Namespace namespace1 = new Namespace("io");
        Namespace namespace2 = new Namespace("abs", namespace1);
        Namespace namespace3 = new Namespace("cli", namespace2);

        assertEquals("io.abs.cli", namespace3.toString());
    }

    @Test
    public void testToStringMethodWithSetParentMethod() {
        Namespace namespace1 = new Namespace("io");
        Namespace namespace2 = new Namespace("abs");
        namespace2.setParent(namespace1);
        Namespace namespace3 = new Namespace("cli");
        namespace3.setParent(namespace2);

        assertEquals("io.abs.cli", namespace3.toString());
    }

    @Test
    public void testToStringMethodWithAddChildMethod() {
        Namespace namespace1 = new Namespace("io");
        Namespace namespace2 = new Namespace("abs");
        namespace1.addChild(namespace2);
        Namespace namespace3 = new Namespace("cli");
        namespace2.addChild(namespace3);

        assertEquals("io.abs.cli", namespace3.toString());
    }

/*
    @Test
    public void testParseNamespaceMethod() throws LexerException {
        TokenStream tokenStream = buildTokenStream("io.abs.cli;");
        ErrorHandler errorHandler = new ErrorHandler();
        Namespace namespace = new Namespace(tokenStream, errorHandler);

        assertEquals("io.abs.cli", namespace.toString());
        assertEquals(0, errorHandler.errorCount());
    }

    @Test
    public void testToStringMethodWithParseNamespaceMethod() throws LexerException {
        TokenStream tokenStream = buildTokenStream("package io.abs.cli;");
        ErrorHandler errorHandler = new ErrorHandler();
        PackageDeclaration packageDeclaration = new PackageDeclaration(tokenStream, errorHandler);

        errorHandler.printErrors();

        assertEquals(0, errorHandler.errorCount());
        assertEquals("io.abs.cli", packageDeclaration.getNamespace().toString());
    }
*/

    /*
    @Test
    public void testParserConstructor() throws ParserException {
        Parser parser = buildParser("a.b.c");

        Namespace aNamespace = new Namespace(parser);

        assertNotNull(aNamespace);
        assertNull(aNamespace.getParent());
        assertNotNull(aNamespace.getChild());
        assertNotNull(aNamespace.getName());
        assertEquals("a", aNamespace.getName());

        Namespace bNamespace = aNamespace.getChild();

        assertNotNull(bNamespace);
        assertNotNull(bNamespace.getParent());
        assertEquals(aNamespace, bNamespace.getParent());
        assertNotNull(bNamespace.getChild());
        assertNotNull(bNamespace.getName());
        assertEquals("b", bNamespace.getName());

        Namespace cNamespace = bNamespace.getChild();

        assertNotNull(cNamespace);
        assertNotNull(cNamespace.getParent());
        assertEquals(bNamespace, cNamespace.getParent());
        assertNull(cNamespace.getChild());
        assertNotNull(cNamespace.getName());
        assertEquals("c", cNamespace.getName());
    }

    @Test
    public void testParseError() throws ParserException {
        Parser parser = buildParser("5 a.b.c");
        ErrorHandler errorHandler = parser.getErrorHandler();

        Namespace namespace = new Namespace(parser);
        assertNull(namespace.getName());
        assertNull(namespace.getParent());
        assertNull(namespace.getChild());
        assertEquals(1, errorHandler.errorCount());
        assertEquals(0, errorHandler.fatalErrorCount());
        assertEquals("ParseError{message='Found 5 when identifier expected', range=Range{startColumn=1, stopColumn=2, startLine=1, stopLine=1, startPosition=1, stopPosition=2, length=1}}", errorHandler.getErrors().get(0).toString());
    }
*/
}
