package com.korotkov.models.island;

public class Field {
    private int x; // - строка (Height)
    private int y; // - столбец (Width)

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": X = " + getX() + " Y = " + getY();
    }
}
