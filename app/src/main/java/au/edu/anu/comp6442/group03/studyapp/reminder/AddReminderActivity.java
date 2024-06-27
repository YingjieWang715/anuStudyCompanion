package au.edu.anu.comp6442.group03.studyapp.reminder;

import static au.edu.anu.comp6442.group03.studyapp.Utility.scheduleReminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.Locale;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.Utility;

public class AddReminderActivity extends AppCompatActivity {

    private EditText editTextReminder;
    private TextView textViewDate, textViewTime;
    private Button buttonAdd;
    private ImageButton buttonBack;
    private Switch switchDate, switchTime;

    private int selectedYear = -1, selectedMonth = -1, selectedDay = -1, selectedHour = -1, selectedMinute = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        editTextReminder = findViewById(R.id.editTextReminder);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonBack = findViewById(R.id.button_back);
        switchDate = findViewById(R.id.switchDate);
        switchTime = findViewById(R.id.switchTime);
        textViewDate = findViewById(R.id.dateDisplay);
        textViewTime = findViewById(R.id.timeDisplay);

        switchTime.setEnabled(false); // Disable time switch initially

        // Listener of date switch
        switchDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                // Get current date
                Calendar now = Calendar.getInstance();
                selectedYear = now.get(Calendar.YEAR);
                selectedMonth = now.get(Calendar.MONTH);
                selectedDay = now.get(Calendar.DAY_OF_MONTH);

                // Pop up date picker
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddReminderActivity.this,
                        (view, yearSelected, monthOfYear, dayOfMonth) -> {
                            selectedYear = yearSelected;
                            selectedMonth = monthOfYear;
                            selectedDay = dayOfMonth;

                            displaySelectedDate(selectedYear, selectedMonth, selectedDay);  // Update date display
                            switchTime.setEnabled(true);  // Enable time Switch after selecting date
                        }, selectedYear, selectedMonth, selectedDay);
                datePickerDialog.setOnCancelListener(dialog -> {switchDate.setChecked(false);});
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

        // Listener of time switch
        switchTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Get current time

                Calendar now = Calendar.getInstance();
                selectedHour = now.get(Calendar.HOUR_OF_DAY);
                selectedMinute = now.get(Calendar.MINUTE);

                // Pop up time picker
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddReminderActivity.this,
                        (view, hourOfDay, minuteOfHour) -> {
                            selectedHour = hourOfDay;
                            selectedMinute = minuteOfHour;

                            displaySelectedDate(selectedYear, selectedMonth, selectedDay);
                            displaySelectedTime(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);  // Update time display
                        }, selectedHour, selectedMinute, false);
                timePickerDialog.setOnCancelListener(dialog -> {switchTime.setChecked(false);});
                timePickerDialog.show();
            } else {
                textViewTime.setText("");
                selectedHour = -1;
                selectedMinute = -1;
            }
        });

        buttonAdd.setOnClickListener(v -> saveReminder());
        buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

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
        DocumentReference documentReference = Utility.getCollectionReferenceForReminders().document();
        documentReference.set(reminderItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddReminderActivity.this, "Reminder added successfully.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddReminderActivity.this, ReminderListActivity.class));
                finish();
            } else {
                Toast.makeText(AddReminderActivity.this, "Something wrong while adding the reminder :(", Toast.LENGTH_LONG).show();

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