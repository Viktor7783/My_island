package com.korotkov.models.herbivores;

public class Mouse extends Herbivore {
    public Mouse(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
    }

    private Mouse() {
        super(null, null, null, null);

    }
}
