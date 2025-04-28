package Exceptions;

//Выбрасывается, если в форме создан невалидный объект.

public class InvalidFormException extends Exception {
    public String getMessage() {
        return "Ошибка! Создан невалидный объект.";
    }
}