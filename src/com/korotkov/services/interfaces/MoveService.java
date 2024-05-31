package com.korotkov.services.interfaces;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.models.island.Field;

public interface MoveService {
    void move(Animal movingAnimal, Field from, DirectionType direction, int speed);
}
