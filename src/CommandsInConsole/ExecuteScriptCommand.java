package CommandsInConsole;

import Collection.*;
import Console.Console;
import Data.Coordinates;
import Data.Discipline;
import Data.LabWork;
import Enum.DifficultyEnum;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class ExecuteScriptCommand extends MainCommand {
    private final Console console;
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    private static final Deque<String> executingScripts = new ArrayDeque<>();
    private static int objectsCreated = 0;
    private final Random random = new Random();
    private boolean autoMode = false;
    private List<String> scriptCommands = new ArrayList<>();
    private static final LocalDateTime startTime = LocalDateTime.now();
    private String lastSaveFile = null;
    private String saveFilePath;


    public ExecuteScriptCommand(Console console, CollectionManager collectionManager, CommandManager commandManager) {
        super("execute_script", "выполнить скрипт из файла");
        this.console = console;
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    @Override
    public boolean apply(String[] arguments) {
        String fileName;
        if (arguments.length < 2) {
            fileName = "command.txt";
            console.println("Используется автоматический режим выполнения");
            autoMode = true;
        } else {
            fileName = arguments[1];
        }

        if (executingScripts.contains(fileName)) {
            console.println("Ошибка: Рекурсивный вызов скрипта '" + fileName + "'");
            return false;
        }

        // Читаем команды из файла
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    scriptCommands.add(line);
                }
            }
        } catch (IOException e) {
            console.println("Ошибка при чтении файла скрипта: " + e.getMessage());
            return false;
        }

        // Если файл пустой, используем стандартные команды
        if (scriptCommands.isEmpty() && autoMode) {
            scriptCommands = Arrays.asList(
                    "insert_random",
                    "save",
                    "show",
                    "count_minimal_point 50",
                    "info",
                    "clear",
                    "read"
            );
        }

        executingScripts.push(fileName);

        try {
            console.println("Начало автоматического выполнения команд из скрипта");
            executeAutoCommands();
            console.println("Автоматическое выполнение завершено");
            return true;
        } finally {
            executingScripts.pop();
            scriptCommands.clear(); // Очищаем список команд для следующего выполнения
        }
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }


    public Console getConsole() {
        return console;
    }

    private void executeAutoCommands() {
        try {
            // Отключаем эхо ввода (если нужно)
            if (autoMode) {
                try {
                    Runtime.getRuntime().exec("stty -echo").waitFor();
                } catch (Exception ignored) {}
            }

            for (String command : scriptCommands) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }

                console.println("> " + command);
                executeCommand(command);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            // Включаем эхо обратно (если отключали)
            if (autoMode) {
                try {
                    Runtime.getRuntime().exec("stty echo").waitFor();
                } catch (Exception ignored) {}
            }
        }
    }

    private void executeCommand(String commandLine) {
        if (commandLine == null || commandLine.trim().isEmpty() || Thread.currentThread().isInterrupted()) {
            return;
        }

        String[] parts = commandLine.trim().split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String argumentsStr = parts.length > 1 ? parts[1] : "";

        String[] arguments = parseArguments(argumentsStr);

        try {
            switch (command) {
                case "info":
                    new InfoCommand(console, collectionManager).apply(arguments);
                    break;
                case "help":
                    new HelpCommand(console, commandManager).apply(arguments);
                    break;
                case "clear":
                    new ClearCommand(collectionManager, console).apply(arguments);
                    break;
                case "insert_random":
                    new InsertRandomCommand(console, collectionManager).apply(arguments);
                    break;
                case "count_minimal_point":
                    new CountMinimalPointCommand(console, collectionManager).apply(arguments);
                    break;
                case "remove_greater":
                    removeGreaterRandom();
                    break;
                case "remove_key":
                    removeRandomKey();
                    break;
                case "remove_lower_key_null":
                    new RemLowKeyNullCommand(console, collectionManager).apply(arguments);
                    break;
                case "replace_if_lower_null":
                    new ReplaceLowNullCommand(console, collectionManager).apply(arguments);
                    break;
                case "save":
                    try {
                        new SaveCommand(console, collectionManager).apply(arguments);
                    } catch (Exception e) {
                        System.err.println("Ошибка в SaveCommand: " + e.getMessage());
                        e.printStackTrace();
                    }
                    // Обновляем путь после сохранения
                    this.saveFilePath = collectionManager.getSaveFileName();
                    console.println("Коллекция сохранена в файл: " + this.saveFilePath);
                    break;
                case "show":
                    new ShowCommand(console, collectionManager).apply(arguments);
                    break;
                case "update":
                    updateRandomElement();
                    break;
                case "read":
                    new ReaderFileCommand(console).apply(arguments);
                    console.println("Сохраненные элементы в коллекцию прочитаны");
                    break;
                case "execute_script":
                    console.println("Ошибка: Рекурсивный вызов скриптов запрещен");
                    break;
                case "exit":
                    console.println("Завершение работы...");
                    System.exit(0);  // или другой способ завершения
                    break;
                default:
                    console.println("Неизвестная команда: " + command);
            }
        } catch (Exception e) {
            console.println("Ошибка выполнения команды '" + command + "': " + e.getMessage());
        }
    }

    private void removeRandomKey() {
        TreeMap<Integer, LabWork> collection = (TreeMap<Integer, LabWork>) collectionManager.getCollection();

        if (collection.isEmpty()) {
            console.println("Коллекция пуста, нечего удалять");
            return;
        }

        // Получаем текущий размер коллекции
        int sizeBefore = collection.size();

        // Выбираем случайный ключ
        Integer randomKey = selectRandomKey(collection);

        if (randomKey == null) {
            console.println("Ошибка: не удалось выбрать ключ для удаления");
            return;
        }

        // Удаляем элемент
        LabWork removedElement = collection.remove(randomKey);

        // Получаем новый размер коллекции
        int sizeAfter = collection.size();

        // Формируем отчет
        printRemovalResult(sizeBefore, sizeAfter, randomKey, removedElement);
    }

    private void removeGreaterRandom() {
        TreeMap<Integer, LabWork> collection = (TreeMap<Integer, LabWork>) collectionManager.getCollection();

        if (collection.isEmpty()) {
            console.println("Коллекция пуста, нечего удалять");
            return;
        }

        // Получаем текущий размер коллекции
        int sizeBefore = collection.size();

        // Выбираем случайный ключ для сравнения
        Integer randomKey = selectRandomKey(collection);

        if (randomKey == null) {
            console.println("Ошибка: не удалось выбрать ключ для сравнения");
            return;
        }

        console.println("Случайно выбранный ключ для сравнения: " + randomKey);

        // Удаляем элементы строго больше выбранного ключа
        int removedCount = removeElementsGreaterThan(collection, randomKey);

        // Получаем новый размер коллекции
        int sizeAfter = collection.size();

        // Проверяем, что размер изменился корректно
        if (sizeBefore - sizeAfter != removedCount) {
            console.println("Предупреждение: расхождение в количестве удаленных элементов");
        }

        printBulkRemovalResult(sizeBefore, sizeAfter, removedCount);
    }

    private Integer selectRandomKey(TreeMap<Integer, LabWork> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        List<Integer> keys = new ArrayList<>(collection.keySet());
        return keys.get(random.nextInt(keys.size()));
    }

    private int removeElementsGreaterThan(TreeMap<Integer, LabWork> collection, Integer key) {
        int count = 0;
        Iterator<Map.Entry<Integer, LabWork>> iterator = collection.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, LabWork> entry = iterator.next();
            if (entry.getKey() > key) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }

    private void printRemovalResult(int sizeBefore, int sizeAfter, Integer removedKey, LabWork removedElement) {
        console.println("\n════════════════════════════════════════════");
        console.println("          РЕЗУЛЬТАТ УДАЛЕНИЯ ЭЛЕМЕНТА        ");
        console.println("├──────────────────────────────────────────");
        console.println("│ Элементов было:          " + sizeBefore);
        console.println("│ Элементов стало:         " + sizeAfter);
        console.println("├──────────────────────────────────────────");
        console.println("│ Удалённый ключ:          " + removedKey);

        if (removedElement != null) {
            console.println("├──────────────────────────────────────────");
            console.println("│ Удалённый элемент:                      ");
            console.println("│ ID:               " + removedElement.getID());
            console.println("│ Название:         " + removedElement.getName());
        } else {
            console.println("├──────────────────────────────────────────");
            console.println("│ Внимание: элемент не был найден в коллекции");
        }
        console.println("════════════════════════════════════════════\n");
    }

    private void printBulkRemovalResult(int sizeBefore, int sizeAfter, int removedCount) {
        console.println("\n════════════════════════════════════════════");
        console.println("       РЕЗУЛЬТАТ ГРУППОВОГО УДАЛЕНИЯ        ");
        console.println("├──────────────────────────────────────────");
        console.println("│ Элементов было:          " + sizeBefore);
        console.println("│ Элементов удалено:       " + removedCount);
        console.println("│ Элементов осталось:      " + sizeAfter);

        if (removedCount > 0) {
            console.println("├──────────────────────────────────────────");
            console.println("│ Статус: Успешно удалено " + removedCount + " элементов");
        } else {
            console.println("├──────────────────────────────────────────");
            console.println("│ Статус: Элементы для удаления не найдены");
        }
        console.println("════════════════════════════════════════════\n");
    }

    /**
     * Обновляет случайный элемент коллекции, заменяя его ключ на новый случайный ключ
     * с тем же количеством цифр, что и у исходного ключа
     */
    private void updateRandomElement() {
        // Получаем защищенную копию коллекции для работы
        TreeMap<Integer, LabWork> collection = new TreeMap<>(collectionManager.getCollection());

        if (collection.isEmpty()) {
            console.println("Коллекция пуста, нечего обновлять");
            return;
        }

        try {
            // 1. Выбираем случайный ключ для обновления
            Integer oldKey = selectRandomKey(collection);
            if (oldKey == null) {
                console.println("Ошибка: не удалось выбрать ключ для обновления");
                return;
            }

            // 2. Генерируем новый уникальный ключ с тем же количеством цифр
            int newKey = generateUniqueKeyWithSameLength(oldKey, collection);

            // 3. Получаем элемент по старому ключу
            LabWork labWork = collection.get(oldKey);
            if (labWork == null) {
                console.println("Ошибка: элемент с ключом " + oldKey + " не найден");
                return;
            }

            // 4. Выполняем атомарное обновление
            boolean updated = collectionManager.replaceKey(oldKey, newKey);
            if (!updated) {
                console.println("Ошибка при обновлении ключа элемента");
                return;
            }

            // 5. Формируем информативное сообщение
            printUpdateSuccessMessage(oldKey, newKey, labWork);

        } catch (Exception e) {
            console.println("Критическая ошибка при обновлении элемента: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Генерирует новый уникальный ключ с тем же количеством цифр
     */
    private int generateUniqueKeyWithSameLength(int originalKey, TreeMap<Integer, LabWork> collection) {
        int newKey;
        int maxAttempts = 100; // Защита от бесконечного цикла
        int attempts = 0;

        do {
            newKey = generateKeyWithSameLength(originalKey);
            attempts++;

            if (attempts >= maxAttempts) {
                throw new IllegalStateException("Не удалось сгенерировать уникальный ключ после " + maxAttempts + " попыток");
            }
        } while (collection.containsKey(newKey));

        return newKey;
    }

    /**
     * Генерирует ключ с тем же количеством цифр, что и originalKey
     */
    private int generateKeyWithSameLength(int originalKey) {
        String keyStr = String.valueOf(originalKey);
        int length = Math.max(1, keyStr.length()); // Минимальная длина - 1

        // Для ключей, начинающихся с 0 (хотя в TreeMap это маловероятно)
        boolean startsWithZero = keyStr.startsWith("0");

        int min = startsWithZero ? 0 : (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;

        // Корректировка для минимального значения
        if (min < 0) min = 0;
        if (max < min) max = min + 1000; // Запасной вариант

        return min + random.nextInt(max - min + 1);
    }

    /**
     * Выводит информативное сообщение об успешном обновлении
     */
    private void printUpdateSuccessMessage(int oldKey, int newKey, LabWork labWork) {
        console.println("\n=== Информация об обновлении ключа ===");
        console.println("Старый ключ: " + oldKey);
        console.println("Новый ключ: " + newKey);
        console.println("\nДанные элемента:");
        console.println("ID: " + labWork.getID());
        console.println("Название: " + labWork.getName());
        console.println("Координаты: " + formatCoordinates(labWork.getCoordinates()));
        console.println("Дата создания: " + labWork.getCreationDate());
        console.println("Минимальный балл: " + labWork.getMinimalPoint());
        console.println("Минимальные личные качества: " + labWork.getPersonalQualitiesMinimum());
        console.println("Сложность: " + labWork.getDifficulty());
        console.println("Дисциплина: " + formatDiscipline(labWork.getDiscipline()));
        console.println("Ключ элемента:     " + newKey + " (обновлён)");
        console.println("===================================");

        console.println("Изменения сохранены успешно!");
        console.println("Элемент с ключом " + oldKey + " был переименован в " + newKey);
        console.println("Текущее количество элементов в коллекции: " + collectionManager.getCollection().size());
    }
    private String formatCoordinates(Coordinates coordinates) {
        return coordinates == null ? "null" :
                String.format("(x: %d, y: %d)", coordinates.getX(), coordinates.getY());
    }

    private String formatDiscipline(Discipline discipline) {
        return discipline == null ? "null" :
                String.format("%s (часы: %d)", discipline.getName(), discipline.getLectureHours());
    }

    private String[] generateInsertArguments() {
        return new String[]{
                "key_" + random.nextInt(1000),
                "name_" + random.nextInt(1000),
                String.valueOf(random.nextInt(100)),
                String.valueOf(random.nextInt(100)),
                String.valueOf(random.nextInt(100)),
                DifficultyEnum.values()[random.nextInt(DifficultyEnum.values().length)].name(),
                "discipline_" + random.nextInt(1000),
                String.valueOf(random.nextInt(100))
        };
    }

    private String[] parseArguments(String argsStr) {
        List<String> argsList = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentArg = new StringBuilder();

        for (char c : argsStr.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (Character.isWhitespace(c) && !inQuotes) {
                if (currentArg.length() > 0) {
                    argsList.add(currentArg.toString());
                    currentArg = new StringBuilder();
                }
            } else {
                currentArg.append(c);
            }
        }

        if (currentArg.length() > 0) {
            argsList.add(currentArg.toString());
        }

        return argsList.toArray(new String[0]);
    }
    private void printCollectionInfo() {
        try {
            // Получаем текущее время
            LocalDateTime now = LocalDateTime.now();

            // Форматирование даты и времени
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Рассчитываем время работы программы (совместимый способ)
            Duration uptime = Duration.between(startTime, now);
            long totalSeconds = uptime.getSeconds();
            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            long seconds = totalSeconds % 60;

            // Получаем информацию о коллекции
            String collectionType = collectionManager.collectionType();
            int collectionSize = collectionManager.collectionSize();
            LocalDateTime initTime = collectionManager.getLastInitTime();
            LocalDateTime saveTime = collectionManager.getLastSaveTime();
            String saveFilePath = this.saveFilePath != null ? this.saveFilePath : collectionManager.getSaveFilePath();

            // Формируем вывод информации
            console.println("\n════════════════════════════════════════════");
            console.println("          ИНФОРМАЦИЯ О КОЛЛЕКЦИИ            ");
            console.println("├──────────────────────────────────────────");
            console.println(String.format("│ %-25s %-20s", "Тип коллекции:", collectionType));
            console.println(String.format("│ %-25s %-20s", "Тип элементов:", "LabWork"));
            console.println(String.format("│ %-25s %-20d", "Количество элементов:", collectionSize));
            console.println(String.format("│ %-25s %-20s", "Дата инициализации:",
                    initTime != null ? initTime.format(formatter) : "не инициализирована"));
            console.println(String.format("│ %-25s %-20s", "Дата сохранения:",
                    saveTime != null ? saveTime.format(formatter) : "не сохранена"));
            console.println("Файл сохранения коллекции: " +
                    (saveFilePath == null ? "не указан (используйте 'save')" : saveFilePath));
            final LocalDateTime programStartTime = null;
            Duration.between(programStartTime, LocalDateTime.now());
            uptime.toHours();
            uptime.toMinutes();
            uptime.getSeconds();
            console.println("Время работы программы: " +
                    String.format("%d ч. %d мин. %d сек.", hours, minutes, seconds));
            console.println("════════════════════════════════════════════\n");

        } catch (Exception e) {
            console.println("Ошибка при получении информации о коллекции: " + e.getMessage());
        }
        System.out.println("[DEBUG] Save path after saving: " + collectionManager.getSaveFilePath());
    }

    private void updateElement(String[] arguments) {
        if (collectionManager.getCollection().isEmpty()) {
            console.println("Коллекция пуста, нечего обновлять");
            return;
        }

        Integer keyToUpdate;
        if (arguments.length == 0) {
            // Автоматический режим - берем первый ключ
            keyToUpdate = collectionManager.getCollection().keySet().iterator().next();
        } else {
            try {
                keyToUpdate = Integer.parseInt(arguments[0]);
                if (!collectionManager.getCollection().containsKey(keyToUpdate)) {
                    console.println("Ошибка: элемента с ключом " + keyToUpdate + " не существует");
                    return;
                }
            } catch (NumberFormatException e) {
                console.println("Ошибка: ключ должен быть числом");
                return;
            }
        }

        LabWork oldElement = collectionManager.getCollection().get(keyToUpdate);
        String[] newElementArgs = generateInsertArguments();

        String[] updateArgs = new String[newElementArgs.length + 1];
        updateArgs[0] = String.valueOf(keyToUpdate);
        System.arraycopy(newElementArgs, 0, updateArgs, 1, newElementArgs.length);

        new UpdateCommand(console, collectionManager).apply(updateArgs);

        LabWork updatedElement = collectionManager.getCollection().get(keyToUpdate);

        console.println("Обновлен объект с ключом '" + keyToUpdate + "'");
        console.println("Старый ID: " + oldElement.getID());
        console.println("Новый ID: " + updatedElement.getID());
    }
}