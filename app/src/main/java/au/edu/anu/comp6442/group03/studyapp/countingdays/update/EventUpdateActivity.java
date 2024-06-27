package au.edu.anu.comp6442.group03.studyapp.countingdays.update;

import android.app.Activity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import au.edu.anu.comp6442.group03.studyapp.R;

public class EventUpdateActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ImageButton editButton;
    private ImageButton backButton;
    private Button removeButton;
    private TextView selectedDateText;
    private TextView selectedDatePromptText;

    private EditText eventNameEditText;
    private long selectedDate;
    private DocumentReference eventDocRef;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_update);

        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");

        initializeViews();
        fetchEventInfo(documentId);
        setupListeners();
    }
    private void initializeViews() {
        calendarView = findViewById(R.id.calendarView_edit);
        eventNameEditText = findViewById(R.id.editTextText_edit);
        selectedDateText = findViewById(R.id.selected_date_text_view);
        selectedDatePromptText = findViewById(R.id.selected_date_prompt_text_view);
        editButton = findViewById(R.id.button_edit);
        removeButton = findViewById(R.id.button_remove);
        backButton = findViewById(R.id.button_back);

        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void fetchEventInfo(String documentId) {
        if (currentUser != null) {
            eventDocRef = FirebaseFirestore.getInstance().collection("Counting_days")
                    .document(currentUser.getUid())
                    .collection("my_counting_days")
                    .document(documentId);
            eventDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String eventName = documentSnapshot.getString("name");
                    Long eventDate = documentSnapshot.getLong("date");
                    if (eventName != null && eventDate != null) {
                        eventNameEditText.setText(eventName);
                        selectedDate = eventDate;
                        calendarView.setDate(selectedDate, false, true);
                        updateSelectedDateText(selectedDate);
                    } else {
                        // 处理事件名称或事件日期为空的情况
                        Toast.makeText(this, "Event name or date is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to fetch event.", Toast.LENGTH_SHORT).show();
                finish();
            });
        } else {
            // 处理 currentUser 为空的情况
            Toast.makeText(this, "Current user is null", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupListeners() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                selectedDate = calendar.getTimeInMillis();
                updateSelectedDateText(selectedDate);
            }
        });

        editButton.setOnClickListener(v -> saveEvent());
        removeButton.setOnClickListener(v -> deleteEvent());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateSelectedDateText(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        String dateString = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        selectedDatePromptText.setText(R.string.you_selected);
        selectedDateText.setText(dateString);
    }

    private void saveEvent() {
        String eventName = eventNameEditText.getText().toString();
        if (eventName.isEmpty()) {
            Toast.makeText(this, "Event name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        eventDocRef.update("name", eventName, "date", selectedDate)
                .addOnSuccessListener(aVoid -> {
                    setResult(Activity.RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update event.", Toast.LENGTH_SHORT).show());
    }

    private void deleteEvent() {
        eventDocRef.delete()
                .addOnSuccessListener(aVoid -> {
                    setResult(Activity.RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete event.", Toast.LENGTH_SHORT).show());
    }

}