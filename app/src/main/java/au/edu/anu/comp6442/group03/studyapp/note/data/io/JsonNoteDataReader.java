package au.edu.anu.comp6442.group03.studyapp.note.data.io;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;

public class JsonNoteDataReader implements NoteDataReader {

    @Override
    public List<NoteData> readData(InputStream inputStream) throws Exception {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        Gson gson = new Gson();

        List<NoteData> notes = new ArrayList<>();

        // Begin reading the JSON object
        reader.beginObject();

        // Assuming the JSON object has a specific field name
        while (reader.hasNext()) {
            String fieldName = reader.nextName();
            if ("notes".equals(fieldName)) { // Adjust 'notes' to match your field name
                // Begin reading the array of notes
                reader.beginArray();
                while (reader.hasNext()) {
                    // Read each note object
                    NoteData note = gson.fromJson(reader, NoteData.class);
                    notes.add(note);
                }
                // End reading the array of notes
                reader.endArray();
            } else {
                // Handle other fields if needed
                reader.skipValue();
            }
        }

        // End reading the JSON object
        reader.endObject();

        // Close the reader
        reader.close();

        return notes;
    }
}

