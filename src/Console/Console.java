package Console;

import java.util.Scanner;

public class Console {
    private final Scanner scanner;
    public Console() {
        this.scanner = new Scanner(System.in);
    }

    public void println(Object o) { //принимает объект "o" типа Object и выводит его строковое представление на консоль
        System.out.println(o);
    }

    public String readLine() {
        return scanner.nextLine();
    }
    public String readLine(String text) {
        System.out.print(text);
        return scanner.nextLine();
    }

}