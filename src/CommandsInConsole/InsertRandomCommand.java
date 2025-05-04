package CommandsInConsole;

import Collection.CollectionManager;
import Console.Console;
import Data.Coordinates;
import Data.Discipline;
import Data.LabWork;
import Enum.DifficultyEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Random;

public class InsertRandomCommand extends MainCommand {
    private final Console console;
    private final CollectionManager collectionManager;
    private final Random random = new Random();
    private volatile boolean stopGeneration = false;

    public InsertRandomCommand(Console console, CollectionManager collectionManager) {
        super("insert_random", "добавить случайные элементы (для остановки нажмите Enter)");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] arguments) {
        try {
            if (arguments.length > 0) {
                // Пакетный режим с указанием количества
                return runBatchMode(arguments);
            } else {
                // Интерактивный режим с возможностью прерывания
                return runInteractiveMode();
            }
        } catch (Exception e) {
            console.println("Ошибка: " + e.getMessage());
            return false;
        }
    }

    private boolean runInteractiveMode() {
        console.println("Начало генерации элементов. Нажмите Enter для остановки...");

        // Запускаем поток для отслеживания нажатия Enter
        Thread inputThread = new Thread(() -> {
            try {
                System.in.read(); // Блокируется до нажатия Enter
                stopGeneration = true;
            } catch (IOException e) {
                // Игнорируем ошибки ввода
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();

        int counter = 0;
        try {
            while (!stopGeneration) {
                LabWork labWork = generateRandomLabWork();
                int key = generateUniqueKey();

                collectionManager.insert(key, labWork);
                counter++;
                console.println("Создан элемент #" + counter + " с ключом " + key);

                // Небольшая пауза между элементами
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            console.println("Генерация остановлена. Создано элементов: " + counter);
            stopGeneration = false; // Сброс флага для следующего использования
        }
        return true;
    }

    private boolean runBatchMode(String[] arguments) {
        try {
            int count = Integer.parseInt(arguments[0]);
            if (count <= 0) {
                console.println("Ошибка: количество должно быть положительным числом");
                return false;
            }

            for (int i = 0; i < count; i++) {
                LabWork labWork = generateRandomLabWork();
                int key = generateUniqueKey();
                collectionManager.insert(key, labWork);
                console.println("Создан элемент #" + (i + 1) + " с ключом " + key);
            }
            return true;
        } catch (NumberFormatException e) {
            console.println("Ошибка: неверный формат числа");
            return false;
        }
    }

    private int generateUniqueKey() {
        int key;
        do {
            key = random.nextInt(1000) + 1;
        } while (collectionManager.getCollection().containsKey(key));
        return key;
    }

    // Методы генерации данных остаются без изменений
    private LabWork generateRandomLabWork() {
        return new LabWork(
                generateRandomValue(),
                generateRandomName(),
                generateRandomCoordinates(),
                LocalDateTime.now(),
                generateRandomMinimalPoint(),
                generateRandomPersonalQualities(),
                generateRandomDifficulty(),
                generateRandomDiscipline()
        );
    }

    private double generateRandomValue() {
        return Math.round(random.nextDouble() * 100 * 100) / 100.0;
    }

    private String generateRandomName() {
        return " " + random.nextInt(1000);
    }

    private Coordinates generateRandomCoordinates() {
        return new Coordinates(
                random.nextInt(838) - 419,
                Math.abs(random.nextLong())
        );
    }

    private double generateRandomMinimalPoint() {
        return Math.round(random.nextDouble() * 100 * 100) / 100.0;
    }

    private float generateRandomPersonalQualities() {
        return Math.round(random.nextFloat() * 10 * 100) / 100.0f;
    }

    private DifficultyEnum generateRandomDifficulty() {
        DifficultyEnum[] difficulty = DifficultyEnum.values();
        return difficulty[random.nextInt(difficulty.length)];
    }

    private Discipline generateRandomDiscipline() {
        return new Discipline(
                "AutoDiscipline_" + random.nextInt(100),
                random.nextInt(100) + 1
        );
    }
}