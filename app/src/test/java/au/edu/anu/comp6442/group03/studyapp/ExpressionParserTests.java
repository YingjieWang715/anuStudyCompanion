package au.edu.anu.comp6442.group03.studyapp;

import static org.junit.Assert.*;

import com.google.firebase.Timestamp;

import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.note.search.expression.ContentExp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.ReviseExp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.TagExp;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.TitleExp;

public class ExpressionParserTests {

    @Test
    public void testTitleExpression() {
        TitleExp titleExp = new TitleExp("Example Title");
        assertEquals("Example Title", titleExp.parse());
    }

    @Test
    public void testTagExpression() {
        TagExp tagExp = new TagExp("work, personal, urgent");
        List<String> expectedTags = Arrays.asList("work", "personal", "urgent");
        assertEquals(expectedTags, tagExp.parse());
    }

    @Test
    public void testContentExpression() {
        ContentExp contentExp = new ContentExp("Here is some detailed content.");
        assertEquals("Here is some detailed content.", contentExp.parse());
    }

    @Test
    public void testReviseExpression() {
        ReviseExp reviseExp = new ReviseExp("5");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 5);
        Timestamp expectedTimestamp = new Timestamp(calendar.getTime());
        assertEquals(expectedTimestamp.getSeconds(), ((Timestamp) reviseExp.parse()).getSeconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidReviseExpression() {
        new ReviseExp("invalid").parse();
    }


}
