package com.korotkov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.config.FieldSizeConfig;
import com.korotkov.config.PossibilityOfEatingConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.models.enums.EntityType;
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;
import com.korotkov.models.plants.Grass;
import com.korotkov.models.predators.Wolf;
import com.korotkov.services.MoveService;
import com.korotkov.services.impl.ChooseDirectionServiceImpl;
import com.korotkov.services.impl.MoveServiceImpl;

import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();
        EntityCharacteristicConfig entityCharacteristicConfig = new EntityCharacteristicConfig(objectMapper, "resources/entity_characteristic.json");
        PossibilityOfEatingConfig possibilityOfEatingConfig = new PossibilityOfEatingConfig(objectMapper, "resources/possibility_of_eating.json", entityCharacteristicConfig.getEntityMapConfig());
        FieldSizeConfig fieldSizeConfig = new FieldSizeConfig(100, 20);
        ChooseDirectionServiceImpl chooseDirectionServiceImpl = new ChooseDirectionServiceImpl(random);
        Island island = createIsland(fieldSizeConfig);
        MoveService moveService = new MoveServiceImpl(island);
        int maxPlantsOnField = getMaxCountOnField(entityCharacteristicConfig, EntityType.GRASS);
        int maxWolfsOnField = getMaxCountOnField(entityCharacteristicConfig, EntityType.WOLF);
        //TODO Для начала ручками добавляем в island: grass, wolf, mouse и всех остальных
        // add Grass
        island.getIsland().values()
                .forEach(value -> IntStream.range(0, random.nextInt(maxPlantsOnField))
                        .forEach(_ -> value.add(createGrass(entityCharacteristicConfig))));
        // Add Wolfs
        island.getIsland().values()
                .forEach(value -> IntStream.range(0, random.nextInt(maxWolfsOnField))
                        .forEach(_ -> value.add(createWolf(entityCharacteristicConfig))));

        //Move wolf

        for (Map.Entry<Field, List<Entity>> fieldListEntry : island.getIsland().entrySet()) {
            Field field = fieldListEntry.getKey();
            List<Animal> animals = fieldListEntry.getValue().stream().filter(Animal.class::isInstance).map(e -> (Animal) e).toList();
            for (Animal animal : animals) {
                DirectionType directionToMove = chooseDirectionServiceImpl.chooseDirection();
                int speed = getSpeed(entityCharacteristicConfig, EntityType.WOLF); // Зачем так, ведь мы уже заплонили остров животными? Может лучше animal.getSpeed() - ?
                moveService.move(animal, field, directionToMove, speed);
            }

        }


        System.out.println(island);
    }

    //TODO На примере createGrass сделать остальные методы: createHorse, createMouse и т.д.
    private static Grass createGrass(EntityCharacteristicConfig entityCharacteristicConfig) {
        return new Grass(entityCharacteristicConfig.getEntityMapConfig().get(EntityType.GRASS));
    }

    private static Wolf createWolf(EntityCharacteristicConfig entityCharacteristicConfig) {
        return new Wolf(entityCharacteristicConfig.getEntityMapConfig().get(EntityType.WOLF));
    }

    private static Integer getMaxCountOnField(EntityCharacteristicConfig entityCharacteristicConfig, EntityType entityType) {
        return entityCharacteristicConfig.getEntityMapConfig().get(entityType).getMaxCountOnField();
    }

    private static Integer getSpeed(EntityCharacteristicConfig entityCharacteristicConfig, EntityType entityType) {
        return entityCharacteristicConfig.getEntityMapConfig().get(entityType).getSpeed();
    }

    private static Island createIsland(FieldSizeConfig fieldSizeConfig) {
        Map<Field, List<Entity>> island = new HashMap<>();
        for (int i = 0; i < fieldSizeConfig.getHeight(); i++) {
            for (int j = 0; j < fieldSizeConfig.getWidth(); j++) {
                Field field = new Field(i, j);
                island.put(field, new ArrayList<>());
            }
        }
        return new Island(island);
    }
}