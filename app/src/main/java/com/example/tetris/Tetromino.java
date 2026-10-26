package com.example.tetris;

public enum Tetromino {
    I(new String[]{
        "0000" + "1111" + "0000" + "0000",         //Rotation 0
                "0010" + "0010" + "0010" + "0010", //Rotation 1
                "0000" + "0000" + "1111" + "0000", //Rotation 2
                "0100" + "0100" + "0100" + "0100"  //Rotation 3
    }, 0xFF00F0F0),

    J(new String[]{
        "1000" + "1110" + "0000" + "0000",
                "0110" + "0100" + "0100" + "0000",
                "0000" + "1110" + "0010" + "0000",
                "0100" + "0100" + "1100" + "0000"
    }, 0xFF3050F0),

    L(new String[]{
        "0010" + "1110" + "0000" + "0000",
                "0100" + "0100" + "0110" + "0000",
                "0000" + "1110" + "1000" + "0000",
                "1100" + "0100" + "0100" + "0000"
    }, 0xFFF0A030),

    O(new String[]{
        "0110" + "0110" + "0000" + "0000",
                "0110" + "0110" + "0000" + "0000",
                "0110" + "0110" + "0000" + "0000",
                "0110" + "0110" + "0000" + "0000"
    }, 0xFFF0F030),

    S(new String[]{
        "0110" + "1100" + "0000" + "0000",
                "0100" + "0110" + "0010" + "0000",
                "0000" + "0110" + "1100" + "0000",
                "1000" + "1100" + "0100" + "0000"
    }, 0xFF30F030),

    T(new String[]{
        "0100" + "1110" + "0000" + "0000",
                "0100" + "0110" + "0100" + "0000",
                "0000" + "1110" + "0100" + "0000",
                "0100" + "1100" + "0100" + "0000"
    }, 0xFFA030F0),

    Z(new String[]{
        "1100" + "0110" + "0000" + "0000",
                "0010" + "0110" + "0100" + "0000",
                "0000" + "1100" + "0110" + "0000",
                "0100" + "1100" + "1000" + "0000"
    }, 0xFFF03030);

    public final String[] rotations;
    public final int color;

    Tetromino(String[] rotations, int color) {
        this.rotations = rotations;
        this.color = color;
    }

    public boolean isFilled(int rotation, int row, int col) {
        int idx = row * 4 + col;
        return rotations[rotation % 4].charAt(idx) == '1';
    }

    public static Tetromino random() {
        Tetromino[] all = values();
        return all[(int) (Math.random() * all.length)];
    }
}

