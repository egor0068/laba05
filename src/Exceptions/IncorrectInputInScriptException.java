package Exceptions;

//Выполняется, если пользователь вводит некорректные данные.

public class IncorrectInputInScriptException extends Exception {
    public String getMessage() {
        return "Ошибка! Некорректные данные.";
    }
}