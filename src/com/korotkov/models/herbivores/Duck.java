package com.korotkov.models.herbivores;

public class Duck extends Herbivore {
    public Duck(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Duck() {
        super(null, null, null, null);
    }
}
