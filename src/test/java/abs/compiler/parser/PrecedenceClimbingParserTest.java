package abs.compiler.parser;

import static org.junit.Assert.assertEquals;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.tree.LongNode;
import org.junit.Test;

public class PrecedenceClimbingParserTest extends AbstractParserTest {
    @Test
    public void testEvaluatingSimpleExpression() {
        ParserOptions options = new ParserOptions();
        TokenStream tokenStream = buildTokenStream("1+2*7+9/3^4");
        PrecedenceClimbingParser parser = new PrecedenceClimbingParser(options, tokenStream);
        LongNode result = (LongNode) parser.parse();
        assertEquals(15L, result.getLongValue().longValue());
    }
}
