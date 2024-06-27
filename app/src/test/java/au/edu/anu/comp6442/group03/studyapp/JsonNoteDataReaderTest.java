package au.edu.anu.comp6442.group03.studyapp;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.data.io.JsonNoteDataReader;

public class JsonNoteDataReaderTest {

    @Test
    public void testReadData() throws Exception {
        // Sample JSON data
        String jsonData = "{\"notes\":[{\"title\":\"Test Note\",\"content\":\"This is a test note\"}]}";
        InputStream inputStream = new ByteArrayInputStream(jsonData.getBytes());

        JsonNoteDataReader reader = new JsonNoteDataReader();

        // Execute
        List<NoteData> notes = reader.readData(inputStream);

        // Verify
        assertNotNull("Notes list should not be null", notes);
        assertEquals("Should have one note", 1, notes.size());
        assertEquals("Note title should match", "Test Note", notes.get(0).getTitle());
        assertEquals("Note content should match", "This is a test note", notes.get(0).getContent());
    }

    @Test(expected = Exception.class)
    public void testReadDataWithInvalidInput() throws Exception {
        // Simulating bad input or broken stream
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.read()).thenThrow(new RuntimeException("Failed reading"));

        JsonNoteDataReader reader = new JsonNoteDataReader();

        // This should throw an exception
        reader.readData(inputStream);
    }
}
