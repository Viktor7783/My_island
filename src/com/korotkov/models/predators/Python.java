package com.korotkov.models.predators;

public class Python extends Predator {
    public Python(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Python() {
        super(null, null, null, null);
    }
}
