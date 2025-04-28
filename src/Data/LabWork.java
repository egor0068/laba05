package Data;
import java.time.*;
import java.util.*;

public class LabWork {
    // Класс представляющий собой лабораторную работу
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Double minimalPoint; //Поле может быть null, Значение поля должно быть больше 0
    private float personalQualitiesMinimum; //Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле может быть null
    private Discipline discipline; //Поле может быть null
    private Double value;

    public LabWork(Double value, String name, Coordinates coordinates,
                   LocalDateTime creationDate, Double minimalPoint,
                   float personalQualitiesMinimum, Difficulty difficulty,
                   Discipline discipline) {
        this.id = generateID();
        this.value = value;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.personalQualitiesMinimum = personalQualitiesMinimum;
        this.difficulty = difficulty;
        this.discipline = discipline;
        this.setName(name);
        this.creationDate = LocalDateTime.now();
    }
    public LabWork() {}
    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }
        this.name = name;
    }
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null");
        }
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("Дата создания не может быть null");
        }
        this.creationDate = creationDate;
    }

    public Double getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(Double minimalPoint) {
        if (minimalPoint != null && minimalPoint <= 0) {
            throw new IllegalArgumentException("Минимальный балл должен быть больше 0");
        }
        this.minimalPoint = minimalPoint;
    }

    public float getPersonalQualitiesMinimum() {
        return personalQualitiesMinimum;
    }

    public void setPersonalQualitiesMinimum(float personalQualitiesMinimum) {
        if (personalQualitiesMinimum <= 0) {
            throw new IllegalArgumentException("Минимальные личные качества должны быть больше 0");
        }
        this.personalQualitiesMinimum = personalQualitiesMinimum;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID не может быть null или меньше/равно 0");
        }
        this.id = id;
    }

    public Integer generateID() {
        return Math.abs(new Random().nextInt()) + 1;
    }
    public Double getValue() {
        return this.value;
    }
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LabWork{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", minimalPoint=" + minimalPoint +
                ",value=" + value +
                ", personalQualitiesMinimum=" + personalQualitiesMinimum +
                ", difficulty=" + difficulty +
                ", discipline=" + discipline +
                '}';
    }

    public static LabWork createFromUserInput(Scanner scanner) {  // Метод для создания объекта из пользовательского ввода
        LabWork labWork = new LabWork();

        System.out.print("Введите название лабораторной работы: ");
        labWork.setName(scanner.nextLine());

        System.out.print("Введите координату X (целое число): ");
        int x = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите координату Y (длинное целое число): ");
        long y = Long.parseLong(scanner.nextLine());
        labWork.setCoordinates(new Coordinates(x, y));

        System.out.print("Введите минимальный балл (число с плавающей точкой или 'null'): ");
        String minPointInput = scanner.nextLine();
        labWork.setMinimalPoint("null".equalsIgnoreCase(minPointInput) ? null : Double.parseDouble(minPointInput));

        System.out.print("Введите оценку: ");
        labWork.setValue(Double.parseDouble(scanner.nextLine()));

        System.out.print("Введите минимальные личные качества (число с плавающей точкой > 0): ");
        labWork.setPersonalQualitiesMinimum(Float.parseFloat(scanner.nextLine()));

        System.out.print("Введите сложность (VERY_HARD, IMPOSSIBLE, TERRIBLE или 'null'): ");
        String difficultyInput = scanner.nextLine();
        labWork.setDifficulty("null".equalsIgnoreCase(difficultyInput) ? null : Difficulty.valueOf(difficultyInput.toUpperCase()));

        System.out.print("Введите название дисциплины: ");
        String disciplineName = scanner.nextLine();
        System.out.print("Введите количество лекционных часов: ");
        int lectureHours = Integer.parseInt(scanner.nextLine());

        labWork.setDiscipline(new Discipline(disciplineName, lectureHours));

        return labWork;
    }
}