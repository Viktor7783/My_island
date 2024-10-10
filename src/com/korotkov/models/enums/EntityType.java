package com.korotkov.models.enums;

import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.herbivores.*;
import com.korotkov.models.plants.Grass;
import com.korotkov.models.predators.*;

public enum EntityType {
    WOLF("wolf", Wolf.class),
    PYTHON("python", Python.class),
    FOX("fox", Fox.class),
    BEAR("bear", Bear.class),
    EAGLE("eagle", Eagle.class),
    HORSE("horse", Horse.class),
    DEER("deer", Deer.class),
    RABBIT("rabbit", Rabbit.class),
    MOUSE("mouse", Mouse.class),
    GOAT("goat", Goat.class),
    SHEEP("sheep", Sheep.class),
    HOG("hog", Hog.class),
    BULL("bull", Bull.class),
    DUCK("duck", Duck.class),
    CATERPILLAR("caterpillar", Caterpillar.class),
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
