package com.korotkov.services.impl;

import com.korotkov.config.ImagesOfEntitiesConfig;
import com.korotkov.config.IslandConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.herbivores.Herbivore;
import com.korotkov.models.island.Island;
import com.korotkov.models.plants.Plant;
import com.korotkov.models.predators.Predator;
import com.korotkov.services.interfaces.CollectAndDisplayStatisticsService;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.korotkov.config.Constants.*;

public class CollectAndDisplayStatisticsServiceImpl implements CollectAndDisplayStatisticsService {
    private final Island island;
    private final ImagesOfEntitiesConfig imagesOfEntitiesConfig;
    private final UpdateSettingsService updateSettingsService;
    private int dayNumber;
    private boolean isFirstDay;
    private int countLiveAnimals;
    private int countDeadAnimals;
    private int countBornAnimals;
    private int countLivePlants;
    private int countEatenPlants;
    private int firstDayCountLiveAnimals;
    private int firstDayCountLivePlants;
    private final Comparator<Class<? extends Entity>> classComparator = Comparator.comparing(Class::getSimpleName);
    private final Map<Class<? extends Entity>, Integer> liveAnimals;
    private final Map<Class<? extends Entity>, Integer> deadAnimals;
    private final Map<Class<? extends Entity>, Integer> livePlants;
    private final Map<Class<? extends Entity>, Integer> eatenPlants;
    private final Map<Class<? extends Entity>, Integer> bornAnimals;
    private Map<Class<? extends Entity>, Integer> firstDayLiveAnimals;
    private Map<Class<? extends Entity>, Integer> firstDayLivePlants;

    public CollectAndDisplayStatisticsServiceImpl(Island island, UpdateSettingsService updateSettingsService, ImagesOfEntitiesConfig iConfig) {
        this.island = island;
        imagesOfEntitiesConfig = iConfig;
        this.updateSettingsService = updateSettingsService;
        this.isFirstDay = true;
        liveAnimals = new TreeMap<>(classComparator);
        deadAnimals = new TreeMap<>(classComparator);
        livePlants = new TreeMap<>(classComparator);
        eatenPlants = new TreeMap<>(classComparator);
        bornAnimals = new TreeMap<>(classComparator);
    }

    public void start() {// todo: разбросать по потокам этот метод
        collectStatistics();
        printStatistics(); // поток на вывод изображения
        checkStopGame(); // поток на сбор статистики после потока вывода изображения
        resetLapValues(); // поток на сбор статистики после потока вывода изображения
    }

    @Override
    public void collectStatistics() {
        island.getIsland().values().forEach(list -> list.forEach(entity -> {
            Class<? extends Entity> entityClass = entity.getClass();
            if (entity instanceof Plant plant) {
                if (plant.isEaten()) {
                    eatenPlants.put(entityClass, eatenPlants.get(entityClass) == null ? 1 : eatenPlants.get(entityClass) + 1);
                    ++countEatenPlants;
                } else {
                    livePlants.put(entityClass, livePlants.get(entityClass) == null ? 1 : livePlants.get(entityClass) + 1);
                    ++countLivePlants;
                }
            } else if (entity instanceof Animal animal) {
                if (animal.isBornNewAnimal()) {
                    bornAnimals.put(entityClass, bornAnimals.get(entityClass) == null ? 1 : bornAnimals.get(entityClass) + 1);
                    ++countBornAnimals;
                }
                if (animal.getHealthPercent() > 0) {
                    liveAnimals.put(entityClass, liveAnimals.get(entityClass) == null ? 1 : liveAnimals.get(entityClass) + 1);
                    ++countLiveAnimals;
                } else {
                    deadAnimals.put(entityClass, deadAnimals.get(entityClass) == null ? 1 : deadAnimals.get(entityClass) + 1);
                    ++countDeadAnimals;
                }
            }
        }));
        if (isFirstDay) {
            firstDayLiveAnimals = new TreeMap<>(classComparator);
            firstDayLiveAnimals.putAll(liveAnimals);
            firstDayLivePlants = new TreeMap<>(classComparator);
            firstDayLivePlants.putAll(livePlants);
            firstDayCountLiveAnimals = countLiveAnimals;
            firstDayCountLivePlants = countLivePlants;
            isFirstDay = false;
        }
        ++dayNumber;
    }

