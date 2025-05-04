import Collection.*;
import CommandsInConsole.*;

import java.util.NoSuchElementException;
import java.util.Scanner;
import Console.Console;
import static java.lang.System.*;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(in);
        CollectionManager collectionManager = new CollectionManager();
        Console console1 = null;
        CommandManager commandManager = new CommandManager(console1, collectionManager);
        Console console = new Console();
        registerCommands(commandManager, console, collectionManager);

        try {
            while (true) {
                printCommandList(commandManager);
                out.print("Введите номер или название команды: ");
                try {
                    String input = scanner.nextLine().trim();
                    handleUserInput(input, scanner, commandManager);
                } catch (NoSuchElementException e) {
                    out.println("\nОбнаружен EOF (Ctrl+D). Завершение программы.");
                    ExitCommand exitCommand = new ExitCommand(console);
                    exitCommand.apply(new String[0]);
                    break;
                }
            }
        } finally {
            scanner.close();
        }
    }
    private static void registerCommands(CommandManager commandManager, Console console, CollectionManager collectionManager) {
        commandManager.registerCommand(new InfoCommand(console, collectionManager));
        commandManager.registerCommand(new HelpCommand(console, commandManager));
        commandManager.registerCommand(new ClearCommand(collectionManager, console));
        commandManager.registerCommand(new InsertCommand(collectionManager, console));
        commandManager.registerCommand(new CountMinimalPointCommand(console, collectionManager));
        commandManager.registerCommand(new RemGreaterCommand(console, collectionManager));
        commandManager.registerCommand(new RemKeyCommand(console, collectionManager));
        commandManager.registerCommand(new RemLowKeyNullCommand(console, collectionManager));
        commandManager.registerCommand(new ReplaceLowNullCommand(console, collectionManager));
        commandManager.registerCommand(new SaveCommand(console, collectionManager));
        commandManager.registerCommand(new ReaderFileCommand(console));
        commandManager.registerCommand(new ShowCommand(console, collectionManager));
        commandManager.registerCommand(new UpdateCommand(console, collectionManager));
        commandManager.registerCommand(new ExecuteScriptCommand(console, collectionManager, commandManager));
        commandManager.registerCommand(new ExitCommand(console));
        commandManager.registerCommand(new InsertRandomCommand(console, collectionManager));
    }

    // Выводит список команд с номерами
    public static void printCommandList(CommandManager commandManager) {
        System.out.println("Доступные команды (введите число или название требуемой для выполнения команды):");
        List<MainCommand> commands = new ArrayList<>(commandManager.getCommands().values());
        for (int i = 0; i < commands.size(); i++) {
            System.out.println((i + 1) + ". " + commands.get(i).getName());
        }
    }
    private static void handleUserInput(String input, Scanner scanner, CommandManager cm) {
        // Получаем команду по вводу (либо по имени, либо по номеру)
        MainCommand command = cm.getCommand(input);
        if (command == null) {
            // Попробуем интерпретировать ввод как номер команды
            try {
                int num = Integer.parseInt(input);
                List<MainCommand> commands = new ArrayList<>(cm.getCommands().values());
                if (num > 0 && num <= commands.size()) {
                    command = commands.get(num - 1);
                }
            } catch (NumberFormatException ignored) {}
        }
        String argument = "";
        if (command instanceof UpdateCommand || command instanceof RemLowKeyNullCommand) {
            System.out.print("Введите аргумент для " + command.getName() + ": ");
            try {
                argument = scanner.nextLine();
            } catch (NoSuchElementException e) {
                out.println("\nОбнаружен EOF (Ctrl+D) при вводе аргумента. Команда отменена.");
                return;
            }
        }
        String[] args = argument.isEmpty() ? new String[0] : new String[]{argument};
        if (!command.apply(args)) {
            System.out.println("Использование: " + command.getName() + " - " + command.getDescription());
        }
    }

}