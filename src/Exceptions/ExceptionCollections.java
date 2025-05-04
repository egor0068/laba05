package Exceptions;


class CollectionException extends LabWorkException {
    public CollectionException(String message) {
        super(message);
    }
}

class CollectionEmptyException extends CollectionException {
    public CollectionEmptyException(String operationName) {
        super("Невозможно выполнить " + operationName + ": коллекция пуста");
    }
}

class ElementNotFoundException extends CollectionException {
    public ElementNotFoundException(String key) {
        super("Элемент с ключом " + key + " не найден в коллекции");
    }
}
class KeyAlreadyExistsException extends CollectionException {
    public KeyAlreadyExistsException(String key) {
        super("Ключ " + key + " уже существует в коллекции");
    }
}
