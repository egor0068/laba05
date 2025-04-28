package CommandsInConsole;

import Console.Console;
import Collection.CommandManager;

public class HelpCommand extends MainCommand {
    private final Console console;
    private final CommandManager commandManager;

    public HelpCommand(Console console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commandManager = commandManager;
    }
    @Override
    public boolean apply(String[] arguments) {
        // Проверка, есть ли лишние аргументы
        if (arguments.length > 1) {
            console.println("Использование: " + getName());
            return false;
        }
        console.println("Доступные команды:");
        commandManager.getCommands().values().forEach(command -> {
            console.println(command.getName() + " - " + command.getDescription());
        });
        return true;
    }
}