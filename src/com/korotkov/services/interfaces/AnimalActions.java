package com.korotkov.services.interfaces;

import com.korotkov.models.abstracts.Entity;

public interface AnimalActions {

    void eat(Entity entity);

    void reproduce();

    void chooseDirection();
}
