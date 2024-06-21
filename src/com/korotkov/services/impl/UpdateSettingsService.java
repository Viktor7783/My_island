package com.korotkov.services.impl;

import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.config.IslandConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.EntityType;
import com.korotkov.models.herbivores.Caterpillar;
import com.korotkov.models.herbivores.Herbivore;
import com.korotkov.models.herbivores.Mouse;
import com.korotkov.models.plants.Plant;
import com.korotkov.models.predators.Predator;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static com.korotkov.config.Constants.*;

public class UpdateSettingsService {
    private final IslandConfig islandConfig;
    private final EntityCharacteristicConfig entityCharacteristicConfig;
    private final BufferedReader reader;
    private int intNumber;
    private int numberOfStopCondition;

    public UpdateSettingsService(IslandConfig islandConfig, EntityCharacteristicConfig entityCharacteristicConfig) {
        this.islandConfig = islandConfig;
        this.entityCharacteristicConfig = entityCharacteristicConfig;
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void updateSettings() {
        System.out.println(CHANGE_SETTINGS);
        intNumber = safeIntegerRead(reader);
        if (intNumber == 1) {
            while (true) {
                System.out.println(CHOOSE_YOUR_DESTINY);
                intNumber = safeIntegerRead(reader);
                switch (intNumber) {
                    case 1 -> changeIslandSettings();
                    case 2 -> changeEntitySettings();
                    case 3 -> setConditionsForStopGame();
                    case 4 -> {
                        exitSettings(reader);
                        return;
                    }
                    default -> exitGame(reader);
                }
            }
        } else if (intNumber == 2) {
            exitSettings(reader);
        } else exitGame(reader);
    }

    private void setConditionsForStopGame() {
        System.out.println(CHOOSE_STOP_CONDITIONS);
        int intNumber = safeIntegerRead(reader);
        switch (intNumber) {
            case 1 -> numberOfStopCondition = 1;// Умерли все животные
            case 2 -> numberOfStopCondition = 2; // Умерли все хищники
            case 3 -> numberOfStopCondition = 3; // Умерли все травоядные
            case 4 -> {
            } //Выход в главное меню настроек
            default -> exitGame(reader);
        }

    }

    public int getNumberOfStopCondition() {
        return numberOfStopCondition;
    }

    private void changeIslandSettings() {
        System.out.println(SET_WIDTH_OF_ISLAND);
        islandConfig.setWidth(safeIntegerRead(reader));
        System.out.println(SET_HEIGHT_OF_ISLAND);
        islandConfig.setHeight(safeIntegerRead(reader));
        System.out.println(SET_DAYS_OF_ISLAND);
        islandConfig.setDaysOfLife(safeIntegerRead(reader));
        System.out.println(SETTINGS_HAVE_BEEN_CHANGED);
    }

    private void changeEntitySettings() {
        while (true) {
            System.out.println(CHOOSE_CURRENT_ENTITY_SETTINGS);
            intNumber = safeIntegerRead(reader);
            switch (intNumber) {
                case 1 -> changePredatorsSettings();
                case 2 -> changeHerbivoresSettings();
                case 3 -> changePlantsSettings();
                case 4 -> {
                    return;
                }
                default -> exitGame(reader);
            }
        }
    }


    private void changePredatorsSettings() {
        while (true) {
            System.out.println(CHANGE_PREDATOR_SETTINGS);
            intNumber = safeIntegerRead(reader);
            switch (intNumber) {
                case 1 -> changeAllAnimals(Predator.class);
                case 2 -> selectAndChangeEntity(Predator.class);
                case 3 -> {
                    return;
                }
                default -> exitGame(reader);
            }
        }
    }

    private void changeHerbivoresSettings() {
        while (true) {
            System.out.println(CHANGE_HERBIVORES_SETTINGS);
            intNumber = safeIntegerRead(reader);
            switch (intNumber) {
                case 1 -> changeAllAnimals(Herbivore.class);
                case 2 -> selectAndChangeEntity(Herbivore.class);
                case 3 -> {
                    return;
                }
                default -> exitGame(reader);
            }
        }
    }

    private void changePlantsSettings() {
        while (true) {
            System.out.println(CHANGE_PLANTS_SETTINGS);
            intNumber = safeIntegerRead(reader);
            switch (intNumber) {
                case 1 -> selectAndChangeEntity(Plant.class);
                case 2 -> {
                    return;
                }
                default -> exitGame(reader);
            }
        }
    }

    private void changeAllAnimals(Class<? extends Entity> entityClass) {
        String animal = entityClass == Predator.class ? "хищников" : "травоядных";
        int maxCountText = entityClass == Predator.class ? 35 : 200;
        System.out.printf(ALL_ANIMAL_WEIGHT, animal);
        double weight = safeDoubleRead(reader);
        System.out.printf(ALL_ANIMAL_COUNT, animal, maxCountText);
        int count = safeIntegerRead(reader);
        System.out.printf(ALL_ANIMAL_SPEED, animal);
        int speed = safeIntegerRead(reader);
        System.out.printf(ALL_ANIMAL_KG_TO_GET_FULL, animal);
        double maxKg = safeDoubleRead(reader);
        System.out.printf(ALL_ANIMAL_COUNT_BORN_BABY, animal);
        int maxBornBaby = safeIntegerRead(reader);

        if (entityClass == Predator.class) {
            entityCharacteristicConfig.getEntityMapConfig().values().stream()
                    .filter(entity -> entity instanceof Predator)
                    .map(e -> (Animal) e)
                    .forEach(predator -> changeCurrentAnimal(predator, weight, count, speed, maxKg, maxBornBaby));
        } else if (entityClass == Herbivore.class) {
            entityCharacteristicConfig.getEntityMapConfig().values().stream()
                    .filter(entity -> entity instanceof Herbivore)
                    .map(e -> (Animal) e)
                    .forEach(herbivore -> changeCurrentAnimal(herbivore, weight, count, speed, maxKg, maxBornBaby));
        }
        System.out.println(SETTINGS_HAVE_BEEN_CHANGED);
    }


    private void changeCurrentAnimal(Animal animal, double weight, int count, int speed, double maxKg, int maxBorn) {
        animal.setWeight(animal.getWeight() + weight);
        animal.setMaxCountOnField(animal.getMaxCountOnField() + count);
        animal.setSpeed(animal.getSpeed() + speed);
        animal.setKgToGetFull(animal.getKgToGetFull() + maxKg);
        animal.setCountBornBaby(animal.getCountBornBaby() + maxBorn);
    }

    private void setCurrentEntity(Entity entity, double weight, int count, int speed, double maxKg, int maxBorn) {
        entity.setWeight(weight);
        entity.setMaxCountOnField(count);
        if (entity instanceof Animal animal) {
            animal.setWeight(weight);
            animal.setMaxCountOnField(count);
            animal.setSpeed(speed);
            animal.setKgToGetFull(maxKg);
            animal.setCountBornBaby(maxBorn);
        }
        System.out.println(SETTINGS_HAVE_BEEN_CHANGED);
    }

    private void selectAndChangeEntity(Class<? extends Entity> entityClass) {
        final int[] counter = {1};
        Map<Integer, EntityType> entityTypeMap = new HashMap<>();
        String entityName = entityClass == Plant.class ? "растение" : "животное";

        if (entityClass == Plant.class) entityCharacteristicConfig.getEntityMapConfig().entrySet().stream()
                .filter(pair -> pair.getValue() instanceof Plant)
                .forEach(pair -> entityTypeMap.put(counter[0]++, pair.getKey()));
        else if (entityClass == Predator.class) {
            entityCharacteristicConfig.getEntityMapConfig().entrySet().stream()
                    .filter(pair -> pair.getValue() instanceof Predator)
                    .forEach(pair -> entityTypeMap.put(counter[0]++, pair.getKey()));
        } else if (entityClass == Herbivore.class) {
            entityCharacteristicConfig.getEntityMapConfig().entrySet().stream()
                    .filter(pair -> pair.getValue() instanceof Herbivore)
                    .forEach(pair -> entityTypeMap.put(counter[0]++, pair.getKey()));
        }

        System.out.printf(CHOOSE_CURRENT_ENTITY, entityName);
        entityTypeMap.forEach((k, v) -> System.out.println(k + " - " + v.getType()));
        do {
            System.out.println(CHOOSE_FROM_LIST);
            intNumber = safeIntegerRead(reader);
        } while (!entityTypeMap.containsKey(intNumber));
        EntityType currentEntityType = entityTypeMap.get(intNumber);
        Entity currentEntity = entityCharacteristicConfig.getEntityMapConfig().get(currentEntityType);
        int maxCountText = 0;
        if (currentEntity instanceof Mouse) maxCountText = 500;
        else if (currentEntity instanceof Caterpillar) maxCountText = 1100;
        else if (currentEntity instanceof Predator) maxCountText = 35;
        else if (currentEntity instanceof Herbivore) maxCountText = 200;
        else if (currentEntity instanceof Plant) maxCountText = 500;
        double weight, maxKg = 0.0;
        int count, speed = 0, maxBorn = 0;
        System.out.printf(SET_WEIGHT, currentEntityType.getType());
        weight = safeDoubleRead(reader);
        System.out.printf(SET_COUNT, currentEntityType.getType(), maxCountText);
        count = safeIntegerRead(reader);

        if (entityClass == Predator.class || entityClass == Herbivore.class) {
            System.out.printf(SET_SPEED, currentEntityType.getType());
            speed = safeIntegerRead(reader);
            System.out.printf(SET_KG_TO_FULL, currentEntityType.getType());
            maxKg = safeDoubleRead(reader);
            System.out.printf(SET_COUNT_MAX_BORN_BABY, currentEntityType.getType());
            maxBorn = safeIntegerRead(reader);
        }

        setCurrentEntity(entityCharacteristicConfig.getEntityMapConfig().get(currentEntityType), weight, count, speed, maxKg, maxBorn);
    }

    private int safeIntegerRead(BufferedReader reader) {
        while (true) {
            try {
                return Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.out.println(INTEGER_PARSE_ERROR);
            }
        }
    }

    private double safeDoubleRead(BufferedReader reader) {
        while (true) {
            try {
                return Double.parseDouble(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.out.println(DOUBLE_PARSE_ERROR);
            }
        }
    }

    private void safeCloseReader(BufferedReader reader) {
        try {
            reader.close();
        } catch (IOException _) {
            System.out.println(INPUT_OUTPUT_ERROR);
        }
    }

    private void exitGame(BufferedReader reader) {
        System.out.println(GAME_OVER);
        safeCloseReader(reader);
        System.exit(0);
    }

    private void exitSettings(BufferedReader reader) {
        System.out.println(EXIT_FROM_SETTINGS);
        safeCloseReader(reader);
    }
}
