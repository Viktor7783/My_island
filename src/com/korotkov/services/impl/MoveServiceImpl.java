package com.korotkov.services.impl;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;
import com.korotkov.services.MoveService;

public class MoveServiceImpl implements MoveService {
    private Island island;

    public MoveServiceImpl(Island island) {
        this.island = island;
    }

    @Override
    public void move(Animal movingAnimal, Field from, DirectionType direction, int speed) { //Добавить животное???
        // 0.0 -> RIGHT, DOWN (Проверяем, если мы в 0.0 -> то двигаться можем только вправо или вниз
        // TODO: проверить - а сколько там еще таких же животных и можно ли в эту клетку добавить ещё одного?
        // TODO: move entity from one field to another field

    }
}
