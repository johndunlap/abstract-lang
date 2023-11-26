package abs.compiler.parser.tree;

/**
 * This interface represents a lossless parse tree. It is lossless in the sense that it
 * contains all information from the source code, including comments and whitespace. This
 * allows the original source code to be reconstructed from the parse tree. This is a
 * fundamental requirement of any IDE.
 */
public interface ParseTree {
    TreeNode getRootNode();
}
