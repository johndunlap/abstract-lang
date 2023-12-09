package abs.compiler.parser;

/**
 * An enumeration of the types of errors which can occur. This may be informative to the user or enhance error recovery.
 *
 * // TODO: The definitions for each of these types should be better defined and more consistently applied.
 */
public enum ErrorTypeEnum {
    /**
     * An identifier is spelled incorrectly.
     */
    LEXICAL,

    /**
     * Missing a semicolon or curly brace.
     */
    SYNTACTIC,

    /**
     * Incompatible types like assigning a string to an integer.
     */
    SEMANTIC,

    /**
     * Code not reachable, infinite loop, or assigning null to a non-nullable variable.
     */
    LOGICAL;
}
