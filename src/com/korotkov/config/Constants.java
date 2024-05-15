package com.korotkov.config;

public class Constants {
    public static final String PATH_TO_ENTITY_CHARACTERISTIC = "resources/entity_characteristic.json";
    public static final String PATH_TO_POSSIBILITY_OF_EATING = "resources/possibility_of_eating.json";
    public static final String PATH_TO_ISLAND_SETTINGS = "resources/settings.properties";

    public static final String OBJECT_MAPPER_READ_ERROR = """
            Ошибка ввода-вывода:
            1. проверьте правильно ли вы указали путь к файлу %s"
            2. Проверьте структуру указанного файла %s""";

    public static final String GET_CONSTRUCTOR_ERROR = "Ошибка! Нет указанного конструктора в классе: %s";
    public static final String CREATE_CURRENT_ENTITY_ERROR = "Ошибка! Не удалось создать объект сласса: %s";
}
