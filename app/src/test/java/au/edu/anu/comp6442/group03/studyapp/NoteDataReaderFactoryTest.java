package au.edu.anu.comp6442.group03.studyapp;

import static org.junit.Assert.*;

import org.junit.Test;

import au.edu.anu.comp6442.group03.studyapp.note.data.io.JsonNoteDataReader;
import au.edu.anu.comp6442.group03.studyapp.note.data.io.NoteDataReader;
import au.edu.anu.comp6442.group03.studyapp.note.data.io.NoteDataReaderFactory;
import au.edu.anu.comp6442.group03.studyapp.note.data.io.XmlNoteDataReader;

public class NoteDataReaderFactoryTest {

    @Test
    public void testGetJsonNoteDataReader() throws Exception {
        NoteDataReader reader = NoteDataReaderFactory.getReader("application/json");
        assertNotNull("The reader should not be null", reader);
        assertTrue("The reader should be an instance of JsonNoteDataReader", reader instanceof JsonNoteDataReader);
    }

    @Test
    public void testGetXmlNoteDataReader() throws Exception {
        NoteDataReader reader = NoteDataReaderFactory.getReader("text/xml");
        assertNotNull("The reader should not be null", reader);
        assertTrue("The reader should be an instance of XmlNoteDataReader", reader instanceof XmlNoteDataReader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetReaderWithUnsupportedType() throws Exception {
        NoteDataReaderFactory.getReader("application/pdf");
    }
}
