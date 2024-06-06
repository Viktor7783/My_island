package com.korotkov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korotkov.config.AnimalConfig;
import com.korotkov.config.EntityCharacteristicConfig;
import com.korotkov.config.IslandConfig;
import com.korotkov.config.PossibilityOfEatingConfig;
import com.korotkov.models.abstracts.Animal;
import com.korotkov.models.abstracts.Entity;
import com.korotkov.models.abstracts.Insect;
import com.korotkov.models.abstracts.Rodent;
import com.korotkov.models.enums.Action;
import com.korotkov.models.enums.DirectionType;
import com.korotkov.models.enums.EntityType;
import com.korotkov.models.herbivores.*;
import com.korotkov.models.island.Field;
import com.korotkov.models.island.Island;
import com.korotkov.models.plants.Plant;
import com.korotkov.services.interfaces.MoveService;
import com.korotkov.services.impl.ChooseDirectionServiceImpl;
import com.korotkov.services.impl.MoveServiceImpl;
import com.korotkov.services.impl.UpdateSettingsService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.korotkov.config.Constants.*;

public class Main {


    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();
        EntityCharacteristicConfig entityCharacteristicConfig = new EntityCharacteristicConfig(objectMapper, PATH_TO_ENTITY_CHARACTERISTIC);
        PossibilityOfEatingConfig possibilityOfEatingConfig = new PossibilityOfEatingConfig(objectMapper, PATH_TO_POSSIBILITY_OF_EATING, entityCharacteristicConfig.getEntityMapConfig());
        IslandConfig islandConfig = new IslandConfig(PATH_TO_ISLAND_SETTINGS);
        AnimalConfig animalConfig = new AnimalConfig(PATH_TO_ISLAND_SETTINGS);
        ChooseDirectionServiceImpl chooseDirectionServiceImpl = new ChooseDirectionServiceImpl(random);
        UpdateSettingsService updateSettingsService = new UpdateSettingsService(islandConfig, entityCharacteristicConfig);
        updateSettingsService.updateSettings();
        System.out.println(GO_GO_GO);
        Island island = createIsland(islandConfig);
        fillIslandAnimalsAndPlants(island, random, entityCharacteristicConfig); //Заполняем остров животными и растениями
        MoveService moveService = new MoveServiceImpl(island);
        int daysOfLive = islandConfig.getDaysOfLife();
        //Цикл начинается здесь!!! (несколько конов)
        while (daysOfLive > 0) { //TODO: Вынести в один метод (жизнь животных и растений) Или пока все животные не умрут?!?!
            island.removeDeathAnimal(); // Удаляем мертвечину (Те животные у кого health<=0)
            island.removeEatenPlants(); // Удаляем пожранные растения (isEaten = true)
            island.refillPlants(entityCharacteristicConfig, random);// Подсаживаем растения за один кон
            island.restoreEatAndBornState(); //обнуляем eat (если ел) и isBornNewAnimal() в начале кона!!!

            for (Map.Entry<Field, List<Entity>> fieldListEntry : island.getIsland().entrySet()) {
                Field field = fieldListEntry.getKey();
                List<Entity> entities = fieldListEntry.getValue();
                List<Animal> animals = entities.stream().filter(entity -> entity instanceof Animal && ((Animal) entity).getHealthPercent() > 0).map(e -> (Animal) e).toList();
                List<Plant> plants = entities.stream().filter(entity -> entity instanceof Plant && !((Plant) entity).isEaten()).map(e -> (Plant) e).toList();
                for (Animal animal : animals) {
                    if (animal.isBornNewAnimal()) continue;
                    Action action = Action.values()[random.nextInt(Action.values().length)];
                    switch (action) {
                        case MOVE -> //TODO: иди придумай как конкретно мы будем двигаться!?!
                                moveAnimal(animal, chooseDirectionServiceImpl, entityCharacteristicConfig, moveService, field);
                        case EAT -> feedAnimal(animal, random, animals, possibilityOfEatingConfig, plants);
                        case REPRODUCE -> reproduceAnimal(entities, animal, animals);
                    }
                }
            }

            island.decreaseAnimalsHealthIfNotEat(animalConfig); //уменьшаем здоровье непоевшим в конце кона! через island!
            --daysOfLive; // День прошёл

            //TODO Вывод статистики в конце каждого кона (Сбор статистики идет для всех животных + трава)
            // Каждый ход собирается статистика:
            // сколько животных осталось на текущий момент и травы ( если все подохли - конец циклу 365 дней)
            // сколько животных умерло от голода/были съедены
            // сколько родилось новых животных
            // разница между первым ходом и текущим по кол-ву животных

        }
        System.out.println(island); // DELETE
    }

    private static void reproduceAnimal(List<Entity> entities, Animal animal, List<Animal> animals) {
        List<Animal> currentAnimals = animals.stream().filter(a -> a.getClass() == animal.getClass() && a.getHealthPercent() > 0).toList();
        Optional<Animal> optionalAnimal = (currentAnimals.stream().filter(a -> a != animal && !a.isBornNewAnimal()).findFirst());
        if (optionalAnimal.isPresent() && !animal.isBornNewAnimal()) {
            Animal baby = animal.reproduce(optionalAnimal.get()); // Если не null - то попытка родить получилась!
            if (currentAnimals.size() < animal.getMaxCountOnField() && baby != null) {
                entities.add(baby);// Если добавили животное - то успешно родили ребенка!!!
            }
        }
    }

    // TODO: Продумать за что отвечает island а за что отвечает Game_Of_Island

    private static void fillIslandAnimalsAndPlants(Island island, Random random, EntityCharacteristicConfig entityCharacteristicConfig) {
        island.getIsland().values()
                .forEach(list -> List.of(EntityType.values())
                        .forEach(currentEntityType -> IntStream.range(0, random.nextInt(getMaxCountOnField(entityCharacteristicConfig, currentEntityType)))
                                .forEach(_ -> list.add(createCurrentEntity(entityCharacteristicConfig, currentEntityType)))));
    }

    private static void feedAnimal(Animal animal, Random random, List<Animal> animals, PossibilityOfEatingConfig possibilityOfEatingConfig, List<Plant> plants) {
        int randomPercent = random.nextInt(1, 101); //выпало 10: проверить что можнор съесть! Число 0 -нельзя съесть!
        Set<Class<? extends Animal>> toBeEatenAnimalsClasses = animals.stream().map(Animal::getClass).collect(Collectors.toSet());//Набор классов животных для поедания
        Map<Map<Entity, Entity>, Long> animalEatPossibilities = possibilityOfEatingConfig.getPossibilityOfEating().entrySet().stream()
                .filter(pair -> {
                    Map.Entry<Entity, Entity> entities = pair.getKey().entrySet().iterator().next();
                    return entities.getKey().getClass() == animal.getClass() && entities.getValue() instanceof Animal && pair.getValue() > 0 && randomPercent <= pair.getValue() && toBeEatenAnimalsClasses.contains(entities.getValue().getClass());
                }) //Фильтруем пары с нашим хищником и жертвой и возможностью поедания > 0
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (!animalEatPossibilities.isEmpty()) { // Если Кабан и есть мышь и  гусеница покушать:
            Animal bestAnimalToEat = (Animal) (animalEatPossibilities.entrySet().stream()
                    .min((pair1, pair2) -> Math.toIntExact(pair1.getValue() - pair2.getValue()))
                    .get().getKey().entrySet().iterator().next().getValue());// Нашли гусеницу или мышь
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
                if (animalToEat != animal && animalToEat.getClass() == bestAnimalToEat.getClass() && animalToEat.getHealthPercent() > 0) {
                    animal.eat(animalToEat);//Хищник съел одно животное
                    --countOfEatAnimals;
                }
                if (countOfEatAnimals < 1) break;
            }
        }
        if (animal instanceof Herbivore) {
            int countOfEatPlants = random.nextInt(1, 4);
            for (Plant plantToEat : plants) {
                if (!plantToEat.isEaten()) {
                    animal.eat(plantToEat);
                    --countOfEatPlants;
                }
                if (countOfEatPlants < 1) break;
            }
        }
    }

    private static void moveAnimal(Animal animal, ChooseDirectionServiceImpl chooseDirectionServiceImpl, EntityCharacteristicConfig entityCharacteristicConfig, MoveService moveService, Field field) {
        DirectionType directionToMove = chooseDirectionServiceImpl.chooseDirection();
        int speed = getSpeed(entityCharacteristicConfig, Arrays.stream(EntityType.values())
                .filter(entityType -> entityType.getClazz() == animal.getClass()).
                findFirst().get());
        moveService.move(animal, field, directionToMove, speed);
    }


    private static Entity createCurrentEntity(EntityCharacteristicConfig entityCharacteristicConfig, EntityType currentEntityType) {
        Entity currentEntity = null;
        Class clazz = currentEntityType.getClazz();
        Constructor constructor = null;
        try {
            constructor = clazz.getDeclaredConstructor(Entity.class);
        } catch (NoSuchMethodException e) {
            System.out.printf(GET_CONSTRUCTOR_ERROR, clazz);
            System.exit(0);
        }
        try {
            currentEntity = (Entity) constructor.newInstance(entityCharacteristicConfig.getEntityMapConfig().get(currentEntityType));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            System.out.printf(CREATE_CURRENT_ENTITY_ERROR, currentEntityType.getType());
        }
        return currentEntity;
    }


    private static Integer getMaxCountOnField(EntityCharacteristicConfig entityCharacteristicConfig, EntityType entityType) {
        return entityCharacteristicConfig.getEntityMapConfig().get(entityType).getMaxCountOnField();
    }

    private static Integer getSpeed(EntityCharacteristicConfig entityCharacteristicConfig, EntityType entityType) {
        return entityCharacteristicConfig.getEntityMapConfig().get(entityType).getSpeed();
    }

    private static Island createIsland(IslandConfig islandConfig) {
        Map<Field, List<Entity>> island = new HashMap<>();
        for (int i = 0; i < islandConfig.getHeight(); i++) {
            for (int j = 0; j < islandConfig.getWidth(); j++) {
                Field field = new Field(i, j);
                island.put(field, new ArrayList<>());
            }
        }
        return new Island(island);
    }
}

class MyTestClass { //TODO: Удалить перед pullRequest!!!
    public static void main(String[] args) {
        /*List<Class<? extends Animal>> list = List.of(Wolf.class, Horse.class, Hog.class, Caterpillar.class, Wolf.class, Wolf.class);
        Class<? extends Animal> clazz = new Wolf(1.0, 1, 3, 3.0).getClass();
        System.out.println(list);
        System.out.println(list.contains(clazz));*/
       /* List<Animal> list = List.of(new Wolf(1.0, 3, 4, 5.0), new Wolf(3.0, 3, 3, 3.0), new Hog(1.0, 1, 2, 3.0), new Deer(2.0, 2, 2, 2.0), new Hog(5.0, 5, 5, 5.0));
        Set<Class<? extends Animal>> set = list.stream().map(Animal::getClass).collect(Collectors.toSet());
        System.out.println("SET = " + set);
        Wolf wolf = new Wolf(1.0, 3, 4, 5.0);
        System.out.println(list.contains(wolf));
        System.out.println(list);*/
        Random random = new Random();
        for (int i = 0; i < random.nextInt(1, 2); i++) {
            System.out.println("TEST");
        }
    }
}

