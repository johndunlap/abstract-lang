package abs.compiler.archive.parser.graph;

import abs.ImplementMeException;
import abs.compiler.model.EntryPointMethod;
import abs.compiler.model.Namespace;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for everything which has been successfully parsed. Multiple files can be parsed
 * and then merged into the root namespace tree of this object. Entry point methods are cached during the namespace
 * merging process, which simplifies invoking them. Source files can be parsed sequentially or in parallel and then
 * merged into this object.
 */
public class Sandbox {
    /**
     * This is the root namespace of all namespaces encountered by the compiler
     */
    private Namespace rootNamespace = new Namespace("root");

    /**
     * A list of entry point methods which were declared by modules within the sandbox. This list will be empty if no
     * entry point methods were declared.
     */
    private List<EntryPointMethod> entryPoints = new ArrayList<>();

    public List<EntryPointMethod> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(List<EntryPointMethod> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public Namespace getRootNamespace() {
        return rootNamespace;
    }

    /**
     * Merge the specified namespace into the root namespace of this compilation unit. This allows multiple files to be
     * parsed in parallel and, once each of them have been parsed, a single thread can merge each of them into this
     * compilation unit to create a unified tree of everything which was compiled. This tree can be updated on the fly
     * as files are modified by a developer.
     *
     * @param sourceFile The source file which should be merged into the root namespace
     */
    public void merge(SourceFile sourceFile) {
        // Get the root namespace of the provided source file
        Namespace namespace = sourceFile.getPackageDeclaration().getNamespace().getRootNamespace();

        // Add the namespace as a child of the root namespace
        rootNamespace.addChild(namespace);

        // TODO: Cache any entry point methods which exist within the provided namespace

        throw new ImplementMeException();
    }
}
