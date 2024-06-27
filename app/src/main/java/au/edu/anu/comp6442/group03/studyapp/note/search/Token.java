package au.edu.anu.comp6442.group03.studyapp.note.search;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Token {
    private final String token;
    private final TokenType type;

    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String str) {
            super(str);
        }
    }

    public Token(TokenType type, String str) {
        this.type = type;
        this.token = str;
    }

    public String getToken() {
        return this.token;
    }

    public TokenType getType() {
        return this.type;
    }

    public String toString() {
        if (this.type == TokenType.TITLE) {
            return "TITLE(" + this.token + ")";
        }
        if (this.type == TokenType.TAG) {
            return "TAG(" + this.token + ")";
        }
        if (this.type == TokenType.CONTENT) {
            return "CONTENT(" + this.token + ")";
        }
        if (this.type == TokenType.REVISE) {
            return "REVISE(" + this.token + ")";
        }
        return this.type + " " + this.token;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Token)) {
            return false;
        }
        Token token2 = (Token) obj;
        if (this.type != token2.getType() || !this.token.equals(token2.getToken())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.token, this.type});
    }
}