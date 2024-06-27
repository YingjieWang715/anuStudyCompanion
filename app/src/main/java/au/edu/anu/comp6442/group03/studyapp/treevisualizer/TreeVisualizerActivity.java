package au.edu.anu.comp6442.group03.studyapp.treevisualizer;

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

import com.google.firebase.auth.FirebaseAuth;

import au.edu.anu.comp6442.group03.studyapp.MainActivity;
import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.account.AccountEntryActivity;
import au.edu.anu.comp6442.group03.studyapp.databinding.ActivityTreeVisualizerBinding;

public class TreeVisualizerActivity extends AppCompatActivity {

    private ActivityTreeVisualizerBinding binding;

    private EditText numInput;
    private BinaryTree binaryTree;
    private FrameLayout treeContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTreeVisualizerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binaryTree = new BinaryTree();
        numInput = findViewById(R.id.numberInput);
        treeContainer = findViewById(R.id.treeContainer);

        // add button listener
        findViewById(R.id.addButton).setOnClickListener(view -> {
            int value = Integer.parseInt(numInput.getText().toString());

            binaryTree.add(value);
            numInput.setText("");
            refreshTreeView();

            System.out.println(binaryTree.root); //  print out the root( for testing
        });

        // undoButton/ remove last add button listenr
        findViewById(R.id.undoButton).setOnClickListener(view -> {
            binaryTree.removeLastAdd();
            refreshTreeView();
        });

        // clear button listener
        findViewById(R.id.clearButton).setOnClickListener(view -> {
            binaryTree.clearTree();
            refreshTreeView();
        });

        //delete button listener ; to delete the input number
        findViewById(R.id.deleteButton).setOnClickListener(view -> {
            int value = Integer.parseInt(numInput.getText().toString());
            binaryTree.delete(value);
            numInput.setText("");
            refreshTreeView();
        });

        refreshTreeView();

        binding.buttonMenu.setOnClickListener(v -> showMenu());
        binding.buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }


    // to update  the biew of the tree
    private void refreshTreeView() {
        if (treeContainer.getChildCount() > 0) {
            treeContainer.removeAllViews();
        }
        BinaryTreeVisualizationView visualizationView = new BinaryTreeVisualizationView(this, binaryTree);
        treeContainer.addView(visualizationView);
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(TreeVisualizerActivity.this, binding.buttonMenu);
        popupMenu.getMenu().add("Try RB-Tree?");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Try RB-Tree?")) {
                startActivity(new Intent(TreeVisualizerActivity.this, RBTreeVisualizerActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }


}