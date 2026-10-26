package com.example.tetris;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TopScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_score);

        TextView topScoreValue = findViewById(R.id.topScoreValue);
        topScoreValue.setText(String.valueOf(ScoreManager.getTopScore(this)));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}