package Data;

import java.util.Objects;

public class Discipline {
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Integer lectureHours; // Поле не может быть null
    public Discipline(String name, Integer lectureHours) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя дисциплины не может быть null или пустым.");
        }
        if (lectureHours == null) {
            throw new IllegalArgumentException("Количество лекционных часов не может быть null.");
        }
        this.name = name;
        this.lectureHours = lectureHours;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "Discipline {name='" + name + "', lectureHours=" + lectureHours + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lectureHours);
    }
}