package com.korotkov.models.plants;

import com.korotkov.models.abstracts.Entity;

public abstract class Plant extends Entity {
    private boolean isEaten;

    public Plant(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    public Plant(Entity entity) {
        super(entity);
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }

    @Override
    public void setMaxCountOnField(Integer maxCountOnField) {
        if (maxCountOnField > 0 && maxCountOnField <= 200) this.maxCountOnField = maxCountOnField;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
