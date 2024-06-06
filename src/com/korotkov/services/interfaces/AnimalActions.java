package com.korotkov.services.interfaces;

import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;

public interface AnimalActions {

    void eat(Entity entity);

    Animal reproduce(Animal animal);

    void chooseDirection();
}
