package abs.compiler.parser.oop.packages;

import abs.compiler.parser.AbstractNode;

public class PackageDeclaration extends AbstractNode {
    private String name;

    private PackageDeclaration child;

    public PackageDeclaration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PackageDeclaration getChild() {
        return child;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChild(PackageDeclaration child) {
        this.child = child;
    }

    @Override
    public String toString() {
        String result = "package " + name;

        if (child != null) {
            result += "." + child;
        }

        return result;
    }
}
