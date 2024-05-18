package com.korotkov.services;

import com.korotkov.config.EntityCharacteristicConfig;

import java.util.Random;

public interface IslandActions {
    void removeDeathAnimal();

    void refillPlants(EntityCharacteristicConfig entityCharacteristicConfig, Random random);
}
