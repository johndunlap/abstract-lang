package abs.compiler.parser;

import static org.junit.Assert.assertEquals;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.tree.LongNode;
import org.junit.Test;

public class PrecedenceClimbingParserV1Test extends AbstractParserTest {
    @Test
    public void testEvaluatingSimpleExpression() {
        ParserOptions options = new ParserOptions();
        TokenStream tokenStream = buildTokenStream("1+2*7+9/3^4");
        PrecedenceClimbingParserV1 parser = new PrecedenceClimbingParserV1(options, tokenStream);
        LongNode result = (LongNode) parser.parse();
        assertEquals(15L, result.getLongValue().longValue());
    }
}
