package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;

public class Bull extends Herbivore {
    public Bull(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Bull(Entity entity) {
        super(entity);
    }

    private Bull() {
        super(null, null, null, null);
    }
}
