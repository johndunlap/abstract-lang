import abs.compiler.Options;
import abs.compiler.Util;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.TokenStream;
import abs.compiler.parser.ErrorNode;
import abs.compiler.parser.Node;
import abs.compiler.parser.RootNode;
import abs.compiler.parser.RootParser;

public class Main {
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        CharacterStream characterStream = new CharacterStream("paradigm oop; package abs.compiler.test;", options);
        TokenStream tokenStream = new TokenStream(characterStream, options);
        RootParser parser = new RootParser(tokenStream, options);
        Node node = parser.parse(null);

        if (node instanceof ErrorNode) {
            System.err.println(((ErrorNode)node).getMessage());
        } else {
            String dotText = ((RootNode)node).toDot();
            System.out.println(dotText);
            Util.toFile(dotText, "tree.dot");
            Runtime.getRuntime().exec("dot -Tpng -o tree.png tree.dot");
        }
    }
}
