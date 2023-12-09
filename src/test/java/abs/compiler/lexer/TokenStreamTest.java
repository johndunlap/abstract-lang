package abs.compiler.lexer;

import static abs.compiler.lexer.Type.ADD;
import static abs.compiler.lexer.Type.ADDADD;
import static abs.compiler.lexer.Type.ADDEQ;
import static abs.compiler.lexer.Type.AND;
import static abs.compiler.lexer.Type.ANDAND;
import static abs.compiler.lexer.Type.ANDEQ;
import static abs.compiler.lexer.Type.COLON;
import static abs.compiler.lexer.Type.COLONCOLON;
import static abs.compiler.lexer.Type.COMMA;
import static abs.compiler.lexer.Type.COMMENT;
import static abs.compiler.lexer.Type.COMMENTL;
import static abs.compiler.lexer.Type.DIV;
import static abs.compiler.lexer.Type.DIVEQ;
import static abs.compiler.lexer.Type.DOLLAR;
import static abs.compiler.lexer.Type.DQUOTE;
import static abs.compiler.lexer.Type.EOF;
import static abs.compiler.lexer.Type.EQ;
import static abs.compiler.lexer.Type.EQEQ;
import static abs.compiler.lexer.Type.FLOAT_LITERAL;
import static abs.compiler.lexer.Type.GT;
import static abs.compiler.lexer.Type.GTEQ;
import static abs.compiler.lexer.Type.HEX_LITERAL;
import static abs.compiler.lexer.Type.IDENTIFIER;
import static abs.compiler.lexer.Type.IMPORT;
import static abs.compiler.lexer.Type.LBRACE;
import static abs.compiler.lexer.Type.LBRACKET;
import static abs.compiler.lexer.Type.LF;
import static abs.compiler.lexer.Type.LPAREN;
import static abs.compiler.lexer.Type.LT;
import static abs.compiler.lexer.Type.LTEQ;
import static abs.compiler.lexer.Type.MOD;
import static abs.compiler.lexer.Type.MODEQ;
import static abs.compiler.lexer.Type.MUL;
import static abs.compiler.lexer.Type.MULEQ;
import static abs.compiler.lexer.Type.NOT;
import static abs.compiler.lexer.Type.NOTEQ;
import static abs.compiler.lexer.Type.OR;
import static abs.compiler.lexer.Type.OREQ;
import static abs.compiler.lexer.Type.OROR;
import static abs.compiler.lexer.Type.PARADIGM;
import static abs.compiler.lexer.Type.PERIOD;
import static abs.compiler.lexer.Type.PIPE;
import static abs.compiler.lexer.Type.QUESTION;
import static abs.compiler.lexer.Type.RBRACE;
import static abs.compiler.lexer.Type.RBRACKET;
import static abs.compiler.lexer.Type.RPAREN;
import static abs.compiler.lexer.Type.SEMICOLON;
import static abs.compiler.lexer.Type.SL;
import static abs.compiler.lexer.Type.SLEQ;
import static abs.compiler.lexer.Type.SR;
import static abs.compiler.lexer.Type.SREQ;
import static abs.compiler.lexer.Type.STRCONST;
import static abs.compiler.lexer.Type.SUB;
import static abs.compiler.lexer.Type.SUBEQ;
import static abs.compiler.lexer.Type.SUBSUB;
import static abs.compiler.lexer.Type.TAB;
import static abs.compiler.lexer.Type.TILDE;
import static abs.compiler.lexer.Type.TILDEEQ;
import static abs.compiler.lexer.Type.WHITESPACE;
import static abs.compiler.lexer.Type.WHOLE_NUMBER_LITERAL;
import static abs.compiler.lexer.Type.XOR;
import static abs.compiler.lexer.Type.XOREQ;
import static abs.compiler.lexer.Type.values;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import abs.compiler.Options;
import abs.compiler.Util;
import abs.compiler.exception.IllegalCharacterException;
import abs.compiler.exception.LexerException;
import abs.compiler.exception.UnexpectedEndOfInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenStreamTest {
    public static Map<Type, Boolean> encounteredTypes = new HashMap<>();

    @Test
    public void testVariablesEmbeddedInStringWithBraces() throws LexerException {
        Type[] expectedTypes = {DQUOTE,STRCONST,DOLLAR,LBRACE, IDENTIFIER,PERIOD, IDENTIFIER,RBRACE,STRCONST,DQUOTE};
        String[] expectedValues = {"\"","This ","$","{","car",".","name","}"," gets terrible gas mileage","\""};
        String input = "\"This ${car.name} gets terrible gas mileage\"";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testVariablesEmbeddedInStringWithoutBraces() throws LexerException {
        Type[] expectedTypes = {DQUOTE,STRCONST,DOLLAR,IDENTIFIER,PERIOD,IDENTIFIER,STRCONST,DQUOTE};
        String[] expectedValues = {"\"","This ","$","car",".","name"," gets terrible gas mileage","\""};
        String input = "\"This $car.name gets terrible gas mileage\"";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testEscapedVariablesEmbeddedInStringWithBraces() throws LexerException {
        Type[] expectedTypes = {DQUOTE,STRCONST,DQUOTE};
        String[] expectedValues = {"\"","This \\${car.name} gets terrible gas mileage","\""};
        String input = "\"This \\${car.name} gets terrible gas mileage\"";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testEscapedVariablesEmbeddedInStringWithoutBraces() throws LexerException {
        Type[] expectedTypes = {DQUOTE,STRCONST,DQUOTE};
        String[] expectedValues = {"\"","This \\$car.name gets terrible gas mileage","\""};
        String input = "\"This \\$car.name gets terrible gas mileage\"";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testPeriod() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,PERIOD, IDENTIFIER,LPAREN,RPAREN,SEMICOLON};
        String[] expectedValues = {"obj",".","method","(",")",";"};
        String input = "obj.method();";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testPipe() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,LPAREN,RPAREN,LF,TAB,PIPE,WHITESPACE, IDENTIFIER,LPAREN,RPAREN,SEMICOLON};
        String[] expectedValues = {"functionA","(",")","\n","\t","|>"," ","functionB","(",")",";"};
        String input = "functionA()\n\t|> functionB();";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBitwiseShiftLeftEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE,SLEQ,WHITESPACE,HEX_LITERAL,SEMICOLON};
        String[] expectedValues = {"x"," ","<<="," ","0xFF",";"};
        String input = "x <<= 0xFF;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBitwiseShiftLeft() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE,EQ,WHITESPACE, IDENTIFIER,WHITESPACE,SL,WHITESPACE,WHOLE_NUMBER_LITERAL,SEMICOLON};
        String[] expectedValues = {"x"," ","="," ","x"," ","<<"," ","2",";"};
        String input = "x = x << 2;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBitwiseShiftRightEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE,SREQ,WHITESPACE,HEX_LITERAL,SEMICOLON};
        String[] expectedValues = {"x"," ",">>="," ","0xFF",";"};
        String input = "x >>= 0xFF;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBitwiseShiftRight() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE,EQ,WHITESPACE, IDENTIFIER,WHITESPACE,SR,WHITESPACE,WHOLE_NUMBER_LITERAL,SEMICOLON};
        String[] expectedValues = {"x"," ","="," ","x"," ",">>"," ","2",";"};
        String input = "x = x >> 2;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBitwiseXorEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE,XOREQ,WHITESPACE,HEX_LITERAL,SEMICOLON};
        String[] expectedValues = {"x"," ","^="," ","0xFF",";"};
        String input = "x ^= 0xFF;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBitwiseXor() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE,EQ,WHITESPACE, IDENTIFIER,WHITESPACE,XOR,WHITESPACE,HEX_LITERAL,SEMICOLON};
        String[] expectedValues = {"x"," ","="," ","x"," ","^"," ","0xFF",";"};
        String input = "x = x ^ 0xFF;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testLogicalOr() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,LPAREN, IDENTIFIER,EQEQ,IDENTIFIER,WHITESPACE,OROR,WHITESPACE, IDENTIFIER,LT, FLOAT_LITERAL,RPAREN,LBRACE,RBRACE};
        String[] expectedValues = {"if","(","a","==","true"," ","||"," ","b","<","7.5",")","{","}"};
        String input = "if(a==true || b<7.5){}";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBitwiseOrEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE,OREQ,WHITESPACE,HEX_LITERAL,SEMICOLON};
        String[] expectedValues = {"x"," ","|="," ","0xFF",";"};
        String input = "x |= 0xFF;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBitwiseOr() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE,EQ,WHITESPACE, IDENTIFIER,WHITESPACE,OR,WHITESPACE,HEX_LITERAL,SEMICOLON};
        String[] expectedValues = {"x"," ","="," ","x"," ","|"," ","0xFF",";"};
        String input = "x = x | 0xFF;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBitwiseAndEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE,ANDEQ,WHITESPACE,HEX_LITERAL,SEMICOLON};
        String[] expectedValues = {"x"," ","&="," ","0xFF",";"};
        String input = "x &= 0xFF;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBitwiseAnd() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE,EQ,WHITESPACE, IDENTIFIER,WHITESPACE,AND,WHITESPACE,HEX_LITERAL,SEMICOLON};
        String[] expectedValues = {"x"," ","="," ","x"," ","&"," ","0xFF",";"};
        String input = "x = x & 0xFF;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testLogicalAnd() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,LPAREN, IDENTIFIER,EQEQ,IDENTIFIER,WHITESPACE,ANDAND,WHITESPACE, IDENTIFIER,LT, FLOAT_LITERAL,RPAREN,LBRACE,RBRACE};
        String[] expectedValues = {"if","(","a","==","true"," ","&&"," ","b","<","7.5",")","{","}"};
        String input = "if(a==true && b<7.5){}";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testLogicalNotEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,LPAREN, IDENTIFIER,NOTEQ, WHOLE_NUMBER_LITERAL,RPAREN,LBRACE,RBRACE};
        String[] expectedValues = {"if","(","x","!=","5",")","{","}"};
        String input = "if(x!=5){}";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testLogicalNot() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,LPAREN,NOT,IDENTIFIER,RPAREN,LBRACE,RBRACE};
        String[] expectedValues = {"if","(","!","true",")","{","}"};
        String input = "if(!true){}";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testModulusEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,MODEQ, WHOLE_NUMBER_LITERAL,SEMICOLON};
        String[] expectedValues = {"x","%=","2",";"};
        String input = "x%=2;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testModulus() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,EQ, IDENTIFIER,MOD, WHOLE_NUMBER_LITERAL,SEMICOLON};
        String[] expectedValues = {"x","=","y","%","2",";"};
        String input = "x=y%2;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testDivideEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,DIVEQ, WHOLE_NUMBER_LITERAL,SEMICOLON};
        String[] expectedValues = {"x","/=","2",";"};
        String input = "x/=2;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testMultiplyEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,MULEQ, IDENTIFIER,SEMICOLON};
        String[] expectedValues = {"x","*=","y",";"};
        String input = "x*=y;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testDecrement() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,SUBSUB,SEMICOLON};
        String[] expectedValues = {"x","--",";"};
        String input = "x--;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testSubEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,SUBEQ, IDENTIFIER,SEMICOLON};
        String[] expectedValues = {"x","-=","y",";"};
        String input = "x-=y;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testIncrement() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,ADDADD,SEMICOLON};
        String[] expectedValues = {"x","++",";"};
        String input = "x++;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testAddEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,ADDEQ, IDENTIFIER,SEMICOLON};
        String[] expectedValues = {"x","+=","y",";"};
        String input = "x+=y;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testTildeEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,TILDEEQ, IDENTIFIER,SEMICOLON};
        String[] expectedValues = {"x","~=","y",";"};
        String input = "x~=y;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testTilde() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,EQ, IDENTIFIER,TILDE, IDENTIFIER,SEMICOLON};
        String[] expectedValues = {"x","=","y","~","z",";"};
        String input = "x=y~z;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testTypeCast() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,WHITESPACE, IDENTIFIER,EQ, IDENTIFIER,COLONCOLON,IDENTIFIER,SEMICOLON};
        String[] expectedValues = {"Integer"," ","y","=","x","::","Integer",";"};
        String input = "Integer y=x::Integer;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testTernaryOperator() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,EQ, IDENTIFIER,EQEQ, WHOLE_NUMBER_LITERAL,QUESTION, IDENTIFIER,COLON, IDENTIFIER,SEMICOLON};
        String[] expectedValues = {"y","=","b","==","6","?","b",":","c",";"};
        String input = "y=b==6?b:c;";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testBrackets() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,EQ,LBRACKET, WHOLE_NUMBER_LITERAL,COMMA, WHOLE_NUMBER_LITERAL,COMMA, WHOLE_NUMBER_LITERAL,RBRACKET};
        String[] expectedValues = {"y","=","[","1",",","2",",","3","]"};
        String input = "y=[1,2,3]";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testIfStatementWithLessThanOrEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,LPAREN, IDENTIFIER,LTEQ, IDENTIFIER,RPAREN,LBRACE, IDENTIFIER,EQ, WHOLE_NUMBER_LITERAL,SEMICOLON,RBRACE};
        String[] expectedValues = {"if","(","a","<=","b",")","{","c","=","5",";","}"};
        String input = "if(a<=b){c=5;}";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testIfStatementWithLessThan() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,LPAREN, IDENTIFIER,LT, IDENTIFIER,RPAREN,LBRACE, IDENTIFIER,EQ, WHOLE_NUMBER_LITERAL,SEMICOLON,RBRACE};
        String[] expectedValues = {"if","(","a","<","b",")","{","c","=","5",";","}"};
        String input = "if(a<b){c=5;}";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testIfStatementWithGreaterThanOrEquals() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,LPAREN, IDENTIFIER,GTEQ, IDENTIFIER,RPAREN,LBRACE, IDENTIFIER,EQ, WHOLE_NUMBER_LITERAL,SEMICOLON,RBRACE};
        String[] expectedValues = {"if","(","a",">=","b",")","{","c","=","5",";","}"};
        String input = "if(a>=b){c=5;}";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testIfStatementWithGreaterThan() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,LPAREN, IDENTIFIER,GT, IDENTIFIER,RPAREN,LBRACE, IDENTIFIER,EQ, WHOLE_NUMBER_LITERAL,SEMICOLON,RBRACE};
        String[] expectedValues = {"if","(","a",">","b",")","{","c","=","5",";","}"};
        String input = "if(a>b){c=5;}";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testMyDearAuntSally() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,EQ, IDENTIFIER,MUL, IDENTIFIER,DIV, IDENTIFIER,ADD,LPAREN, IDENTIFIER,SUB, IDENTIFIER,RPAREN,SEMICOLON};
        String[] expectedValues = {"a","=","a","*","b","/","c","+","(","d","-","e",")",";"};
        String input = "a=a*b/c+(d-e);";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testParenthesis() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER,EQ, IDENTIFIER,DIV,LPAREN, IDENTIFIER,SUB, WHOLE_NUMBER_LITERAL,RPAREN,SEMICOLON};
        String[] expectedValues = {"a","=","a","/","(","b","-","1",")",";"};
        String input = "a=a/(b-1);";

        verifyExpectedOutputs(expectedTypes, expectedValues, input);
    }

    @Test
    public void testEofEmptyInput() throws LexerException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        CharacterStream characterStream = new CharacterStream("", o);
        TokenStream tokenStream = createTokenStream(characterStream, o);

        assertEquals(tokenStream.next().getType(), EOF);
    }

    /**
     * This test verifies that the division operator isn't confused with the beginning of a comment.
     * @throws LexerException throw when something exception happens
     */
    @Test
    public void testCommentAndDivision() throws LexerException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        Token token;
        String input1 = "// This is a test";

        CharacterStream characterStream1 = new CharacterStream(input1, o);
        TokenStream tokenStream1 = createTokenStream(characterStream1, o);

        token = tokenStream1.next();

        assertEquals(COMMENTL, token.getType());

        token = tokenStream1.next();

        assertEquals(COMMENT, token.getType());
        assertEquals(" This is a test", token.getValue());

        String input2 = "a=a/b;";

        CharacterStream characterStream2 = new CharacterStream(input2, o);
        TokenStream tokenStream2 = createTokenStream(characterStream2, o);

        token = tokenStream2.next();
        assertEquals(IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());

        token = tokenStream2.next();
        assertEquals(EQ, token.getType());
        assertEquals("=", token.getValue());

        token = tokenStream2.next();
        assertEquals(IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());

        token = tokenStream2.next();
        assertEquals(DIV, token.getType());
        assertEquals("/", token.getValue());

        token = tokenStream2.next();
        assertEquals(IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());

        token = tokenStream2.next();
        assertEquals(SEMICOLON, token.getType());
        assertEquals(";", token.getValue());
    }

    @Test
    public void testReadStringConstantMethod() throws LexerException, IOException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        Map<String, List<Token>> inputs = new HashMap<>() {{
            put("\"\"", new ArrayList<>(){{
                add(new Token(DQUOTE, new Range(new Position(1,1,1), new Position(2,1,2)), "\""));
                add(new Token(DQUOTE, new Range(new Position(2,1,2), new Position(3,1,3)), "\""));
            }});
            put("\"This is\na\tstring constant\"", new ArrayList<>(){{
                add(new Token(DQUOTE, new Range(new Position(1,1,1), new Position(2,1,2)), "\""));
                add(new Token(STRCONST, new Range(new Position(2,1,2), new Position(27,2,18)), "This is\na\tstring constant"));
                add(new Token(DQUOTE, new Range(new Position(27,2,18), new Position(28,2,19)), "\""));
            }});
        }};

        int x = 0;
        for (String input : inputs.keySet()) {
            CharacterStream characterStream = new CharacterStream(input, o);
            TokenStream tokenStream = createTokenStream(characterStream, o);

            List<Token> expectedTokens = inputs.get(input);
            List<Token> actualTokens = new ArrayList<>();

            while (!tokenStream.peek().getType().equals(EOF)) {
                actualTokens.add(tokenStream.next());
            }

            assertEquals(expectedTokens.size(), actualTokens.size());

            for (int i = 0; i < expectedTokens.size(); i++) {
                Token expectedToken = expectedTokens.get(i);
                Token actualToken = actualTokens.get(i);

                try {
                    // Verify that we got what we expected
                    assertEquals(expectedToken.getType(), actualToken.getType());
                    assertEquals(expectedToken.getValue(), actualToken.getValue());
                    assertEquals(expectedToken.getRange(), actualToken.getRange());

                    // Verify that we can extract the values from the raw text based on the range
                    assertEquals(expectedToken.getValue(), Util.substring(input, expectedToken.getRange()));
                    assertEquals(actualToken.getValue(), Util.substring(input, actualToken.getRange()));
                } catch (Throwable e) {
                    System.err.printf(
                            "INPUT %d OUTPUT %d FAILED\n\tInput    :\"%s\"\n\tExpected :\"%s\"\n\tActual   :\"%s\"\n----------------------------------------\n",
                            x,
                            i,
                            input.replaceAll("(\\r\\n|\\n)", "\\\\n").replaceAll("(\\t)", "\\\\t"),
                            expectedToken.toString().replaceAll("(\\r\\n|\\n)", "\\\\n").replaceAll("(\\t)", "\\\\t"),
                            actualToken.toString().replaceAll("(\\r\\n|\\n)", "\\\\n").replaceAll("(\\t)", "\\\\t")
                    );
                    throw e;
                }
            }

            x++;
        }
    }

    @Test
    public void testReadTabsMethod() throws LexerException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        CharacterStream characterStream = new CharacterStream("\t", o);
        TokenStream tokenStream = createTokenStream(characterStream, o);

        Token t = tokenStream.next();
        assertEquals(TAB, t.getType());
    }

    /**
     * This is an expected lexer ambiguity which must be dealt with during parsing.
     */
    @Test
    public void testGreaterThanAmbiguityInGenerics() throws LexerException {
        Type[] expectedTypes = {IDENTIFIER, LT, IDENTIFIER, COMMA, WHITESPACE, IDENTIFIER, LT, IDENTIFIER, COMMA, WHITESPACE, IDENTIFIER, GT, WHITESPACE, GT};
        String[] expectedValues = {"Map","<","String",","," ","Map","<","String",","," ", "String", ">", " ", ">"};
        String input = "Map<String, Map<String, String> >";
        verifyExpectedOutputs(expectedTypes, expectedValues, input);

        Type[] expectedTypes2 = {IDENTIFIER, LT, IDENTIFIER, COMMA, WHITESPACE, IDENTIFIER, LT, IDENTIFIER, COMMA, WHITESPACE, IDENTIFIER, SR};
        String[] expectedValues2 = {"Map","<","String",","," ","Map","<","String",","," ", "String", ">>"};
        String input2 = "Map<String, Map<String, String>>";
        verifyExpectedOutputs(expectedTypes2, expectedValues2, input2);
    }

    @Test
    public void testReadNameMethod() throws LexerException, IOException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        Map<String,Token> inputs = new HashMap<>();

        for (ReservedWord r : ReservedWord.values()) {
            inputs.put(r.name() + " ", new Token(IDENTIFIER, new Range(new Position(1,1,1), new Position(r.name().length() + 1,1,r.name().length() + 1)), r.name()));
            inputs.put(r.name(), new Token(IDENTIFIER, new Range(new Position(1,1,1), new Position(r.name().length() + 1,1,r.name().length() + 1)), r.name()));
        }

        for (String input : inputs.keySet()) {
            Token expectedToken = inputs.get(input);
            Token actualToken;

            // Verify the token directly via the readName method
            CharacterStream characterStream1 = new CharacterStream(input, o);
            TokenStream tokenStream1 = createTokenStream(characterStream1, o);
            actualToken = tokenStream1.readIdentifier();
            assertEquals(expectedToken, actualToken);
            assertEquals(expectedToken.getValue(), Util.substring(input, actualToken.getRange()));

            // Verify the token indirectly via the next method
            CharacterStream characterStream2 = new CharacterStream(input, o);
            TokenStream tokenStream2 = createTokenStream(characterStream2, o);
            actualToken = tokenStream2.next();
            assertEquals(expectedToken, actualToken);
            assertEquals(expectedToken.getValue(), Util.substring(input, actualToken.getRange()));
        }
    }

    @Test
    public void testReadNumberMethod() throws LexerException, IOException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        Map<String,Token> inputs = new HashMap<>() {{
            put("1234 ", new Token(Type.WHOLE_NUMBER_LITERAL, new Range(new Position(1,1,1), new Position(5,1,5)), "1234"));
            put("1234", new Token(Type.WHOLE_NUMBER_LITERAL, new Range(new Position(1,1,1), new Position(5,1,5)), "1234"));
            put("3.14158265 ", new Token(Type.FLOAT_LITERAL, new Range(new Position(1,1,1), new Position(11,1,11)), "3.14158265"));
            put("3.14158265", new Token(Type.FLOAT_LITERAL, new Range(new Position(1,1,1), new Position(11,1,11)), "3.14158265"));
            put("0xFF ", new Token(Type.HEX_LITERAL, new Range(new Position(1,1,1), new Position(5,1,5)), "0xFF"));
            put("0xFF", new Token(Type.HEX_LITERAL, new Range(new Position(1,1,1), new Position(5,1,5)), "0xFF"));
        }};

        for (String input : inputs.keySet()) {
            Token expectedToken = inputs.get(input);
            Token actualToken;

            // Verify the token directly via the readNumber method
            CharacterStream characterStream1 = new CharacterStream(input, o);
            TokenStream tokenStream1 = createTokenStream(characterStream1, o);
            actualToken = tokenStream1.readNumber();
            assertEquals(expectedToken, actualToken);
            assertEquals(expectedToken.getValue(), Util.substring(input, actualToken.getRange()));

            // Verify the token indirectly via the next method
            CharacterStream characterStream2 = new CharacterStream(input, o);
            TokenStream tokenStream2 = createTokenStream(characterStream2, o);
            actualToken = tokenStream2.next();
            assertEquals(expectedToken, actualToken);
            assertEquals(expectedToken.getValue(), Util.substring(input, actualToken.getRange()));
        }
    }

    @Test
    public void testReadOperatorMethod() throws LexerException, IOException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        Map<String,Token> inputs = new HashMap<>() {{
            put(".= ", new Token(Type.PERIODEQ, new Range(new Position(1,1,1), new Position(3,1,3)), ".="));
            put(".=", new Token(Type.PERIODEQ, new Range(new Position(1,1,1), new Position(3,1,3)), ".="));
            put("=", new Token(Type.EQ, new Range(new Position(1,1,1), new Position(2,1,2)), "="));
            put("==", new Token(Type.EQEQ, new Range(new Position(1,1,1), new Position(3,1,3)), "=="));

            /*
             * This is necessary in an operator test because float constants beginning with a period will be treated as
             * an operator until the first number is encountered.
             */
            put(".56 ", new Token(Type.FLOAT_LITERAL, new Range(new Position(1,1,1), new Position(4,1,4)), ".56"));
            put(".56", new Token(Type.FLOAT_LITERAL, new Range(new Position(1,1,1), new Position(4,1,4)), ".56"));
        }};

        for (String input : inputs.keySet()) {
            Token expectedOutput = inputs.get(input);
            Token actualOutput;

            // Verify the token directly via the readOperator method
            CharacterStream characterStream1 = new CharacterStream(input, o);
            TokenStream tokenStream1 = createTokenStream(characterStream1, o);
            actualOutput = tokenStream1.readOperator();
            assertEquals(expectedOutput, actualOutput);
            assertEquals(expectedOutput.getValue(), Util.substring(input, actualOutput.getRange()));

            // Verify the token indirectly via the next method
            CharacterStream characterStream2 = new CharacterStream(input, o);
            TokenStream tokenStream2 = createTokenStream(characterStream2, o);
            actualOutput = tokenStream2.next();
            assertEquals(expectedOutput, actualOutput);
            assertEquals(expectedOutput.getValue(), Util.substring(input, actualOutput.getRange()));
        }
    }

    @Test
    public void testCommentMethod() throws LexerException, IOException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        Map<String, List<Token>> inputs = new HashMap<>() {{
            List<Token> singleLineExpectedTokens = new ArrayList<>() {{
                add(new Token(Type.COMMENTL, new Range(new Position(1,1,1), new Position(3,1,3)), "//"));
                add(new Token(Type.COMMENT, new Range(new Position(3,1,3), new Position(33,1,33)), " This is a single line comment"));
                add(new Token(Type.LF, new Range(new Position(33,1,33), new Position(34,2,1)), "\n"));
            }};
            put("// This is a single line comment\n", singleLineExpectedTokens);

            List<Token> multiLineExpectedTokens = new ArrayList<>() {{
                add(new Token(Type.COMMENTO, new Range(new Position(1,1,1), new Position(3,1,3)), "/*"));
                add(new Token(Type.COMMENT, new Range(new Position(3,1,3), new Position(34,3,1)), " This is a multi\n line comment\n"));
                add(new Token(Type.COMMENTC, new Range(new Position(34,3,1), new Position(36,3,3)), "*/"));
                add(new Token(Type.LF, new Range(new Position(36,3,3), new Position(37,4,1)), "\n"));
            }};
            put("/* This is a multi\n line comment\n*/\n", multiLineExpectedTokens);
        }};

        int x = 0;

        for (String input : inputs.keySet()) {
            List<Token> expectedOutputs = inputs.get(input);

            CharacterStream characterStream = new CharacterStream(input, o);
            TokenStream tokenStream = createTokenStream(characterStream, o);

            List<Token> actualOutputs = new ArrayList<>();
            Token t;

            while(!(t=tokenStream.next()).getType().equals(EOF)) {
                actualOutputs.add(t);
            }

            assertEquals(expectedOutputs.size(), actualOutputs.size());

            for (int i = 0; i < expectedOutputs.size(); i++) {
                Token expected = expectedOutputs.get(i);
                Token actual = actualOutputs.get(i);

                try {
                    Range expectedRange = expected.getRange();
                    Range actualRange = actual.getRange();

                    String expectedText = input.substring(expectedRange.getStartPosition() - 1, expectedRange.getStopPosition() - 1);
                    assertEquals(expectedText, expected.getValue());

                    String actualText = input.substring(actualRange.getStartPosition() - 1, actualRange.getStopPosition() - 1);
                    assertEquals(actualText, actual.getValue());

                    // Verify that we can find the token text with the start/stop positions
                    assertEquals(expectedText, actualText);

                    expectedText = Util.substring(input, expectedRange);
                    assertEquals(expectedText, expected.getValue());

                    actualText = Util.substring(input, actualRange);
                    assertEquals(actualText, actual.getValue());

                    // Verify that we can find the token text with the start/stop line/column
                    assertEquals(expectedText, actualText);

                    // It's easier to debug toString() than equals() when there's a problem with the range
                    assertEquals(actual.getType() + ": " + expected.getRange().toString(), actual.getType() + ": " + actual.getRange().toString());
                    assertEquals(expected, actual);
                }
                catch(Throwable e) {
                    System.err.printf(
                        "INPUT %d OUTPUT %d FAILED\n\tInput    :\"%s\"\n\tExpected :\"%s\"\n\tActual   :\"%s\"\n----------------------------------------\n",
                        x,
                        i,
                        input.replaceAll("(\\r\\n|\\n)", "\\\\n"),
                        expected,
                        actual
                    );
                    throw e;
                }
            }

            x++;
        }
    }

    @Test
    public void testNextMethod() throws LexerException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};
        CharacterStream characterStream = new CharacterStream("paradigm oop;\n", o);
        TokenStream tokenStream = createTokenStream(characterStream, o);

        assertEquals(tokenStream.next().getValue(), "paradigm");
        assertEquals(tokenStream.next().getValue(), " ");
        assertEquals(tokenStream.next().getValue(), "oop");
        assertEquals(tokenStream.next().getValue(), ";");
        assertEquals(tokenStream.next().getValue(), "\n");
    }

    @Test
    public void testEatUntilSingle() {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};
        CharacterStream characterStream = new CharacterStream("paradigm oop;\nimport abs.compiler.test.MyModule;", o);
        TokenStream tokenStream = createTokenStream(characterStream, o);
        tokenStream.eatUntil(IMPORT);
        assertEquals(IMPORT, tokenStream.peek().getType());
    }

    @Test
    public void testEatUntilMultiple1() {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};
        CharacterStream characterStream = new CharacterStream("paradigm oop;\nimport abs.compiler.test.MyModule;", o);
        TokenStream tokenStream = createTokenStream(characterStream, o);
        tokenStream.eatUntil(IMPORT, SEMICOLON);
        assertEquals(SEMICOLON, tokenStream.peek().getType());
    }

    @Test
    public void testEatUntilMultiple2() {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};
        CharacterStream characterStream = new CharacterStream("paradigm oop;\nimport abs.compiler.test.MyModule;", o);
        TokenStream tokenStream = createTokenStream(characterStream, o);
        tokenStream.eatUntil(IDENTIFIER, SEMICOLON);
        assertEquals(IDENTIFIER, tokenStream.peek().getType());
    }

    @Test
    public void testTokenIsTypeMethod() throws LexerException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};
        CharacterStream characterStream = new CharacterStream("paradigm oop;\n", o);
        TokenStream tokenStream = createTokenStream(characterStream, o);

        assertTrue(tokenStream.next().hasType(PARADIGM));
        assertTrue(tokenStream.next().hasType(WHITESPACE));
        assertTrue(tokenStream.next().hasType(IDENTIFIER));
        assertTrue(tokenStream.next().hasType(SEMICOLON));
        assertTrue(tokenStream.next().hasType(LF));
    }

    @Test(expected = IllegalCharacterException.class)
    public void testIllegalCharacterAtFrontOfComment() throws LexerException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};
        CharacterStream characterStream = new CharacterStream("bb", o);
        TokenStream tokenStream = createTokenStream(characterStream, o);
        tokenStream.readComment();
    }

    @Test(expected = IllegalCharacterException.class)
    public void testIllegalCharacter() throws LexerException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        // TODO: This may or may not cease being an illegal character as the language evolves
        CharacterStream characterStream = new CharacterStream("`", o);
        TokenStream tokenStream = createTokenStream(characterStream, o);
        tokenStream.next();
    }

    @Test
    public void testNextNumMethod() throws LexerException, IOException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        String input = "paradigm oop;";
        CharacterStream characterStream = new CharacterStream(input, o);
        TokenStream tokenStream = createTokenStream(characterStream, o);
        List<Token> tokens = new ArrayList<>();

        for (int x = 0; x < 6; x++) {
            tokens.add(tokenStream.next());
        }

        assertEquals(6, tokens.size());

        assertEquals("paradigm", tokens.get(0).getValue());
        assertEquals("paradigm", Util.substring(input, tokens.get(0).getRange()));
        assertEquals(" ", tokens.get(1).getValue());
        assertEquals(" ", Util.substring(input, tokens.get(1).getRange()));
        assertEquals("oop", tokens.get(2).getValue());
        assertEquals("oop", Util.substring(input, tokens.get(2).getRange()));
        assertEquals(";", tokens.get(3).getValue());
        assertEquals(";", Util.substring(input, tokens.get(3).getRange()));
        assertEquals(5, tokenStream.getIndex());

        // Verify that nulls are padding the list when too many are requested
        assertEquals(tokens.get(4).getType(), EOF);
        assertEquals(tokens.get(5).getType(), EOF);
    }

    @Test
    public void testPeekNumMethod() throws LexerException, IOException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        String input = "paradigm oop;";
        CharacterStream characterStream = new CharacterStream(input, o);
        TokenStream tokenStream = createTokenStream(characterStream, o);

        assertEquals("paradigm", tokenStream.peek(0).getValue());
        assertEquals("paradigm", Util.substring(input, tokenStream.peek(0).getRange()));
        assertEquals(" ", tokenStream.peek(1).getValue());
        assertEquals(" ", Util.substring(input, tokenStream.peek(1).getRange()));
        assertEquals("oop", tokenStream.peek(2).getValue());
        assertEquals("oop", Util.substring(input, tokenStream.peek(2).getRange()));
        assertEquals(";", tokenStream.peek(3).getValue());
        assertEquals(";", Util.substring(input, tokenStream.peek(3).getRange()));
        assertEquals(1, tokenStream.getIndex());

        assertNotNull(tokenStream.peek(0));
        assertNotNull(tokenStream.peek(1));
        assertNotNull(tokenStream.peek(2));
        assertNotNull(tokenStream.peek(3));
        assertEquals(tokenStream.peek(4).getType(), EOF);
        assertEquals(tokenStream.peek(5).getType(), EOF);
    }

    @Test
    public void testReadSymbolExceptions() {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        Map<String, Class> inputs = new HashMap<>() {{
            put("`", IllegalCharacterException.class);
        }};

        for (String input : inputs.keySet()) {
            boolean exception = false;
            Class expectedException = inputs.get(input);
            assertNotNull(expectedException);

            try {

                CharacterStream characterStream = new CharacterStream(input, o);
                TokenStream tokenStream = createTokenStream(characterStream, o);
                tokenStream.readSymbol();
            }

            catch (LexerException e) {
                exception = true;
                assertEquals(expectedException, e.getClass());
            }

            assertTrue(exception);
        }
    }

    @Test
    public void testReadStringConstantExceptions() throws LexerException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        Map<String, Class> inputs = new HashMap<>() {{
            put("", UnexpectedEndOfInput.class);
            put("\"", UnexpectedEndOfInput.class);
            put("b", IllegalCharacterException.class);
            put("\"\\", UnexpectedEndOfInput.class);
            put("\"\\n", UnexpectedEndOfInput.class);
        }};

        for (String input : inputs.keySet()) {
            boolean exception = false;
            Class expectedException = inputs.get(input);
            assertNotNull(expectedException);

            try {

                CharacterStream characterStream = new CharacterStream(input, o);
                TokenStream tokenStream = createTokenStream(characterStream, o);
                tokenStream.readStringConstant();
            }

            catch (LexerException e) {
                exception = true;
                assertEquals(expectedException, e.getClass());
            }

            assertTrue(exception);
        }
    }

