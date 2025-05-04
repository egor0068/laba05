package CommandsInConsole;

import Collection.CollectionManager;
import Console.Console;
import java.io.IOException;
import java.time.LocalDateTime;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveCommand extends MainCommand {
    private final Console console;
    private CollectionManager collectionManager;

    public SaveCommand(Console console, CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] arguments) {
        try {
            collectionManager.setLastSaveTime(LocalDateTime.now());

            String filename;
            if (arguments.length > 0) {
                // Если передан аргумент, используем его как имя файла
                filename = arguments[0].endsWith(".csv") ? arguments[0] : arguments[0] + ".csv";
            } else {
                // Иначе используем имя по умолчанию
                filename = "saveFile.csv";
            }

            // Получаем абсолютный путь к файлу
            Path filePath = Paths.get(filename).toAbsolutePath();
            String fullPath = filePath.toString();

            // Сохраняем коллекцию
            collectionManager.saveCollection(fullPath);

            // Обновляем путь сохранения в менеджере коллекции
            collectionManager.setSaveFilePath(fullPath);

            console.println("\nКоллекция сохранена в файл: " + fullPath + "\n");
            return true;
        } catch (IOException e) {
            console.println("Ошибка сохранения: " + e.getMessage());
            return false;
        }
    }

    // Остальные методы остаются без изменений
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public String toString() {
        return "SaveCommand{" +
                "collectionManager=" + collectionManager +
                '}';
    }

    public Console getConsole() {
        return console;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
}