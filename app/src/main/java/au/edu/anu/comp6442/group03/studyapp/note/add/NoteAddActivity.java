package au.edu.anu.comp6442.group03.studyapp.note.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.Utility;
import au.edu.anu.comp6442.group03.studyapp.databinding.ActivityNoteAddBinding;
import au.edu.anu.comp6442.group03.studyapp.note.data.BinarySearchTree;
import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.list.NoteListActivity;

public class NoteAddActivity extends AppCompatActivity {

    private ActivityNoteAddBinding binding;
    private Calendar reminderCalendar;
    BinarySearchTree<String> existingTags = new BinarySearchTree<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNoteAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reminderCalendar = Calendar.getInstance();

        binding.buttonSaveNote.setOnClickListener(v -> saveNote());
        binding.buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        setupReviseControls();
        setupTagControls();
    }

    private void saveNote() {
        String noteTitle = binding.editTextTitle.getText().toString();
        String noteContent = binding.editTextDescription.getText().toString();
        List<String> tags = new ArrayList<>();
//        for (int i = 0; i < binding.chipGroupTags.getChildCount(); i++) {
//            Chip chip = (Chip) binding.chipGroupTags.getChildAt(i);
//            tags.add(chip.getText().toString());
//        }

        for (String existingTag : existingTags) {
            tags.add(existingTag);
        }

        if (noteTitle.equals(null) || noteTitle.isEmpty()) {
            binding.editTextTitle.setError("Title is required");
            return;
        }

        NoteData noteData = new NoteData();
        noteData.setTitle(noteTitle);
        noteData.setContent(noteContent);
        noteData.setTimestamp(Timestamp.now());
        if (binding.switchReminder.isChecked()) {
            noteData.setReminder(new Timestamp(reminderCalendar.getTime()));
        }
        noteData.setTags(tags);

        saveNoteToFirebase(noteData);
    }

    private void saveNoteToFirebase(NoteData noteData) {
        DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document();
        documentReference.set(noteData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(NoteAddActivity.this, "Note added successfully.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(NoteAddActivity.this, NoteListActivity.class));
                finish();
            } else {
                Toast.makeText(NoteAddActivity.this, "Failed while adding the note.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupReviseControls() {
        binding.switchReminder.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            binding.buttonSetDate.setEnabled(isChecked);
            binding.buttonSetTime.setEnabled(isChecked);
            if (isChecked) {
                binding.labelReminder.setText(R.string.next_revise_time);
            } else {
                binding.labelReminder.setText(R.string.revise_disabled);
            }
        }));


        binding.buttonSetDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                reminderCalendar.set(Calendar.YEAR, year);
                reminderCalendar.set(Calendar.MONTH, month);
                reminderCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }, reminderCalendar.get(Calendar.YEAR), reminderCalendar.get(Calendar.MONTH), reminderCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        binding.buttonSetTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                reminderCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                reminderCalendar.set(Calendar.MINUTE, minute);
            }, reminderCalendar.get(Calendar.HOUR_OF_DAY), reminderCalendar.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        });
    }

    private void setupTagControls() {

        binding.buttonAddTag.setOnClickListener(v -> {
            binding.buttonAddTag.setVisibility(View.GONE);
            binding.textAddTag.setVisibility(View.GONE);
            binding.editTextTag.setVisibility(View.VISIBLE);
            binding.buttonAddTagConfirm.setVisibility(View.VISIBLE);
        });

        binding.buttonAddTagConfirm.setOnClickListener(v -> {

            String tag = binding.editTextTag.getText().toString().trim();
            if (!tag.isEmpty() && !existingTags.contains(tag)) {
                existingTags.insert(tag);
                addTagToChipGroup(tag);
                binding.editTextTag.setText("");
            } else if (tag.isEmpty()) {
                Toast.makeText(NoteAddActivity.this, "Tag cannot be empty", Toast.LENGTH_SHORT).show();
            }
            resetTagInputVisibility();
        });
        setupDefaultTags();
    }

    private void resetTagInputVisibility() {
        binding.buttonAddTag.setVisibility(View.VISIBLE);
        binding.textAddTag.setVisibility(View.VISIBLE);
        binding.editTextTag.setVisibility(View.GONE);
        binding.buttonAddTagConfirm.setVisibility(View.GONE);
    }

    private void addTagToChipGroup(String tag) {
        Chip chip = new Chip(this);
        chip.setText(tag);
        chip.setCloseIconVisible(true);
        chip.setCheckedIconVisible(true);
        chip.setChecked(true);
        chip.setClickable(true);
        chip.setFocusable(true);
        chip.setCheckable(true);
        chip.setOnCloseIconClickListener(v -> {
            binding.chipGroupTags.removeView(chip);
            existingTags.delete(tag);
        });
        binding.chipGroupTags.addView(chip);
    }

    private void setupDefaultTags() {
        binding.chipHomework.setOnClickListener(v -> {
            String tag = binding.chipHomework.getText().toString();
            if (binding.chipHomework.isChecked()) {
                existingTags.insert(tag);
            } else {
                existingTags.delete(tag);
            }
        });

        binding.chipExams.setOnClickListener(v -> {
            String tag = binding.chipExams.getText().toString();
            if (binding.chipExams.isChecked()) {
                existingTags.insert(tag);
            } else {
                existingTags.delete(tag);
            }
        });

        binding.chipProjects.setOnClickListener(v -> {
            String tag = binding.chipProjects.getText().toString();
            if (binding.chipProjects.isChecked()) {
                existingTags.insert(tag);
            } else {
                existingTags.delete(tag);
            }
        });

        binding.chipLectures.setOnClickListener(v -> {
            String tag = binding.chipLectures.getText().toString();
            if (binding.chipLectures.isChecked()) {
                existingTags.insert(tag);
            } else {
                existingTags.delete(tag);
            }
        });

        binding.chipReadings.setOnClickListener(v -> {
            String tag = binding.chipReadings.getText().toString();
            if (binding.chipReadings.isChecked()) {
                existingTags.insert(tag);
            } else {
                existingTags.delete(tag);
            }
        });

        binding.chipEssays.setOnClickListener(v -> {
            String tag = binding.chipEssays.getText().toString();
            if (binding.chipEssays.isChecked()) {
                existingTags.insert(tag);
            } else {
                existingTags.delete(tag);
            }
        });

        binding.chipLabWork.setOnClickListener(v -> {
            String tag = binding.chipLabWork.getText().toString();
            if (binding.chipLabWork.isChecked()) {
                existingTags.insert(tag);
            } else {
                existingTags.delete(tag);
            }
        });

        binding.chipGroupStudy.setOnClickListener(v -> {
            String tag = binding.chipGroupStudy.getText().toString();
            if (binding.chipGroupStudy.isChecked()) {
                existingTags.insert(tag);
            } else {
                existingTags.delete(tag);
            }
        });

        binding.chipPresentations.setOnClickListener(v -> {
            String tag = binding.chipPresentations.getText().toString();
            if (binding.chipPresentations.isChecked()) {
                existingTags.insert(tag);
            } else {
                existingTags.delete(tag);
            }
        });

        binding.chipResearch.setOnClickListener(v -> {
            String tag = binding.chipResearch.getText().toString();
            if (binding.chipResearch.isChecked()) {
                existingTags.insert(tag);
            } else {
                existingTags.delete(tag);
            }
        });

    }
}