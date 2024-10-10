package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;

public class Deer extends Herbivore {
    public Deer(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Deer(Entity entity) {
        super(entity);
    }

    private Deer() {
        super(null, null, null, null);
    }
}
