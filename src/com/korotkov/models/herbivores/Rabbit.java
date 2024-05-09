package com.korotkov.models.herbivores;

public class Rabbit extends Herbivore {

    public Rabbit(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Rabbit() {
        super(null, null, null, null);
    }
}
