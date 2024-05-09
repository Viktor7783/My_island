package com.korotkov.models.herbivores;

public class Deer extends Herbivore {
    public Deer(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Deer() {
        super(null, null, null, null);
    }
}
