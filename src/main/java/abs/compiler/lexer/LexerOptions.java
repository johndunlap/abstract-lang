package abs.compiler.lexer;

/*
TODO: When writing test cases, make sure you test every combination of settings in this file
 */
public class LexerOptions extends abs.compiler.Options {
    private boolean includeSpaces = false;
    private boolean includeLineFeed = false;
    private boolean includeTab = false;
    private boolean includeComment = false;

    public boolean isIncludeSpaces() {
        return includeSpaces;
    }

    public void setIncludeSpaces(boolean includeSpaces) {
        this.includeSpaces = includeSpaces;
    }

    public boolean isIncludeLineFeed() {
        return includeLineFeed;
    }

    public void setIncludeLineFeed(boolean includeLineFeed) {
        this.includeLineFeed = includeLineFeed;
    }

    public boolean isIncludeTab() {
        return includeTab;
    }

    public void setIncludeTab(boolean includeTab) {
        this.includeTab = includeTab;
    }

    public boolean isIncludeComment() {
        return includeComment;
    }

    public void setIncludeComment(boolean includeComment) {
        this.includeComment = includeComment;
    }
}
