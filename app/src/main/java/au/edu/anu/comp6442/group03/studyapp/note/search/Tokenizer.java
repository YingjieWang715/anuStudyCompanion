package au.edu.anu.comp6442.group03.studyapp.note.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


//public class Tokenizer {
//    private String input;
//    private final List<Token> tokens = new ArrayList<>();
//    private int pos = 0;
//
//    public Tokenizer(String text) {
//        input = text;          // save input text (string)
//    }
//
//    public List<Token> tokenize() {
//        while (pos < input.length()) {
//            char current = input.charAt(pos);
//            if (Character.isWhitespace(current)) {
//                pos++;  // Skip whitespace
//            } else if (current == ':') {
//                tokens.add(new Token(TokenType.COLON, ":"));
//                pos++;
//            } else if (current == ';') {
//                tokens.add(new Token(TokenType.SEMICOLON, ";"));
//                pos++;
//            } else {
//                tokenizeText();
//            }
//        }
//        tokens.add(new Token(TokenType.EOF, ""));
//        // Debug output to check tokenization results
//        for (Token token : tokens) {
//            System.out.println(token.getType() + " '" + token.getToken() + "'");
//        }
//        return tokens;
//    }
//
//    private void tokenizeText() {
////        int start = pos;
////        // Process characters until a colon, semicolon, or whitespace is encountered
////        while (pos < input.length() && input.charAt(pos) != ':' && input.charAt(pos) != ';' && !Character.isWhitespace(input.charAt(pos))) {
////            pos++;
////        }
////
////        String tokenValue = input.substring(start, pos).trim();
////
////        // Check if the token matches any of the special keywords
////        switch (tokenValue.toLowerCase()) {
////            case "title":
////                tokens.add(new Token(TokenType.TITLE, tokenValue));
////                break;
////            case "tag":
////                tokens.add(new Token(TokenType.TAG, tokenValue));
////                break;
////            case "content":
////                tokens.add(new Token(TokenType.CONTENT, tokenValue));
////                break;
////            case "revise":
////                tokens.add(new Token(TokenType.REVISE, tokenValue));
////                break;
////            default:
////                tokens.add(new Token(TokenType.TEXT, tokenValue));  // Default to text
////
////                // Check if the current position is a colon or semicolon and add them as separate tokens
////                if (pos < input.length() && (input.charAt(pos) == ':' || input.charAt(pos) == ';')) {
////                    if (input.charAt(pos) == ':') {
////                        tokens.add(new Token(TokenType.COLON, ":"));
////                    } else {
////                        tokens.add(new Token(TokenType.SEMICOLON, ";"));
////                    }
////                    pos++; // Move past the colon or semicolon
////                }
////        }
//        int start = pos;
//        while (pos < input.length() && input.charAt(pos) != ':' && input.charAt(pos) != ';' && !Character.isWhitespace(input.charAt(pos))) {
//            pos++;
//        }
//        String potentialKey = input.substring(start, pos).trim();
//
//        // Adjust pos to skip whitespaces after the potential key and before a colon
//        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
//            pos++;
//        }
//
//        // Add a token for colon or semicolon if it follows directly after the potential key
//        if (pos < input.length() && input.charAt(pos) == ':') {
//            if (Arrays.asList("title", "tag", "content", "revise").contains(potentialKey.toLowerCase())) {
//                tokens.add(new Token(TokenType.valueOf(potentialKey.toUpperCase()), potentialKey));
//                tokens.add(new Token(TokenType.COLON, ":"));
//            } else {
//                tokens.add(new Token(TokenType.TEXT, potentialKey));
//            }
//            pos++;
//        } else if (pos < input.length() && input.charAt(pos) == ';') {
//            tokens.add(new Token(TokenType.TEXT, potentialKey));
//            tokens.add(new Token(TokenType.SEMICOLON, ";"));
//            pos++;
//        } else {
//            tokens.add(new Token(TokenType.TEXT, potentialKey));
//        }
//
//        // Handle normal text until next special character
//        start = pos;
//        while (pos < input.length() && input.charAt(pos) != ';' && !Character.isWhitespace(input.charAt(pos))) {
//            pos++;
//        }
//        if (start != pos) {
//            tokens.add(new Token(TokenType.TEXT, input.substring(start, pos).trim()));
//        }
//    }
//}

public class Tokenizer {
    private final String input;
    private final List<Token> tokens = new ArrayList<>();
    private int pos = 0;

    public Tokenizer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        while (pos < input.length()) {
            if (Character.isWhitespace(input.charAt(pos))) {
                pos++;  // Skip whitespace
            } else if (input.charAt(pos) == ':') {
                tokens.add(new Token(TokenType.COLON, ":"));
                pos++;  // Move past the colon
                tokenizeExpression();  // Tokenize until you reach a semicolon
            } else if (input.charAt(pos) == ';') {
                tokens.add(new Token(TokenType.SEMICOLON, ";"));
                pos++;  // Move past the semicolon
            } else {
                tokenizeKeywordOrText();
                if (pos == 0){ // if position got reset
                    break;
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private void tokenizeKeywordOrText() {
        int start = pos;
        while (pos < input.length() && input.charAt(pos) != ':' && input.charAt(pos) != ';' && !Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
        String word = input.substring(start, pos).trim();
        if (word.equalsIgnoreCase("title") || word.equalsIgnoreCase("tag") ||
                word.equalsIgnoreCase("content") || word.equalsIgnoreCase("revise")) {
            tokens.add(new Token(TokenType.valueOf(word.toUpperCase()), word));
        } else {
            // Reaching here means there's an error in input formatting, or it's just text before a colon
            pos = start; // Reset to start to capture full expression after colon
        }
    }

    private void tokenizeExpression() {
        int start = pos;
        while (pos < input.length() && input.charAt(pos) != ';') {
            pos++;
        }
        String expression = input.substring(start, pos).trim();
        if (!expression.isEmpty()) {
            tokens.add(new Token(TokenType.TEXT, expression));
        }
    }
}
