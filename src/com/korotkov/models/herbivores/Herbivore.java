package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.plants.Plant;

public abstract class Herbivore extends Animal {
    public Herbivore(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Herbivore(Entity entity) {
        super(entity);
    }

    @Override
    public void setMaxCountOnField(Integer maxCountOnField) {
        if (maxCountOnField > 0 && maxCountOnField <= 200) this.maxCountOnField = maxCountOnField;
    }

}
