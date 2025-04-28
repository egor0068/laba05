package Exceptions;
//Выбрасывается, если команда не найдена или аргумент команды некорректен.

public class NoSuchCommandException extends Exception {

    public String getMessage() {
        return "Ошибка! Команда с неверным аргументом";
    }
}