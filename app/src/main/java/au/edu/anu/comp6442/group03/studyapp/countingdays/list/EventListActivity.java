package au.edu.anu.comp6442.group03.studyapp.countingdays.list;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.security.cert.CertPathBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.Utility;
import au.edu.anu.comp6442.group03.studyapp.countingdays.OnItemClickListener;
import au.edu.anu.comp6442.group03.studyapp.countingdays.add.EventAddActivity;
import au.edu.anu.comp6442.group03.studyapp.countingdays.data.EventData;
import au.edu.anu.comp6442.group03.studyapp.countingdays.update.EventUpdateActivity;

public class EventListActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;
    private EventListAdapter adapter;
    private List<EventData> events = new ArrayList<>();

    private FloatingActionButton addEventButton;
    private ImageButton backButton;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_list);

        loadEventsFromFirebase();
        initializeRecyclerView();

        addEventButton = findViewById(R.id.button);
        addEventButton.setOnClickListener(v -> launchAddEventActivity());
        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void initializeRecyclerView() {
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventListAdapter(events);
        adapter.setOnItemClickListener(this::launchEventUpdateActivity);
        eventsRecyclerView.setAdapter(adapter);
    }

    private void loadEventsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Counting_days").document(currentUser.getUid()).collection("my_counting_days")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        events.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String eventName = document.getString("name");
                            Long eventDate = document.getLong("date");
                            if (eventName != null && eventDate != null) {
                                EventData event = new EventData(eventName, eventDate);
                                event.setDocumentId(document.getId());
                                events.add(event);
                            } else {
                                Log.e("EventListActivity", "Missing data for document: " + document.getId());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        checkEventsForToday();
                    } else {
                        Log.e("EventListActivity", "Error getting documents: ", task.getException());
                        Toast.makeText(this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void launchEventUpdateActivity(int position) {
        EventData event = events.get(position);
        Intent intent = new Intent(this, EventUpdateActivity.class);
        intent.putExtra("documentId", event.getDocumentId());
        startActivity(intent);
    }

    private void launchAddEventActivity() {
        Intent intent = new Intent(this, EventAddActivity.class);
        startActivity(intent);
    }

    private void sendNotification(String eventName) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 创建通知渠道，仅在API级别26及以上版本中需要
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "event_reminder",
                    "Event Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT); // 可考虑设置为IMPORTANCE_HIGH
            notificationManager.createNotificationChannel(channel);
        }

        // 检查NotificationManager是否可用
        if (notificationManager == null) {
            Log.e("Notification", "NotificationManager not available");
            return;
        }

        // 构建通知内容
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "event_reminder")
                .setSmallIcon(R.drawable.logo)  // 确保这个图标在drawable中存在
                .setContentTitle("Event Reminder")
                .setContentText("Reminder: " + eventName)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Today's Event: " + eventName))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);// 考虑设置为PRIORITY_HIGH
        if (notificationManager != null) {
            Log.e("Notification", "NotificationManager not null");
            notificationManager.notify(100, notificationBuilder.build()); // 使用一个唯一的ID来标识这个通知
        }
    }

    private void checkEventsForToday() {
        for (EventData event : events) {
            Log.d("EventCheck", "hello");
            if (event.isEventToday()) {
                Log.d("EventCheck", "Event today: " + event.getName());
                sendNotification(event.getName());
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEventsFromFirebase();
    }
}