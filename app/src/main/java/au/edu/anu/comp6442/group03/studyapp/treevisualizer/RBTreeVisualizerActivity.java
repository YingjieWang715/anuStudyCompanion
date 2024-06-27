package au.edu.anu.comp6442.group03.studyapp.treevisualizer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.databinding.ActivityRbtreeVisualizerBinding;

public class RBTreeVisualizerActivity extends AppCompatActivity {
    private ActivityRbtreeVisualizerBinding binding;
    private EditText numInput;
    private RBTree redBlackTree;

    private FrameLayout redBlackTreeContainer;

    private NodeAndCoordinates nodeCoordPair;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRbtreeVisualizerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nodeCoordPair = new NodeAndCoordinates();
        redBlackTree = new RBTree(nodeCoordPair);
        numInput = findViewById(R.id.rb_numberInput);
        redBlackTreeContainer = findViewById(R.id.rb_treeContainer);

        findViewById(R.id.rb_addButton).setOnClickListener(view -> {
            int value = Integer.parseInt(numInput.getText().toString());
            redBlackTree.updateCoordinate(value, new LevelAndCoordinates(0, new CoordPair(0, 0)));
            redBlackTree.add(value);
            numInput.setText("");
            refreshRBTreeVisualization();
        });

        findViewById(R.id.rb_clearButton).setOnClickListener(view -> {
            redBlackTree.clearRBTree();
            refreshRBTreeVisualization();
        });

        refreshRBTreeVisualization();

        binding.buttonMenu.setOnClickListener(v -> showMenu());
        binding.buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

    }

    private void refreshRBTreeVisualization() {
        if (redBlackTreeContainer.getChildCount() > 0) {
            redBlackTreeContainer.removeAllViews();
        }
        RBTreeVisualizationView visualizationView = new RBTreeVisualizationView(this, redBlackTree);
        redBlackTreeContainer.addView(visualizationView);
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(RBTreeVisualizerActivity.this, binding.buttonMenu);
        popupMenu.getMenu().add("Try BST");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Try BST")) {
                startActivity(new Intent(RBTreeVisualizerActivity.this, TreeVisualizerActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

}