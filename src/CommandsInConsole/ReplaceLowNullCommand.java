package CommandsInConsole;

import Collection.CollectionManager;
import Console.Console;
import Data.LabWork;

public class ReplaceLowNullCommand extends MainCommand {
    private final Console console;
    private final CollectionManager collection;

    public ReplaceLowNullCommand(Console console, CollectionManager collection) {
        super("replacelowNull", "заменить элемент если новое значение меньше");
        this.console = console;
        this.collection = collection;
    }
    @Override
    public boolean apply(String[] args) {
        try {
            console.println("Введите ключ элемента: ");
            int key = Integer.parseInt(console.readLine());
            if (!collection.containsKey(key)) {
                console.println("Элемент с ключом " + key + " не найден");
                return false;
            }
            console.println("Введите новые данные:");
            console.println("Название: ");
            String name = console.readLine();

            console.println("Оценка: ");
            double value  = Double.parseDouble(console.readLine());

            console.println("Минимальный балл: ");
            double minimalPoint = Double.parseDouble(console.readLine());

            console.println("Личные качества: ");
            float personalQualitiesMinimum = Float.parseFloat(console.readLine());

            LabWork newElement = new LabWork();
            newElement.setName(name);
            newElement.setMinimalPoint(minimalPoint);
            newElement.setValue(value);
            newElement.setPersonalQualitiesMinimum(personalQualitiesMinimum);
            LabWork oldElement = collection.get(key);

            if (newElement.getMinimalPoint() < oldElement.getMinimalPoint()) {
                collection.put(key, newElement);
                collection.saveCollection(collection.getSaveFilePath());
                console.println("Элемент успешно заменен");
                return true;
            } else {
                console.println("Условие замены не выполнено (новый балл не меньше)");
                return false;
            }
        } catch (NumberFormatException e) {
            console.println("Ошибка: введите корректное число");
            return false;
        } catch (Exception e) {
            console.println("Ошибка: " + e.getMessage());
            return false;
        }
    }
}