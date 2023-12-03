package abs.compiler.parser.oop.packages;

import abs.compiler.parser.AbstractNode;

public class PackageDeclaration extends AbstractNode {
    private PackageSegment packageName;

    public PackageDeclaration(PackageSegment packageName) {
        this.packageName = packageName;
    }

    public PackageSegment getPackageName() {
        return packageName;
    }

    public void setPackageName(PackageSegment packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        // This is recursive because of the implicit toString() call on the child
        return "package " + packageName;
    }
}
