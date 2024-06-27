package au.edu.anu.comp6442.group03.studyapp.note.data.io;

public class NoteDataReaderFactory {
    public static NoteDataReader getReader(String type) throws Exception {
        if (type.equalsIgnoreCase("application/json")) {
            return new JsonNoteDataReader();
        } else if (type.equalsIgnoreCase("text/xml")) {
            return new XmlNoteDataReader();
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + type);
        }
    }
}
