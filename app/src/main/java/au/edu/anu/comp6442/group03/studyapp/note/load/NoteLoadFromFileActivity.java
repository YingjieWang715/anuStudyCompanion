package au.edu.anu.comp6442.group03.studyapp.note.load;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;

import java.io.InputStream;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.Utility;
import au.edu.anu.comp6442.group03.studyapp.databinding.ActivityNoteLoadFromFileBinding;
import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.data.io.NoteDataReader;
import au.edu.anu.comp6442.group03.studyapp.note.data.io.NoteDataReaderFactory;

public class NoteLoadFromFileActivity extends AppCompatActivity {
    private ActivityNoteLoadFromFileBinding binding;
    private List<NoteData> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNoteLoadFromFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityResultLauncher<String> filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                this::processFileSelection
        );

        binding.buttonLoadFile.setOnClickListener(v -> filePickerLauncher.launch("*/*"));
        binding.buttonUpload.setOnClickListener(v -> uploadData(notes));
    }

    private void processFileSelection(Uri uri) {
        if (uri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                String fileType = getContentResolver().getType(uri);
                NoteDataReader reader = NoteDataReaderFactory.getReader(fileType);
                notes = reader.readData(inputStream);
                displayNotes(notes);
            } catch (Exception e) {
                Toast.makeText(this, "Error loading file: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No file selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayNotes(List<NoteData> notes) {
        StringBuilder notesPreview = new StringBuilder();
        for (NoteData note : notes) {
            notesPreview.append("Title:").append(note.getTitle()).append("\n");
            notesPreview.append("Content:").append(note.getContent().substring(0, 10)).append("...").append("\n\n");
        }
        binding.textViewStatus.setText(notesPreview.toString());
    }

    private void uploadData(List<NoteData> notes) {
        if (!notes.isEmpty()) {
            for (NoteData note : notes) {
                DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document();
                saveNoteToFirebase(note, documentReference);
            }
        } else {
            Toast.makeText(this, "No content to upload.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNoteToFirebase(NoteData noteData, DocumentReference documentReference) {
        documentReference.set(noteData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(NoteLoadFromFileActivity.this, "Note " + noteData.getTitle() + " added successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NoteLoadFromFileActivity.this, "Failed while adding the note: " + noteData.getTitle() + ".", Toast.LENGTH_SHORT).show();
            }
        });
    }

}