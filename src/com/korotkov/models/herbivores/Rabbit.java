package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;

public class Rabbit extends Herbivore {

    public Rabbit(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }
    public Rabbit(Entity entity) {
        super(entity);
    }

    private Rabbit() {
        super(null, null, null, null);
    }
}
