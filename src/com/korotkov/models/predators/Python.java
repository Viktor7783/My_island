package com.korotkov.models.predators;

import com.korotkov.models.abstracts.Entity;

public class Python extends Predator {
    public Python(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Python(Entity entity) {
        super(entity);
    }

    private Python() {
        super(null, null, null, null);
    }
}
