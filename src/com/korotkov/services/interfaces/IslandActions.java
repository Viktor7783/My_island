package com.korotkov.services.interfaces;

import com.korotkov.config.EntityCharacteristicConfig;

import java.util.Random;

public interface IslandActions {
    void removeDeathAnimal();

    void removeEatenPlants();

    void refillPlants(EntityCharacteristicConfig entityCharacteristicConfig, Random random);
}
