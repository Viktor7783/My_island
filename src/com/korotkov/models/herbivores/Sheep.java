package com.korotkov.models.herbivores;

public class Sheep extends Herbivore{
    public Sheep(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }
    private Sheep() {
        super(null, null, null, null);
    }
}
