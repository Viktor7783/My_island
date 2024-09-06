package com.korotkov.models.island;

import com.korotkov.config.AnimalConfig;
import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.EntityType;
import com.korotkov.models.plants.Grass;
import com.korotkov.models.plants.Plant;
import com.korotkov.services.interfaces.IslandActions;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class Island implements IslandActions {
    private final Map<Field, List<Entity>> island;

    public Map<Field, List<Entity>> getIsland() {
        return island;
    }

    public Island(Map<Field, List<Entity>> island) {
        this.island = island;
    }

    @Override
    public void removeAndRestoreAnimals() {// Сначала убираем мертвечину - потом ресторим показатели
        island.values().forEach(list -> {
            synchronized (list) {
                list.removeIf(entity -> entity instanceof Animal && ((Animal) entity).getHealthPercent() <= 0);
                list.stream().filter(entity -> entity instanceof Animal)
                        .map(entity -> (Animal) entity)
                        .forEach(animal -> {
                            if (animal.isBornNewAnimal()) animal.setBornNewAnimal(false);
                            if (animal.isEatInThisLap()) animal.setEatInThisLap(false);
                            if (animal.isMovedInThisLap()) animal.setMovedInThisLap(false);
                        });
            }
        });
    }

    @Override
    public void decreaseAnimalsHealthIfNotEat(AnimalConfig animalConfig) { //todo: synchronized
        island.values().forEach(list -> list.stream().filter(entity -> entity instanceof Animal)
                .map(entity -> (Animal) entity)
                .filter(animal -> animal.getHealthPercent() > 0 && !animal.isEatInThisLap())
                .forEach(animal -> animal.decreaseHealthPercent(animalConfig.getPercentsToRemove())));

    }

    /*@Override
    public void restoreEatMoveBornState() {
        island.values().forEach(list -> list.stream().filter(entity -> entity instanceof Animal)
                .map(entity -> (Animal) entity)
                .forEach(animal -> {
                    if (animal.isBornNewAnimal()) animal.setBornNewAnimal(false);
                    if (animal.isEatInThisLap()) animal.setEatInThisLap(false);
                    if (animal.isMovedInThisLap()) animal.setMovedInThisLap(false);
                }));
    }*/


    @Override
    public void removeEatenPlants() {
        //island.values().forEach(list -> list.removeIf(entity -> entity instanceof Plant && ((Plant) entity).isEaten()));
        /*island.values().forEach(list -> {
            synchronized (list) {
                list.removeIf(entity -> entity instanceof Plant && ((Plant) entity).isEaten());
            }
        });*/
        List<List<Entity>> listOfEntityLists = island.values().stream().toList();
        for (int i = listOfEntityLists.size() - 1; i >= 0; i--) {
            synchronized (listOfEntityLists.get(i)) {
                listOfEntityLists.get(i).removeIf(entity -> entity instanceof Plant && ((Plant) entity).isEaten());
            }
        }
    }

    @Override
    public void refillPlants(EntityCharacteristicConfig entityCharacteristicConfig, Random random) {
        int maxCountOfPlantsInOneField = entityCharacteristicConfig.getEntityMapConfig().get(EntityType.GRASS).getMaxCountOnField();
        island.values().forEach(list -> {
            synchronized (list) {
                int totalCountOfPlants = (int) list.stream().filter(e -> e instanceof Plant).count();
                if (totalCountOfPlants < maxCountOfPlantsInOneField / 3) {
                    list.addAll(IntStream.range(0, random.nextInt(maxCountOfPlantsInOneField / 3 - totalCountOfPlants, maxCountOfPlantsInOneField - totalCountOfPlants))
                            .mapToObj(_ -> new Grass(entityCharacteristicConfig.getEntityMapConfig().get(EntityType.GRASS)))
                            .toList());
                }
            }
        });

    }
}
