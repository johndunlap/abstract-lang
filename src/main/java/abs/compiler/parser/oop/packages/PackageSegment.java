package abs.compiler.parser.oop.packages;

import abs.compiler.parser.AbstractNode;

/**
 * Represents a fully qualified package name. This class can be referenced from import statements, class declarations,
 * package declarations, etc.
 */
public class PackageSegment extends AbstractNode {
    private String name;

    private PackageSegment child;

    public PackageSegment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PackageSegment getChild() {
        return child;
    }

    public void setChild(PackageSegment child) {
        this.child = child;
    }

    @Override
    public String toString() {
        // This is recursive because of the implicit toString() call on the child
        return name + (child != null ? "." + child : "");
    }
}
