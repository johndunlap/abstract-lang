package abs.cli;

import abs.compiler.Util;
import abs.compiler.archive.parser.ErrorHandler;
import abs.compiler.lexer.CharacterStream;
import abs.compiler.lexer.LexerOptions;
import abs.compiler.lexer.TokenStream;

import java.io.File;
import java.io.IOException;

public class RenderParseTreeAsImage {
    public static void main(String[] args) throws IOException {
        String inputFilename;

        if (args.length == 0) {
            inputFilename = "src/test/resources/abs/samples/lexer/car.abs";
        } else {
            inputFilename = args[0];
        }

        File inputFile = new File(inputFilename);

        if (!inputFile.exists()) {
            System.err.println("Input file does not exist: " + inputFilename);
            System.exit(2);
        }

        String fileContents = Util.fromFile(inputFile);

        LexerOptions o = new LexerOptions();

        CharacterStream characterStream = new CharacterStream(fileContents, o);
        TokenStream tokenStream = new TokenStream(characterStream, o);

        ErrorHandler errorHandler = new ErrorHandler();

        // TODO: Fix this when the parser outputs models instead of nodes
/*
        Node n = parser.parse();
        Util.toFile(n.toDot(), "tmp.dot");
        Util.runCommand("dot", "-Tpng", "tmp.dot", "-o", "tmp.png");
        Util.runCommand("go", "tmp.png");

        File tmpDot = new File("tmp.dot");
        tmpDot.delete();
        File tmpPng = new File("tmp.png");
        tmpPng.delete();
*/
    }
}
