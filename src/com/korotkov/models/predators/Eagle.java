package com.korotkov.models.predators;

public class Eagle extends Predator {
    public Eagle(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Eagle() {
        super(null, null, null, null);
    }
}
