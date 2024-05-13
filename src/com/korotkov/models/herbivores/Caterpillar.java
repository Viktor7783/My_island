package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;

public class Caterpillar extends Herbivore {
    public Caterpillar(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Caterpillar(Entity entity) {
        super(entity);
    }

    private Caterpillar() {
        super(null, null, null, null);
    }
}
