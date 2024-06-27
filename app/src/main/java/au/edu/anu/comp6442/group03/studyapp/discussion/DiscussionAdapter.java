package au.edu.anu.comp6442.group03.studyapp.discussion;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.countingdays.OnItemClickListener;
import au.edu.anu.comp6442.group03.studyapp.countingdays.list.EventListAdapter;
import au.edu.anu.comp6442.group03.studyapp.discussion.DiscussionData;
import au.edu.anu.comp6442.group03.studyapp.discussion.DiscussionOnItemClickLinstener;


public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder> {
    private List<DiscussionData> discussions;
    private OnItemClickListener listener;

    public DiscussionAdapter(List<DiscussionData> discussions) {
        this.discussions = discussions;
    }

    @NonNull
    @Override
    public DiscussionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussion_row_layout, parent, false);
        return new DiscussionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionViewHolder holder, int position) {
        DiscussionData discussion = discussions.get(position);
        holder.titleTextView.setText(discussion.getTitle());
        holder.contentTextView.setText(discussion.getDescription());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("DiscussionAdapter", "getItemCount called, size: " + discussions.size());
        return discussions.size();
    }

    public void updateDiscussions(List<DiscussionData> newDiscussions) {
        discussions = newDiscussions;
        notifyDataSetChanged();
    }

    public void setDiscussionOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class DiscussionViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView;

        DiscussionViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.discussion_view_title);
            contentTextView = itemView.findViewById(R.id.discussion_view_description);
        }
    }

}