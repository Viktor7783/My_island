package com.korotkov.models.abstracts;

import com.korotkov.services.AnimalActions;

public abstract class Animal extends Entity implements AnimalActions {

    private boolean isMovedInThisLap; // TODO: если животное уже двигалось в этот ход (круг) - то двигать его не нужно

    public Animal(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    @Override
    public void move() {
        System.out.println("Animal move");
    }

    @Override
    public void eat() {
        System.out.println("Animal eat");
    }

    @Override
    public void reproduce() {
        System.out.println("Animal reproduce");
    }

    @Override
    public void chooseDirection() {
        System.out.println("Animal choose direction");
    }
}
