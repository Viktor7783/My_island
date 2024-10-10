package com.korotkov.multithreading;

import com.korotkov.GameOfIsland;
import com.korotkov.config.AnimalConfig;
import com.korotkov.config.PossibilityOfEatingConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.Action;
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;
import com.korotkov.services.interfaces.MoveService;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

public class AnimalsLife implements Runnable {
    private final GameOfIsland game;
    private final Random random;
    private final PossibilityOfEatingConfig possibilityOfEatingConfig;
    private final AnimalConfig animalConfig;
    private final DailyActivities dailyActivities;

    public AnimalsLife(GameOfIsland game) {
        this.game = game;
        random = game.getRandom();
        possibilityOfEatingConfig = game.getPossibilityOfEatingConfig();
        animalConfig = game.getAnimalConfig();
        dailyActivities = game.getDailyActivities();
    }

    @Override
    public void run() {
        try {
            synchronized (dailyActivities) {
                while (!dailyActivities.isIslandInitialized()) {
                    dailyActivities.wait();
                }
            }
            Island island = game.getIsland();
            MoveService moveService = game.getMoveService();
            while (!Thread.interrupted()) {
                synchronized (dailyActivities) {
                    while (!dailyActivities.isTimeToAnimalActions() || dailyActivities.isPressPause()) {
                        dailyActivities.wait();
                    }
                }
                for (Map.Entry<Field, List<Entity>> fieldListEntry : island.getIslandCells().entrySet()) {
                    Field field = fieldListEntry.getKey();
                    List<Entity> entities = fieldListEntry.getValue();
                    ListIterator<Entity> entityListIterator = entities.listIterator();
                    while (entityListIterator.hasNext()) {
                        Entity entity = entityListIterator.next();
                        if (entity instanceof Animal animal) {
                            if (!animal.isBornNewAnimal() && !animal.isMovedInThisLap() && animal.getHealthPercent() > 0) {
                                Action action = Action.values()[random.nextInt(Action.values().length)];
                                switch (action) {
                                    case MOVE -> {
                                        if (animal.getSpeed() > 0)
                                            moveService.move(animal, random.nextInt(1, animal.getSpeed() + 1), field, animal.chooseDirection(random), entityListIterator);
                                    }
                                    case EAT -> animal.eat(entities, possibilityOfEatingConfig, random);
                                    case REPRODUCE -> {
                                        Animal baby = animal.reproduce(entities);
                                        if (baby != null) entityListIterator.add(baby);
                                    }
                                }
                            }
                        }
                    }
                }
                island.decreaseAnimalsHealthIfNotEat(animalConfig);
                synchronized (dailyActivities) {
                    dailyActivities.setAnimalActionsCompleted(true);
                    dailyActivities.notifyAll();
                }
            }
        } catch (InterruptedException _) {
        }
    }
}
