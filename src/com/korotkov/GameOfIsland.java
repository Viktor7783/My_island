package com.korotkov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korotkov.config.AnimalConfig;
import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.config.IslandConfig;
import com.korotkov.config.PossibilityOfEatingConfig;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.EntityType;
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;
import com.korotkov.multithreading.*;
import com.korotkov.services.impl.CollectAndDisplayStatisticsServiceImpl;
import com.korotkov.config.ImagesOfEntitiesConfig;
import com.korotkov.services.interfaces.MoveService;
import com.korotkov.services.impl.MoveServiceImpl;
import com.korotkov.services.impl.UpdateSettingsService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    private final ImagesOfEntitiesConfig imagesOfEntitiesConfig;
    private final PossibilityOfEatingConfig possibilityOfEatingConfig;
    private final IslandConfig islandConfig;
    private final AnimalConfig animalConfig;
    private final UpdateSettingsService updateSettingsService;
    private Island island;
    private MoveService moveService;
    private CollectAndDisplayStatisticsServiceImpl collectAndDisplayStatisticsService;
    private final DailyActivities dailyActivities;
    private final ExecutorService executor;
    private final BufferedReader reader;
    private ExecutorService shutdownExecutor;
    private final PauseMenu pauseMenu;
    private final RestoreAnimals restoreAnimals;
    private final RestorePlants restorePlants;
    private final AnimalsLife animalsLife;
    private final CollectStatistics collectStatistics;
    private final ShowStatistics showStatistics;


    public GameOfIsland() {
        ObjectMapper objectMapper = new ObjectMapper();
        random = new Random();
        entityCharacteristicConfig = new EntityCharacteristicConfig(objectMapper, PATH_TO_ENTITY_CHARACTERISTIC);
        imagesOfEntitiesConfig = new ImagesOfEntitiesConfig(objectMapper, PATH_TO_IMAGES_OF_ENTITIES);
        possibilityOfEatingConfig = new PossibilityOfEatingConfig(objectMapper, PATH_TO_POSSIBILITY_OF_EATING, entityCharacteristicConfig.getEntityMapConfig());
        islandConfig = new IslandConfig(PATH_TO_ISLAND_SETTINGS);
        animalConfig = new AnimalConfig(PATH_TO_ISLAND_SETTINGS);
        dailyActivities = new DailyActivities();
        reader = new BufferedReader(new InputStreamReader(System.in));
        updateSettingsService = new UpdateSettingsService(islandConfig, entityCharacteristicConfig, reader);
        executor = Executors.newCachedThreadPool();
        pauseMenu = new PauseMenu(this);
        restoreAnimals = new RestoreAnimals(this);
        restorePlants = new RestorePlants(this);
        animalsLife = new AnimalsLife(this);
        collectStatistics = new CollectStatistics(this);
        showStatistics = new ShowStatistics(this);
    }

    public static void main(String[] args) {
        new GameOfIsland().start();
    }

    public void start() {
        executor.execute(pauseMenu);
        executor.execute(restoreAnimals);
        executor.execute(restorePlants);
        executor.execute(animalsLife);
        executor.execute(collectStatistics);
        executor.execute(showStatistics);
    }

    private void fillIslandAnimalsAndPlants(Island island, Random random, EntityCharacteristicConfig
            entityCharacteristicConfig) {
        island.getIslandCells().values()
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

    private Island createIsland(IslandConfig islandConfig) {
        Map<Field, List<Entity>> islandCell = new HashMap<>();
        for (int i = 0; i < islandConfig.getHeight(); i++) {
            for (int j = 0; j < islandConfig.getWidth(); j++) {
                Field field = new Field(i, j);
                islandCell.put(field, new ArrayList<>());
            }
        }
        return new Island(islandCell);
    }

    private void greetings() {
        System.out.println(DOLLARS);
        System.out.println("ПРИВЕТ, АНТОН!\uD83D\uDE09");
        for (char c : GREETINGS.toCharArray()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException _) {
            }
            System.out.print(c);
        }
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException _) {
        }
        System.out.println("\n" + DOLLARS);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException _) {
        }
    }

    public void initializeIsland() {
        greetings();
        updateSettingsService.updateSettings();
        System.out.println(GO_GO_GO);
        island = createIsland(islandConfig);
        fillIslandAnimalsAndPlants(island, random, entityCharacteristicConfig);
        moveService = new MoveServiceImpl(island, islandConfig);
        collectAndDisplayStatisticsService = new CollectAndDisplayStatisticsServiceImpl(island, updateSettingsService, imagesOfEntitiesConfig);
        synchronized (dailyActivities) {
            dailyActivities.setIslandInitialized(true);
            dailyActivities.notifyAll();
        }
    }

    public Random getRandom() {
        return random;
    }

    public EntityCharacteristicConfig getEntityCharacteristicConfig() {
        return entityCharacteristicConfig;
    }

    public PossibilityOfEatingConfig getPossibilityOfEatingConfig() {
        return possibilityOfEatingConfig;
    }

    public AnimalConfig getAnimalConfig() {
        return animalConfig;
    }

    public UpdateSettingsService getUpdateSettingsService() {
        return updateSettingsService;
    }

    public Island getIsland() {
        return island;
    }

    public MoveService getMoveService() {
        return moveService;
    }

    public CollectAndDisplayStatisticsServiceImpl getCollectAndDisplayStatisticsService() {
        return collectAndDisplayStatisticsService;
    }

    public DailyActivities getDailyActivities() {
        return dailyActivities;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public ExecutorService getShutdownExecutor() {
        return shutdownExecutor;
    }

    public void setShutdownExecutor(ExecutorService shutdownExecutor) {
        this.shutdownExecutor = shutdownExecutor;
    }
}


