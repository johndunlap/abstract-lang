package abs.compiler.model;

import abs.ImplementMeException;

/**
 * This class is a container for everything which has been compiled. Multiple files can be parsed and then merged into
 * the namespace tree of this object. Entry point methods are cached in the merge process, which simplified invoking
 * them. Source files can be compiled sequentially or in parallel
 */
public class CompilationUnit {
    /**
     * This is the root namespace of all namespaces encountered by the parser.
     */
    private Namespace rootNamespace = new Namespace("root");


    /**
     * Merge the specified namespace into the root namespace of this compilation unit. This allows multiple files to be
     * parsed in parallel and, once each of them have been parsed, a single thread can merge each of them into this
     * compilation unit to create a unified tree of everything which was compiled. This tree can be updated on the fly
     * as files are modified by a developer.
     *
     * @throws IllegalStateException thrown if the provided namespace has a non-null parent
     * @param namespace The namespace which should be merged into the root namespace of this compilation unit
     */
    public void merge(Namespace namespace) {
        if (namespace.getParent() != null) {
            throw new IllegalStateException("Only parent namespaces can be merged into a compilation unit");
        }

        // Add the namespace as a child of the root namespace
        rootNamespace.addChild(namespace);

        // TODO: Cache any entry point methods which exist within the provided namespace

        throw new ImplementMeException();
    }
}
