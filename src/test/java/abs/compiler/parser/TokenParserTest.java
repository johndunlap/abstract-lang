package abs.compiler.parser;

import static abs.compiler.lexer.Type.IDENTIFIER;
import static abs.compiler.lexer.Type.PARADIGM;
import static abs.compiler.lexer.Type.SEMICOLON;
import static abs.compiler.parser.paradigms.ParadigmEnum.OOP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import abs.compiler.Options;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.paradigms.ParadigmDeclaration;
import abs.compiler.parser.paradigms.ParadigmEnum;
import org.junit.Test;

import java.util.List;

public class TokenParserTest extends AbstractParserTest {

    @Test
    public void testMatchMethodWithValidCode() throws ParseErrorException {
        Options options = new Options();
        CharacterStream characterStream = new CharacterStream("paradigm oop;", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        List<Token> tokens = new GenericParser<>(tokenStream, options)
                .match(PARADIGM)
                .match(IDENTIFIER, "oop")
                .match(SEMICOLON)
                .tokens();

        ParadigmDeclaration paradigm = new ParadigmDeclaration(tokens);

        // Verify content of tokens
        assertEquals(3, tokens.size());
        assertEquals(PARADIGM, tokens.get(0).getType());
        assertEquals(IDENTIFIER, tokens.get(1).getType());
        assertEquals(OOP, ParadigmEnum.findByName(tokens.get(1).getValue()));
        assertEquals(SEMICOLON, tokens.get(2).getType());

        // Verify content of paradigm AST node
        assertNotNull(paradigm);
        assertEquals(OOP, paradigm.getParadigm());
    }

    @Test(expected = ParseErrorException.class)
    public void testMatchMethodWithInvalidCode() throws ParseErrorException {
        Options options = new Options();
        CharacterStream characterStream = new CharacterStream("paradigm oop", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        new GenericParser<>(tokenStream, options)
                .match(PARADIGM)
                .match(IDENTIFIER, "oop")
                .match(SEMICOLON)
                .tokens();
    }

    /*
    @Test
    public void testNextMethod() throws ParserException {
        Parser parser = buildParser("paradigm oop;");

        assertEquals(Type.IDENTIFIER, parser.next().getType());
        assertEquals(Type.IDENTIFIER, parser.next().getType());
        assertEquals(Type.SEMICOLON, parser.next().getType());
    }

    @Test
    public void testNextMethodWithEmptyInput() throws ParserException {
        Parser parser = buildParser("");
        assertEquals(Type.EOF, parser.next().getType());
    }

    @Test
    public void testPeekMethod() throws ParserException {
        Parser parser = buildParser("Integer x = 5;");

        assertEquals(Type.IDENTIFIER, parser.peek().getType());
        assertEquals(Type.IDENTIFIER, parser.peek().getType());
   }

    @Test
    public void testPeekMethodWithEmptyInput() throws ParserException {
        Parser parser = buildParser("");
        assertEquals(Type.EOF, parser.peek().getType());
    }

    @Test
    public void testEatMethod() throws ParserException {
        Parser parser = buildParser("Float x = 5;");
        parser.eat();

        assertEquals(Type.IDENTIFIER, parser.peek().getType());
    }

    @Test
    public void testParsePackageStatementMethodWithMissingSemicolon() throws ParserException {
        String packageName = "io.abs.core";
        Parser parser = buildParser("package " + packageName);
        parser.parsePackageStatement();

        assertEquals(1, parser.errorCount());
        assertEquals("Missing semicolon", parser.getErrorHandler().getErrors().get(0).getMessage());
    }

    @Test
    public void testParsePackageStatementMethod() throws ParserException {
        String packageName = "io.abs.core";
        Parser parser = buildParser("package " + packageName + ";");
        PackageNode packageNode = parser.parsePackageStatement();

        assertEquals(0, parser.errorCount());
        assertEquals(packageName, packageNode.getNamespace().toString());
    }

    @Test
    public void testParsePackageStatementMethodWithWrongKeyword() throws ParserException {
        String packageName = "io.abs.core";
        Parser parser = buildParser("module " + packageName + ";");
        PackageNode packageNode = parser.parsePackageStatement();

        assertEquals(1, parser.errorCount());
        assertEquals("Expected \"package\" but found \"module\" instead", parser.getErrorHandler().getErrors().get(0).getMessage());
        assertEquals(packageName, packageNode.getNamespace().toString());
    }

    @Test
    public void testEatCountMethod() throws ParserException {
        Parser parser = buildParser("Float x = 5;");
        parser.eat(2);

        assertEquals(Type.EQ, parser.peek().getType());
    }

    @Test
    public void testEatMethodWithEmptyInput() throws ParserException {
        Parser parser = buildParser("");
        parser.eat();

        assertEquals(Type.EOF, parser.peek().getType());
    }

    @Test
    public void testPeekTypeListMethod() throws ParserException {
        Parser parser = buildParser("paradigm oop;");
        List<Type> tokens = parser.peekTypeList(3);

        assertEquals(3, tokens.size());
        assertEquals(Type.IDENTIFIER, tokens.get(0));
        assertEquals(Type.IDENTIFIER, tokens.get(1));
        assertEquals(Type.SEMICOLON, tokens.get(2));
        assertEquals(Type.IDENTIFIER, parser.peek().getType());
    }

    @Test
    public void testPeekTypeListMethodWithEmptyInput() throws ParserException {
        Parser parser = buildParser("");
        List<Type> tokens = parser.peekTypeList(1);

        assertEquals(1, tokens.size());
        assertEquals(Type.EOF, tokens.get(0));
    }

    @Test
    public void testPeekTypeListMethodWithIncompleteInput() throws ParserException {
        Parser parser = buildParser("paradigm");
        List<Type> tokens = parser.peekTypeList(2);

        assertEquals(2, tokens.size());
        assertEquals(Type.IDENTIFIER, tokens.get(0));
        assertEquals(Type.EOF, tokens.get(1));
    }

    @Test
    public void testPeekListMethod() throws ParserException {
        Parser parser = buildParser("paradigm oop;");
        List<Token> tokens = parser.peekList(3);

        assertEquals(3, tokens.size());
        assertEquals(Type.IDENTIFIER, tokens.get(0).getType());
        assertEquals(Type.IDENTIFIER, tokens.get(1).getType());
        assertEquals(Type.SEMICOLON, tokens.get(2).getType());
        assertEquals(Type.IDENTIFIER, parser.peek().getType());
    }

    @Test
    public void testPeekListMethodWithEmptyInput() throws ParserException {
        Parser parser = buildParser("");
        List<Token> tokens = parser.peekList(1);

        assertEquals(1, tokens.size());
        assertEquals(Type.EOF, tokens.get(0).getType());
    }

    @Test
    public void testPeekListMethodWithIncompleteInput() throws ParserException {
        Parser parser = buildParser("paradigm");
        List<Token> tokens = parser.peekList(2);

        assertEquals(2, tokens.size());
        assertEquals(Type.IDENTIFIER, tokens.get(0).getType());
        assertEquals(Type.EOF, tokens.get(1).getType());
    }
*/
}
