package com.korotkov.models.predators;

public class Fox extends Predator {
    public Fox(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Fox() {
        super(null, null, null, null);
    }
}
