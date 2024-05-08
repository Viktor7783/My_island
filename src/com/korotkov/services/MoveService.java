package com.korotkov.services;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;

public interface MoveService {
    void move(Animal movingAnimal, Field from, DirectionType direction, int speed);
}
