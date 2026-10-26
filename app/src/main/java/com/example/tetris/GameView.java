package com.example.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom View that draws the Tetris playfield and runs the automatic
 * "gravity" drop loop using a Handler.
 */
public class GameView extends View {

    private Board board;
    private final Paint paint = new Paint();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean running = false;
    private GameListener listener;
    private int lastLevel = 1;
    private boolean gameOverNotified = false;              // ← BAGO: para isang beses lang tumawag ang onGameOver

    public interface GameListener {
        void onScoreChanged(int score, int level);
        void onGameOver();
        void onLinesCleared(int count);
        void onLevelUp(int newLevel);
    }

    private final Runnable dropRunnable = new Runnable() {
        @Override
        public void run() {
            if (!running || board.isGameOver()) return;
            board.softDrop();
            postInvalidate();
            notifyListener();
            checkGameOver();                               // ← pinalitan
            if (board.isGameOver()) return;
            handler.postDelayed(this, board.dropIntervalMs());
        }
    };

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        board = new Board();
    }

    /** Nagpapaalam sa listener ng score, nabura na linya, at level up. */
    private void notifyListener() {
        if (listener == null) return;

        int cleared = board.consumeLastClearCount();
        int level = board.getLevel();

        if (level > lastLevel) {
            listener.onLevelUp(level);
        } else if (cleared > 0) {
            listener.onLinesCleared(cleared);
        }
        lastLevel = level;

        listener.onScoreChanged(board.getScore(), level);
    }

    /** Kapag Game Over na, ipaalam sa listener nang isang beses lang. */   // ← BAGO
    private void checkGameOver() {
        if (board.isGameOver() && !gameOverNotified) {
            gameOverNotified = true;
            running = false;
            handler.removeCallbacks(dropRunnable);
            if (listener != null) listener.onGameOver();
        }
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public Board getBoard() {
        return board;
    }

    public void start() {
        if (board.isGameOver()) return;                    // ← BAGO: huwag ibalik ang timer kapag tapos na
        running = true;
        handler.removeCallbacks(dropRunnable);
        handler.postDelayed(dropRunnable, board.dropIntervalMs());
    }

    public void pause() {
        running = false;
        handler.removeCallbacks(dropRunnable);
    }

    public void restart() {
        board.reset();
        lastLevel = 1;
        gameOverNotified = false;                          // ← BAGO
        start();
        invalidate();
        if (listener != null) listener.onScoreChanged(board.getScore(), board.getLevel());
    }

    public void moveLeft() {
        if (board.isGameOver()) return;
        board.moveLeft();
        invalidate();
    }

    public void moveRight() {
        if (board.isGameOver()) return;
        board.moveRight();
        invalidate();
    }

    public void rotate() {
        if (board.isGameOver()) return;
        board.rotate();
        invalidate();
    }

    public void softDropOnce() {
        if (board.isGameOver()) return;
        board.softDrop();
        invalidate();
        notifyListener();
        checkGameOver();                                   // ← BAGO: dito dating hindi nagpapaalam ng Game Over
    }

    public void hardDrop() {
        if (board.isGameOver()) return;
        board.hardDrop();
        invalidate();
        notifyListener();
        checkGameOver();                                   // ← pinalitan
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cols = Board.COLS;
        int rows = Board.ROWS;
        float cellSize = Math.min((float) getWidth() / cols, (float) getHeight() / rows);
        float boardWidthPx = cellSize * cols;
        float offsetX = (getWidth() - boardWidthPx) / 2f;

        paint.setColor(Color.parseColor("#282840"));
        paint.setStrokeWidth(1f);
        for (int c = 0; c <= cols; c++) {
            float x = offsetX + c * cellSize;
            canvas.drawLine(x, 0, x, cellSize * rows, paint);
        }
        for (int r = 0; r <= rows; r++) {
            float y = r * cellSize;
            canvas.drawLine(offsetX, y, offsetX + boardWidthPx, y, paint);
        }

        int[][] grid = board.getGrid();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != 0) {
                    drawCell(canvas, offsetX, r, c, cellSize, grid[r][c]);
                }
            }
        }

        Tetromino current = board.getCurrent();
        if (current != null) {
            int rotation = board.getCurrentRotation();
            int baseRow = board.getCurrentRow();
            int baseCol = board.getCurrentCol();
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if (current.isFilled(rotation, r, c)) {
                        int boardRow = baseRow + r;
                        int boardCol = baseCol + c;
                        if (boardRow >= 0) {
                            drawCell(canvas, offsetX, boardRow, boardCol, cellSize, current.color);
                        }
                    }
                }
            }
        }
    }

    private void drawCell(Canvas canvas, float offsetX, int row, int col, float cellSize, int color) {
        float left = offsetX + col * cellSize;
        float top = row * cellSize;
        paint.setColor(color);
        canvas.drawRect(left + 1, top + 1, left + cellSize - 1, top + cellSize - 1, paint);
    }
}