package com.korotkov.models.predators;

import com.korotkov.models.abstracts.Entity;

public class Fox extends Predator {
    public Fox(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Fox(Entity entity) {
        super(entity);
    }

    private Fox() {
        super(null, null, null, null);
    }
}
