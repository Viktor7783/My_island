package com.korotkov.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static com.korotkov.config.Constants.OBJECT_READ_ERROR;

public class AnimalConfig {
    private int percentsToRemove;

    public AnimalConfig(String pathToProperties) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToProperties))) {
            Properties properties = new Properties();
            properties.load(reader);
            this.percentsToRemove = Integer.parseInt(properties.getProperty("animal.percentsToRemove"));

        } catch (IOException e) {
            System.out.printf(OBJECT_READ_ERROR, pathToProperties, pathToProperties);
            System.exit(0);
        }
    }

    public int getPercentsToRemove() {
        return percentsToRemove;
    }
}
