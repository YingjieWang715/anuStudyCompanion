package au.edu.anu.comp6442.group03.studyapp.account;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.buttonCreate.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String email = binding.editTextEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString();

        boolean isValid = validateAccountInformation(email, password, confirmPassword);
        if (!isValid) {
            return;
        }

        createAccountInFirebase(email, password);
    }

    private void createAccountInFirebase(String email, String password) {
        setIsLoading(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setIsLoading(false);
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Successfully created account.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private void setIsLoading(boolean isLoading) {
        if (isLoading) {
            binding.registerProgressBar.setVisibility(View.VISIBLE);
            binding.buttonCreate.setVisibility(View.GONE);
        } else {
            binding.registerProgressBar.setVisibility(View.GONE);
            binding.buttonCreate.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateAccountInformation(String email, String password, String confirmPassword) {
        final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}$";
        final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmail.setError("Invalid email.");
            return false;
        }

        if (!passwordPattern.matcher(password).matches()) {
            binding.editTextPassword.setError("Password has a length between 8 and 20, contains at least one digit and one letter."); // TODO: Design password requirements.
            return false;
        }

        if (!password.equals(confirmPassword)) {
            binding.editTextConfirmPassword.setError("Password doesn't match.");
        }

        return true;
    }
}