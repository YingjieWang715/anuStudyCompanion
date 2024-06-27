package au.edu.anu.comp6442.group03.studyapp.note.search;

public enum TokenType {
    TITLE("title"),
    TAG("tag"),
    CONTENT("content"),
    REVISE("revise"),
    COLON(":"),
    SEMICOLON(";"),
    TEXT(""), // To represent actual text content for title, tag, content
    NUMBER(""), // To represent the number of days
    EOF(""), // End of file or input
    WHITESPACE(" ");

    public final String pattern;

    TokenType(String pattern) {
        this.pattern = pattern;
    }
}