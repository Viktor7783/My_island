package com.korotkov.models.island;

import com.korotkov.config.AnimalConfig;
import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.config.IslandConfig;
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
    public void decreaseAnimalsHealthIfNotEat(AnimalConfig animalConfig) {
        island.values().forEach(list -> list.stream().filter(entity -> entity instanceof Animal)
                .map(entity -> (Animal) entity)
                .filter(animal -> animal.getHealthPercent() > 0 && !animal.isEatInThisLap())
                .forEach(animal -> animal.decreaseHealthPercent(animalConfig.getPercentsToRemove())));

    }

    @Override
    public void restoreEatMoveBornState() {
        island.values().forEach(list -> list.stream().filter(entity -> entity instanceof Animal)
                .map(entity -> (Animal) entity)
                .forEach(animal -> {
                    if (animal.isBornNewAnimal()) animal.setBornNewAnimal(false);
                    if (animal.isEatInThisLap()) animal.setEatInThisLap(false);
                    if (animal.isMovedInThisLap()) animal.setMovedInThisLap(false);
                }));
    }

    @Override
    public void removeDeathAnimal() {
        island.values().forEach(list -> list.removeIf(entity -> entity instanceof Animal && ((Animal) entity).getHealthPercent() <= 0));
    }

    @Override
    public void removeEatenPlants() {
        island.values().forEach(list -> list.removeIf(entity -> entity instanceof Plant && ((Plant) entity).isEaten()));
    }

    @Override
    public void refillPlants(EntityCharacteristicConfig entityCharacteristicConfig, Random random) {
        int maxCountOfPlantsInOneField = entityCharacteristicConfig.getEntityMapConfig().get(EntityType.GRASS).getMaxCountOnField();
        for (List<Entity> entities : island.values()) {
            int totalCountOfPlants = (int) entities.stream().filter(e -> e instanceof Plant).count();
            if (totalCountOfPlants < maxCountOfPlantsInOneField / 3) {
                entities.addAll(IntStream.range(0, random.nextInt(maxCountOfPlantsInOneField / 3 - totalCountOfPlants, maxCountOfPlantsInOneField - totalCountOfPlants))
                        .mapToObj(_ -> new Grass(entityCharacteristicConfig.getEntityMapConfig().get(EntityType.GRASS)))
                        .toList());
            }
        }
    }
}
