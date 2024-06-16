package com.korotkov.config;

public class Constants {
    public static final String PATH_TO_ENTITY_CHARACTERISTIC = "resources/entity_characteristic.json";
    public static final String PATH_TO_POSSIBILITY_OF_EATING = "resources/possibility_of_eating.json";
    public static final String PATH_TO_ISLAND_SETTINGS = "resources/settings.properties";

    public static final String GREETINGS = """
            $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
            !!! Добро пожаловать в игру GAME_OF_ISLAND !!!
            $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
            """;

    public static final String CHANGE_SETTINGS = """
            ***********************************************************************
            Хотите внести изменения в настройки игры?
            1 - Да
            2 - Нет
            Для выхода из игры нажмите любую другую цифру!
            ***********************************************************************
            """;

    public static final String CHOOSE_YOUR_DESTINY = """
            &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
            <<< Главное меню настроек >>>
            Пожалуйста, выберите нужную категорию:
            1 - Изменение настроек острова.
            2 - Изменение настроек животных и растений.
            3 - Изменение условия остановки игры
            4 - Выход из настроек.
            Для выхода из игры нажмите любую другую цифру!
            &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
            """;

    public static final String CHOOSE_STOP_CONDITIONS = """
            #######################################################################
             Выберите условие остановки игры:
             1 - Если все животные погибли
             2 - Если погибли все хищники
             3 - Если погибли все травоядные
             4 - Выход в главное меню настроек
             Для выхода из игры нажмите любую другую цифру!
             #######################################################################
            """;

    public static final String SET_WIDTH_OF_ISLAND = """
            #######################################################################
             Пожалуйста, введите ширину острова:
             (цифры от 1 до 150: некорректные данные не будут применены!)
            #######################################################################
            """;

    public static final String SET_HEIGHT_OF_ISLAND = """
            #######################################################################
             Пожалуйста, введите длину острова:
             (цифры от 1 до 150: некорректные данные не будут применены!)
            #######################################################################
            """;

    public static final String SET_DAYS_OF_ISLAND = """
            #######################################################################
             Пожалуйста, введите количество дней жизни на острове:
             (цифры от 1 до 365: некорректные данные не будут применены!)
            #######################################################################
            """;

    public static final String GO_GO_GO = """
            >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            <=== !!! ПОЕХАЛИ !!! ===>
            >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            """;

    public static final String CHOOSE_CURRENT_ENTITY_SETTINGS = """
            #######################################################################
            Пожалуйста, выберите нужную категорию:
            1 - Хищники
            2 - Травоядные
            3 - Растения
            4 - Выход в главное меню настроек
            Для выхода из игры нажмите любую другую цифру!
            #######################################################################
            """;

    public static final String CHANGE_PREDATOR_SETTINGS = """
            #######################################################################
            1 - Изменение настроек всех хищников
            2 - Выбрать хищника
            3 - Выход в главное меню настроек
            Для выхода из игры нажмите любую другую цифру!
            #######################################################################
            """;

    public static final String CHANGE_HERBIVORES_SETTINGS = """
            #######################################################################
            1 - Изменение настроек всех травоядных
            2 - Выбрать травоядное
            3 - Выход в главное меню настроек
            Для выхода из игры нажмите любую другую цифру!
            #######################################################################
            """;


    public static final String ALL_ANIMAL_WEIGHT = """
            #######################################################################
            На сколько килограмм увеличить (- уменьшить) вес всех %s ?
            Введите double-число от -50.0 до 100.0 :
            #######################################################################
            """;
    public static final String ALL_ANIMAL_COUNT = """
            #######################################################################
            На сколько единиц увеличить (- уменьшить) максимальное количество
            на одной клетке всех %s ? Введите целое число от -50 до %d :
            #######################################################################
            """;
    public static final String ALL_ANIMAL_SPEED = """
            #######################################################################
            На сколько единиц увеличить (- уменьшить) скорость всех %s ?
            Введите целое число от -5 до 5
            #######################################################################
            """;
    public static final String ALL_ANIMAL_KG_TO_GET_FULL = """
            #######################################################################
            На сколько единиц увеличить (- уменьшить) количество килограмм пищи
            до полного насыщения всех %s ?
            Введите double-число число от -50.0 до 100.0 :
            #######################################################################
            """;

    public static final String CHANGE_PLANTS_SETTINGS = """
            #######################################################################
            Изменение настроек растений:
            1 - выбрать растение
            2 - Выход в главное меню настроек
            Для выхода из игры нажмите любую другую цифру!
            #######################################################################
            """;

