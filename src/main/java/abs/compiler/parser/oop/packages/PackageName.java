package abs.compiler.parser.oop.packages;

import abs.compiler.parser.AbstractNode;

/**
 * Represents a fully qualified package name. This class can be referenced from import statements, class declarations,
 * package declarations, etc.
 */
public class PackageName extends AbstractNode {
    private String name;

    private PackageName child;

    public PackageName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PackageName getChild() {
        return child;
    }

    public void setChild(PackageName child) {
        this.child = child;
    }

    @Override
    public String toString() {
        // This is recursive because of the implicit toString() call on the child
        return name + (child != null ? "." + child : "");
    }
}
