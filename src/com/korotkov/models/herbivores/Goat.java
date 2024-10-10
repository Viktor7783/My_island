package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;

public class Goat extends Herbivore {
    public Goat(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Goat(Entity entity) {
        super(entity);
    }

    private Goat() {
        super(null, null, null, null);
    }
}
