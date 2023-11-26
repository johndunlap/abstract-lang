package abs.compiler.model;

import abs.compiler.lexer.Token;
import abs.compiler.lexer.TokenStream;
import abs.compiler.lexer.Type;
import abs.compiler.archive.parser.ErrorHandler;
import abs.compiler.archive.parser.Paradigm;
import abs.compiler.archive.parser.graph.SourceFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Namespace {
    /**
     * The name of this namespace. If the namespace is a package, the name must begin with a lower case letter. If
     * the namespace is a module, the name must begin with an upper case letter. This variable is private instead of
     * protected because this allows its setter to enforce this rule. All names must be alphanumeric. It is illegal for
     * a namespace name to begin with a number.
     */
    protected String name;

    /**
     * This will be null if this is the root namespace(which has no parent) and non-null otherwise.
     */
    protected Namespace parent;
    /**
     * This will be empty if this namespace doesn't have any children. Children can be both packages and modules. This
     * is a map both because order is not important in terms of execution and because a map allows for de-duplication.
     * In situations where order is important, like automatically generating source code documentation, the children
     * can be sorted alphabetically.
     */
    private Map<String, Namespace> children = new HashMap<>();
    /**
     * The paradigm of this module. This attribute avoids the need for reflection to determine the paradigm
     * of the sub-class.
     */
    private Paradigm paradigm;

    /**
     * A list of files which have declared themselves within this namespace.
     */
    private List<SourceFile> SourceFiles = new ArrayList<>();

    public Namespace(TokenStream tokenStream, ErrorHandler errorHandler) {
        Namespace currentNamespace = null;

        Token token = tokenStream.peek();

        if (token.hasType(Type.IDENTIFIER)) {
            currentNamespace = new Namespace(token.getValue());

            // Start looking at the next token
            token = tokenStream.eatAndPeek();

            boolean lastWasIdentifier = true;

            while (token.hasType(Type.IDENTIFIER) || token.hasType(Type.PERIOD)) {
                if (lastWasIdentifier) {
                    if (token.hasType(Type.PERIOD)) {
                        lastWasIdentifier = false;
                    } else {
                        errorHandler.addExpectationError(".", token);
                    }
                } else {
                    if (token.hasType(Type.IDENTIFIER)) {
                        Namespace child = new Namespace(token.getValue());
                        currentNamespace.addChild(child);
                        currentNamespace = child;
                        lastWasIdentifier = true;
                    } else {
                        errorHandler.addMissingIdentifierError(token);
                    }
                }

                // Start looking at the next token
                token = tokenStream.eatAndPeek();
            }
        } else {
            errorHandler.addMissingIdentifierError(token);
        }
    }

    public Namespace(String name) {
        this.name = name;
    }

    public Namespace(String name, Namespace parent) {
        this.name = name;
        this.parent = parent;
        parent.addChild(this);
    }

    public void addSourceFile(SourceFile SourceFile) {
        SourceFiles.add(SourceFile);
    }

    public void addChild(Namespace child) {
        // Don't add a child twice
        if (!children.containsKey(child.getName())) {
            children.put(child.getName(), child);
        }

        // Don't overwrite the parent if one has already been set
        if (child.getParent() == null) {
            child.setParent(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Namespace getParent() {
        return parent;
    }

    public void setParent(Namespace parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    public Map<String, Namespace> getChildren() {
        return children;
    }

    public void setChildren(Map<String, Namespace> children) {
        this.children = children;
    }

    public Paradigm getParadigm() {
        return paradigm;
    }

    public void setParadigm(Paradigm paradigm) {
        this.paradigm = paradigm;
    }

    public String toString() {
        if (parent == null) {
            return name;
        } else {
            return parent.toString() + "." + name;
        }
    }

    public Namespace getRootNamespace() {
        if (getParent() != null) {
            return getParent();
        }

        return this;
    }
}
