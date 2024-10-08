package com.korotkov.multithreading;

import com.korotkov.GameOfIsland;
import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.models.island.Island;

import java.util.Random;

public class RestorePlants implements Runnable {
    private final GameOfIsland game;
    private final EntityCharacteristicConfig entityCharacteristicConfig;
    private final Random random;
    private final DailyActivities dailyActivities;

    public RestorePlants(GameOfIsland game) {
        this.game = game;
        entityCharacteristicConfig = game.getEntityCharacteristicConfig();
        random = game.getRandom();
        dailyActivities = game.getDailyActivities();
    }

    @Override
    public void run() {
        try {
            synchronized (dailyActivities) {
                while (!dailyActivities.isIslandInitialized()) {
                    dailyActivities.wait();
                }
            }
            Island island = game.getIsland();
            while (!Thread.interrupted()) {
                synchronized (dailyActivities) {
                    while (!dailyActivities.isShownDailyStatistics() || dailyActivities.isPressPause()) {
                        dailyActivities.wait();
                    }
                }
                island.removeEatenPlants();
                island.refillPlants(entityCharacteristicConfig, random);
                synchronized (dailyActivities) {
                    dailyActivities.setGrassPlanted(true);
                    dailyActivities.notifyAll();
                    while (!dailyActivities.isRemoveAndRestoreAnimals() || dailyActivities.isPressPause()) {
                        dailyActivities.wait();
                    }
                    if (dailyActivities.isShownDailyStatistics()) {
                        dailyActivities.setShownDailyStatistics(false);
                        dailyActivities.notifyAll();
                    }
                }
            }
        } catch (InterruptedException _) {
        }
    }
}
