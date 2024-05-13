package com.korotkov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korotkov.config.Constants;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.IntStream;

import static com.korotkov.config.Constants.CREATE_CURRENT_ENTITY_ERROR;
import static com.korotkov.config.Constants.GET_CONSTRUCTOR_ERROR;
import static com.korotkov.models.enums.EntityType.WOLF;

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
        /*int maxPlantsOnField = getMaxCountOnField(entityCharacteristicConfig, EntityType.GRASS);
        int maxWolfsOnField = getMaxCountOnField(entityCharacteristicConfig, WOLF);*/

        island.getIsland().values()
                .forEach(list -> List.of(EntityType.values())
                        .forEach(currentEntityType -> IntStream.range(0, random.nextInt(getMaxCountOnField(entityCharacteristicConfig, currentEntityType)))
                                .forEach(x -> list.add(createCurrentEntity(entityCharacteristicConfig, currentEntityType)))));

       /* island.getIsland().values()
                .forEach(value -> IntStream.range(0, random.nextInt(maxPlantsOnField))
                        .forEach(_ -> value.add(createGrass(entityCharacteristicConfig))));
        // Add Wolfs
        island.getIsland().values()
                .forEach(value -> IntStream.range(0, random.nextInt(maxWolfsOnField))
                        .forEach(_ -> value.add(createWolf(entityCharacteristicConfig))));*/

        //Здесь мы двигаем всех животных (Animal) во всех полях (Field) по одному разу!!! (Получается один кон)
        for (Map.Entry<Field, List<Entity>> fieldListEntry : island.getIsland().entrySet()) {
            Field field = fieldListEntry.getKey();
            List<Animal> animals = fieldListEntry.getValue().stream().filter(Animal.class::isInstance).map(e -> (Animal) e).toList();
            for (Animal animal : animals) {
                DirectionType directionToMove = chooseDirectionServiceImpl.chooseDirection();
                int speed = getSpeed(entityCharacteristicConfig, Arrays.stream(EntityType.values())
                        .filter(entityType -> entityType.getClazz() == animal.getClass()).
                        findFirst().get());
                moveService.move(animal, field, directionToMove, speed);
            }
        }


        System.out.println(island);
    }

    private static Entity createCurrentEntity(EntityCharacteristicConfig entityCharacteristicConfig, EntityType currentEntityType) {
        Entity currentEntity = null;
        Class clazz = currentEntityType.getClazz();
        Constructor constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor(Entity.class);
        } catch (NoSuchMethodException e) {
            System.out.printf(GET_CONSTRUCTOR_ERROR, clazz);
            System.exit(0);
        }
        try {
            currentEntity = (Entity) constructor.newInstance(entityCharacteristicConfig.getEntityMapConfig().get(currentEntityType));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            System.out.printf(CREATE_CURRENT_ENTITY_ERROR, currentEntityType.getType());
        }
        return currentEntity;
    }


   /* private static Grass createGrass(EntityCharacteristicConfig entityCharacteristicConfig) {
        return new Grass(entityCharacteristicConfig.getEntityMapConfig().get(EntityType.GRASS));
    }

    private static Wolf createWolf(EntityCharacteristicConfig entityCharacteristicConfig) {
        return new Wolf(entityCharacteristicConfig.getEntityMapConfig().get(EntityType.WOLF));
    }*/

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