package com.korotkov.multithreading;

import com.korotkov.GameOfIsland;
import com.korotkov.services.impl.CollectAndDisplayStatisticsServiceImpl;

public class CollectStatistics implements Runnable {
    private final DailyActivities dailyActivities;
    private final CollectAndDisplayStatisticsServiceImpl collectAndDisplayStatisticsService;

    public CollectStatistics(GameOfIsland game) {
        dailyActivities = game.getDailyActivities();
        collectAndDisplayStatisticsService = game.getCollectAndDisplayStatisticsService();
    }

    @Override
    public void run() {
        try {
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
            System.out.println("Interrupted сбор статистики");
        }
    }
}
