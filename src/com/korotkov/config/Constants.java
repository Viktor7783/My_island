package com.korotkov.config;

public class Constants {
    public final static String OBJECT_MAPPER_READ_ERROR = """
            Ошибка ввода-вывода:
            1. проверьте правильно ли вы указали путь к файлу %s"
            2. Проверьте структуру указанного файла %s""";

    public final static String GET_CONSTRUCTOR_ERROR = "Ошибка! Нет указанного конструктора в классе: %s";
    public final static String CREATE_CURRENT_ENTITY_ERROR = "Ошибка! Не удалось создать объект сласса: %s";
}
