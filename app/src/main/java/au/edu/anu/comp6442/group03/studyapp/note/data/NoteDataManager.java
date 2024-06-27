package au.edu.anu.comp6442.group03.studyapp.note.data;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.anu.comp6442.group03.studyapp.Utility;
import au.edu.anu.comp6442.group03.studyapp.note.search.Parser;
import au.edu.anu.comp6442.group03.studyapp.note.search.SearchParsingService;
import au.edu.anu.comp6442.group03.studyapp.note.search.Token;
import au.edu.anu.comp6442.group03.studyapp.note.search.Tokenizer;
import au.edu.anu.comp6442.group03.studyapp.note.search.expression.Exp;

public class NoteDataManager {
    private static NoteDataManager instance;
    private ArrayList<NoteData> allNotes = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private NoteDataManager() { }

    public static synchronized NoteDataManager getInstance() {
        if (instance == null) {
            instance = new NoteDataManager();
        }
        return instance;
    }


    public void downloadNotes() {
        db.collection("notes").document(currentUser.getUid()).collection("my_notes").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    NoteData noteData = document.toObject(NoteData.class);
                    allNotes.add(noteData);
                }
                Log.i("Firebase Log", "Notes downloaded and cached in memory.");
            } else {
                Log.e("Firebase Error", "Error getting documents: " + task.getException());
            }
        });
    }

    private ArrayList<NoteData> getAllNotes() {
        return allNotes;
    }

    public void synchronizeData() {
        db.collection("notes")
                .document(currentUser.getUid())
                .collection("my_notes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        long currentTime = System.currentTimeMillis();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            NoteData note = document.toObject(NoteData.class);
                            if (allNotes.contains(note)) {
                                allNotes.remove(note);
                                allNotes.add(note); // Replace with the updated note
                            } else {
                                allNotes.add(note); // Add new note
                            }
                        }
                        Log.i("Firebase Log", "Data synchronized.");
                    } else {
                        Log.e("Firebase Error", "Error synchronizing data: " + task.getException());
                    }
                });
    }

    public ArrayList<NoteData> searchNotesWithoutFilter(String searchText) {
        ArrayList<NoteData> searchResults = new ArrayList<>();
        String prcSearchText = searchText.trim().replaceAll("[^aA-zZ0-9]","");

        for (NoteData note : allNotes) {
            if (note.getContent().toLowerCase().contains(prcSearchText.toLowerCase()) ||
                    note.getContent().toLowerCase().contains(prcSearchText.toLowerCase())) {
                searchResults.add(note);
            }
        }
        return searchResults;
    }

    public ArrayList<NoteData> searchNotesWithFilter(String searchText) {
        ArrayList<NoteData> searchResults = new ArrayList<>();
        List<Exp> parseExpressions = new SearchParsingService().parse(searchText);
        for (NoteData note : allNotes) {
            boolean match = false;

            for (Exp exp : parseExpressions) {
                if (exp.match(note)) {
                    match = true;
                }
            }
            if (match) {
                searchResults.add(note);
            }
        }

        return searchResults;
    }
}