//    @Test
    public void testTokenizeUnitConversion() throws URISyntaxException, IOException {
        String name = "abs/sample/UnitConversion.abs";
        Path filePath = Paths.get(getClass().getClassLoader().getResource(name).toURI());
        byte[] fileContent = Files.readAllBytes(filePath);
        String source = new String(fileContent, StandardCharsets.UTF_8);

        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};
        CharacterStream characterStream = new CharacterStream(source, o);
        TokenStream tokenStream = createTokenStream(characterStream, o);

        ObjectMapper om = new ObjectMapper();
        List<Token> tokenList = new ArrayList<>();
        Token token;
        while (!(token = tokenStream.next()).getType().equals(EOF)) {
            tokenList.add(token);
        }

        String json = om.writerWithDefaultPrettyPrinter()
                .writeValueAsString(tokenList);
        System.out.println(json);
    }

//    @Test
    public void testHttpClient() throws URISyntaxException, IOException {
        String name = "abs/sample/HttpClient.abs";
        Path filePath = Paths.get(getClass().getClassLoader().getResource(name).toURI());
        byte[] fileContent = Files.readAllBytes(filePath);
        String source = new String(fileContent, StandardCharsets.UTF_8);

        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};
        CharacterStream characterStream = new CharacterStream(source, o);
        TokenStream tokenStream = createTokenStream(characterStream, o);

        ObjectMapper om = new ObjectMapper();
        List<Token> tokenList = new ArrayList<>();
        Token token;
        while (!(token = tokenStream.next()).getType().equals(EOF)) {
            tokenList.add(token);
        }

        String json = om.writerWithDefaultPrettyPrinter()
                .writeValueAsString(tokenList);
        System.out.println(json);
    }

    /**
     * This method ensures that all token types have been tested for. Without this, it would be difficult to guarantee
     * if the lexer supported all token types. Note that this test will fail unless you run all tests within this class
     * prior to running this test.
     */
    //@AfterClass
    public static void verifyAllTypesTested() {
        int x = 1;
        int expectedSize = values().length;

        for (Type type : Type.values()) {
            assertTrue(format("Failed to test for type(%d of %d, %.2f%%): ", x, expectedSize,((float)x)/((float)expectedSize)*100.0f) + type, encounteredTypes.containsKey(type));
            assertTrue(format("Failed to test for type(%d of %d, %.2f%%): ", x, expectedSize,((float)x)/((float)expectedSize)*100.0f) + type, encounteredTypes.get(type));
            x++;
        }
    }

    /**
     * This method creates a token stream instance and overrides its next method. This allows the encounteredTypes map
     * to be automatically updated without explicitly updating from within each of the tests.
     * @param characterStream The character stream from which input should be taken
     * @param options the options which should be passed to the token stream
     * @return the requested token stream
     */
    private TokenStream createTokenStream(CharacterStream characterStream, Options options) {
        return new TokenStream(characterStream, options) {
            @Override
            public Token next() throws LexerException {
                Token t = super.next();
                encounteredTypes.put(t.getType(), true);
                return t;
            }
        };
    }

    /**
     * This is a pattern which gets repeated often in this test.
     * @throws LexerException thrown when something exceptional happens
     */
    private void verifyExpectedOutputs(Type[] expectedTypes, String[] expectedValues, String input) throws LexerException {
        Options o = new Options() {{
            setTrace(System.out);
            setTraceEnabled(false);
        }};

        verifyExpectedOutputs(expectedTypes, expectedValues, input, o);
    }

    /**
     * This is a pattern which gets repeated often in this test.
     * @throws LexerException thrown when something exceptional happens
     */
    private void verifyExpectedOutputs(Type[] expectedTypes, String[] expectedValues, String input, Options o) throws LexerException {
        assertEquals(expectedTypes.length, expectedValues.length);

        CharacterStream characterStream = new CharacterStream(input, o);
        TokenStream tokenStream = createTokenStream(characterStream, o);
        Token token;
        int x = 0;

        while (!(token = tokenStream.next()).getType().equals(EOF)) {
            if (x >= expectedTypes.length) {
                throw new IllegalStateException("Found more tokens than expected");
            }

            assertEquals(expectedTypes[x], token.getType());
            assertEquals(expectedValues[x], token.getValue());

            x++;
        }
    }
}
