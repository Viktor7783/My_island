package com.korotkov.services.impl;

import com.korotkov.config.IslandConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;
import com.korotkov.services.interfaces.MoveService;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class MoveServiceImpl implements MoveService {
    private final Island island;
    private final IslandConfig islandConfig;

    public MoveServiceImpl(Island island, IslandConfig islandConfig) {
        this.island = island;
        this.islandConfig = islandConfig;
    }

    @Override
    public void move(Animal movingAnimal, int animalSpeed, Field fromField, DirectionType directionType, ListIterator<Entity> listIteratorFromRemove) {
        if (animalSpeed < 1 || movingAnimal.getHealthPercent() <= 0) return; //А вдруг скорость = 0 или его слопали
        --animalSpeed;
        if (!movingAnimal.isMovedInThisLap()) movingAnimal.setMovedInThisLap(true);
        List<Entity> fromEntities = island.getIsland().get(fromField);
        Optional<Field> optionalField = getFieldToMove(fromField, directionType); // Поле куда будем ходить
        if (optionalField.isPresent()) { //Если есть куда ходить то ходим!!! Или стоим на той же клетке
            List<Entity> entitiesToMove = island.getIsland().get(optionalField.get());
            int countAnimals = (int) entitiesToMove.stream().filter(e -> e.getClass() == movingAnimal.getClass() && ((Animal) e).getHealthPercent() > 0).count();
            if (countAnimals < movingAnimal.getMaxCountOnField()) { // Если животных не max - то добавляем животное на новую локацию
                synchronized (fromEntities) {
                    listIteratorFromRemove.remove(); // Удаляем Animal с данной локации по которой итерируемся!
                }
                synchronized (entitiesToMove) {
                    entitiesToMove.add(movingAnimal); // Добавляем Animal на новую локацию
                }
                //System.out.println("Животное " + movingAnimal + " переместили с поля: " + fromField + " на поле: " + optionalField.get());
                if (animalSpeed > 0)
                    repeatMove(movingAnimal, animalSpeed, optionalField.get(), directionType);
            }//Или стоим на месте в этот кон
        }
    }

    private void repeatMove(Animal movingAnimal, int animalSpeed, Field fromField, DirectionType directionType) {
        if (animalSpeed < 1 || movingAnimal.getHealthPercent() <= 0) return; //А вдруг скорость = 0 или его слопали
        --animalSpeed;
        //if (!movingAnimal.isMovedInThisLap()) movingAnimal.setMovedInThisLap(true);
        List<Entity> fromEntities = island.getIsland().get(fromField);
        Optional<Field> optionalField = getFieldToMove(fromField, directionType); // Поле куда будем ходить
        if (optionalField.isPresent()) { //Если есть куда ходить то ходим!!! Или стоим на той же клетке
            List<Entity> entitiesToMove = island.getIsland().get(optionalField.get());
            int countAnimals = (int) entitiesToMove.stream().filter(e -> e.getClass() == movingAnimal.getClass() && ((Animal) e).getHealthPercent() > 0).count();
            if (countAnimals < movingAnimal.getMaxCountOnField()) { // Если животных не max - то добавляем животное на новую локацию
                synchronized (fromEntities) {
                    fromEntities.remove(movingAnimal); // Удаляем Animal с данной локации
                }
                synchronized (entitiesToMove) {
                    entitiesToMove.add(movingAnimal); // Добавляем Animal на новую локацию
                }
                //System.out.println("Животное " + movingAnimal + " переместили с поля: " + fromField + " на поле: " + optionalField.get());
                if (animalSpeed > 0)
                    repeatMove(movingAnimal, animalSpeed, optionalField.get(), directionType);
            }//Или стоим на месте в этот кон
        }
    }

    private Optional<Field> getFieldToMove(Field fromField, DirectionType directionType) {
        Optional<Field> optionalField = Optional.empty();
        switch (directionType) {
            case LEFT -> {
                if (fromField.getY() != 0)
                    optionalField = island.getIsland().keySet().stream().filter(field -> field.getX() == fromField.getX() && field.getY() == fromField.getY() - 1).findFirst();

            }
            case RIGHT -> {
                if (fromField.getY() != islandConfig.getWidth() - 1)
                    optionalField = island.getIsland().keySet().stream().filter(field -> field.getX() == fromField.getX() && field.getY() == fromField.getY() + 1).findFirst();

            }
            case UP -> {
                if (fromField.getX() != 0)
                    optionalField = island.getIsland().keySet().stream().filter(field -> field.getY() == fromField.getY() && field.getX() == fromField.getX() - 1).findFirst();

            }
            case DOWN -> {
                if (fromField.getX() != islandConfig.getHeight() - 1)
                    optionalField = island.getIsland().keySet().stream().filter(field -> field.getY() == fromField.getY() && field.getX() == fromField.getX() + 1).findFirst();
            }
        }
        return optionalField;
    }
}
