package com.korotkov.services.interfaces;

import com.korotkov.config.PossibilityOfEatingConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.services.impl.ChooseDirectionServiceImpl;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public interface AnimalActions {

    void eat(List<Entity> entities, PossibilityOfEatingConfig possibilityOfEatingConfig, Random random);

    Animal reproduce(List<Entity> entities);

    DirectionType chooseDirection(Random random);
}
