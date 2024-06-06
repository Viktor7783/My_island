package com.korotkov.models.abstracts;

import com.korotkov.exceptions.NullAnimalException;
import com.korotkov.exceptions.WrongAnimalClassExceptions;
import com.korotkov.models.plants.Plant;
import com.korotkov.services.interfaces.AnimalActions;

import java.lang.reflect.InvocationTargetException;

import static com.korotkov.config.Constants.*;

public abstract class Animal extends Entity implements AnimalActions {
    private static long counter = 0;
    private final long id = ++counter;

    private double healthPercent;
    private boolean isMovedInThisLap; // TODO: если животное уже двигалось в этот ход (круг) - то двигать его не нужно
    private boolean isBornNewAnimal;
    private boolean isEatInThisLap;

    protected Animal(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
        healthPercent = 100;
        isMovedInThisLap = false;
        isBornNewAnimal = false;
        isEatInThisLap = false;
    }

    protected Animal(Entity entity) {
        super(entity);
        healthPercent = 100;
        isMovedInThisLap = false;
        isBornNewAnimal = false;
        isEatInThisLap = false;
    }

    public boolean isEatInThisLap() {
        return isEatInThisLap;
    }

    public void setEatInThisLap(boolean eatInThisLap) {
        isEatInThisLap = eatInThisLap;
    }

    public boolean isBornNewAnimal() {
        return isBornNewAnimal;
    }

    public void setBornNewAnimal(boolean bornNewAnimal) {
        isBornNewAnimal = bornNewAnimal;
    }

    public void setHealthPercent(double healthPercent) {
        this.healthPercent = healthPercent;
    }

    public void setMovedInThisLap(boolean movedInThisLap) {
        isMovedInThisLap = movedInThisLap;
    }

    public double getHealthPercent() {
        return healthPercent;
    }

    public boolean isMovedInThisLap() {
        return isMovedInThisLap;
    }

    /*Животные могут:
    есть растения и/или других животных (если в их локации есть подходящая еда),
    передвигаться (в соседние локации),
    размножаться (при наличии пары в их локации),
    умирать от голода или быть съеденными.
*/
    // TODO В конкретных классах того или иного вида можно дорабатывать все методы под особенности животного.


    @Override
    public void eat(Entity entity) {
        if (entity instanceof Plant plant) {
            increaseHealthPercent(plant);
            plant.setEaten(true);
        } else if (entity instanceof Animal animal) {
            increaseHealthPercent(animal);
            animal.setHealthPercent(0);
        }
        this.isEatInThisLap = true;
    }

    @Override
    public Animal reproduce(Animal animalForBorn) {
        Animal baby = null;
        if (animalForBorn == null) {
            try {
                throw new NullAnimalException();
            } catch (NullAnimalException e) {
                System.out.println(NULL_ANIMAL_ERROR);
            }

        } else if (this.getClass() != animalForBorn.getClass()) {
            try {
                throw new WrongAnimalClassExceptions();
            } catch (WrongAnimalClassExceptions e) {
                System.out.printf(WRONG_ANIMAL_CLASS_ERROR, this.getClass().getSimpleName(), animalForBorn.getClass().getSimpleName());
            }
        } else {
            try {
                baby = this.getClass().getDeclaredConstructor(animalForBorn.getClass()).newInstance(animalForBorn);
                this.setBornNewAnimal(true);
                animalForBorn.setBornNewAnimal(true);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                System.out.printf(REPRODUCE_ERROR, this, animalForBorn);
            }
        }
        return baby;
    }

    @Override
    public void chooseDirection() {
        System.out.println("Animal choose direction"); // Выбирают движения все животные одинаково
    }


    public void decreaseHealthPercent(int decreaseFor) { // каждый день уменьшаем на 20%
        this.healthPercent -= decreaseFor;
    }

    public void increaseHealthPercent(Entity entity) {
        this.healthPercent += (entity.getWeight() * 100 / this.kgToGetFull);
        if (this.healthPercent > 100) this.healthPercent = 100;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " id: " + id;
    }
}
