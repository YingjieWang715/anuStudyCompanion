package au.edu.anu.comp6442.group03.studyapp.note.search;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.databinding.ActivityNoteSearchBinding;
import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.data.NoteDataManager;

public class NoteSearchActivity extends AppCompatActivity {
    private NoteDataManager noteDataManager;
    private NoteSearchListAdapter adapter;
    private ActivityNoteSearchBinding binding;

    private List<String> existingTags;
    private Timestamp startTimestamp;
    private Timestamp endTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNoteSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteDataManager = NoteDataManager.getInstance();
        noteDataManager.downloadNotes();

        setupRecyclerView();
        setupSearchView();
        setupFilters();
        setupSortNotes();

    }

    private void setupDatePicker() {
        binding.buttonDateFrom.setOnClickListener(view -> showDatePickerDialog(true));
        binding.buttonDateTo.setOnClickListener(view -> showDatePickerDialog(false));
    }

    private void showDatePickerDialog(boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar date = Calendar.getInstance();
                    date.set(Calendar.YEAR, year1);
                    date.set(Calendar.MONTH, monthOfYear);
                    date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    Timestamp selectedDate = new Timestamp(date.getTime());

                    if (isStartDate) {
                        startTimestamp = selectedDate;
                        binding.buttonDateFrom.setText(DateFormat.getDateInstance().format(date.getTime()));
                    } else {
                        endTimestamp = selectedDate;
                        binding.buttonDateTo.setText(DateFormat.getDateInstance().format(date.getTime()));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void setupFilters() {
        setupAutoCompleteTextView();
        setupDatePicker();
        binding.buttonApplyFilters.setOnClickListener(v -> applyFilters());
    }

    private void applyFilters() {
        String selectedTag = binding.autoCompleteTextViewTag.getText().toString();
        List<NoteData> currentNotes = adapter.getNoteList();
        List<NoteData> filteredTagResults = filterNotesByTag(selectedTag, currentNotes);
        List<NoteData> filteredDateResults = filterNotesByDate(filteredTagResults);
        adapter.updateData(filteredDateResults);
    }

    private List<NoteData> filterNotesByTag(String tag, List<NoteData> notes) {
        if (Objects.equals(tag, "") || tag == null) {
            return notes;
        }
        return notes.stream()
                .filter(note -> note.getTags().contains(tag))
                .collect(Collectors.toList());
    }

    private List<NoteData> filterNotesByDate(List<NoteData> notes) {
        if (startTimestamp == null || endTimestamp == null) {
            return notes;
        }

        return notes.stream()
                .filter(note -> note.getTimestamp().compareTo(startTimestamp) >= 0 &&
                        note.getTimestamp().compareTo(endTimestamp) <= 0)
                .collect(Collectors.toList());
    }

    private void setupAutoCompleteTextView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, existingTags);
        binding.autoCompleteTextViewTag.setAdapter(adapter);

        binding.autoCompleteTextViewTag.setOnEditorActionListener(((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addTagToChipGroup(binding.autoCompleteTextViewTag.getText().toString());
                binding.autoCompleteTextViewTag.setText("");
                return true;
            }
            return false;
        }));
    }

    private void addTagToChipGroup(String tag) {
        if (tag.isEmpty()) return;
        Chip chip = new Chip(this);
        chip.setText(tag);
        chip.setChipIconResource(R.drawable.ic_tag);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> binding.chipGroupTags.removeView(chip));
        binding.chipGroupTags.addView(chip);
    }

    private void setupSortNotes(){
        binding.buttonSortby.setOnClickListener(v -> sortBottomDialog());
    }
    private List<NoteData> sortByTitle(List<NoteData> retrievedNotes){
        //List<NoteData> retrievedNotes = adapter.getNoteList();

        return retrievedNotes.stream()
                .sorted(Comparator.comparing(NoteData::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
        //return retrievedNotes.stream().sorted(Comparator.comparing(NoteData::getTitle)).collect(Collectors.toList());
    }

    private List<NoteData> sortByDate(List<NoteData> retrievedNotes){
        //List<NoteData> retrievedNotes = adapter.
        return retrievedNotes.stream()
                .sorted(Comparator.comparing(NoteData::getTimestamp).reversed())
                .collect(Collectors.toList());

    }

    private void sortBottomDialog(){
        List<NoteData> retrievedNotes = adapter.getNoteList();

        // implementation from Android Knowledge youtube channel
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_sort_search);

        MaterialToolbar sortTitleBar = dialog.findViewById(R.id.sort_titleBar);
        sortTitleBar.setTitle("Sort Notes by:");
        FloatingActionButton closeSortDialogButton = dialog.findViewById(R.id.button_closeSort);
        RadioButton sortByTitleButton = dialog.findViewById(R.id.button_sortby_title);
        RadioButton sortByDateButton = dialog.findViewById(R.id.button_sortby_date);

        closeSortDialogButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        sortByTitleButton.setOnClickListener(v -> adapter.updateData(sortByTitle(retrievedNotes)));
        sortByDateButton.setOnClickListener(v -> adapter.updateData(sortByDate(retrievedNotes)));
    }

    private void setupRecyclerView() {
        adapter = new NoteSearchListAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearchWithFilter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearchWithoutFilter(newText);

                return false;
            }

        });
    }

    private void performSearchWithoutFilter(String query) {
        List<NoteData> matchedNotes = noteDataManager.searchNotesWithoutFilter(query);
        adapter.updateData(matchedNotes);
    }

    private void performSearchWithFilter(String query) {
        try {
            List<NoteData> filteredNotes = noteDataManager.searchNotesWithFilter(query);
            adapter.updateData(filteredNotes);
        } catch (Parser.ParseException e) {
            String errorMsg = e.getMessage();
            assert errorMsg != null;
            if (errorMsg.equals("Unknown field or value!")) {
                invalidSearchAlert(query);
            }
            /*
            else if(errorMsg.equals("Expect ':' after field.")){


            }
            else if(errorMsg.equals("Expect text after ':'.")){

            }
            else{

            }*/

        }
    }

    private void invalidSearchAlert(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteSearchActivity.this);
        builder.setTitle("Invalid search!");
        builder.setMessage("Display the notes with the closest results?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performSearchWithoutFilter(text);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog invalidSearchDialog = builder.create();
        invalidSearchDialog.show();

    }

    /*
    private void invalidSyntaxAlert(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteSearchActivity.this);
        builder.setTitle("Invalid query syntax!");
        String msg = "";
        switch(text){
            case ":":
                msg = "Please insert a colon after typing the title"
        }
        builder.setMessage("")
    }
    */

}