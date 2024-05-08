package com.korotkov.config;

public class FieldSizeConfig {
    private int width;
    private int height;

    public FieldSizeConfig(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
