package com.korotkov.services.interfaces;

import com.korotkov.config.AnimalConfig;
import com.korotkov.config.EntityCharacteristicConfig;

import java.util.Random;

public interface IslandActions {

    void removeAndRestoreAnimals();

    void removeEatenPlants();

    void refillPlants(EntityCharacteristicConfig entityCharacteristicConfig, Random random);

    void decreaseAnimalsHealthIfNotEat(AnimalConfig animalConfig);

}