    public static final String CHOOSE_CURRENT_ENTITY = """
            #######################################################################
            Пожалуйста выберите конкретное %s:
            #######################################################################
            """;

    public static final String CHOOSE_FROM_LIST = """
            #######################################################################
            Пожалуйста введите цифру из предложенного списка!
            #######################################################################
            """;

    public static final String SET_WEIGHT = """
            #######################################################################
            Пожалуйста введите вес для %s (double-число):
            #######################################################################
            """;
    public static final String SET_COUNT = """
            #######################################################################
            Пожалуйста введите максимальное количество на одной клетки для %s
            (целое число от 1 до %d):
            #######################################################################
            """;
    public static final String SET_SPEED = """
            #######################################################################
            Пожалуйста введите скорость перемещения для %s
            (целое число):
            #######################################################################
            """;
    public static final String SET_KG_TO_FULL = """
            #######################################################################
            Пожалуйста введите количество килограмм пищи
            до полного насыщения для %s (double-число):
            #######################################################################
            """;

    public static final String SETTINGS_HAVE_BEEN_CHANGED = """
            @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            Настройки были успешно изменены!
            @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            """;

    public static final String EXIT_FROM_SETTINGS = """
            ///////////////////////////////////////////////////////////////////////
            >>> Выход из настроек! >>>
            ///////////////////////////////////////////////////////////////////////
            """;

    public static final String INTEGER_PARSE_ERROR = """
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Ошибка, Вы неправильно ввели число! Пожалуйста введите целое число!
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            """;

    public static final String DOUBLE_PARSE_ERROR = """
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Ошибка, Вы неправильно ввели число!
            Пожалуйста введите double - число c точкой!
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            """;

    public static final String INPUT_OUTPUT_ERROR = """
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Внимание, зафиксирована ошибка ввода-ввывода при считывании ввода клавиатуры!
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            """;

    public static final String OBJECT_READ_ERROR = """
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Ошибка ввода-вывода:
            1. проверьте правильно ли вы указали путь к файлу %s"
            2. Проверьте структуру указанного файла %s
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            """;

    public static final String GET_CONSTRUCTOR_ERROR = """
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Ошибка! Нет указанного конструктора в классе: %s
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            """;
    public static final String CREATE_CURRENT_ENTITY_ERROR = """
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Ошибка! Не удалось создать объект сласса: %s
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            """;
    public static final String NULL_ANIMAL_ERROR = """
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Ошибка! Для создания нового экземпляра животного нужно два родителя!
            Ссылка на переданное в метод животное = null!
            Новый экземпляр животного не создан!
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            """;


    public static final String WRONG_ANIMAL_CLASS_ERROR = """
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Ошибка! Разные классы животных: %s и %s
            не могут создать новый экземпляр животного!
            Новый экземпляр животного не создан!
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            """;
    public static final String REPRODUCE_ERROR = """
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Ошибка! У животных: %s и %s
            не получилось создать новый экземпляр животного!
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            """;

    public static final String STATISTIC_OF_DAY = """
            ***********************************************************************
             Статистика за прошедший день № %d:
            """;
    public static final String LIVE_ANIMALS = "На острове живут %d животных\n";
    public static final String LIVE_PREDATORS_AND_HERBIVORES = "Из них: хищников = %d, травоядных = %d\n";
    public static final String BORN_ANIMALS = "Родились %d животных\n";
    public static final String DEATH_ANIMALS = "Умерло %d животных\n";
    public static final String LIVE_PLANTS = "Растут %d растений\n";
    public static final String EAT_PLANTS = "Съедено %d растений\n";
    public static final String DIFFERENCE = "Разница между первым и текущим днём:";
    public static final String MORE_ANIMALS = "Животных стало больше на %d особей\n";
    public static final String LESS_ANIMALS = "Животных стало меньше на %d особей\n";
    public static final String NO_CHANGES_ANIMALS = "Количество животных не изменилось";
    public static final String MORE_PLANTS = "Растений стало больше на %d штук\n";
    public static final String LESS_PLANTS = "Растений стало меньше на %d штук\n";
    public static final String NO_CHANGES_PLANTS = "Количество растений не изменилось";
    public static final String END = "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&";

    public static final String GAME_OVER = """
            $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
            <<< !!! GAME OVER !!! >>>
            $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
            """;
}
