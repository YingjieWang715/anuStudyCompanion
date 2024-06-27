package au.edu.anu.comp6442.group03.studyapp.reminder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.Utility;


public class ReminderListAdapter extends FirestoreRecyclerAdapter<ReminderItem, ReminderListAdapter.ReminderViewHolder> {

    private Context context;
    public ReminderListAdapter(@NonNull FirestoreRecyclerOptions<ReminderItem> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReminderViewHolder holder, int position, @NonNull ReminderItem reminderItem) {
        holder.reminderTextView.setText(reminderItem.getReminderText());
        holder.reminderTimeTextView.setText(Utility.timestampToString(reminderItem.getTimestamp()));


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditReminderActivity.class);
            intent.putExtra("reminderText", reminderItem.getReminderText());
            intent.putExtra("selectedYear", reminderItem.getYear());
            intent.putExtra("selectedMonth", reminderItem.getMonth());
            intent.putExtra("selectedDay", reminderItem.getDay());
            intent.putExtra("selectedHour", reminderItem.getHour());
            intent.putExtra("selectedMinute", reminderItem.getMinute());
            String reminderId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("reminderId", reminderId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent,false);
        return new ReminderViewHolder(view);
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder{

        TextView reminderTextView;
        TextView reminderTimeTextView;
        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderTextView = itemView.findViewById(R.id.reminder_text_view);
            reminderTimeTextView = itemView.findViewById(R.id.reminder_time_text_view);
        }
    }
}
