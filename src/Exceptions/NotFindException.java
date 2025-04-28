package Exceptions;
//Выбрасывается, если что-то не найдено
public class NotFindException extends Exception{
    public String getMessage() {
        return "Ошибка! Информация не найдена.";
    }
}
