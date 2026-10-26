package com.example.tetris;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnSetDifficulty = findViewById(R.id.btnSetLanguage);   // "SET Difficulty" ang text nito
        Button btnTopScore = findViewById(R.id.btnTopScore);
        Button btnCredits = findViewById(R.id.btnPreferences);         // "CREDITS" ang text nito

        btnPlay.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, GameActivity.class)));

        btnSetDifficulty.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, DifficultyActivity.class)));

        btnTopScore.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, TopScoreActivity.class)));

        btnCredits.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CreditsActivity.class)));
    }
}