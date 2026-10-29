package com.example.tetris;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements GameView.GameListener {

    // ---------- Mga View ----------
    private GameView gameView;
    private NextPieceView nextPieceView;
    private TextView scoreValue;
    private TextView levelValue;
    private Button btnPause;                                   // ← BAGO

    // ---------- Tunog ----------
    private SoundPool soundPool;
    private int sfxButton, sfxClear, sfxLevelUp;
    private MediaPlayer bgMusic;

    // ---------- Hold-to-drop para sa ▼ button ----------
    private final Handler holdHandler = new Handler(Looper.getMainLooper());
    private static final long HOLD_START_DELAY_MS = 250;   // gaano katagal bago magsimula ang auto-drop
    private static final long HOLD_REPEAT_MS = 50;         // bilis ng pagbaba habang hawak

    private final Runnable downRepeat = new Runnable() {
        @Override
        public void run() {
            if (gameView.getBoard().isGameOver() || gameView.isUserPaused()) return;  // hihinto kapag Game Over o Pause
            gameView.softDropOnce();
            holdHandler.postDelayed(this, HOLD_REPEAT_MS);
        }
    };

    // ---------- Lifecycle ----------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setupSoundEffects();

        bgMusic = MediaPlayer.create(this, R.raw.tetris_theme);
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.5f, 0.5f);

        gameView = findViewById(R.id.gameView);
        nextPieceView = findViewById(R.id.nextPieceView);
        scoreValue = findViewById(R.id.scoreValue);
        levelValue = findViewById(R.id.levelValue);
        btnPause = findViewById(R.id.pauseBtn);                // ← BAGO

        gameView.setListener(this);
        gameView.getBoard().setDifficulty(DifficultyActivity.loadDifficulty(this));   // ← BAGO: difficulty

        // ← BAGO: Pause / Resume button
        btnPause.setOnClickListener(v -> {
            if (gameView.getBoard().isGameOver()) return;
            playSfx(sfxButton);                                // ← BAGO: tunog ng button controller, pang-Pause at pang-Resume
            boolean pausing = !gameView.isUserPaused();
            gameView.setUserPaused(pausing);
            holdHandler.removeCallbacks(downRepeat);
            if (pausing) {
                btnPause.setText("Resume");
                if (bgMusic != null) bgMusic.pause();
            } else {
                btnPause.setText("Pause");
                if (bgMusic != null) bgMusic.start();
            }
        });

        // ← BAGO: i-tap ang grid kapag Game Over para mag-restart (score balik sa 0)
        gameView.setOnClickListener(v -> {
            if (!gameView.getBoard().isGameOver()) return;
            gameView.restart();
            btnPause.setText("Pause");
            if (bgMusic != null) {
                bgMusic.seekTo(0);
                bgMusic.start();
            }
        });

        // ◀ ▶ ⟳ : isang pindot = isang galaw + tunog
        findViewById(R.id.btnLeft).setOnClickListener(v -> {
            gameView.moveLeft();
            playButtonSfx();
        });
        findViewById(R.id.btnRight).setOnClickListener(v -> {
            gameView.moveRight();
            playButtonSfx();
        });
        findViewById(R.id.btnRotate).setOnClickListener(v -> {
            gameView.rotate();
            playButtonSfx();
        });

        // ▼ : tap = isang hakbang, hawak = tuloy-tuloy na bagsak
        findViewById(R.id.btnDown).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (gameView.getBoard().isGameOver() || gameView.isUserPaused()) return true;   // Game Over / Pause, walang tunog/galaw
                    v.setPressed(true);
                    gameView.softDropOnce();                              // isang hakbang agad
                    playButtonSfx();                                   // tunog, isang beses lang
                    holdHandler.postDelayed(downRepeat, HOLD_START_DELAY_MS);
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setPressed(false);
                    holdHandler.removeCallbacks(downRepeat);              // hinto
                    return true;
            }
            return false;
        });

        nextPieceView.setPiece(gameView.getBoard().getNext());
        onScoreChanged(0, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.start();
        if (bgMusic != null && !gameView.isUserPaused() && !gameView.getBoard().isGameOver()) bgMusic.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        holdHandler.removeCallbacks(downRepeat);
        gameView.pause();
        if (bgMusic != null) bgMusic.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        holdHandler.removeCallbacks(downRepeat);
        if (bgMusic != null) {
            bgMusic.release();
            bgMusic = null;
        }
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    // ---------- GameListener ----------
    @Override
    public void onScoreChanged(int score, int level) {
        scoreValue.setText("Points: " + score);
        levelValue.setText("Level " + level);
        nextPieceView.setPiece(gameView.getBoard().getNext());
    }

    @Override
    public void onGameOver() {
        holdHandler.removeCallbacks(downRepeat);
        if (bgMusic != null) bgMusic.pause();

        int finalScore = gameView.getBoard().getScore();                    // top score, sine-save pa rin
        ScoreManager.saveIfHighScore(this, finalScore);
        // ← BAGO: wala nang Toast; ang "Game Over! Click to try again" ay iginuguhit na sa loob ng grid (GameView)
    }

    @Override
    public void onLinesCleared(int count) {
        playSfx(sfxClear);
    }

    @Override
    public void onLevelUp(int newLevel) {
        playSfx(sfxLevelUp);
    }

    // ---------- Sound effects ----------
    private void setupSoundEffects() {
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(attrs)
                .build();

        sfxButton  = soundPool.load(this, R.raw.buttons_sound, 1);
        sfxClear   = soundPool.load(this, R.raw.clear_sound, 1);
        sfxLevelUp = soundPool.load(this, R.raw.level_up_sound, 1);
    }

    private void playSfx(int soundId) {
        if (soundPool != null) {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
        }
    }

    /** Tunog ng mga button, tumutunog lang kapag hindi pa Game Over. */
    private void playButtonSfx() {
        if (gameView.getBoard().isGameOver() || gameView.isUserPaused()) return;
        playSfx(sfxButton);
    }
}