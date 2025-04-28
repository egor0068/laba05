package CommandsInConsole;

//Команда 'update'. Обновляет ID  коллекции

import Collection.CollectionManager;
import Console.Console;
import Data.LabWork;
import java.util.Optional;

public class UpdateCommand extends MainCommand {
    private final Console console;
    private final CollectionManager collectionManager;

    public UpdateCommand(Console console, CollectionManager collectionManager) {
        super("update", "изменить ID элемента");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    @Override
    public boolean apply(String[] arguments) {
        if (arguments.length < 2) {
            console.println("Нужно ввести ID элемента для изменения");
            return false;
        }
        try {
            int oldId = Integer.parseInt(arguments[1]);

            Optional<LabWork> labWorkOpt = collectionManager.getById(oldId);
            if (!labWorkOpt.isPresent()) {
                console.println("Элемент с ID " + oldId + " не найден");
                return false;
            }

            LabWork labWork = labWorkOpt.get();
            console.println("Найден элемент: " + labWork.getName() + " (ID: " + oldId + ")");

            console.println("Введите новый ID: ");
            int newId = Integer.parseInt(console.readLine().trim());

            if (newId <= 0) {
                console.println("ID должен быть положительным числом!");
                return false;
            }

            if (collectionManager.getById(newId).isPresent()) {
                console.println("Элемент с ID " + newId + " уже существует!");
                return false;
            }

            labWork.setID(newId);
            console.println("ID успешно изменён на " + newId);
            return true;

        } catch (NumberFormatException e) {
            console.println("Ошибка: нужно вводить только числа для ID");
            return false;
        }
    }
}