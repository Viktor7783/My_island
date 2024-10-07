package com.korotkov.multithreading;

import com.korotkov.GameOfIsland;
import com.korotkov.models.island.Island;

public class RestoreAnimals implements Runnable {
    private final Island island;
    private final DailyActivities dailyActivities;

    public RestoreAnimals(GameOfIsland game) {
        island = game.getIsland();
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
            while (!Thread.interrupted()) {
                synchronized (dailyActivities) {
                    while (!dailyActivities.isShownDailyStatistics() || dailyActivities.isPressPause()) {
                        dailyActivities.wait();
                    }
                }
                island.removeAndRestoreAnimals();
                synchronized (dailyActivities) {
                    dailyActivities.setRemoveAndRestoreAnimals(true);
                    dailyActivities.notifyAll();
                    while (!dailyActivities.isGrassPlanted() || dailyActivities.isPressPause()) {
                        dailyActivities.wait();
                    }
                    if (dailyActivities.isShownDailyStatistics()) {
                        dailyActivities.setShownDailyStatistics(false);
                        dailyActivities.notifyAll();
                    }
                }
            }
        } catch (InterruptedException _) {
            System.out.println("Interrupted дохлятина");
        }

    }
}
