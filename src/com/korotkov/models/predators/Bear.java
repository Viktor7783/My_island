package com.korotkov.models.predators;

import com.korotkov.models.abstracts.Entity;

public class Bear extends Predator {
    public Bear(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Bear(Entity entity) {
        super(entity);
    }

    private Bear() {
        super(null, null, null, null);
    }
}
