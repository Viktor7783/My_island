package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.plants.Plant;

public class Hog extends Herbivore {
    public Hog(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Hog(Entity entity) {
        super(entity);
    }

    private Hog() {
        super(null, null, null, null);
    }
}
