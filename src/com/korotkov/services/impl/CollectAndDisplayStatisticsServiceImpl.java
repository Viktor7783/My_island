package com.korotkov.services.impl;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.island.Island;
import com.korotkov.models.plants.Plant;
import com.korotkov.services.interfaces.CollectAndDisplayStatisticsService;

import static com.korotkov.config.Constants.*;

public class CollectAndDisplayStatisticsServiceImpl implements CollectAndDisplayStatisticsService {
    private final Island island;
    private int dayNumber;
    private int countLiveAnimals;
    private int firstDayCountLiveAnimals;
    private int countLivePlants;
    private int firstDayCountLivePlants;
    private int countDeathAnimal;
    private int countEatPlants;
    private int countBornAnimals;
    private int totalDifferenceLiveAnimals;
    private int totalDifferenceLivePlants;
    private boolean isFirstDay;

    public CollectAndDisplayStatisticsServiceImpl(Island island) {
        this.island = island;
        this.isFirstDay = true;
    }

    public void start() {
        collectStatistics();
        printStatistics();
        resetLapValues();
    }

    @Override
    public void collectStatistics() {
        island.getIsland().values().forEach(list -> list.forEach(entity -> {
            if (entity instanceof Plant plant) {
                if (plant.isEaten()) ++countEatPlants;
                else ++countLivePlants;
            } else if (entity instanceof Animal animal) {
                if (animal.isBornNewAnimal()) ++countBornAnimals;

                if (animal.getHealthPercent() > 0) ++countLiveAnimals;
                else ++countDeathAnimal;
            }
        }));
        if (isFirstDay) {
            firstDayCountLiveAnimals = countLiveAnimals;
            firstDayCountLivePlants = countLivePlants;
            isFirstDay = false;
        }
        countBornAnimals /= 2;
        totalDifferenceLiveAnimals = countLiveAnimals - firstDayCountLiveAnimals;
        totalDifferenceLivePlants = countLivePlants - firstDayCountLivePlants;
        ++dayNumber;
    }

    @Override
    public void printStatistics() {
        System.out.printf(STATISTIC_OF_DAY, dayNumber);
        System.out.printf(LIVE_ANIMALS, countLiveAnimals);
        System.out.printf(BORN_ANIMALS, countBornAnimals);
        System.out.printf(DEATH_ANIMALS, countDeathAnimal);
        System.out.printf(LIVE_PLANTS, countLivePlants);
        System.out.printf(EAT_PLANTS, countEatPlants);
        System.out.println(DIFFERENCE);
        if (totalDifferenceLiveAnimals > 0)
            System.out.printf(MORE_ANIMALS, totalDifferenceLiveAnimals);
        else if (totalDifferenceLiveAnimals < 0)
            System.out.printf(LESS_ANIMALS, Math.abs(totalDifferenceLiveAnimals));
        else System.out.println(NO_CHANGES_ANIMALS);
        if (totalDifferenceLivePlants > 0)
            System.out.printf(MORE_PLANTS, totalDifferenceLivePlants);
        else if (totalDifferenceLivePlants < 0)
            System.out.printf(LESS_PLANTS, Math.abs(totalDifferenceLivePlants));
        else System.out.println(NO_CHANGES_PLANTS);
        System.out.println(END);
    }

    public void resetLapValues() {
        countLiveAnimals = 0;
        countLivePlants = 0;
        countDeathAnimal = 0;
        countEatPlants = 0;
        countBornAnimals = 0;
    }
}
