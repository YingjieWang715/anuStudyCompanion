package au.edu.anu.comp6442.group03.studyapp.note.data.io;

import java.io.InputStream;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;

public interface NoteDataReader {
    List<NoteData> readData(InputStream inputStream) throws Exception;
}
