package com.korotkov.models.herbivores;

public class Goat extends Herbivore {
    public Goat(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Goat() {
        super(null, null, null, null);
    }
}
