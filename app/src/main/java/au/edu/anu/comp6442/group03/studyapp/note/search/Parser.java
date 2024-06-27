package au.edu.anu.comp6442.group03.studyapp.note.search;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.note.search.expression.ContentExp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.Exp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.ReviseExp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.TagExp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.TitleExp;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private void skipWhitespace() {
        while (pos < tokens.size() && tokens.get(pos).getType() == TokenType.WHITESPACE) {
            pos++;
        }
    }

    public List<Exp> parseQuery() {
        List<Exp> expressions = new ArrayList<>();
        if (tokens.size() == 1 && tokens.get(0).getType() == TokenType.EOF){
            throw new ParseException("Unknown field or value!");
        }
        while (!match(TokenType.EOF)) {
            if (match(TokenType.TITLE, TokenType.TAG, TokenType.CONTENT, TokenType.REVISE)) {
                Token field = previous();
                consume(TokenType.COLON, "Expect ':' after field.");
                Token data = consume(TokenType.TEXT, "Expect text after ':'.");
                consume(TokenType.SEMICOLON, "Expect ';' after text.");
                expressions.add(parseExp(field.getType(), data.getToken()));
            }
        }
        return expressions;
    }

    private Exp parseExp(TokenType type, String data) {
        switch (type) {
            case TITLE:
                return new TitleExp(data);
            case TAG:
                return new TagExp(data);
            case CONTENT:
                return new ContentExp(data);
            case REVISE:
                return new ReviseExp(data);
            default:
                throw new RuntimeException("Unexpected type: " + type);
        }
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String errorMsg) {
        if (check(type)) return advance();
        throw new ParseException(errorMsg);
    }

    private boolean check(TokenType type) {
        skipWhitespace();
        if (isAtEnd()) return false;
        return tokens.get(pos).getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) pos++;
        return previous();
    }

    private boolean isAtEnd() {
        return pos >= tokens.size();
    }

    private Token previous() {
        return tokens.get(pos - 1);
    }

    public static class ParseException extends RuntimeException {
        ParseException(String message) {
            super(message);
        }
    }
}
