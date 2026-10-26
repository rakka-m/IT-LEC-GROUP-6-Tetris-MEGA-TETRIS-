package com.example.tetris;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        // Back button: isinasara lang ang screen na ito, bumabalik sa menu
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}