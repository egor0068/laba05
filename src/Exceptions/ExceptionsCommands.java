package Exceptions;

class CommandException extends LabWorkException {
    public CommandException(String message) {
        super(message);
    }
}

class CommandExecutionException extends CommandException {
    public CommandExecutionException(String command, String details) {
        super("Ошибка выполнения команды '" + command + "': " + details);
    }
}

class CommandNotFoundException extends CommandException {
    public CommandNotFoundException(String command) {
        super("Команда '" + command + "' не найдена");
    }
}

class IllegalCommandArgumentsException extends CommandException {
    public IllegalCommandArgumentsException(String command, String requirements) {
        super("Некорректные аргументы для команды '" + command + "'. Требуется: " + requirements);
    }
}

class ScriptRecursionException extends CommandException {
    public ScriptRecursionException(String filename) {
        super("Обнаружена рекурсия в скрипте '" + filename + "'");
    }
}
