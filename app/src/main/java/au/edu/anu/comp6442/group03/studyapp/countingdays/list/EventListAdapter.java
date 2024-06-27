package au.edu.anu.comp6442.group03.studyapp.countingdays.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.countingdays.OnItemClickListener;
import au.edu.anu.comp6442.group03.studyapp.countingdays.data.EventData;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<EventData> events;
    private OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventNameTextView, eventDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.text_view_name);
            eventDateTextView = itemView.findViewById(R.id.text_view_time);
        }
    }

    public EventListAdapter(List<EventData> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventData event = events.get(position);
        holder.eventNameTextView.setText(event.getName());
        holder.eventDateTextView.setText(event.getDateString()); // 确保 EventData 类有这个方法
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
