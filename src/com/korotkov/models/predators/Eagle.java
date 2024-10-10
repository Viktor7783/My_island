package com.korotkov.models.predators;

import com.korotkov.models.abstracts.Entity;

public class Eagle extends Predator {
    public Eagle(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Eagle(Entity entity) {
        super(entity);
    }

    private Eagle() {
        super(null, null, null, null);
    }
}
