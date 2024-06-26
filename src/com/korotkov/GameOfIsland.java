package com.korotkov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korotkov.config.AnimalConfig;
import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.config.IslandConfig;
import com.korotkov.config.PossibilityOfEatingConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.Action;
import com.korotkov.models.enums.EntityType;
import com.korotkov.models.herbivores.Caterpillar;
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;
import com.korotkov.models.plants.Grass;
import com.korotkov.services.impl.CollectAndDisplayStatisticsServiceImpl;
import com.korotkov.services.interfaces.MoveService;
import com.korotkov.services.impl.MoveServiceImpl;
import com.korotkov.services.impl.UpdateSettingsService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.IntStream;

import static com.korotkov.config.Constants.*;

public class GameOfIsland {
    private final Random random;
    private final EntityCharacteristicConfig entityCharacteristicConfig;
    private final PossibilityOfEatingConfig possibilityOfEatingConfig;
    private final IslandConfig islandConfig;
    private final AnimalConfig animalConfig;
    private final UpdateSettingsService updateSettingsService;
    private Island island;
    private MoveService moveService;
    private CollectAndDisplayStatisticsServiceImpl collectAndDisplayStatisticsService;
    private int daysOfLife;

    public GameOfIsland() {
        ObjectMapper objectMapper = new ObjectMapper();
        random = new Random();
        entityCharacteristicConfig = new EntityCharacteristicConfig(objectMapper, PATH_TO_ENTITY_CHARACTERISTIC);
        possibilityOfEatingConfig = new PossibilityOfEatingConfig(objectMapper, PATH_TO_POSSIBILITY_OF_EATING, entityCharacteristicConfig.getEntityMapConfig());
        islandConfig = new IslandConfig(PATH_TO_ISLAND_SETTINGS);
        animalConfig = new AnimalConfig(PATH_TO_ISLAND_SETTINGS);
        updateSettingsService = new UpdateSettingsService(islandConfig, entityCharacteristicConfig);
    }

    public static void main(String[] args) {
        new GameOfIsland().start();
    }

    public void start() {
        System.out.println(GREETINGS);
        updateSettingsService.updateSettings();
        System.out.println(GO_GO_GO);
        island = createIsland(islandConfig);
        fillIslandAnimalsAndPlants(island, random, entityCharacteristicConfig);
        moveService = new MoveServiceImpl(island, islandConfig);
        collectAndDisplayStatisticsService = new CollectAndDisplayStatisticsServiceImpl(island, updateSettingsService);
        daysOfLife = islandConfig.getDaysOfLife();

        //Цикл начинается здесь!!! (несколько конов)
        while (daysOfLife > 0) { //TODO: Вынести в один метод (жизнь животных и растений) Или пока все животные не умрут?!?!
            island.removeDeathAnimal(); // Удаляем мертвечину (Те животные у кого health<=0)
            island.removeEatenPlants(); // Удаляем пожранные растения (isEaten = true)
            island.refillPlants(entityCharacteristicConfig, random);// Подсаживаем растения за один кон
            island.restoreEatMoveBornState(); //обнуляем eat (если ел) и isBornNewAnimal()  и move в начале кона!!!

            for (Map.Entry<Field, List<Entity>> fieldListEntry : island.getIsland().entrySet()) {
                Field field = fieldListEntry.getKey();
                List<Entity> entities = fieldListEntry.getValue();
                ListIterator<Entity> entityListIterator = entities.listIterator();
                while (entityListIterator.hasNext()) {
                    Entity entity = entityListIterator.next();
                    if (entity instanceof Animal animal) {
                        if (!animal.isBornNewAnimal() && !animal.isMovedInThisLap() && animal.getHealthPercent() > 0) {//Если животное двигалось или уже рожало совместно с другим животным - пропускаем ход - он уже сделан
                            Action action = Action.values()[random.nextInt(Action.values().length)];
                            switch (action) {
                                case MOVE -> {
                                    if (animal.getSpeed() > 0)
                                        moveService.move(animal, random.nextInt(1, animal.getSpeed() + 1), field, animal.chooseDirection(random), entityListIterator);
                                }
                                case EAT -> animal.eat(entities, possibilityOfEatingConfig, random);
                                case REPRODUCE -> {
                                    Animal baby = animal.reproduce(entities); // Мы можем только рожать, не можем добавлять на остров
                                    if (baby != null) entityListIterator.add(baby);
                                }
                            }
                        }
                    }
                }
            }
            island.decreaseAnimalsHealthIfNotEat(animalConfig);
            --daysOfLife; // День прошёл
            collectAndDisplayStatisticsService.start();
        }
    }


    private void fillIslandAnimalsAndPlants(Island island, Random random, EntityCharacteristicConfig
            entityCharacteristicConfig) {
        island.getIsland().values()
                .forEach(list -> List.of(EntityType.values())
                        .forEach(currentEntityType -> IntStream.range(0, random.nextInt(getMaxCountOnField(entityCharacteristicConfig, currentEntityType)))
                                .forEach(_ -> list.add(createCurrentEntity(entityCharacteristicConfig, currentEntityType)))));
    }


    private Entity createCurrentEntity(EntityCharacteristicConfig entityCharacteristicConfig, EntityType
            currentEntityType) {
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


    private Integer getMaxCountOnField(EntityCharacteristicConfig entityCharacteristicConfig, EntityType
            entityType) {
        return entityCharacteristicConfig.getEntityMapConfig().get(entityType).getMaxCountOnField();
    }

    /*private Integer getSpeed(EntityCharacteristicConfig entityCharacteristicConfig, EntityType entityType) {
        return entityCharacteristicConfig.getEntityMapConfig().get(entityType).getSpeed();
    }*/

    private Island createIsland(IslandConfig islandConfig) {
        Map<Field, List<Entity>> island = new HashMap<>();
        for (int i = 0; i < islandConfig.getHeight(); i++) {
            for (int j = 0; j < islandConfig.getWidth(); j++) {
                Field field = new Field(i, j);
                island.put(field, Collections.synchronizedList(new ArrayList<>())); //Синхронайзед лист!!!
            }
        }
        return new Island(island);
    }
}

class MyTestClass { //TODO: Удалить перед pullRequest!!!
    public static void main(String[] args) {
        Caterpillar caterpillar = new Caterpillar(0.01, 1000, 0, 0.0);
        Grass grass = new Grass(1.0, 200, 0, 0.0);
        caterpillar.increaseHealthPercent(grass);
        System.out.println(caterpillar.getKgToGetFull());
        double fin = 30.4;
        fin += 0.01 * 100 / 0.0;
        if (fin > 100.0) fin = 100.0;
        System.out.println(fin);

        double zzz = 1.0 * 100 / 0.0 + 100;
        System.out.println(zzz);
        System.out.println(1.0 * 100 / 0.0 > 100.0);
        /*double finMouse = 33.1;
        finMouse += (0.01 * 100 / 0.01);
        System.out.println(finMouse);*/


    }
}

