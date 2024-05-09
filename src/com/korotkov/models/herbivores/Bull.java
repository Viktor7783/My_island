package com.korotkov.models.herbivores;

public class Bull extends Herbivore {
    public Bull(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Bull() {
        super(null, null, null, null);
    }
}
