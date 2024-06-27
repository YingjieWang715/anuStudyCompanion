package au.edu.anu.comp6442.group03.studyapp.discussion;

import com.google.firebase.Timestamp;

public class CommentData {
    private String comment;
    private String userEmail;
    private Timestamp timestamp;

    public CommentData() {
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
