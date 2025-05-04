package Exceptions;

class FileException extends IOOperationException {
    public FileException(String message) {
        super(message);
    }
}

class FilePermissionException extends FileException {
    public FilePermissionException(String path) {
        super("Нет прав доступа к файлу: " + path);
    }
}

class FileNotFoundException extends FileException {
    public FileNotFoundException(String path) {
        super("Файл не найден: " + path);
    }
}

class FileReadException extends FileException {
    public FileReadException(String path) {
        super("Ошибка чтения файла: " + path);
    }
}

class FileWriteException extends FileException {
    public FileWriteException(String path) {
        super("Ошибка записи в файл: " + path);
    }
}

class InvalidFileFormatException extends FileException {
    public InvalidFileFormatException(String expectedFormat) {
        super("Неверный формат файла. Ожидается: " + expectedFormat);
    }
}
