package com.korotkov.models.herbivores;

public class Horse extends Herbivore {
    public Horse(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Horse() {
        super(null, null, null, null);
    }
}
