package com.korotkov.services.interfaces;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.models.island.Field;

import java.util.ListIterator;

public interface MoveService {
    //void move(Animal movingAnimal, Field from, DirectionType direction, int speed, ListIterator<Entity> listIteratorForRemove);
    void move(Animal movingAnimal, int animalSpeed, Field from, DirectionType directionType, ListIterator<Entity> listIteratorFromRemove);
}
