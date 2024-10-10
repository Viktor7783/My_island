package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.abstracts.Rodent;
import com.korotkov.models.plants.Plant;

public class Mouse extends Herbivore implements Rodent {
    public Mouse(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Mouse(Entity entity) {
        super(entity);
    }

    private Mouse() {
        super(null, null, null, null);
    }

    @Override
    public void setMaxCountOnField(Integer maxCountOnField) {
        if (maxCountOnField > 0 && maxCountOnField <= 500) this.maxCountOnField = maxCountOnField;
    }
}
