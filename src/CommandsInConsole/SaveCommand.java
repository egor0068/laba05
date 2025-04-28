package CommandsInConsole;
//охранение коллекции в CSV файл

import Collection.CollectionManager;
import Console.Console;
import java.io.IOException;
import java.time.LocalDateTime;

public class SaveCommand extends MainCommand {
    private final Console console;
    private final CollectionManager collectionManager;

    public SaveCommand(Console console, CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    @Override
    public boolean apply(String[] arguments) {
        try {
            collectionManager.setLastSaveTime(LocalDateTime.now());

            String defaultFilename = "saveFile.csv"; // Добавляем расширение .csv по умолчанию
            if (arguments.length > 0) { // Если передан аргумент, используем его как имя файла
                String filename = arguments[0].endsWith(".csv") ? arguments[0] : arguments[0] + ".csv";
                collectionManager.saveCollection(filename);
                console.println(" ");
                console.println("Коллекция сохранена в файл: " + filename);
                console.println(" ");
            } else {
                collectionManager.saveCollection(defaultFilename);
                console.println("Коллекция сохранена в файл по умолчанию: " + defaultFilename);
            }
            return true;
        } catch (IOException e) {
            console.println("Ошибка сохранения: " + e.getMessage());
            return false;
        }
    }

}