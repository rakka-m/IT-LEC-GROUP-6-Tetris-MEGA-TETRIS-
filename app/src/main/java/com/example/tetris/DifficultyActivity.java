package com.example.tetris;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "tetris_prefs";
    public static final String KEY_DIFFICULTY = "difficulty";

    private RadioGroup difficultyGroup;

    /** Kinukuha ang naka-save na difficulty. Kung wala pa, EASY ang default. */
    public static Board.Difficulty loadDifficulty(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String saved = prefs.getString(KEY_DIFFICULTY, Board.Difficulty.EASY.name());
        try {
            return Board.Difficulty.valueOf(saved);
        } catch (IllegalArgumentException e) {
            return Board.Difficulty.EASY;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        difficultyGroup = findViewById(R.id.difficultyGroup);

        // i-check ang radio button ng kasalukuyang naka-save na difficulty
        checkRadioFor(loadDifficulty(this));

        findViewById(R.id.btnApply).setOnClickListener(v -> {
            Board.Difficulty selected = getSelectedDifficulty();
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putString(KEY_DIFFICULTY, selected.name())
                    .apply();
            Toast.makeText(this, "Difficulty: " + selected.name(), Toast.LENGTH_SHORT).show();
            finish();   // bumalik sa menu
        });

        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());   // walang sine-save
    }

    private void checkRadioFor(Board.Difficulty difficulty) {
        switch (difficulty) {
            case MEDIUM:  difficultyGroup.check(R.id.radioMedium);  break;
            case HARD:    difficultyGroup.check(R.id.radioHard);    break;
            case LUNATIC: difficultyGroup.check(R.id.radioLunatic); break;
            default:      difficultyGroup.check(R.id.radioEasy);    break;
        }
    }

    private Board.Difficulty getSelectedDifficulty() {
        int id = difficultyGroup.getCheckedRadioButtonId();
        if (id == R.id.radioMedium)  return Board.Difficulty.MEDIUM;
        if (id == R.id.radioHard)    return Board.Difficulty.HARD;
        if (id == R.id.radioLunatic) return Board.Difficulty.LUNATIC;
        return Board.Difficulty.EASY;
    }
}