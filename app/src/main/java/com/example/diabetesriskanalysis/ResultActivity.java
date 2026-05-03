package com.example.diabetesriskanalysis;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ResultActivity extends AppCompatActivity {

    private LinearProgressIndicator riskBar;
    private TextView resultText, messageText;
    private final String channelId = "diabetes_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbarResult);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Bind views
        resultText = findViewById(R.id.resultText);
        messageText = findViewById(R.id.messageText);
        riskBar = findViewById(R.id.riskBar);
        Button backBtn = findViewById(R.id.backBtn);

        // Get data from Intent
        String risk = getIntent().getStringExtra("risk");
        int score = getIntent().getIntExtra("score", 0);

        displayResult(risk, score);

        backBtn.setOnClickListener(v -> finish());

        // Handle Notifications
        setupNotifications(risk);
    }

    private void displayResult(String risk, int score) {
        String message;
        int color;

        // Algorithm Logic Mapping
        if ("LOW".equals(risk)) {
            message = "Your condition appears normal. Maintain a healthy lifestyle.";
            color = Color.parseColor("#2E7D32"); // Material Green
        } else if ("MEDIUM".equals(risk)) {
            message = "Some warning signs detected. Improve diet and monitor health.";
            color = Color.parseColor("#EF6C00"); // Material Orange
        } else {
            message = "High risk detected. Please consult a doctor immediately.";
            color = Color.parseColor("#C62828"); // Material Red
        }

        resultText.setText(risk);
        resultText.setTextColor(color);
        messageText.setText(message);
        riskBar.setIndicatorColor(color);

        // BONUS: Animate the progress bar from 0 to score
        ObjectAnimator.ofInt(riskBar, "progress", 0, score)
                .setDuration(1200) // 1.2 seconds animation
                .start();
    }

    private void setupNotifications(String risk) {
        createNotificationChannel();

        // Request permission for Android 13+ (Tiramisu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
                return;
            }
        }
        sendNotification(risk);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Risk Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String risk) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your icon later
                .setContentTitle("Health Analysis Complete")
                .setContentText("Your Diabetes Risk Level is: " + risk)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Final check before sending
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            notificationManager.notify(1, builder.build());
        }
    }
}