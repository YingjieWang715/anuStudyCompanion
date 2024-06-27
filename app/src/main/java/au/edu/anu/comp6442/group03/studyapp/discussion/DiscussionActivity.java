package au.edu.anu.comp6442.group03.studyapp.discussion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.countingdays.data.EventData;
import au.edu.anu.comp6442.group03.studyapp.countingdays.list.EventListAdapter;
import au.edu.anu.comp6442.group03.studyapp.countingdays.update.EventUpdateActivity;

public class DiscussionActivity extends AppCompatActivity {

    private RecyclerView discussionRecyclerView;
    private DiscussionAdapter adapter;
    // private DatabaseReference databaseReference;
    private FloatingActionButton addDiscussionButton;
    private ImageButton backButton;

    private Handler handler = new Handler();
    private Runnable loadDataRunnable;
    private List<DiscussionData> discussionDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_discussion);

        initializeRecyclerView();

//        loadDataRunnable = new Runnable() {
//            @Override
//            public void run() {
//                simulateDataStream();
//                handler.postDelayed(this, 20000);
//            }
//        };
//        handler.post(loadDataRunnable);

        loadEventsFromFirebase();

        addDiscussionButton = findViewById(R.id.addDiscussionButton);
        addDiscussionButton.setOnClickListener(v -> {
            Intent intent = new Intent(DiscussionActivity.this, DiscussionAddActivity.class);
            startActivity(intent);

        });

        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
    private void initializeRecyclerView() {
        discussionRecyclerView = findViewById(R.id.discussionRecyclerView);
        discussionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiscussionAdapter(discussionDatas);
        adapter.setDiscussionOnItemClickListener(this::launchEventUpdateActivity);
        discussionRecyclerView.setAdapter(adapter);
    }

    private void launchEventUpdateActivity(int position) {
        DiscussionData discussionData = discussionDatas.get(position);
        Intent intent = new Intent(this, DiscussionCommentActivity.class);
        intent.putExtra("id", discussionData.getId());
        intent.putExtra("title", discussionData.getTitle());
        intent.putExtra("description", discussionData.getDescription());
        intent.putExtra("userEmail", discussionData.getUserEmail());
        //String discussionId = this.getSnapshot
        startActivity(intent);
    }
    private void loadEventsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discussion")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        discussionDatas.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String description = document.getString("description");
                            String title = document.getString("title");
                            String id = document.getString("id");
                            String userId = document.getString("userId");
                            String userEmail = document.getString("userEmail");
                            if (description != null && title != null) {
                                DiscussionData discussionData = new DiscussionData(id, title, description, userId, userEmail);
                                discussionData.setId(document.getId());
                                discussionDatas.add(discussionData);
                            } else {
                                Log.e("DiscussionActivity", "Missing data for document: " + document.getId());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("DiscussionActivity", "Error getting documents: ", task.getException());
                        Toast.makeText(this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void simulateDataStream(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("data.csv"), StandardCharsets.UTF_8));
            int totalLines = 0;
            while (bufferedReader.readLine() != null) {
                totalLines++;
            }

            Random random = new Random();
            int randomLineNum = random.nextInt(totalLines) + 1;

            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("data.csv"), StandardCharsets.UTF_8));
            int currentLineNum = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null){
                currentLineNum++;
                if (currentLineNum == randomLineNum){
                    String[] tokens = line.split(",");
                    DiscussionData data = new DiscussionData();
                    String title = tokens[0];
                    String description = tokens[1];
                    String userId = tokens[2];
                    String id = userId + "_" + System.currentTimeMillis();
                    data.setTitle(title);
                    data.setDescription(description);
                    data.setUserId(userId);
                    data.setId(id);

                    if (!title.isEmpty() && !description.isEmpty() && userId != null) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("discussion").add(data)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error adding event", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Title, Description and User authentication cannot be empty.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        loadEventsFromFirebase();
    }
    protected void onResume() {
        super.onResume();
        loadEventsFromFirebase();
    }
}