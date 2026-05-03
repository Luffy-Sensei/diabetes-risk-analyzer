package com.example.diabetesriskanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button signupBtn = findViewById(R.id.signupBtn);
        TextView tvGoToLogin = findViewById(R.id.tvGoToLogin);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        // 1. Create Account Logic
        signupBtn.setOnClickListener(v -> {
            String em = email.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (em.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(em, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Account Created! Please Login.", Toast.LENGTH_LONG).show();

                            // GO BACK TO LOGIN PAGE
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Signup Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // 2. Go to Login Link
        tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
