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
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;
import com.korotkov.multithreading.DailyActivities;
import com.korotkov.services.impl.CollectAndDisplayStatisticsServiceImpl;
import com.korotkov.services.interfaces.MoveService;
import com.korotkov.services.impl.MoveServiceImpl;
import com.korotkov.services.impl.UpdateSettingsService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    private DailyActivities dailyActivities;
    private ExecutorService executors;

    public GameOfIsland() {
        ObjectMapper objectMapper = new ObjectMapper();
        random = new Random();
        entityCharacteristicConfig = new EntityCharacteristicConfig(objectMapper, PATH_TO_ENTITY_CHARACTERISTIC);
        possibilityOfEatingConfig = new PossibilityOfEatingConfig(objectMapper, PATH_TO_POSSIBILITY_OF_EATING, entityCharacteristicConfig.getEntityMapConfig());
        islandConfig = new IslandConfig(PATH_TO_ISLAND_SETTINGS);
        animalConfig = new AnimalConfig(PATH_TO_ISLAND_SETTINGS);
        updateSettingsService = new UpdateSettingsService(islandConfig, entityCharacteristicConfig);
        dailyActivities = new DailyActivities();
        executors = Executors.newCachedThreadPool();
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
        while (daysOfLife > 0) { //TODO: Вынести в один метод (жизнь животных и растений)
            executors.execute(() -> {//Поток на удаление дохлятины и восстановление показателей животных  todo: вынести из цикла daysOfLife
                        try {
                            while (!Thread.interrupted()) {
                                synchronized (dailyActivities) {
                                    while (!dailyActivities.isShownDailyStatistics()) {
                                        dailyActivities.wait();
                                    }
                                }
                                island.removeAndRestoreAnimals();
                                synchronized (dailyActivities) {
                                    dailyActivities.setRemoveAndRestoreAnimals(true);
                                    dailyActivities.notifyAll();
                                    while (!dailyActivities.isGrassPlanted()) {
                                        dailyActivities.wait();
                                    }
                                    if (dailyActivities.isShownDailyStatistics()) {
                                        dailyActivities.setShownDailyStatistics(false);
                                        dailyActivities.notifyAll();
                                    }
                                }
                            }
                        } catch (InterruptedException _) {

                        }
                    }
            );

            executors.execute(() -> {//Поток на удаление съеденных и посадку новых растений работает параллельно с дохлятиной todo: вынести из цикла daysOfLife
                        try {
                            while (!Thread.interrupted()) {
                                synchronized (dailyActivities) {
                                    while (!dailyActivities.isShownDailyStatistics()) {
                                        dailyActivities.wait();
                                    }
                                }
                                island.removeEatenPlants();
                                island.refillPlants(entityCharacteristicConfig, random);
                                synchronized (dailyActivities) {
                                    dailyActivities.setGrassPlanted(true);
                                    dailyActivities.notifyAll();
                                    while (!dailyActivities.isRemoveAndRestoreAnimals()) {
                                        dailyActivities.wait();
                                    }
                                    if (dailyActivities.isShownDailyStatistics()) {
                                        dailyActivities.setShownDailyStatistics(false);
                                        dailyActivities.notifyAll();
                                    }
                                }
                            }
                        } catch (InterruptedException _) {

                        }
                    }
            );


            //todo: Здесь будет поток AnimalActions - после grassPlanted и animalsRemoveAndRestore и еще что то чтобы не делал цикл постоянно!
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
            island.decreaseAnimalsHealthIfNotEat(animalConfig); //todo: добавляем в поток Animal Actions


            executors.execute(() -> { // Поток со статистикой
                try {
                    while (!Thread.interrupted()) {
                        synchronized (dailyActivities) {
                            while (!dailyActivities.isTimeToShowStatistics()) {
                                dailyActivities.wait();
                            }
                        }
                        --daysOfLife;
                        collectAndDisplayStatisticsService.start();
                        synchronized (dailyActivities) {
                            dailyActivities.setGrassPlanted(false);
                            dailyActivities.setRemoveAndRestoreAnimals(false);
                            dailyActivities.setAnimalActionsCompleted(false);
                            dailyActivities.setShownDailyStatistics(true);
                            dailyActivities.notifyAll();
                        }
                    }
                } catch (InterruptedException _) {

                }
            });
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
                island.put(field, new ArrayList<>());
            }
        }
        return new Island(island);
    }
}

class MyTestClass { //TODO: Удалить перед pullRequest!!!
    public static void main(String[] args) {
       /* List<List<Integer>> set = new ArrayList<>();
        IntStream.range(0, 5).forEach(_ -> {
            List<Integer> list = new ArrayList<>();
            IntStream.range(0, 100000).forEach(list::add);
            set.add(list);
        });

        ExecutorService executors = Executors.newCachedThreadPool();
        executors.execute(() -> {
            set.forEach(list -> {
                synchronized (list) {
                    System.out.println("Удаляем четные числа");
                    list.removeIf(number -> number % 2 == 0);
                    System.out.println("Кончили удалять чётные");
                }
            });

        });
        executors.execute(() -> {
            set.forEach(list -> {
                synchronized (list) {
                    System.out.println("Удаляем Нечетные числа");
                    list.removeIf(number -> number % 2 != 0);
                    System.out.println("Кончили с нечетными");
                }
            });

        });
        executors.shutdown();
        try {
            TimeUnit.MILLISECONDS.sleep(900);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (set) {
            set.forEach(list -> System.out.println(list.size()));
        }
        Set<List<Integer>> set1 = new HashSet<>();
        set1.add(List.of(1, 2, 3, 4, 5));
        set1.add(List.of(1, 2, 3, 4, 5));
        set1.add(List.of(1, 2, 3, 4, 5));
        set1.add(List.of(1, 2, 3, 4, 5));
        System.out.println("Size of set1 = " + set1.size());

        List<Integer> myList = new ArrayList<>();
        IntStream.range(0, 100000).forEach(myList::add);
        new Thread(() -> myList.forEach(System.out::println)).start();
        new Thread(() -> myList.forEach(System.out::println)).start();
        new Thread(() -> myList.forEach(System.out::println)).start();
        new Thread(() -> myList.forEach(System.out::println)).start();
        new Thread(() -> myList.forEach(System.out::println)).start();*/

        IntStream.range(0, 10).forEach(System.out::println);
        IntStream.range(0, 10).mapToObj(e -> "a").forEach(System.out::println);

    }
}

