package com.korotkov.models.herbivores;

import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.abstracts.Insect;

public class Caterpillar extends Herbivore implements Insect {
    public Caterpillar(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Caterpillar(Entity entity) {
        super(entity);
    }

    private Caterpillar() {
        super(0.0, 0, 0, 0.0);
    }

    @Override
    public void setSpeed(Integer speed) {
        this.speed = 0;
    }

    @Override
    public void setKgToGetFull(Double kgToGetFull) {
        super.setKgToGetFull(0.0);
    }

    @Override
    public void setMaxCountOnField(Integer maxCountOnField) {
        if (maxCountOnField > 0 && maxCountOnField <= 1100) this.maxCountOnField = maxCountOnField;
    }
}
