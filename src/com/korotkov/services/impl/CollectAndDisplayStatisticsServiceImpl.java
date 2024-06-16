package com.korotkov.services.impl;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.herbivores.Herbivore;
import com.korotkov.models.island.Island;
import com.korotkov.models.plants.Plant;
import com.korotkov.models.predators.Predator;
import com.korotkov.services.interfaces.CollectAndDisplayStatisticsService;

import static com.korotkov.config.Constants.*;

public class CollectAndDisplayStatisticsServiceImpl implements CollectAndDisplayStatisticsService {
    private final Island island;
    UpdateSettingsService updateSettingsService;
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
    private int countLivePredators;
    private int countLiveHerbivores;
    private boolean isFirstDay;

    public CollectAndDisplayStatisticsServiceImpl(Island island, UpdateSettingsService updateSettingsService) {
        this.island = island;
        this.updateSettingsService = updateSettingsService;
        this.isFirstDay = true;
    }

    public void start() {
        collectStatistics();
        printStatistics();
        checkStopGame();
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

                if (animal.getHealthPercent() > 0) {
                    ++countLiveAnimals;
                    if (animal instanceof Predator) ++countLivePredators;
                    else if (animal instanceof Herbivore) ++countLiveHerbivores;
                } else ++countDeathAnimal;
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
        System.out.printf(LIVE_PREDATORS_AND_HERBIVORES, countLivePredators, countLiveHerbivores);
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
        countLivePredators = 0;
        countLiveHerbivores = 0;
    }

    private void checkStopGame() {
        int stopNumber = updateSettingsService.getNumberOfStopCondition();
        if (stopNumber == 1) {
            if (countLiveAnimals < 1) System.exit(0);
        } else if (stopNumber == 2) {
            if (countLivePredators < 1) System.exit(0);
        } else if (stopNumber == 3) {
            if (countLiveHerbivores < 1) System.exit(0);
        }

    }
}
