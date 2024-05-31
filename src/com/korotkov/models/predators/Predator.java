package com.korotkov.models.predators;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;

public abstract class Predator extends Animal {
    public Predator(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Predator(Entity entity) {
        super(entity);
    }

    @Override
    public void setMaxCountOnField(Integer maxCountOnField) {
        if (maxCountOnField > 0 && maxCountOnField <= 35) this.maxCountOnField = maxCountOnField;
    }

    @Override
    public void eat(Entity entity) {

    }
}
