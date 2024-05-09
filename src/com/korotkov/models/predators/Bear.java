package com.korotkov.models.predators;

public class Bear extends Predator {
    public Bear(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Bear() {
        super(null, null, null, null);
    }
}
