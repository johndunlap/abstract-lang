package abs.compiler.parser;

import static org.junit.Assert.assertEquals;
import abs.compiler.Options;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.tree.DoubleNode;
import abs.compiler.parser.tree.PrecedenceClimbingTreeParserV1;
import org.junit.Test;

public class PrecedenceClimbingParserV1Test extends AbstractParserTest {
    @Test
    public void testEvaluatingSimpleExpression() {
        Options options = new Options();
        TokenStream tokenStream = buildTokenStream("1+2*7+9/3");
        PrecedenceClimbingTreeParserV1 parser = new PrecedenceClimbingTreeParserV1(options, tokenStream);
        DoubleNode result = (DoubleNode) parser.parse();
        assertEquals(18, result.getValue().longValue());
    }
}
