package au.edu.anu.comp6442.group03.studyapp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.note.search.Token;
import au.edu.anu.comp6442.group03.studyapp.note.search.TokenType;
import au.edu.anu.comp6442.group03.studyapp.note.search.Tokenizer;

public class TokenizerTests {

    @Test
    public void testTokenizeCompleteSentence() {
        // Input string includes keywords, colons, text contents, and semicolons
        String input = "title: Example Title; tag: work, personal, urgent; content: Here is some detailed content; revise: 5;";
        Tokenizer tokenizer = new Tokenizer(input);
        List<Token> tokens = tokenizer.tokenize();

        // Print each token's type and content for visual verification
        tokens.forEach(token -> System.out.println(token.getType() + " -> '" + token.getToken() + "'"));

        // Assert the correct number of tokens, including keywords, colons, semicolons, texts, and EOF
        assertEquals("Expected number of tokens", 17, tokens.size());

        // Detailed assertions to verify each part of the tokenization
        assertEquals(TokenType.TITLE, tokens.get(0).getType());
        assertEquals("title", tokens.get(0).getToken());

        assertEquals(TokenType.COLON, tokens.get(1).getType());
        assertEquals(":", tokens.get(1).getToken());

        assertEquals(TokenType.TEXT, tokens.get(2).getType());
        assertEquals("Example Title", tokens.get(2).getToken());

        assertEquals(TokenType.SEMICOLON, tokens.get(3).getType());
        assertEquals(";", tokens.get(3).getToken());

        assertEquals(TokenType.TAG, tokens.get(4).getType());
        assertEquals("tag", tokens.get(4).getToken());

        assertEquals(TokenType.COLON, tokens.get(5).getType());
        assertEquals(":", tokens.get(5).getToken());

        assertEquals(TokenType.TEXT, tokens.get(6).getType());
        assertEquals("work, personal, urgent", tokens.get(6).getToken());

        assertEquals(TokenType.SEMICOLON, tokens.get(7).getType());
        assertEquals(";", tokens.get(7).getToken());

        assertEquals(TokenType.CONTENT, tokens.get(8).getType());
        assertEquals("content", tokens.get(8).getToken());

        assertEquals(TokenType.COLON, tokens.get(9).getType());
        assertEquals(":", tokens.get(9).getToken());

        assertEquals(TokenType.TEXT, tokens.get(10).getType());
        assertEquals("Here is some detailed content", tokens.get(10).getToken());

        assertEquals(TokenType.SEMICOLON, tokens.get(11).getType());
        assertEquals(";", tokens.get(11).getToken());

        assertEquals(TokenType.REVISE, tokens.get(12).getType());
        assertEquals("revise", tokens.get(12).getToken());

        assertEquals(TokenType.COLON, tokens.get(13).getType());
        assertEquals(":", tokens.get(13).getToken());

        assertEquals(TokenType.TEXT, tokens.get(14).getType());
        assertEquals("5", tokens.get(14).getToken());

        assertEquals(TokenType.SEMICOLON, tokens.get(15).getType());
        assertEquals(";", tokens.get(15).getToken());

        assertEquals(TokenType.EOF, tokens.get(16).getType());
        assertEquals("", tokens.get(16).getToken());
    }

    @Test
    public void testTokenizeMixedContentTypes() {
        String input = "title: Complex Example Title; tag: important, update; content: Need to review!; revise: 10;";
        Tokenizer tokenizer = new Tokenizer(input);
        List<Token> tokens = tokenizer.tokenize();

        tokens.forEach(token -> System.out.println(token.getType() + " -> '" + token.getToken() + "'"));

        assertEquals("Expected number of tokens", 17, tokens.size());

        int i = 0;
        assertEquals(TokenType.TITLE, tokens.get(i++).getType());
        assertEquals(TokenType.COLON, tokens.get(i++).getType());
        assertEquals(TokenType.TEXT, tokens.get(i++).getType());
        assertEquals("Complex Example Title", tokens.get(i-1).getToken());
        assertEquals(TokenType.SEMICOLON, tokens.get(i++).getType());

        assertEquals(TokenType.TAG, tokens.get(i++).getType());
        assertEquals(TokenType.COLON, tokens.get(i++).getType());
        assertEquals(TokenType.TEXT, tokens.get(i++).getType());
        assertEquals("important, update", tokens.get(i-1).getToken());
        assertEquals(TokenType.SEMICOLON, tokens.get(i++).getType());

        assertEquals(TokenType.CONTENT, tokens.get(i++).getType());
        assertEquals(TokenType.COLON, tokens.get(i++).getType());
        assertEquals(TokenType.TEXT, tokens.get(i++).getType());
        assertEquals("Need to review!", tokens.get(i-1).getToken());
        assertEquals(TokenType.SEMICOLON, tokens.get(i++).getType());

        assertEquals(TokenType.REVISE, tokens.get(i++).getType());
        assertEquals(TokenType.COLON, tokens.get(i++).getType());
        assertEquals(TokenType.TEXT, tokens.get(i++).getType());
        assertEquals("10", tokens.get(i-1).getToken());
        assertEquals(TokenType.SEMICOLON, tokens.get(i++).getType());

        assertEquals(TokenType.EOF, tokens.get(i++).getType());
    }

    @Test
    public void testTokenizeWithEscapedCharacters() {
        String input = "title: Learning Java Programming; content: It is fun to learn!; tag: education;";
        Tokenizer tokenizer = new Tokenizer(input);
        List<Token> tokens = tokenizer.tokenize();

        tokens.forEach(token -> System.out.println(token.getType() + " -> '" + token.getToken() + "'"));

        assertEquals("Expected number of tokens", 13, tokens.size());

        int i = 0;
        assertEquals(TokenType.TITLE, tokens.get(i++).getType());
        assertEquals(TokenType.COLON, tokens.get(i++).getType());
        assertEquals(TokenType.TEXT, tokens.get(i++).getType());
        assertEquals("Learning Java Programming", tokens.get(i-1).getToken());
        assertEquals(TokenType.SEMICOLON, tokens.get(i++).getType());

        assertEquals(TokenType.CONTENT, tokens.get(i++).getType());
        assertEquals(TokenType.COLON, tokens.get(i++).getType());
        assertEquals(TokenType.TEXT, tokens.get(i++).getType());
        assertEquals("It is fun to learn!", tokens.get(i-1).getToken());
        assertEquals(TokenType.SEMICOLON, tokens.get(i++).getType());

        assertEquals(TokenType.TAG, tokens.get(i++).getType());
        assertEquals(TokenType.COLON, tokens.get(i++).getType());
        assertEquals(TokenType.TEXT, tokens.get(i++).getType());
        assertEquals("education", tokens.get(i-1).getToken());
        assertEquals(TokenType.SEMICOLON, tokens.get(i++).getType());

        assertEquals(TokenType.EOF, tokens.get(i++).getType());
    }

}
