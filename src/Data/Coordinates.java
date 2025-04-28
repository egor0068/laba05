package Data;

import Utilite.*;
import java.util.*;

public class Coordinates {
    // Задает координаты субъекта и действия с ними
    private static Integer x; // Значение должно быть больше -419, не может быть null
    private static long y; // Значение должно быть больше -942
    public Coordinates(Integer x, long y) {
        this.x = x;
        this.y = y;
        if (x == null || x <= -419) {
            throw new IllegalArgumentException("Координата x должна быть больше -419 и не может быть null.");
        }
        if (y <= -942) {
            throw new IllegalArgumentException("Координата y должна быть больше -942.");
        }
    }
    public void setX(Integer x){
        this.x = x;
    }
    public static Integer getX(){
        return x;
    }
    public void setY(long y){
        this.y = y;
    }
    public static long getY(){
        return y;
    }
    // проверка полей на правильность заданному условию

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates s = (Coordinates) o;
        return Objects.equals(x, s.x) && Objects.equals(y, s.y);
    }
}
