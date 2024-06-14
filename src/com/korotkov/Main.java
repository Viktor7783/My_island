package com.korotkov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korotkov.config.AnimalConfig;
import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.config.IslandConfig;
import com.korotkov.config.PossibilityOfEatingConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.abstracts.Insect;
import com.korotkov.models.abstracts.Rodent;
import com.korotkov.models.enums.Action;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.models.enums.EntityType;
import com.korotkov.models.herbivores.*;
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;
import com.korotkov.models.plants.Plant;
import com.korotkov.services.interfaces.MoveService;
import com.korotkov.services.impl.ChooseDirectionServiceImpl;
import com.korotkov.services.impl.MoveServiceImpl;
import com.korotkov.services.impl.UpdateSettingsService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.korotkov.config.Constants.*;

public class Main {


    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();
        EntityCharacteristicConfig entityCharacteristicConfig = new EntityCharacteristicConfig(objectMapper, PATH_TO_ENTITY_CHARACTERISTIC);
        PossibilityOfEatingConfig possibilityOfEatingConfig = new PossibilityOfEatingConfig(objectMapper, PATH_TO_POSSIBILITY_OF_EATING, entityCharacteristicConfig.getEntityMapConfig());
        IslandConfig islandConfig = new IslandConfig(PATH_TO_ISLAND_SETTINGS);
        AnimalConfig animalConfig = new AnimalConfig(PATH_TO_ISLAND_SETTINGS);
        UpdateSettingsService updateSettingsService = new UpdateSettingsService(islandConfig, entityCharacteristicConfig);
        updateSettingsService.updateSettings();
        System.out.println(GO_GO_GO);
        Island island = createIsland(islandConfig);
        fillIslandAnimalsAndPlants(island, random, entityCharacteristicConfig); //Заполняем остров животными и растениями
        MoveService moveService = new MoveServiceImpl(island, islandConfig);
        int daysOfLive = islandConfig.getDaysOfLife();
        //Цикл начинается здесь!!! (несколько конов)
        while (daysOfLive > 0) { //TODO: Вынести в один метод (жизнь животных и растений) Или пока все животные не умрут?!?!
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
                                    if (baby != null) {
                                        entityListIterator.add(baby);
                                        //++countBornAnimals;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            island.decreaseAnimalsHealthIfNotEat(animalConfig); //уменьшаем здоровье непоевшим в конце кона! через island!
            --daysOfLive; // День прошёл
            //TODO Вывод статистики в конце каждого кона (Сбор статистики идет для всех животных + трава)
            // Каждый ход собирается статистика:
            // сколько животных осталось на текущий момент и травы ( если все подохли - конец циклу 365 дней)
            // сколько животных умерло от голода/были съедены
            // сколько родилось новых животных
            // разница между первым ходом и текущим по кол-ву животных
            int countLiveAnimals; // Всё это делаем в одном цикле!
            int totalCountLiveAnimals;
            int countLivePlants;
            int totalCountLivePlants;
            int countDeathAnimal;
            int countEatPlants;
            int countBornAnimals;
            int totalDifferenceLiveAnimals; // = totalCountLiveAnimals-firstDayCountLiveAnimals
            int firstDayCountLiveAnimals;
            int totalDifferenceLivePlants; // = totalCountLivePlants-firstDayCountLivePlants
            int firstDayCountLivePlants;
            System.out.println(island); // DELETE
        }
    }

    private static void fillIslandAnimalsAndPlants(Island island, Random random, EntityCharacteristicConfig
            entityCharacteristicConfig) {
        island.getIsland().values()
                .forEach(list -> List.of(EntityType.values())
                        .forEach(currentEntityType -> IntStream.range(0, random.nextInt(getMaxCountOnField(entityCharacteristicConfig, currentEntityType)))
                                .forEach(_ -> list.add(createCurrentEntity(entityCharacteristicConfig, currentEntityType)))));
    }


    private static Entity createCurrentEntity(EntityCharacteristicConfig entityCharacteristicConfig, EntityType
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


    private static Integer getMaxCountOnField(EntityCharacteristicConfig entityCharacteristicConfig, EntityType
            entityType) {
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
                island.put(field, Collections.synchronizedList(new ArrayList<>())); //Синхронайзед лист!!!
            }
        }
        return new Island(island);
    }
}

class MyTestClass { //TODO: Удалить перед pullRequest!!!
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        List<Integer> sList = list.stream().filter(n -> n % 2 == 0).toList();

        ListIterator<Integer> listIterator = list.listIterator();


    }
}

