package com.korotkov.models.enums;

import java.util.Random;

public enum Action { //Используем для выбора действия за один кон для каждого животного
    MOVE,
    EAT,
    REPRODUCE;

    public static Action getRandomAction(Random random) {
        var number = random.nextInt(values().length);
        return values()[number];
    }
}
