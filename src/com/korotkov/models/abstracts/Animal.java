package com.korotkov.models.abstracts;

import com.korotkov.config.PossibilityOfEatingConfig;
import com.korotkov.exceptions.NullAnimalException;
import com.korotkov.exceptions.WrongAnimalClassExceptions;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.models.herbivores.Herbivore;
import com.korotkov.models.plants.Plant;
import com.korotkov.services.impl.ChooseDirectionServiceImpl;
import com.korotkov.services.interfaces.AnimalActions;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static com.korotkov.config.Constants.*;

public abstract class Animal extends Entity implements AnimalActions {
    private static long counter = 0;
    private final long id = ++counter;

    private double healthPercent;
    private boolean isMovedInThisLap;
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

    @Override
    public void eat(List<Entity> entities, PossibilityOfEatingConfig possibilityOfEatingConfig, Random random) {
        eatAnimal(entities, possibilityOfEatingConfig, random);
        eatPlant(entities, random);
    }

    private void eatPlant(List<Entity> entities, Random random) {
        if (this instanceof Herbivore) {
            int countOfEatPlants = random.nextInt(1, 4);
            if (this instanceof Rodent || this instanceof Insect) countOfEatPlants = 1;
            List<Plant> plants = entities.stream().filter(e -> e instanceof Plant).map(e -> (Plant) e).filter(p -> !p.isEaten()).toList();
            for (Plant plantToEat : plants) {
                this.increaseHealthPercent(plantToEat);
                plantToEat.setEaten(true);
                if (!this.isEatInThisLap) this.isEatInThisLap = true;
                --countOfEatPlants;
                if (countOfEatPlants < 1) break;
            }
        }
    }

    private void eatAnimal(List<Entity> entities, PossibilityOfEatingConfig possibilityOfEatingConfig, Random random) {
        int randomPercent = random.nextInt(1, 101);
        List<Animal> animals = entities.stream().filter(e -> e instanceof Animal).map(e -> (Animal) e).filter(a -> a.getHealthPercent() > 0).toList();//Животные для поедания
        Set<Class<? extends Animal>> toBeEatenAnimalsClasses = animals.stream().map(Animal::getClass).collect(Collectors.toSet());//Набор классов животных для поедания
        Map<Map<Entity, Entity>, Long> animalEatPossibilities = possibilityOfEatingConfig.getPossibilityOfEating().entrySet().stream()
                .filter(pair -> {
                    Map.Entry<Entity, Entity> eatEntities = pair.getKey().entrySet().iterator().next();
                    return eatEntities.getKey().getClass() == this.getClass() && toBeEatenAnimalsClasses.contains(eatEntities.getValue().getClass()) && randomPercent <= pair.getValue();
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (!animalEatPossibilities.isEmpty()) {
            Animal bestAnimalToEat = (Animal) (animalEatPossibilities.entrySet().stream()
                    .min((pair1, pair2) -> Math.toIntExact(pair1.getValue() - pair2.getValue()))
                    .get().getKey().entrySet().iterator().next().getValue());
            int from = 1, to = 2;
            if (bestAnimalToEat instanceof Insect) {
                from = 50;
                to = 101;
            } else if (bestAnimalToEat instanceof Rodent) {
                from = 20;
                to = 51;
            }
            int countOfEatAnimals = random.nextInt(from, to);
            for (Animal animalToEat : animals) {
                if (animalToEat != this && animalToEat.getClass() == bestAnimalToEat.getClass() && animalToEat.getHealthPercent() > 0) {
                    this.increaseHealthPercent(animalToEat);
                    animalToEat.setHealthPercent(0);
                    this.isEatInThisLap = true;
                    --countOfEatAnimals;
                }
                if (countOfEatAnimals < 1) break;
            }
        }
    }

    @Override
    public Animal reproduce(List<Entity> entities) {
        Animal baby = null;
        List<Animal> currentAnimals = entities.stream().filter(e -> e.getClass() == this.getClass()).map(e -> (Animal) e).filter(a -> a.getHealthPercent() > 0).toList();
        Optional<Animal> optionalAnimal = (currentAnimals.stream().filter(a -> a != this && !a.isBornNewAnimal()).findFirst());
        if (optionalAnimal.isPresent() && !this.isBornNewAnimal()) {
            Animal animalForBorn = optionalAnimal.get();
            this.setBornNewAnimal(true); //Попытка была
            animalForBorn.setBornNewAnimal(true);
            if (currentAnimals.size() < this.getMaxCountOnField()) {
                try {
                    baby = this.getClass().getDeclaredConstructor(Entity.class).newInstance(animalForBorn);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    System.out.printf(REPRODUCE_ERROR, this, animalForBorn);
                }
            }
        }
        return baby;
    }

    @Override
    public DirectionType chooseDirection(Random random) {
        return DirectionType.values()[random.nextInt(DirectionType.values().length)];
    }


    public void decreaseHealthPercent(int decreaseFor) {
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
