package com.example.tetris;

import android.content.Context;
import android.content.SharedPreferences;

public class ScoreManager {

    private static final String PREFS_NAME = "tetris_prefs";
    private static final String KEY_TOP_SCORE = "top_score";

    public static int getTopScore(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_TOP_SCORE, 0);
    }

    /** I-save ang bagong score kung mas mataas ito sa naka-save. Ibinabalik ang true kung bagong record. */
    public static boolean saveIfHighScore(Context context, int newScore) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int currentTop = prefs.getInt(KEY_TOP_SCORE, 0);
        if (newScore > currentTop) {
            prefs.edit().putInt(KEY_TOP_SCORE, newScore).apply();
            return true;
        }
        return false;
    }
}