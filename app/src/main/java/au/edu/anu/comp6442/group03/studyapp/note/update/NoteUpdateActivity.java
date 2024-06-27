package au.edu.anu.comp6442.group03.studyapp.note.update;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.Utility;
import au.edu.anu.comp6442.group03.studyapp.databinding.ActivityNoteUpdateBinding;
import au.edu.anu.comp6442.group03.studyapp.note.add.NoteAddActivity;
import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.list.NoteListActivity;

public class NoteUpdateActivity extends AppCompatActivity {

    private ActivityNoteUpdateBinding binding;
    private String documentId;
    private NoteData currentNote;
    private Calendar reminderCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNoteUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        documentId = getIntent().getStringExtra("documentId");
        if (documentId != null) {
            loadNoteData(documentId);
        }

        setupEventListeners();
        setupReviseControls();
        setupTagControls();
    }

    private void loadNoteData(String noteId) {
        DocumentReference noteRef = Utility.getCollectionReferenceForNotes().document(noteId);
        noteRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    currentNote = document.toObject(NoteData.class);
                    updateUIWithNoteData(currentNote);
                } else {
                    Toast.makeText(this, "Note not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to load note", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupEventListeners() {
        binding.buttonSaveNote.setOnClickListener(v -> saveNote());
        binding.buttonMenu.setOnClickListener(v -> showMenu());
        binding.buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void updateUIWithNoteData(NoteData note) {
        binding.editTextTitle.setText(note.getTitle());
        binding.editTextDescription.setText(note.getContent());
        binding.switchReminder.setChecked(note.getReminder() != null);
        if (note.getReminder() != null) {
            reminderCalendar.setTime(note.getReminder().toDate());
        }
        for (String tag : note.getTags()) {
            addTagToChipGroup(tag);
        }
    }

    private void addTagToChipGroup(String tag) {
        Chip chip = new Chip(this);
        chip.setText(tag);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            binding.chipGroupTags.removeView(chip);
        });
        binding.chipGroupTags.addView(chip);
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
            if (!tag.isEmpty() && !currentNote.getTags().contains(tag)) {
                currentNote.addTag(tag);
                addTagToChipGroup(tag);
                binding.editTextTag.setText("");
            } else if (tag.isEmpty()) {
                Toast.makeText(NoteUpdateActivity.this, "Tag cannot be empty", Toast.LENGTH_SHORT).show();
            }
            resetTagInputVisibility();
        });
    }

    private void resetTagInputVisibility() {
        binding.buttonAddTag.setVisibility(View.VISIBLE);
        binding.textAddTag.setVisibility(View.VISIBLE);
        binding.editTextTag.setVisibility(View.GONE);
        binding.buttonAddTagConfirm.setVisibility(View.GONE);
    }

    private void saveNote() {
        String noteTitle = binding.editTextTitle.getText().toString();
        String noteContent = binding.editTextDescription.getText().toString();
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < binding.chipGroupTags.getChildCount(); i++) {
            Chip chip = (Chip) binding.chipGroupTags.getChildAt(i);
            tags.add(chip.getText().toString());
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
        DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(documentId);
        documentReference.set(noteData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(NoteUpdateActivity.this, "Note updated successfully.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(NoteUpdateActivity.this, NoteListActivity.class));
                finish();
            } else {
                Toast.makeText(NoteUpdateActivity.this, "Failed while updating the note.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteNoteFromFirebase() {
        new AlertDialog.Builder(this)
                .setTitle("Delete '" + binding.editTextTitle.getText().toString() + "'?")
                .setMessage("Are you sure you want to remove '" + binding.editTextTitle.getText().toString() + "'?")
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    Utility.getCollectionReferenceForNotes().document(documentId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), R.string.note_deleted_success, Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), R.string.note_deleted_failed, Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }


    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(NoteUpdateActivity.this, binding.buttonMenu);
        popupMenu.getMenu().add("Delete");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Delete")) {
                deleteNoteFromFirebase();
            }
            return false;
        });
    }

}