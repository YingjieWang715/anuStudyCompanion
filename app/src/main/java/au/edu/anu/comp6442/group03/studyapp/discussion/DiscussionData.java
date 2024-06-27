package au.edu.anu.comp6442.group03.studyapp.discussion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscussionData {
    private String id;
    private String title;
    private String description;
    private String userId; // 添加用户ID字段
    private String userEmail;
    private List<CommentData> commentList = new ArrayList<>();

    public DiscussionData() {
    }

    public DiscussionData(String id, String title, String description, String userId, String userEmail, List<CommentData> commentList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.userEmail = userEmail;
        this.commentList = commentList;
    }

    public DiscussionData(String id, String title, String description, String userId, String userEmail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    // getters and setters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<CommentData> getCommentList() {
        return commentList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setCommentList() {
    }
}
