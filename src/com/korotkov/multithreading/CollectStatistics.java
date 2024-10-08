package com.korotkov.multithreading;

import com.korotkov.GameOfIsland;
import com.korotkov.services.impl.CollectAndDisplayStatisticsServiceImpl;

public class CollectStatistics implements Runnable {
    private final GameOfIsland game;
    private final DailyActivities dailyActivities;

    public CollectStatistics(GameOfIsland game) {
        this.game = game;
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
            CollectAndDisplayStatisticsServiceImpl collectAndDisplayStatisticsService = game.getCollectAndDisplayStatisticsService();
            while (!Thread.interrupted()) {
                synchronized (dailyActivities) {
                    while (!dailyActivities.isTimeToCollectStatistics() || dailyActivities.isPressPause()) {
                        dailyActivities.wait();
                    }
                }
                collectAndDisplayStatisticsService.collectStatistics();
                synchronized (dailyActivities) {
                    dailyActivities.setCollectStatistics(true);
                    dailyActivities.notifyAll();
                }
            }
        } catch (InterruptedException _) {
        }
    }
}
