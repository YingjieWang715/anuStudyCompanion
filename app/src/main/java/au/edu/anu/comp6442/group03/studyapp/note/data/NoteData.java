package au.edu.anu.comp6442.group03.studyapp.note.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteData {
    private String title;
    private String content;
    private Timestamp timestamp;
    private List<String> tags = new ArrayList<>();
    private Timestamp reminder;

    @DocumentId
    private String documentId = UUID.randomUUID().toString();



    public String getDocumentId() {
        return documentId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public Timestamp getReminder() {
        return reminder;
    }

    public void setReminder(Timestamp reminder) {
        this.reminder = reminder;
    }

    public NoteData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
