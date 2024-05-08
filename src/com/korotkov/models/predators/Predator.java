package com.korotkov.models.predators;

import com.korotkov.models.abstracts.Animal;

public abstract class Predator extends Animal {
    public Predator(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }
}
