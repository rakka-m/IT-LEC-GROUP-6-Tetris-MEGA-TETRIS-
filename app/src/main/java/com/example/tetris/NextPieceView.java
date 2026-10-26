package com.example.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/** Small 4x4 preview of the next tetromino. */
public class NextPieceView extends View {

    private Tetromino piece;
    private final Paint paint = new Paint();

    public NextPieceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPiece(Tetromino piece) {
        this.piece = piece;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (piece == null) return;
        float cell = Math.min(getWidth(), getHeight()) / 4f;
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (piece.isFilled(0, r, c)) {
                    paint.setColor(piece.color);
                    float left = c * cell;
                    float top = r * cell;
                    canvas.drawRect(left + 1, top + 1, left + cell - 1, top + cell - 1, paint);
                }
            }
        }
    }
}