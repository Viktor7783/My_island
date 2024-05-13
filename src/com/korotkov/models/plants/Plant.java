package com.korotkov.models.plants;

import com.korotkov.models.abstracts.Entity;

public abstract class Plant extends Entity {
    public Plant(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Plant(Entity entity) {
        super(entity);
    }
}
