package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;

public class Horse extends Herbivore {
    public Horse(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Horse(Entity entity) {
        super(entity);
    }

    private Horse() {
        super(null, null, null, null);
    }
}
