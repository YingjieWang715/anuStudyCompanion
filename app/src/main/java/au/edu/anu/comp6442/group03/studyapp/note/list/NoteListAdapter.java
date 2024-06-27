package au.edu.anu.comp6442.group03.studyapp.note.list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import au.edu.anu.comp6442.group03.studyapp.databinding.NoteRowLayoutBinding;
import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.update.NoteUpdateActivity;

public class NoteListAdapter extends FirestoreRecyclerAdapter<NoteData, NoteListAdapter.NoteViewHolder> {
    private Context context;

    public NoteListAdapter(@NonNull FirestoreRecyclerOptions<NoteData> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position, @NonNull NoteData noteData) {
        noteViewHolder.binding.textViewTitle.setText(noteData.getTitle());
        noteViewHolder.binding.textViewDescription.setText(noteData.getContent());
        noteViewHolder.binding.rowBackground.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteUpdateActivity.class);
            intent.putExtra("title", noteViewHolder.binding.textViewTitle.getText().toString());
            intent.putExtra("description", noteViewHolder.binding.textViewDescription.getText().toString());
            intent.putExtra("documentId", noteData.getDocumentId());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return NoteViewHolder.from(parent);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        private final NoteRowLayoutBinding binding;
        public NoteViewHolder(NoteRowLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private static NoteViewHolder from(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            NoteRowLayoutBinding binding = NoteRowLayoutBinding.inflate(layoutInflater, parent, false);
            return new NoteViewHolder(binding);
        }
    }
}
