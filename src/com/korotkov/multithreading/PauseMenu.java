package com.korotkov.multithreading;

import com.korotkov.GameOfIsland;
import com.korotkov.models.island.Island;
import com.korotkov.services.impl.UpdateSettingsService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.korotkov.config.Constants.*;

public class PauseMenu implements Runnable {
    private final GameOfIsland game;
    private final Island island;
    private final UpdateSettingsService updateSettingsService;
    private final BufferedReader reader;
    private final DailyActivities dailyActivities;

    public PauseMenu(GameOfIsland game) {
        this.game = game;
        island = game.getIsland();
        updateSettingsService = game.getUpdateSettingsService();
        reader = game.getReader();
        dailyActivities = game.getDailyActivities();
    }

    @Override
    public void run() {
        if (!dailyActivities.isIslandInitialized()) {
            game.initializeIsland();
        }
        try {
            while (!Thread.interrupted()) {
                if (reader.readLine().equalsIgnoreCase("p")) {
                    synchronized (dailyActivities) {
                        dailyActivities.setPressPause(true);
                        dailyActivities.notifyAll();
                    }
                    synchronized (dailyActivities) {
                        while (dailyActivities.isBeginPrintStatistics()) {
                            dailyActivities.wait();
                        }
                    }
                    while (dailyActivities.isPressPause() && !Thread.interrupted()) {
                        System.out.println(PAUSE_MENU);
                        String pauseButton;
                        while (!(pauseButton = reader.readLine()).equalsIgnoreCase("c") && !pauseButton.equalsIgnoreCase("o") && !pauseButton.equalsIgnoreCase("r") && !pauseButton.equalsIgnoreCase("e") && !Thread.interrupted()) {
                            System.out.println(CHOOSE_CORE);
                        }
                        switch (pauseButton.toLowerCase()) {
                            case "c" -> {
                                synchronized (dailyActivities) {
                                    dailyActivities.setPressPause(false);
                                    dailyActivities.notifyAll();
                                }
                            }
                            case "o" -> updateSettingsService.updateLiveIslandSettings(island);
                            case "r" -> {
                                game.setShutdownExecutor(game.getExecutor());
                                new GameOfIsland().start();
                                game.getShutdownExecutor().shutdownNow();
                                TimeUnit.MILLISECONDS.sleep(1);
                            }
                            case "e" -> updateSettingsService.exitGame(reader);
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException _) {
            System.out.println("interrupted прослушка" + Thread.currentThread().getName());//todo: delete
        }
    }
}

