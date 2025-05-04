package Collection;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import Data.*;
import java.util.*;

public class CollectionManager {

    public Map<Integer, LabWork> getCollection() {
        return collection;
    }
    private final Map<Integer, LabWork> collection;  // Коллекция для хранения элементов
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;

    public CollectionManager() {
        this.collection = new TreeMap<>(); // Инициализация коллекции
        this.lastInitTime = LocalDateTime.now(); // Время инициализации
        this.lastSaveTime = null;
    }
    private String saveFileName = "saveFile.csv"; // Дефолтное имя файла

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String fileName) {
        this.saveFileName = fileName;
    }
    // Метод для очистки коллекции
    public void clearCollection() {
        collection.clear();
    }
    // Метод для подсчета совпадений минимального балла в коллекциях
    public long countByMinimalPoint(double minimalPoint) {                      // Считает совпадения через Stream API
        return collection.values().stream()                                     // Создание потока
                .filter(labwork -> labwork.getMinimalPoint() != null)           // Фильтрация не-null значения
                .filter(labwork -> labwork.getMinimalPoint() == minimalPoint)   // Фильтрация по совпадению с minimalPoint
                .count();                                                       // Подсчет результатов
    }
    // Метод для сохранения коллекции LabWork в CSV файл
    public void saveCollection(String filename) throws IOException {
        try (BufferedOutputStream bufStream = new BufferedOutputStream(new FileOutputStream(filename))) {  // Создание буферизированного потока вывода для записи в файл
            for (LabWork lab : collection.values()) {
                String line = "ID; " + lab.getID() + "; \n"
                        + "Name; " + lab.getName() + "; \n"
                        + "Value; " + lab.getValue() + "; \n"
                        + "Coordinates on X; " +  Coordinates.getX() + "; \n"
                        + "Coordinates on Y; " + Coordinates.getY() + "; \n"
                        + "Creation date; " + lab.getCreationDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + "; \n"
                        + "Minimal point; " + lab.getMinimalPoint() + "; \n"
                        + "Personal qualities; " + lab.getPersonalQualitiesMinimum() + "; \n"
                        + "Difficulty; " + (lab.getDifficulty() != null ? lab.getDifficulty(): "null") + "; \n"
                        + "Discipline; " + (lab.getDiscipline() != null ? lab.getDiscipline().getName() : "null") + "; "
                        + "\n\n";
                bufStream.write(line.getBytes());  // Конвертирует строку в байты и записывает данные в буферизированный поток
            }
        }
    }
    // Метод для получения времени последней инициализации
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    // Метод для получения времени последнего сохранения
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }
    public void setLastSaveTime(LocalDateTime saveTime) {
        this.lastSaveTime = saveTime;
    }

    // Имя типа коллекции
    public String collectionType() {
        return collection.getClass().getName();
    }

    // Размер коллекции
    public int collectionSize() {
        return collection.size();
    }

    // Метод для удаления элемента по ключу
    public boolean removeByKey(Integer key) {
        if (key == null) {
            return false;
        }
        if (!collection.containsKey(key)) { // Проверка существование элемента с таким ключом
            return false;
        }
        collection.remove(key);  // Удаление только одного элемента с указанным ключом
        return true;
    }
    // Метод для удаления элемента по ключу
    public Optional<LabWork> getById(long id) {
        LabWork byKey = collection.get((int) id);   // Проверяем как ключ коллекции (быстрый поиск)
        if (byKey != null && byKey.getID() == id) {
            return Optional.of(byKey);
        }
        for (LabWork labWork : collection.values()) {  // Если не нашли по ключу, ищем по всем значениям (медленнее)
            if (labWork.getID() == id) {
                return Optional.of(labWork);
            }
        }
        return Optional.empty();
    }
    public boolean containsKey(int key) {
        return collection.containsKey(key);
    }

    public LabWork get(int key) {
        return collection.get(key);
    }

    public void put(int key, LabWork element) {
        collection.put(key, element);
    }

    public boolean insert(int key, LabWork lab) {
        if (lab == null) {
            return false;
        }
        collection.put(key, lab);
        return true;
    }
    public int removeKeysGreaterThan(int thresholdKey) {
        int count = 0;
        Iterator<Map.Entry<Integer, LabWork>> iterator = collection.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, LabWork> entry = iterator.next();
            if (entry.getKey() > thresholdKey) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }
    private String saveFilePath;

    public void setLastInitTime(LocalDateTime lastInitTime) {
        this.lastInitTime = lastInitTime;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }
    public String getSaveFilePath() {
        return this.saveFilePath;
    }
    public synchronized boolean replaceKey(Integer oldKey, Integer newKey) {
        if (!collection.containsKey(oldKey) || collection.containsKey(newKey)) {
            return false;
        }
        LabWork labWork = collection.remove(oldKey);
        collection.put(newKey, labWork);
        return true;
    }

}