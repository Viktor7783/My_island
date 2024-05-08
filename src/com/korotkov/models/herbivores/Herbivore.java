package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Animal;

public abstract class Herbivore extends Animal {
    public Herbivore(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }
}
