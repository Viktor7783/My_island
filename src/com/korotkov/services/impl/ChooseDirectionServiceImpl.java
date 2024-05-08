package com.korotkov.services.impl;

import com.korotkov.models.enums.DirectionType;

import java.util.Random;

public class ChooseDirectionServiceImpl {
    private final Random random;

    public ChooseDirectionServiceImpl(Random random) {
        this.random = random;
    }

    public DirectionType chooseDirection() {
        int direction = random.nextInt(DirectionType.values().length);
        return DirectionType.values()[direction];
    }
}
