package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;

public class Sheep extends Herbivore{
    public Sheep(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Sheep(Entity entity) {
        super(entity);
    }
    private Sheep() {
        super(null, null, null, null);
    }
}
