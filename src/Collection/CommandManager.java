package Collection;

import CommandsInConsole.MainCommand;
import Console.Console;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, MainCommand> commands = new HashMap<>();

    public CommandManager(Console console, CollectionManager collectionManager) {
    }

    public void registerCommand(MainCommand command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    public MainCommand getCommand(String commandName) {
        return commands.get(commandName.toLowerCase());
    }

    public Map<String, MainCommand> getCommands() {
        return commands;
    }
}