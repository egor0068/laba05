package Exceptions;

class ParseException extends LabWorkException {
    public ParseException(String message) {
        super(message);
    }
}

class DateParseException extends ParseException {
    public DateParseException(String dateString) {
        super("Невозможно распознать дату: " + dateString);
    }
}

class NumberParseException extends ParseException {
    public NumberParseException(String value) {
        super("Невозможно преобразовать '" + value + "' в число");
    }
}

class EnumParseException extends ParseException {
    public EnumParseException(String value, Class<?> enumClass) {
        super("Недопустимое значение '" + value + "' для " + enumClass.getSimpleName());
    }
}
