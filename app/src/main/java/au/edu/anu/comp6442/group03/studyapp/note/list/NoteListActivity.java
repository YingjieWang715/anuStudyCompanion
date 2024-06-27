package au.edu.anu.comp6442.group03.studyapp.note.list;

import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import au.edu.anu.comp6442.group03.studyapp.Utility;
import au.edu.anu.comp6442.group03.studyapp.databinding.ActivityNoteListBinding;
import au.edu.anu.comp6442.group03.studyapp.note.add.NoteAddActivity;
import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.load.NoteLoadFromFileActivity;
import au.edu.anu.comp6442.group03.studyapp.note.search.NoteSearchActivity;

public class NoteListActivity extends AppCompatActivity {

    private ActivityNoteListBinding binding;
    private NoteListAdapter noteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNoteListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonAddNote.setOnClickListener(v -> startActivity(new Intent(NoteListActivity.this, NoteAddActivity.class)));
        binding.buttonMenu.setOnClickListener(v -> showMenu());
        binding.buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.buttonSearch.setOnClickListener(v -> startActivity(new Intent(NoteListActivity.this, NoteSearchActivity.class)));
        setupRecyclerView();

    }

    private void showMenu() {
        String loadNoteFromFile = "Load Note From File";
        PopupMenu popupMenu = new PopupMenu(NoteListActivity.this, binding.buttonMenu);
        popupMenu.getMenu().add(loadNoteFromFile);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals(loadNoteFromFile)) {
                startActivity(new Intent(NoteListActivity.this, NoteLoadFromFileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NoteData> options = new FirestoreRecyclerOptions.Builder<NoteData>()
                .setQuery(query, NoteData.class).build();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteListAdapter = new NoteListAdapter(options, this);
        binding.recyclerView.setAdapter(noteListAdapter);
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteListAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteListAdapter.notifyDataSetChanged();
    }

}