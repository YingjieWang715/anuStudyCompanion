package au.edu.anu.comp6442.group03.studyapp.discussion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.R;

public class DiscussionCommentActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private Button postBtn;
    private ImageButton backBtn;
    private EditText editTextComment;
    private TextView titleTextView, descriptionTextView, postByTextView;
    private RecyclerView commentView;
    private CommentAdapter adapter;
    private List<CommentData> commentList = new ArrayList<>();
    private String idByText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_comment);

        commentView = findViewById(R.id.comments_list); // Ensure this is the correct ID
        if (commentView == null) {
            throw new RuntimeException("Unable to find RecyclerView with ID: " + R.id.comments_list);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentView.setLayoutManager(layoutManager);

        initializeViews();
        retrieveIntentData();
        initializeRecyclerView();
        setupEventListeners();
    }

    private void initializeViews() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        postBtn = findViewById(R.id.post_btn);
        backBtn = findViewById(R.id.button_back);
        editTextComment = findViewById(R.id.comment_edit_text);
        titleTextView = findViewById(R.id.title_text);
        descriptionTextView = findViewById(R.id.description_text);
        postByTextView = findViewById(R.id.posted_by_text);
        commentView = findViewById(R.id.comments_list);
    }

    private void retrieveIntentData() {
        titleTextView.setText(getIntent().getStringExtra("title"));
        descriptionTextView.setText(getIntent().getStringExtra("description"));
        postByTextView.setText("- posted by " + getIntent().getStringExtra("userEmail"));
        idByText = getIntent().getStringExtra("id");
    }

    private void initializeRecyclerView() {
        adapter = new CommentAdapter(commentList);
        commentView.setAdapter(adapter);
        loadCommentsFromFirebase();
    }

    private void setupEventListeners() {
        postBtn.setOnClickListener(v -> saveComments());
        backBtn.setOnClickListener(v -> onBackPressed());
    }

    private void saveComments() {
        String commentText = editTextComment.getText().toString().trim();
        if (commentText.isEmpty()) {
            editTextComment.setError("The comment can't be empty :(");
            return;
        }

        CommentData commentData = new CommentData();
        commentData.setComment(commentText);
        commentData.setUserEmail(currentUser.getEmail());
        commentData.setTimestamp(Timestamp.now());

        FirebaseFirestore.getInstance().collection("discussion").document(idByText)
                .collection("comments").add(commentData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                    editTextComment.setText("");
                    loadCommentsFromFirebase();  // Reload comments to show the new one
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add comment: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void loadCommentsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discussion").document(idByText)
                .collection("comments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        commentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CommentData comment = document.toObject(CommentData.class);
                            if (comment.getComment() != null && comment.getUserEmail() != null) {
                                commentList.add(comment);
                                Log.d("Comments", "Added a comment: " + comment.getComment());
                            } else {
                                Log.e("DiscussionCommentActivity", "Missing data for comment: " + document.getId());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error loading comments: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewComments;
        TextView textViewUserEmail;

        public CommentViewHolder(View itemView) {
            super(itemView);
            textViewComments = itemView.findViewById(R.id.text_view_comments);
            textViewUserEmail = itemView.findViewById(R.id.text_view_userEmail);
        }
    }

    public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
        private List<CommentData> commentDataList;

        public CommentAdapter(List<CommentData> commentDataList) {
            this.commentDataList = commentDataList;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            CommentData comment = commentDataList.get(position);
            holder.textViewComments.setText(comment.getComment());
            holder.textViewUserEmail.setText(comment.getUserEmail());
        }

        @Override
        public int getItemCount() {
            return commentDataList.size();
        }
    }
}
