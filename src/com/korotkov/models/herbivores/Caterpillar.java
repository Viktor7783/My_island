package com.korotkov.models.herbivores;

public class Caterpillar extends Herbivore {
    public Caterpillar(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Caterpillar() {
        super(null, null, null, null);
    }
}
