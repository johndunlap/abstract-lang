package abs.compiler.parser.paradigms;

import static abs.compiler.lexer.Type.EOF;
import static abs.compiler.lexer.Type.IDENTIFIER;
import static abs.compiler.lexer.Type.SEMICOLON;
import static abs.compiler.lexer.Type.WHOLE_NUMBER_LITERAL;
import static org.junit.Assert.assertEquals;
import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.AbstractParserTest;
import abs.compiler.parser.ErrorNode;
import abs.compiler.parser.Node;
import org.junit.Test;

public class ParadigmParserTest extends AbstractParserTest {
    @Test
    public void testParseValidOopParadigm() {
        Options options = new Options();
        TokenStream tokenStream = buildTokenStream("paradigm oop;");
        ParadigmParser parser = new ParadigmParser(tokenStream, options);
        Node result = parser.parse();

        assertNotError(result);
        assertEquals(ParadigmDeclaration.class, result.getClass());

        ParadigmDeclaration paradigmDeclaration = (ParadigmDeclaration) result;

        assertEquals(ParadigmEnum.OOP, paradigmDeclaration.getParadigm());
        assertEquals(3, paradigmDeclaration.getTokens().size());
        assertEquals(IDENTIFIER, paradigmDeclaration.getTokens().get(0).getType());
        assertEquals("paradigm", paradigmDeclaration.getTokens().get(0).getValue());
        assertEquals(IDENTIFIER, paradigmDeclaration.getTokens().get(1).getType());
        assertEquals("oop", paradigmDeclaration.getTokens().get(1).getValue());
        assertEquals(SEMICOLON, paradigmDeclaration.getTokens().get(2).getType());
    }

    @Test
    public void testParseInvalidFirstToken() {
        Options options = new Options();
        TokenStream tokenStream = buildTokenStream("invalid");
        ParadigmParser parser = new ParadigmParser(tokenStream, options);
        Node result = parser.parse();

        assertEquals(ErrorNode.class, result.getClass());

        ErrorNode errorNode = (ErrorNode) result;
        assertEquals("Expected \"paradigm\" but found \"invalid\" instead", errorNode.getMessage());
        assertEquals(1, errorNode.getTokens().size());
        assertEquals(IDENTIFIER, errorNode.getTokens().get(0).getType());
        assertEquals("invalid", errorNode.getTokens().get(0).getValue());
    }

    @Test
    public void testParseInvalidSecondTokenScenario1() {
        Options options = new Options();
        TokenStream tokenStream = buildTokenStream("paradigm other");
        ParadigmParser parser = new ParadigmParser(tokenStream, options);
        Node result = parser.parse();

        assertEquals(ErrorNode.class, result.getClass());

        ErrorNode errorNode = (ErrorNode) result;
        assertEquals("\"other\" is not a valid paradigm", errorNode.getMessage());
        assertEquals(2, errorNode.getTokens().size());
        assertEquals(IDENTIFIER, errorNode.getTokens().get(0).getType());
        assertEquals("paradigm", errorNode.getTokens().get(0).getValue());
        assertEquals(IDENTIFIER, errorNode.getTokens().get(1).getType());
        assertEquals("other", errorNode.getTokens().get(1).getValue());
    }

    @Test
    public void testParseInvalidSecondTokenScenario2() {
        Options options = new Options();
        TokenStream tokenStream = buildTokenStream("paradigm 5");
        ParadigmParser parser = new ParadigmParser(tokenStream, options);
        Node result = parser.parse();

        assertEquals(ErrorNode.class, result.getClass());

        ErrorNode errorNode = (ErrorNode) result;
        assertEquals("Expected identifier but found \"5\" instead", errorNode.getMessage());
        assertEquals(2, errorNode.getTokens().size());
        assertEquals(IDENTIFIER, errorNode.getTokens().get(0).getType());
        assertEquals("paradigm", errorNode.getTokens().get(0).getValue());
        assertEquals(WHOLE_NUMBER_LITERAL, errorNode.getTokens().get(1).getType());
        assertEquals("5", errorNode.getTokens().get(1).getValue());
    }

    @Test
    public void testParseInvalidThirdTokenScenario1() {
        Options options = new Options();
        TokenStream tokenStream = buildTokenStream("paradigm oop");
        ParadigmParser parser = new ParadigmParser(tokenStream, options);
        Node result = parser.parse();

        assertEquals(ErrorNode.class, result.getClass());

        ErrorNode errorNode = (ErrorNode) result;
        assertEquals("Expected \";\" but found \"EOF\" instead", errorNode.getMessage());
        assertEquals(3, errorNode.getTokens().size());
        assertEquals(IDENTIFIER, errorNode.getTokens().get(0).getType());
        assertEquals("paradigm", errorNode.getTokens().get(0).getValue());
        assertEquals(IDENTIFIER, errorNode.getTokens().get(1).getType());
        assertEquals("oop", errorNode.getTokens().get(1).getValue());
        assertEquals(EOF, errorNode.getTokens().get(2).getType());
    }

    @Test
    public void testParseInvalidThirdTokenScenario2() {
        Options options = new Options();
        TokenStream tokenStream = buildTokenStream("paradigm oop\nimport com.abs.whatever.MyClass;");
        ParadigmParser parser = new ParadigmParser(tokenStream, options);
        Node result = parser.parse();

        assertEquals(ErrorNode.class, result.getClass());

        ErrorNode errorNode = (ErrorNode) result;
        assertEquals("Expected \";\" but found \"import\" instead", errorNode.getMessage());
        assertEquals(3, errorNode.getTokens().size());
        assertEquals(IDENTIFIER, errorNode.getTokens().get(0).getType());
        assertEquals("paradigm", errorNode.getTokens().get(0).getValue());
        assertEquals(IDENTIFIER, errorNode.getTokens().get(1).getType());
        assertEquals("oop", errorNode.getTokens().get(1).getValue());
        assertEquals(IDENTIFIER, errorNode.getTokens().get(2).getType());
        assertEquals("import", errorNode.getTokens().get(2).getValue());
    }
}
