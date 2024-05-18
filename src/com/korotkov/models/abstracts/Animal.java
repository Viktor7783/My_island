package com.korotkov.models.abstracts;

import com.korotkov.services.AnimalActions;

public abstract class Animal extends Entity implements AnimalActions {

    private int healthPercent;
    private boolean isMovedInThisLap; // TODO: если животное уже двигалось в этот ход (круг) - то двигать его не нужно
    private boolean isBornNewAnimal;

    public Animal(Double weight, Integer maxCountOnField, Integer speed, Double kgToGetFull) {
        super(weight, maxCountOnField, speed, kgToGetFull);
        healthPercent = 100;
        isMovedInThisLap = false;
    }

    public Animal(Entity entity) {
        super(entity);
        healthPercent = 100;
        isMovedInThisLap = false;
    }

    public boolean isBornNewAnimal() {
        return isBornNewAnimal;
    }

    public void setBornNewAnimal(boolean bornNewAnimal) {
        isBornNewAnimal = bornNewAnimal;
    }

    public void setHealthPercent(int healthPercent) {
        this.healthPercent = healthPercent;
    }

    public void setMovedInThisLap(boolean movedInThisLap) {
        isMovedInThisLap = movedInThisLap;
    }

    public int getHealthPercent() {
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
   /* @Override
    public void move() {
        System.out.println("Animal move");
    }*/

    @Override
    public void eat() {
        // TODO В классах травоядного и хищника можно реализовать метод покушать.
        // Но обрати внимание, есть травоядное утка, которое ест гусеницу.
        System.out.println("Animal eat");
    }

    @Override
    public void reproduce() {
        System.out.println("Animal reproduce");
    }

    @Override
    public void chooseDirection() {
        System.out.println("Animal choose direction");
    }

    public void decreaseHealthPercent(int decreaseFor) {
        this.healthPercent -= decreaseFor;
    }
}
