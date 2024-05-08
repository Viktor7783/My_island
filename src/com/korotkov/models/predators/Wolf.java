package com.korotkov.models.predators;

import com.korotkov.models.abstracts.Entity;

public class Wolf extends Predator {
    public Wolf(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Wolf(Entity entity) {
        super(entity.getWeight(), entity.getMaxCountOnField(), entity.getSpeed(), entity.getKgToGetFull());
    }

    private Wolf() {
        super(null, null, null, null);
    }
}
