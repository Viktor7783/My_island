package com.korotkov.multithreading;

import com.korotkov.GameOfIsland;
import com.korotkov.services.impl.CollectAndDisplayStatisticsServiceImpl;

public class ShowStatistics implements Runnable {
    private final DailyActivities dailyActivities;
    private final CollectAndDisplayStatisticsServiceImpl collectAndDisplayStatisticsService;

    public ShowStatistics(GameOfIsland game) {
        dailyActivities = game.getDailyActivities();
        collectAndDisplayStatisticsService = game.getCollectAndDisplayStatisticsService();
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (dailyActivities) {
                    while (!dailyActivities.isTimeToShowStatistics() || dailyActivities.isPressPause()) {
                        dailyActivities.wait();
                    }
                }
                synchronized (dailyActivities) {
                    dailyActivities.setBeginPrintStatistics(true);
                    dailyActivities.notifyAll();
                }
                collectAndDisplayStatisticsService.printStatistics();
                synchronized (dailyActivities) {
                    dailyActivities.setBeginPrintStatistics(false);
                    dailyActivities.notifyAll();
                }
                synchronized (dailyActivities) {
                    while (dailyActivities.isPressPause()) {
                        dailyActivities.wait();
                    }
                }
                collectAndDisplayStatisticsService.checkStopGame();
                collectAndDisplayStatisticsService.resetLapValues();
                resetDailyActivities();
            }
        } catch (InterruptedException _) {
            System.out.println("Interrupted визуализация");
        }
    }

    private void resetDailyActivities() {
        synchronized (dailyActivities) {
            dailyActivities.setGrassPlanted(false);
            dailyActivities.setRemoveAndRestoreAnimals(false);
            dailyActivities.setAnimalActionsCompleted(false);
            dailyActivities.setCollectStatistics(false);
            dailyActivities.setShownDailyStatistics(true);
            dailyActivities.notifyAll();
        }
    }
}
