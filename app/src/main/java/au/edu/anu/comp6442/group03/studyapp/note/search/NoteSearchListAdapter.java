package au.edu.anu.comp6442.group03.studyapp.note.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.databinding.NoteRowLayoutBinding;
import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.update.NoteUpdateActivity;

public class NoteSearchListAdapter extends RecyclerView.Adapter<NoteSearchListAdapter.NoteViewHolder> {
    private List<NoteData> noteList = new ArrayList<>();
    private Context context;

    public NoteSearchListAdapter(Context context) {
        super();
        this.context = context;
    }

    public void updateData(List<NoteData> newNoteList) {
        noteList.clear();
        noteList.addAll(newNoteList);
        notifyDataSetChanged();
    }

    public List<NoteData> getNoteList() {
        return noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return NoteViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteData note = noteList.get(position);
        holder.binding.textViewTitle.setText(note.getTitle());
        holder.binding.textViewDescription.setText(note.getContent());
        holder.binding.rowBackground.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteUpdateActivity.class);
            intent.putExtra("title", holder.binding.textViewTitle.getText().toString());
            intent.putExtra("description", holder.binding.textViewDescription.getText().toString());
            intent.putExtra("documentId", note.getDocumentId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
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

