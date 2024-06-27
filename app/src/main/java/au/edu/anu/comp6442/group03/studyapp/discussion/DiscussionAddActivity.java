package au.edu.anu.comp6442.group03.studyapp.discussion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.countingdays.data.EventData;

public class DiscussionAddActivity extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private Button submitBtn;
    //private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_discussion_add);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        editTitle = findViewById(R.id.edit_discussion_title);
        editDescription = findViewById(R.id.edit_discussion_description);
        submitBtn = findViewById(R.id.submitBtn);

        //databaseReference = FirebaseDatabase.getInstance().getReference("discussions");

        submitBtn.setOnClickListener(v -> addDiscussion());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void addDiscussion() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (!title.isEmpty() && !description.isEmpty() && currentUser != null) {
            String userId = currentUser.getUid();
            String userEmail = currentUser.getEmail();
            String id = userId + "_" + System.currentTimeMillis();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("discussion").add(new DiscussionData(id, title, description, userId, userEmail, null))
                    .addOnSuccessListener(documentReference -> {
                        String documentName=documentReference.getId();
                        documentReference.update("id", documentName);
                        Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error adding event", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Title, Description and User authentication cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }
}