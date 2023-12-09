package abs.compiler.parser;

public enum ErrorRecoveryModeEnum {
    /**
     * Ignore tokens until the next delimiting token, like a semicolon or curly brace.
     */
    PANIC,

    /**
     * Attempt to fix the error by inserting a missing token or replacing an incorrect token.
     */
    STATEMENT,

    /**
     * Custom grammar rules for handling common well known errors conditions.
     */
    ERROR_PRODUCTION,

    /**
     * Attempt to fix the error by taking the whole program into account.
     */
    GLOBAL_CORRECTION,

}
