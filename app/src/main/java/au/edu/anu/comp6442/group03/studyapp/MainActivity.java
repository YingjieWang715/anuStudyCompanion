package au.edu.anu.comp6442.group03.studyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import au.edu.anu.comp6442.group03.studyapp.account.AccountEntryActivity;
import au.edu.anu.comp6442.group03.studyapp.account.ProfilePageActivity;
import au.edu.anu.comp6442.group03.studyapp.countingdays.list.EventListActivity;
import au.edu.anu.comp6442.group03.studyapp.databinding.ActivityMainBinding;
import au.edu.anu.comp6442.group03.studyapp.discussion.DiscussionActivity;
import au.edu.anu.comp6442.group03.studyapp.note.list.NoteListActivity;
import au.edu.anu.comp6442.group03.studyapp.reminder.ReminderListActivity;
import au.edu.anu.comp6442.group03.studyapp.treevisualizer.TreeVisualizerActivity;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.noteCardView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NoteListActivity.class)));
        binding.reminderCardView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ReminderListActivity.class)));
        binding.buttonMenu.setOnClickListener(v -> showMenu());
        binding.treeVisualizerCardView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TreeVisualizerActivity.class)));
        binding.countingCardView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EventListActivity.class)));
        binding.discussionFormCardView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DiscussionActivity.class)));
        binding.profileCardView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfilePageActivity.class)));
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, binding.buttonMenu);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Logout")) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, AccountEntryActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
