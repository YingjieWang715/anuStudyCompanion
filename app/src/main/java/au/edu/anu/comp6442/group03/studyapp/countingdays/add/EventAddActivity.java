package au.edu.anu.comp6442.group03.studyapp.countingdays.add;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.annotation.Documented;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.Utility;
import au.edu.anu.comp6442.group03.studyapp.countingdays.data.EventData;
import au.edu.anu.comp6442.group03.studyapp.countingdays.list.EventListActivity;
import au.edu.anu.comp6442.group03.studyapp.note.add.NoteAddActivity;
import au.edu.anu.comp6442.group03.studyapp.note.list.NoteListActivity;

public class EventAddActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ImageButton saveButton;
    private ImageButton backButton;
    private TextView selectedDateText;
    private EditText eventNameEditText;
    public long selectedDate;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_add);

        calendarView = findViewById(R.id.calendarView);
        eventNameEditText = findViewById(R.id.editTextText);
        saveButton = findViewById(R.id.button_save);
        backButton = findViewById(R.id.button_back);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = convertToDate(year, month, dayOfMonth);
        });

        saveButton.setOnClickListener(v -> saveEvent());
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private long convertToDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTimeInMillis();
    }

    private void saveEvent() {
        String eventName = eventNameEditText.getText().toString().trim();
        if (eventName.isEmpty()) {
            Toast.makeText(this, "Event name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Counting_days").document(currentUser.getUid()).collection("my_counting_days").add(new EventData(eventName, selectedDate))
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error adding event", Toast.LENGTH_SHORT).show();
                });
    }
}