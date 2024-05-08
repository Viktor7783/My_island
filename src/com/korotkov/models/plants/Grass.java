package com.korotkov.models.plants;

import com.korotkov.models.abstracts.Entity;

public class Grass extends Plant {
    public Grass(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Grass(Entity entity) {
        super(entity.getWeight(), entity.getMaxCountOnField(), entity.getSpeed(), entity.getKgToGetFull());
    }

    private Grass() {
        super(null, null, null, null);
    }
}
