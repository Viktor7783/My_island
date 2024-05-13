package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;

public abstract class Herbivore extends Animal {
    public Herbivore(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Herbivore(Entity entity) {
        super(entity);
    }
}
