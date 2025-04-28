package CommandsInConsole;

import Collection.CollectionManager;
import Collection.CommandManager;
import Console.Console;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ExecuteScriptCommand extends MainCommand {
    private final Console console;
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    private static final Deque<String> executingScripts = new ArrayDeque<>();

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
            console.println("Введите имя файла скрипта (с расширением): ");
            fileName = console.readLine();
        } else {
            fileName = arguments[1];
        }

        // Проверка на рекурсию
        if (executingScripts.contains(fileName)) {
            console.println("Ошибка: Рекурсивный вызов скрипта '" + fileName + "'");
            return false;
        }

        // Проверка существования файла
        File scriptFile = new File(fileName);
        if (!scriptFile.exists()) {
            console.println("Ошибка: Файл '" + fileName + "' не найден. Проверьте:");
            console.println("1. Полный путь к файлу: " + scriptFile.getAbsolutePath());
            console.println("2. Наличие файла в указанной директории");
            console.println("3. Правильность расширения файла");
            return false;
        }

        if (!scriptFile.canRead()) {
            console.println("Ошибка: Нет прав на чтение файла '" + fileName + "'");
            return false;
        }

        executingScripts.push(fileName);

        try (InputStreamReader isr = new InputStreamReader(
                new FileInputStream(scriptFile), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            console.println("Начало выполнения скрипта: " + scriptFile.getAbsolutePath());

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) {
                    continue;
                }

                console.println("> " + line);
                executeCommand(line);
            }

            console.println("Скрипт успешно выполнен: " + fileName);
            return true;

        } catch (FileNotFoundException e) {
            console.println("Ошибка: Файл не найден - " + fileName);
            return false;
        } catch (IOException e) {
            console.println("Ошибка ввода-вывода: " + e.getMessage());
            return false;
        } finally {
            executingScripts.pop();
        }
    }

    private void executeCommand(String commandLine) {
        // 1. Проверка на пустую команду
        if (commandLine == null || commandLine.trim().isEmpty()) {
            console.println("Ошибка: Пустая команда");
            return;
        }

        // 2. Разделяем строку на команду и аргументы
        String[] parts = commandLine.trim().split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String argumentsStr = parts.length > 1 ? parts[1] : "";

        // 3. Парсим аргументы (учитываем кавычки)
        String[] arguments = parseArguments(argumentsStr);

        // 4. Выполняем команду
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
                case "insert":
                    new InsertCommand(collectionManager, console).apply(arguments);
                    break;
                case "count_minimal_point":
                    new CountMinimalPointCommand(console, collectionManager).apply(arguments);
                    break;
                case "exit":
                    System.exit(0);
                    break;
                case "remove_greater":
                    new RemGreaterCommand(console, collectionManager).apply(arguments);
                    break;
                case "remove_key":
                    new RemKeyCommand(console, collectionManager).apply(arguments);
                    break;
                case "remove_lower_key_null":
                    new RemLowKeyNullCommand(console, collectionManager).apply(arguments);
                    break;
                case "replace_if_lower_null":
                    new ReplaceLowNullCommand(console, collectionManager).apply(arguments);
                    break;
                case "save":
                    new SaveCommand(console, collectionManager).apply(arguments);
                    console.println("Коллекция сохранена");
                    break;
                case "show":
                    new ShowCommand(console, collectionManager).apply(arguments);
                    break;
                case "update":
                    new UpdateCommand(console, collectionManager).apply(arguments);
                    break;
                case "execute_script":
                    console.println("Ошибка: Рекурсивный вызов скриптов запрещен");
                    break;
                default:
                    console.println("Неизвестная команда: " + command);
            }
        } catch (Exception e) {
            console.println("Ошибка выполнения команды '" + command + "': " + e.getMessage());
        }
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
}