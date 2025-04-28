package CommandsInConsole;

import Collection.CollectionManager;
import Console.Console;
import Data.Coordinates;
import Data.LabWork;
import java.util.Iterator;
import java.util.Map;

public class RemGreaterCommand extends MainCommand {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemGreaterCommand(Console console, CollectionManager collectionManager) {
        super("remove_greater", "обнулить числовые поля, если они больше указанного значения");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    @Override
    public boolean apply(String[] arguments) {
        if (arguments.length < 2) {
            console.println("Используйте: remove_greater значение");
            console.println("Пример: remove_greater 5.5 - обнулит все числовые поля > 5.5");
            return false;
        }
        try {
            double threshold = Double.parseDouble(arguments[1]);
            int modifiedCount = 0;

            Iterator<Map.Entry<Integer, LabWork>> iterator =
                    collectionManager.getCollection().entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<Integer, LabWork> entry = iterator.next();
                LabWork lab = entry.getValue();
                boolean modified = false;
                if (lab.getValue() != null && lab.getValue() > threshold) {   // Проверка и обнуление каждого числового поля
                    lab.setValue(null);
                    modified = true;
                }
                if (lab.getCoordinates() != null) {  // Обработка координат
                    Coordinates coords = lab.getCoordinates();

                    if (coords.getX() > threshold) {
                        coords.setX(0); // Установка значения для X
                        modified = true;
                    }
                    if (coords.getY() > threshold) {
                        coords.setY(0L); // Установка значения для Y
                        modified = true;
                    }
                }
                if (lab.getMinimalPoint() != null && lab.getMinimalPoint() > threshold) {
                    lab.setMinimalPoint(null);
                    modified = true;
                }
                if (lab.getPersonalQualitiesMinimum() > threshold) {
                    lab.setPersonalQualitiesMinimum(0);
                    modified = true;
                }
                if (lab.getID() != null && lab.getID() > threshold) { }
                if (modified) {
                    modifiedCount++;
                    collectionManager.getCollection().put(entry.getKey(), lab);
                }
            }
            console.println("Изменено элементов: " + modifiedCount);
            return true;
        } catch (NumberFormatException e) {
            console.println("Ошибка! Укажите число в качестве аргумента.");
            return false;
        }
    }
}