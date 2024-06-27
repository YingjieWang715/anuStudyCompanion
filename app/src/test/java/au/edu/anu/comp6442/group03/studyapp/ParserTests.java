package au.edu.anu.comp6442.group03.studyapp;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.note.search.Parser;
import au.edu.anu.comp6442.group03.studyapp.note.search.Token;
import au.edu.anu.comp6442.group03.studyapp.note.search.Tokenizer;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.ContentExp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.Exp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.ReviseExp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.TagExp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.TitleExp;

public class ParserTests {

    @Test
    public void testCompleteSentenceParsing() {
        // Assuming we have a suitable constructor for Parser that accepts a list of Tokens
        String input = "title: Example Title; tag: work, personal, urgent; content: Here is some detailed content; revise: 5;";
        Tokenizer tokenizer = new Tokenizer(input);
        List<Token> tokens = tokenizer.tokenize();
        Parser parser = new Parser(tokens);
        List<Exp> expressions = parser.parseQuery();

        // Assertions to ensure each expression is correctly parsed
        assertNotNull("Expressions list should not be null", expressions);
        assertEquals("Should parse four expressions", 4, expressions.size());

        // Test each expression type and value
        assertTrue("First expression should be a TitleExp", expressions.get(0) instanceof TitleExp);
        assertEquals("Example Title", expressions.get(0).parse());

        assertTrue("Second expression should be a TagExp", expressions.get(1) instanceof TagExp);
        assertEquals(Arrays.asList("work", "personal", "urgent"), expressions.get(1).parse());

        assertTrue("Third expression should be a ContentExp", expressions.get(2) instanceof ContentExp);
        assertEquals("Here is some detailed content", expressions.get(2).parse());

        assertTrue("Fourth expression should be a ReviseExp", expressions.get(3) instanceof ReviseExp);
        // Assume ReviseExp.parse() returns a Timestamp calculated from "5" days before the current date
        // This test might need to adjust depending on how the ReviseExp.parse() is implemented.
        assertNotNull("Revise timestamp should not be null", expressions.get(3).parse());
    }

    @Test
    public void testCompleteSentenceParsing2() {
        // Input string includes multiple expressions separated by semicolons
        String input = "title: Example Title; tag: work, personal, urgent; content: Here is some detailed content; revise: 5;";

        // Create tokenizer and parse input into tokens
        Tokenizer tokenizer = new Tokenizer(input);
        List<Token> tokens = tokenizer.tokenize();

        // Create parser with tokens and parse into expressions
        Parser parser = new Parser(tokens);
        List<Exp> expressions = parser.parseQuery();

        // Assertions to ensure each expression is correctly parsed
        assertNotNull("Expressions list should not be null", expressions);
        assertEquals("Should parse four expressions", 4, expressions.size());

        // Detailed assertions for each expression type and value
        assertTrue("First expression should be a TitleExp", expressions.get(0) instanceof TitleExp);
        assertEquals("Example Title", expressions.get(0).parse());

        assertTrue("Second expression should be a TagExp", expressions.get(1) instanceof TagExp);
        assertEquals(Arrays.asList("work", "personal", "urgent"), expressions.get(1).parse());

        assertTrue("Third expression should be a ContentExp", expressions.get(2) instanceof ContentExp);
        assertEquals("Here is some detailed content", expressions.get(2).parse());

        assertTrue("Fourth expression should be a ReviseExp", expressions.get(3) instanceof ReviseExp);
        assertNotNull("Revise value should not be null", expressions.get(3).parse());
    }

    @Test
    public void testUnusualCharacters() {
        String input = "title: *@&^$#!; content: <>{}[]`~;";
        Tokenizer tokenizer = new Tokenizer(input);
        List<Token> tokens = tokenizer.tokenize();
        Parser parser = new Parser(tokens);
        List<Exp> expressions = parser.parseQuery();

        assertEquals("Should parse two expressions", 2, expressions.size());
        assertEquals("*@&^$#!", expressions.get(0).parse());
        assertEquals("<>{}[]`~", expressions.get(1).parse());
    }

}
