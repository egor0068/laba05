package CommandsInConsole;


import Console.Console;
import java.io.*;

public class ReaderFileCommand extends MainCommand {
    private final Console console;

    public ReaderFileCommand(Console console) {
        super("read", "показать сохранённые элементы коллекции");
        this.console = console;
    }

    @Override
    public boolean apply(String[] arguments) {
        String filename = "saveFile.csv";

        try {
            // Открываем файл для чтения
            FileReader fileReader = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fileReader);

            console.println("Содержимое коллекции:");

            // Читаем файл построчно
            String line;
            while ((line = reader.readLine()) != null) {
                console.println(line);
            }

            // Закрываем файл
            reader.close();
            return true;

        } catch (FileNotFoundException e) {
            console.println("Файл не найден!");
            return false;
        } catch (IOException e) {
            console.println("Ошибка при чтении файла: " + e.getMessage());
            return false;
        }
    }
}
