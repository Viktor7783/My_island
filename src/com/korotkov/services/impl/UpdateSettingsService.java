package com.korotkov.services.impl;

import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.config.IslandConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.EntityType;
import com.korotkov.models.herbivores.Herbivore;
import com.korotkov.models.plants.Plant;
import com.korotkov.models.predators.Predator;
import org.w3c.dom.ls.LSOutput;

import java.util.*;

import static com.korotkov.config.Constants.*;

public class UpdateSettingsService {
    IslandConfig islandConfig;
    EntityCharacteristicConfig entityCharacteristicConfig;
    Scanner scanner;

    public UpdateSettingsService(IslandConfig islandConfig, EntityCharacteristicConfig entityCharacteristicConfig) {
        this.islandConfig = islandConfig;
        this.entityCharacteristicConfig = entityCharacteristicConfig;
        scanner = new Scanner(System.in);
    }

    public void updateSettings() {
        System.out.println(CHANGE_SETTINGS);
        String text = scanner.nextLine();
        if (text.equals("1")) {
            System.out.println(CHOOSE_YOUR_DESTINY);
            String choose = scanner.nextLine();
            switch (choose) {
                case "1" -> {
                    changeIslandSettings();
                    updateSettings();
                }
                case "2" -> changeEntitySettings();
                case "3" -> {
                }
                default -> {
                    System.out.println(GAME_OVER);
                    System.exit(0);
                }
            }

        } else if (text.equals("2")) {
            return;
        } else {
            System.out.println(GAME_OVER);
            System.exit(0);
        }
    }

    private void changeIslandSettings() {
        System.out.println(SET_WIDTH_OF_ISLAND);
        int number = scanner.nextInt();
        islandConfig.setWidth(number);
        System.out.println(SET_HEIGHT_OF_ISLAND);
        number = scanner.nextInt();
        islandConfig.setHeight(number);
        System.out.println(SET_DAYS_OF_ISLAND);
        number = scanner.nextInt();
        islandConfig.setDaysOfLife(number);
        System.out.println(SETTINGS_HAVE_BEEN_CHANGED);
    }

    private void changeEntitySettings() {
        // Меняем настройки животных и травы
        System.out.println(CHOOSE_CURRENT_ENTITY_SETTINGS);
        String choose = scanner.nextLine();
        switch (choose) {
            case "1" -> changePredatorsSettings();
            case "2" -> changeHerbivoresSettings();
            case "3" -> changePlantsSettings();
            case "4" -> updateSettings();
            default -> {
                System.out.println(GAME_OVER);
                System.exit(0);
            }
        }
    }


    private void changePredatorsSettings() {
        System.out.println(CHANGE_PREDATOR_SETTINGS);
        String choose = scanner.nextLine();
        switch (choose) {
            case "1" -> {
                changeAllAnimals(Predator.class);
                changeEntitySettings();
            }
            case "2" -> {
                selectAndChangeEntity(Predator.class);
                changeEntitySettings();
            }
            case "3" -> changeEntitySettings(); //назад
            default -> {
                System.out.println(GAME_OVER);
                System.exit(0);
            }
        }
    }

    // Сделать метод для травоядных по подобию changePredatorsSettings()
    private void changeHerbivoresSettings() {
        System.out.println(CHANGE_HERBIVORES_SETTINGS);
        String choose = scanner.nextLine();
        switch (choose) {
            case "1" -> {
                changeAllAnimals(Herbivore.class);
                changeEntitySettings();
            }
            case "2" -> {
                selectAndChangeEntity(Herbivore.class);
                changeEntitySettings();
            }
            case "3" -> changeEntitySettings(); //назад
            default -> {
                System.out.println(GAME_OVER);
                System.exit(0);
            }
        }
    }

    private void changePlantsSettings() {
        System.out.println(CHANGE_PLANTS_SETTINGS);
        String choose = scanner.nextLine();
        switch (choose) {
            case "1" -> {
                selectAndChangeEntity(Plant.class);
                changeEntitySettings();
            }
            case "2" -> changeEntitySettings(); // Назад
            default -> {
                System.out.println(GAME_OVER);
                System.exit(0);
            }
        }
    }

    private void changeAllAnimals(Class<? extends Entity> entityClass) {
        String animal = entityClass == Predator.class ? "хищников" : "травоядных";
        System.out.printf(ALL_ANIMAL_WEIGHT, animal);
        double weight = scanner.nextDouble();
        System.out.printf(ALL_ANIMAL_COUNT, animal);
        int count = scanner.nextInt();
        System.out.println(ALL_ANIMAL_SPEED);
        int speed = scanner.nextInt();
        System.out.println(ALL_ANIMAL_KG_TO_GET_FULL);
        double maxKg = scanner.nextDouble();

        if (entityClass == Predator.class) {
            for (Entity entity : entityCharacteristicConfig.getEntityMapConfig().values()) {
                if (entity instanceof Predator) {
                    changeCurrentAnimal((Animal) entity, weight, count, speed, maxKg);
                }
            }
        } else if (entityClass == Herbivore.class) {
            for (Entity entity : entityCharacteristicConfig.getEntityMapConfig().values()) {
                if (entity instanceof Herbivore) {
                    changeCurrentAnimal((Animal) entity, weight, count, speed, maxKg);
                }
            }
        }
        System.out.println(SETTINGS_HAVE_BEEN_CHANGED);
    }


    private void changeCurrentAnimal(Animal animal, double weight, int count, int speed, double maxKg) {
        animal.setWeight(animal.getWeight() + weight);
        animal.setMaxCountOnField(animal.getMaxCountOnField() + count);
        animal.setSpeed(animal.getSpeed() + speed);
        animal.setKgToGetFull(animal.getKgToGetFull() + maxKg);
    }

    private void setCurrentEntity(Entity entity, double weight, int count, int speed, double maxKg) {
        if (entity instanceof Animal) {
            entity.setWeight(weight);
            entity.setMaxCountOnField(count);
            entity.setSpeed(speed);
            entity.setKgToGetFull(maxKg);
        } else if (entity instanceof Plant) {
            entity.setWeight(weight);
            entity.setMaxCountOnField(count);
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
        int numberOfEntityType = scanner.nextInt(); //Выбрали нужное животное или растение
        EntityType currentEntityType = entityTypeMap.get(numberOfEntityType);
        //double weight, int count, int speed, double maxKg
        double weight = 0.0, maxKg = 0.0;
        int count = 0, speed = 0;
        System.out.printf(SET_WEIGHT, currentEntityType.getType());
        weight = scanner.nextDouble();
        System.out.printf(SET_COUNT, currentEntityType.getType());
        count = scanner.nextInt();

        if (entityClass == Predator.class || entityClass == Herbivore.class) {
            System.out.printf(SET_SPEED, currentEntityType.getType());
            speed = scanner.nextInt();
            System.out.printf(SET_KG_TO_FULL, currentEntityType.getType());
            maxKg = scanner.nextDouble();
        }

        setCurrentEntity(entityCharacteristicConfig.getEntityMapConfig().get(currentEntityType), weight, count, speed, maxKg);
    }
}
