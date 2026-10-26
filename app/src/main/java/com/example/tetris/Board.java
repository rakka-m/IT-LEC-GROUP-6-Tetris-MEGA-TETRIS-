package com.example.tetris;

public class Board {
    public static final int COLS = 10;
    public static final int ROWS = 20;

    /** Hirap ng laro. Ang speedMultiplier ay nagpaparami sa oras ng paghihintay bago bumagsak ang piyesa:
     *  mas malaki = mas mabagal, mas maliit = mas mabilis. */
    public enum Difficulty {
        EASY(1.4f),
        MEDIUM(1.0f),
        HARD(0.15f),
        LUNATIC(0.10f); // Maximum speed agad kahit Level 1!

        public final float speedMultiplier;

        Difficulty(float speedMultiplier) {
            this.speedMultiplier = speedMultiplier;
        }
    }

    // 0 = empty, otherwise stores the ARGB color of the locked block
    private final int[][] grid = new int[ROWS][COLS];

    private Tetromino current;
    private int currentRotation;
    private int currentRow;
    private int currentCol;

    private Tetromino next;

    private int score = 0;
    private int linesCleared = 0;
    private int level = 1;
    private boolean gameOver = false;

    private int lastClearCount = 0;
    private Difficulty difficulty = Difficulty.EASY;     // default

    public Board() {
        next = Tetromino.random();
        spawnNext();
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void reset() {
        for (int[] row : grid) {
            java.util.Arrays.fill(row, 0);
        }
        score = 0;
        linesCleared = 0;
        level = 1;
        gameOver = false;
        lastClearCount = 0;
        next = Tetromino.random();
        spawnNext();
    }

    private void spawnNext() {
        current = next;
        next = Tetromino.random();
        currentRotation = 0;
        currentCol = COLS / 2 - 2;
        currentRow = -1; // start slightly above the visible board

        if (collides(currentRotation, currentRow, currentCol)) {
            gameOver = true;
        }
    }

    /** Checks whether the given placement of the current piece overlaps a filled
     *  cell or goes outside the board bounds. */
    private boolean collides(int rotation, int row, int col) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (!current.isFilled(rotation, r, c)) continue;
                int boardRow = row + r;
                int boardCol = col + c;
                if (boardCol < 0 || boardCol >= COLS || boardRow >= ROWS) return true;
                if (boardRow >= 0 && grid[boardRow][boardCol] != 0) return true;
            }
        }
        return false;
    }

    public boolean moveLeft() {
        if (!collides(currentRotation, currentRow, currentCol - 1)) {
            currentCol--;
            return true;
        }
        return false;
    }

    public boolean moveRight() {
        if (!collides(currentRotation, currentRow, currentCol + 1)) {
            currentCol++;
            return true;
        }
        return false;
    }

    public boolean rotate() {
        int newRotation = (currentRotation + 1) % 4;
        if (!collides(newRotation, currentRow, currentCol)) {
            currentRotation = newRotation;
            return true;
        }
        if (!collides(newRotation, currentRow, currentCol - 1)) {
            currentRotation = newRotation;
            currentCol--;
            return true;
        }
        if (!collides(newRotation, currentRow, currentCol + 1)) {
            currentRotation = newRotation;
            currentCol++;
            return true;
        }
        return false;
    }

    /** Moves the piece down one row. If it can't move, locks it and clears lines.
     *  Returns false when the piece has been locked (caller should spawn a new one). */
    public boolean softDrop() {
        if (!collides(currentRotation, currentRow + 1, currentCol)) {
            currentRow++;
            return true;
        }
        lockPiece();
        return false;
    }

    public void hardDrop() {
        while (!collides(currentRotation, currentRow + 1, currentCol)) {
            currentRow++;
            score += 2;
        }
        lockPiece();
    }

    private void lockPiece() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (current.isFilled(currentRotation, r, c)) {
                    int boardRow = currentRow + r;
                    int boardCol = currentCol + c;
                    if (boardRow >= 0) {
                        grid[boardRow][boardCol] = current.color;
                    }
                }
            }
        }
        clearFullLines();
        spawnNext();
    }

    private void clearFullLines() {
        int cleared = 0;
        for (int r = ROWS - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == 0) { full = false; break; }
            }
            if (full) {
                cleared++;
                for (int rr = r; rr > 0; rr--) {
                    System.arraycopy(grid[rr - 1], 0, grid[rr], 0, COLS);
                }
                java.util.Arrays.fill(grid[0], 0);
                r++;
            }
        }
        if (cleared > 0) {
            lastClearCount += cleared;
            linesCleared += cleared;
            int[] table = {0, 40, 100, 300, 1200};
            score += table[Math.min(cleared, 4)] * level;
            level = 1 + linesCleared / 10;
        }
    }

    /** Ibinabalik ang bilang ng linyang nabura, tapos nire-reset sa 0. */
    public int consumeLastClearCount() {
        int count = lastClearCount;
        lastClearCount = 0;
        return count;
    }

    /** Milliseconds between automatic drops for the current level and difficulty. */
    public long dropIntervalMs() {
        long base = 1000 - (level - 1) * 70L;
        long ms = (long) (base * difficulty.speedMultiplier);
        return Math.max(ms, 100);
    }

    public int[][] getGrid() { return grid; }
    public Tetromino getCurrent() { return current; }
    public int getCurrentRotation() { return currentRotation; }
    public int getCurrentRow() { return currentRow; }
    public int getCurrentCol() { return currentCol; }
    public Tetromino getNext() { return next; }
    public int getScore() { return score; }
    public int getLevel() { return level; }
    public boolean isGameOver() { return gameOver; }
}