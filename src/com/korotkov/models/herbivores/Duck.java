package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.plants.Plant;

public class Duck extends Herbivore {
    public Duck(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Duck(Entity entity) {
        super(entity);
    }

    private Duck() {
        super(null, null, null, null);
    }
}
