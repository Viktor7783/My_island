package com.korotkov.models.herbivores;

public class Hog extends Herbivore {
    public Hog(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Hog() {
        super(null, null, null, null);
    }
}
