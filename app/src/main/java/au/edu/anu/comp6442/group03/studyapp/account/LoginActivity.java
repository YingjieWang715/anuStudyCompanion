package au.edu.anu.comp6442.group03.studyapp.account;

import android.content.Intent;
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

import au.edu.anu.comp6442.group03.studyapp.MainActivity;
import au.edu.anu.comp6442.group03.studyapp.R;
import au.edu.anu.comp6442.group03.studyapp.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.buttonLogin.setOnClickListener(v -> userLogin());
    }

    private void userLogin() {
        String email = binding.editTextEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();

        boolean isValid = validateAccountInformation(email, password);
        if (!isValid) {
            return;
        }

        loginAccountInFirebase(email, password);
    }

    private void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        setIsLoading(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setIsLoading(false);
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setIsLoading(boolean isLoading) {
        if (isLoading) {
            binding.registerProgressBar.setVisibility(View.VISIBLE);
            binding.buttonLogin.setVisibility(View.GONE);
        } else {
            binding.registerProgressBar.setVisibility(View.GONE);
            binding.buttonLogin.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateAccountInformation(String email, String password) {
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

        return true;
    }
}