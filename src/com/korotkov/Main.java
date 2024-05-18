package com.korotkov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.config.IslandConfig;
import com.korotkov.config.PossibilityOfEatingConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.models.enums.EntityType;
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;
import com.korotkov.services.MoveService;
import com.korotkov.services.impl.ChooseDirectionServiceImpl;
import com.korotkov.services.impl.MoveServiceImpl;
import com.korotkov.services.impl.UpdateSettingsService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.IntStream;

import static com.korotkov.config.Constants.*;

public class Main {


    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();
        EntityCharacteristicConfig entityCharacteristicConfig = new EntityCharacteristicConfig(objectMapper, PATH_TO_ENTITY_CHARACTERISTIC);
        PossibilityOfEatingConfig possibilityOfEatingConfig = new PossibilityOfEatingConfig(objectMapper, PATH_TO_POSSIBILITY_OF_EATING, entityCharacteristicConfig.getEntityMapConfig());
        IslandConfig islandConfig = new IslandConfig(PATH_TO_ISLAND_SETTINGS);
        ChooseDirectionServiceImpl chooseDirectionServiceImpl = new ChooseDirectionServiceImpl(random);
        UpdateSettingsService updateSettingsService = new UpdateSettingsService(islandConfig, entityCharacteristicConfig);
        // ОПЦИОНАЛЬНО: менять дефолтные настройки
        updateSettingsService.updateSettings();

        System.out.println(GO_GO_GO);
        Island island = createIsland(islandConfig);
        //Заполняем остров животными и растениями
        island.getIsland().values()
                .forEach(list -> List.of(EntityType.values())
                        .forEach(currentEntityType -> IntStream.range(0, random.nextInt(getMaxCountOnField(entityCharacteristicConfig, currentEntityType)))
                                .forEach(_ -> list.add(createCurrentEntity(entityCharacteristicConfig, currentEntityType)))));

        MoveService moveService = new MoveServiceImpl(island);
        //Здесь мы двигаем всех животных (Animal) во всех полях (Field) по одному разу!!! (Получается один кон)
        island.removeDeathAnimal(); // Удаляем мертвечину (Те животные у кого health<=0)
        //TODO  Животное умирает если пищи нужно животному для полного насыщения = 0 или животное съели
        //Способ проверки: в конце хода если 0; 3 хода без еды; в начале хода
        island.refillPlants(entityCharacteristicConfig, random);// Подсаживаем растения за один кон
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

        //TODO Вывод статистики в конце каждого кона (Сбор статистики идет для всех животных + трава)
        // Каждый ход собирается статистика:
        // сколько животных осталось на текущий момент
        // сколько животных умерло от голода/были съедены
        // сколько родилось новых животных
        // разница между первым ходом и текущим по кол-ву животных


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


    private static Integer getMaxCountOnField(EntityCharacteristicConfig entityCharacteristicConfig, EntityType entityType) {
        return entityCharacteristicConfig.getEntityMapConfig().get(entityType).getMaxCountOnField();
    }

    private static Integer getSpeed(EntityCharacteristicConfig entityCharacteristicConfig, EntityType entityType) {
        return entityCharacteristicConfig.getEntityMapConfig().get(entityType).getSpeed();
    }

    private static Island createIsland(IslandConfig islandConfig) {
        Map<Field, List<Entity>> island = new HashMap<>();
        for (int i = 0; i < islandConfig.getHeight(); i++) {
            for (int j = 0; j < islandConfig.getWidth(); j++) {
                Field field = new Field(i, j);
                island.put(field, new ArrayList<>());
            }
        }
        return new Island(island);
    }
}