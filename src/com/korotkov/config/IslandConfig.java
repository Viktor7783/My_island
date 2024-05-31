package com.korotkov.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static com.korotkov.config.Constants.OBJECT_READ_ERROR;

public class IslandConfig {
    private int width;
    private int height;
    private int daysOfLife;

    public IslandConfig(String pathToPropertiesFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToPropertiesFile))) {
            Properties properties = new Properties();
            properties.load(reader);
            this.width = Integer.parseInt(properties.getProperty("island.width"));
            this.height = Integer.parseInt(properties.getProperty("island.height"));
            this.daysOfLife = Integer.parseInt(properties.getProperty("island.daysOfLife"));
        } catch (IOException e) {
            System.out.printf(OBJECT_READ_ERROR, pathToPropertiesFile, pathToPropertiesFile);
            System.exit(0);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getDaysOfLife() {
        return daysOfLife;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        if (width >= 1 && width <= 150) this.width = width;
    }

    public void setHeight(int height) {
        if (height >= 1 && height <= 150) this.height = height;
    }

    public void setDaysOfLife(int daysOfLife) {
        if (daysOfLife >= 1 && daysOfLife <= 365) this.daysOfLife = daysOfLife;
    }
}
