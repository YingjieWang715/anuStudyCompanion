package au.edu.anu.comp6442.group03.studyapp.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.Utility;

public class ReminderListActivity extends AppCompatActivity {
    private FloatingActionButton buttonAddReminder;
    private ImageButton buttonBack;
    private ReminderListAdapter reminderListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reminder_list);

        recyclerView = findViewById(R.id.recycler_view);
        buttonAddReminder = findViewById(R.id.addNewReminder);
        buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        buttonAddReminder.setOnClickListener(v -> startActivity(new Intent(ReminderListActivity.this, AddReminderActivity.class)));

        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        Query query = Utility.getCollectionReferenceForReminders().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ReminderItem> options = new FirestoreRecyclerOptions.Builder<ReminderItem>()
                .setQuery(query, ReminderItem.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderListAdapter = new ReminderListAdapter(options, this);
        recyclerView.setAdapter(reminderListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        reminderListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        reminderListAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reminderListAdapter.notifyDataSetChanged();
    }
}