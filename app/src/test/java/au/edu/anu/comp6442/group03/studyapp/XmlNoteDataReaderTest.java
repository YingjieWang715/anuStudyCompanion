package au.edu.anu.comp6442.group03.studyapp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.data.io.XmlNoteDataReader;

public class XmlNoteDataReaderTest {

    @Test
    public void testReadData() throws Exception {
        // Mock XML Data
        String xmlData = "<Notes>" +
                "<NoteData>" +
                "<title>Sample Note</title>" +
                "<content>This is a sample note content.</content>" +
                "<timestamp>2020-01-01T12:00:00Z</timestamp>" +
                "<reminder>2020-01-02T12:00:00Z</reminder>" +
                "<tag>important</tag>" +
                "</NoteData>" +
                "</Notes>";

        // Convert String to InputStream
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes());

        // Create an instance of the class to be tested
        XmlNoteDataReader reader = new XmlNoteDataReader();

        // Execute
        List<NoteData> notes = reader.readData(inputStream);

        // Verify
        assertNotNull("List of notes should not be null", notes);
        assertFalse("List of notes should not be empty", notes.isEmpty());
        assertEquals("Should have one note", 1, notes.size());

        NoteData note = notes.get(0);
        assertEquals("Title should match", "Sample Note", note.getTitle());
        assertEquals("Content should match", "This is a sample note content.", note.getContent());
        assertNotNull("Timestamp should not be null", note.getTimestamp());
        assertNotNull("Reminder should not be null", note.getReminder());
        assertTrue("Tags should contain 'important'", note.getTags().contains("important"));
    }
}
