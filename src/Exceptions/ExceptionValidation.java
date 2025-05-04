package Exceptions;

class FieldValidationException extends ValidationException {
    private final String fieldName;

    public FieldValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

class NullFieldException extends FieldValidationException {
    public NullFieldException(String fieldName) {
        super(fieldName, "Поле '" + fieldName + "' не может быть null");
    }
}

class EmptyStringException extends FieldValidationException {
    public EmptyStringException(String fieldName) {
        super(fieldName, "Поле '" + fieldName + "' не может быть пустой строкой");
    }
}

class ValueOutOfRangeException extends FieldValidationException {
    public ValueOutOfRangeException(String fieldName, Number min, Number max) {
        super(fieldName, String.format(
                "Значение поля '%s' должно быть в диапазоне %s..%s",
                fieldName, min, max
        ));
    }
}
