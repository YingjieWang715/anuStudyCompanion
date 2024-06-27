package au.edu.anu.comp6442.group03.studyapp.reminder;

import static au.edu.anu.comp6442.group03.studyapp.Utility.scheduleReminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.Locale;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.Utility;

public class EditReminderActivity extends AppCompatActivity {

    private EditText editTextReminder;
    private ImageButton buttonSave;
    private ImageButton buttonBack;
    private TextView textViewDate, textViewTime, deleteTextViewBtn;
    private Switch switchDate, switchTime;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private String reminderText, reminderId;

    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_reminder);

        editTextReminder = findViewById(R.id.editTextReminder);
        buttonSave = findViewById(R.id.buttonSave);
        buttonBack = findViewById(R.id.button_back);
        switchDate = findViewById(R.id.switchDate);
        switchTime = findViewById(R.id.switchTime);
        textViewDate = findViewById(R.id.dateDisplay);
        textViewTime = findViewById(R.id.timeDisplay);
        deleteTextViewBtn = findViewById(R.id.delete_text_view_btn);

        reminderText = getIntent().getStringExtra("reminderText");
        selectedYear = getIntent().getIntExtra("selectedYear", -1);
        selectedMonth = getIntent().getIntExtra("selectedMonth", -1);
        selectedDay = getIntent().getIntExtra("selectedDay", -1);
        selectedHour = getIntent().getIntExtra("selectedHour", -1);
        selectedMinute = getIntent().getIntExtra("selectedMinute", -1);
        reminderId = getIntent().getStringExtra("reminderId");

        if (reminderId != null && !reminderId.isEmpty()){
            isEditMode = true;
        }

        if (isEditMode){
            deleteTextViewBtn.setVisibility(View.VISIBLE);
        }

        editTextReminder.setText(reminderText);

        if (selectedYear != -1 && selectedMonth != -1 && selectedDay != -1) {
            switchDate.setChecked(true);
            displaySelectedDate(selectedYear, selectedMonth, selectedDay);
        }

        switchTime.setEnabled(switchDate.isChecked());

        if (selectedHour != -1 && selectedMinute != -1) {
            switchTime.setChecked(true);
            displaySelectedTime(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
        }

        switchDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                final Calendar c = Calendar.getInstance();
                selectedYear = c.get(Calendar.YEAR);
                selectedMonth = c.get(Calendar.MONTH);
                selectedDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditReminderActivity.this,
                        (view, yearSelected, monthOfYear, dayOfMonth) -> {
                            selectedYear = yearSelected;
                            selectedMonth = monthOfYear;
                            selectedDay = dayOfMonth;

                            displaySelectedDate(selectedYear, selectedMonth, selectedDay);
                            switchTime.setEnabled(true);
                        }, selectedYear, selectedMonth, selectedDay);
                datePickerDialog.setOnCancelListener(dialog -> switchDate.setChecked(false));
                datePickerDialog.show();
            } else {
                textViewDate.setText("");
                switchTime.setChecked(false);
                switchTime.setEnabled(false);

                // Reset the following variables to their default values
                selectedYear = -1;
                selectedMonth = -1;
                selectedDay = -1;
                selectedHour = -1;
                selectedMinute = -1;
            }
        });

        switchTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                final Calendar c = Calendar.getInstance();
                selectedHour = c.get(Calendar.HOUR_OF_DAY);
                selectedMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditReminderActivity.this,
                        (view, hourOfDay, minuteOfHour) -> {
                            selectedHour = hourOfDay;
                            selectedMinute = minuteOfHour;

                            displaySelectedDate(selectedYear, selectedMonth, selectedDay);
                            displaySelectedTime(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
                        }, selectedHour, selectedMinute, false);
                timePickerDialog.setOnCancelListener(dialog -> switchTime.setChecked(false));
                timePickerDialog.show();
            } else {
                textViewTime.setText("");
                selectedHour = -1;
                selectedMinute = -1;
            }
        });

        buttonSave.setOnClickListener(v -> saveReminder());
        buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        deleteTextViewBtn.setOnClickListener(v -> deleteReminderFromFirebase());
    }

    private void saveReminder(){
        String reminderText = editTextReminder.getText().toString().trim();
        if (reminderText.isEmpty()){
            editTextReminder.setError("The input content cannot be empty :(");
            return;
        }

        ReminderItem reminderItem = new ReminderItem();
        reminderItem.setReminderText(reminderText);
        reminderItem.setYear(selectedYear);
        reminderItem.setMonth(selectedMonth);
        reminderItem.setDay(selectedDay);
        reminderItem.setHour(selectedHour);
        reminderItem.setMinute(selectedMinute);
        reminderItem.setTimestamp(Timestamp.now());

        scheduleReminder(this, selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, reminderText);
        saveReminderToFirebase(reminderItem);
    }

    private void saveReminderToFirebase(ReminderItem reminderItem){
        DocumentReference documentReference;
        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForReminders().document(reminderId);
        } else {
            documentReference = Utility.getCollectionReferenceForReminders().document();
        }
        documentReference.set(reminderItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditReminderActivity.this, "Reminder added successfully.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditReminderActivity.this, ReminderListActivity.class));
                finish();
            } else {
                Toast.makeText(EditReminderActivity.this, "Something wrong while adding the reminder :(", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void deleteReminderFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForReminders().document(reminderId);
        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditReminderActivity.this, "Reminder deleted successfully.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditReminderActivity.this, ReminderListActivity.class));
                finish();
            } else {
                Toast.makeText(EditReminderActivity.this, "Something wrong while deleting the reminder :(", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void displaySelectedDate(int year, int month, int day) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);
        Calendar currentDate = Calendar.getInstance();

        // If the selected date is earlier than the current date, set text color to red
        int dateTextColor = selectedDate.before(currentDate) ? Color.RED : Color.parseColor("#3B82F7");
        textViewDate.setTextColor(dateTextColor);
        textViewDate.setText(String.format(Locale.getDefault(), "%d-%d-%d", year, month + 1, day));
    }

    private void displaySelectedTime(int year, int month, int day, int hour, int minute) {
        Calendar selectedTime = Calendar.getInstance();
        selectedTime.set(year, month, day, hour, minute);
        Calendar currentTime = Calendar.getInstance();

        // If the selected time is earlier than the current time, set both date text and time text color to red
        int timeTextColor = selectedTime.before(currentTime) ? Color.RED : Color.parseColor("#3B82F7");
        textViewDate.setTextColor(timeTextColor);
        textViewTime.setTextColor(timeTextColor);
        textViewTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
    }
}