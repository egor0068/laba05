package Exceptions;
//Выбрасывается, если коллекция пустая
public class CollectionsEmptyException extends Exception {
    public String getMessage() {
        return "Ошибка! Коллекция пустая.";
    }
}