    @Override
    public void printStatistics() {
        System.out.printf(STATISTIC_OF_DAY, dayNumber);
        System.out.printf(LIVE_ANIMALS, countLiveAnimals);
        if (countLiveAnimals != 0)
            liveAnimals.forEach((key, value) -> System.out.print("[" + imagesOfEntitiesConfig.getImagesMapConfig().get(key.getSimpleName().toLowerCase()) + " = " + value + "] "));
        System.out.printf(BORN_ANIMALS, countBornAnimals / 2);
        if (countBornAnimals != 0)
            bornAnimals.forEach((key, value) -> System.out.print("[" + imagesOfEntitiesConfig.getImagesMapConfig().get(key.getSimpleName().toLowerCase()) + " = " + value / 2 + "] "));
        System.out.printf(DEAD_ANIMALS, countDeadAnimals);
        if (countDeadAnimals != 0)
            deadAnimals.forEach((key, value) -> System.out.print("[" + imagesOfEntitiesConfig.getImagesMapConfig().get(key.getSimpleName().toLowerCase()) + " = " + value + "] "));
        System.out.printf(LIVE_PLANTS, countLivePlants);
        if (countLivePlants != 0)
            livePlants.forEach((key, value) -> System.out.print("[" + imagesOfEntitiesConfig.getImagesMapConfig().get(key.getSimpleName().toLowerCase()) + " = " + value + "] "));
        System.out.printf(EAT_PLANTS, countEatenPlants);
        if (countEatenPlants != 0)
            eatenPlants.forEach((key, value) -> System.out.print("[" + imagesOfEntitiesConfig.getImagesMapConfig().get(key.getSimpleName().toLowerCase()) + " = " + value + "] "));
        System.out.println(DIFFERENCE);
        int totalDifferenceLiveAnimals = countLiveAnimals - firstDayCountLiveAnimals;
        int totalDifferenceLivePlants = countLivePlants - firstDayCountLivePlants;
        if (totalDifferenceLiveAnimals > 0)
            System.out.printf(MORE_ANIMALS, totalDifferenceLiveAnimals);
        else if (totalDifferenceLiveAnimals < 0)
            System.out.printf(LESS_ANIMALS, Math.abs(totalDifferenceLiveAnimals));
        else System.out.println(NO_CHANGES_ANIMALS);
        printAnimalsDifference();
        if (totalDifferenceLivePlants > 0)
            System.out.printf(MORE_PLANTS, totalDifferenceLivePlants);
        else if (totalDifferenceLivePlants < 0)
            System.out.printf(LESS_PLANTS, Math.abs(totalDifferenceLivePlants));
        else System.out.println(NO_CHANGES_PLANTS);
        printPlantsDifference();
        System.out.println(END);
    }

    private void printAnimalsDifference() {
        String condition;
        int difference;
        for (Map.Entry<Class<? extends Entity>, Integer> pair : firstDayLiveAnimals.entrySet()) {
            difference = (liveAnimals.get(pair.getKey()) == null ? 0 : liveAnimals.get(pair.getKey())) - pair.getValue();
            condition = difference > 0 ? " стало больше на " : (difference < 0 ? " стало меньше на " : " количество не изменилось ");
            if (difference != 0)
                System.out.print("[" + imagesOfEntitiesConfig.getImagesMapConfig().get(pair.getKey().getSimpleName().toLowerCase()) + condition + Math.abs(difference) + "] ");
            else
                System.out.print("[" + imagesOfEntitiesConfig.getImagesMapConfig().get(pair.getKey().getSimpleName().toLowerCase()) + condition + "] ");
        }
        System.out.println();
    }

    private void printPlantsDifference() {
        String condition;
        int difference;
        for (Map.Entry<Class<? extends Entity>, Integer> pair : firstDayLivePlants.entrySet()) {
            difference = (livePlants.get(pair.getKey()) == null ? 0 : livePlants.get(pair.getKey())) - pair.getValue();
            condition = difference > 0 ? " стало больше на " : (difference < 0 ? " стало меньше на " : " количество не изменилось ");
            if (difference != 0)
                System.out.print("[" + imagesOfEntitiesConfig.getImagesMapConfig().get(pair.getKey().getSimpleName().toLowerCase()) + condition + Math.abs(difference) + "] ");
            else
                System.out.print("[" + imagesOfEntitiesConfig.getImagesMapConfig().get(pair.getKey().getSimpleName().toLowerCase()) + condition + "] ");
        }
        System.out.println();
    }

    public void resetLapValues() {
        liveAnimals.clear();
        livePlants.clear();
        deadAnimals.clear();
        eatenPlants.clear();
        bornAnimals.clear();
        countLiveAnimals = 0;
        countDeadAnimals = 0;
        countBornAnimals = 0;
        countLivePlants = 0;
        countEatenPlants = 0;
    }

    public void checkStopGame() {
        if (dayNumber >= updateSettingsService.getIslandConfig().getDaysOfLife()) {
            System.out.println(GAME_OVER);
            System.exit(0);
        }
        int stopNumber = updateSettingsService.getNumberOfStopCondition();
        if (stopNumber == ALL_ANIMALS_DIED) {
            if (liveAnimals.isEmpty()) {
                System.out.println(GAME_OVER);
                System.exit(0);
            }
        } else if (stopNumber == ALL_PREDATORS_DIED) {
            AtomicInteger predatorsCount = new AtomicInteger();
            island.getIsland().values().forEach(list -> list.forEach(entity -> {
                if (entity instanceof Predator && ((Predator) entity).getHealthPercent() > 0)
                    predatorsCount.incrementAndGet();
            }));
            if (predatorsCount.get() == 0) {
                System.out.println(GAME_OVER);
                System.exit(0);
            }
        } else if (stopNumber == ALL_HERBIVORES_DIED) {
            AtomicInteger herbivoresCount = new AtomicInteger();
            island.getIsland().values().forEach(list -> list.forEach(entity -> {
                if (entity instanceof Herbivore && ((Herbivore) entity).getHealthPercent() > 0)
                    herbivoresCount.incrementAndGet();
            }));
            if (herbivoresCount.get() == 0) {
                System.out.println(GAME_OVER);
                System.exit(0);
            }
        }
    }
}
