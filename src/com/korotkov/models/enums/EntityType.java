package com.korotkov.models.enums;

import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.herbivores.Mouse;
import com.korotkov.models.plants.Grass;
import com.korotkov.models.predators.Wolf;

public enum EntityType {
    WOLF("wolf", Wolf.class),
    MOUSE("mouse", Mouse.class),
    GRASS("grass", Grass.class);


    private String type;
    private Class clazz;

    EntityType(String type, Class clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public String getType() {
        return type;
    }

    public Class getClazz() {
        return clazz;
    }
}
