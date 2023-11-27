package abs.compiler.parser.oop.packagedecl;

import abs.ImplementMeException;
import abs.compiler.parser.AbstractNode;

public class PackageDecl extends AbstractNode {
    private String name;

    private PackageDecl child;

    public PackageDecl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PackageDecl getChild() {
        return child;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChild(PackageDecl child) {
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
