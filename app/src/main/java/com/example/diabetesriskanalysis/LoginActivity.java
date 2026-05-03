package com.example.diabetesriskanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // Ensure your XML is named login.xml

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button loginBtn = findViewById(R.id.loginBtn); // Updated ID to match clean XML
        TextView tvGoToSignup = findViewById(R.id.tvGoToSignup); // New ID for the link

        FirebaseAuth auth = FirebaseAuth.getInstance();

        // 1. Handle Login Logic
        loginBtn.setOnClickListener(v -> {
            String em = email.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (em.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(em, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Failed";
                            Toast.makeText(this, "Login Failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // 2. Handle Navigation to Signup Screen
        tvGoToSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}
