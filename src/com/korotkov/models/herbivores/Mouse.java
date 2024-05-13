package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;

public class Mouse extends Herbivore {
    public Mouse(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }
    public Mouse(Entity entity) {
        super(entity);
    }

    private Mouse() {
        super(null, null, null, null);
    }
}
