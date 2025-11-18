package general;
import java.util.*;
/**
 * Главный класс программы для тестирования калькулятора выражений.
 * Предоставляет интерфейс командной строки для ввода выражений.
 * 
 * @author Кристина
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        SimpleCalculator calculator = new SimpleCalculator();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Калькулятор выражений ===");
        
        
        while (true) {
            System.out.print("\nВведите выражение (или 'выход'): ");
            String expression = scanner.nextLine();
            
            if (expression.equalsIgnoreCase("выход")) {
                break;
            }
            
            try {
                // Ищем переменные в выражении
                calculator.findVariables(expression);
                
                // Если есть переменные - запрашиваем их значения
                if (calculator.hasVariables()) {
                    calculator.askForVariables(scanner);
                }
                
                // Вычисляем результат
                double result = calculator.calculate(expression);
                System.out.println(">>> Результат: " + result);
                
            } catch (Exception e) {
                System.out.println("ОШИБКА: " + e.getMessage());
            }
        }
        
        System.out.println("Программа завершена. До свидания!");
        scanner.close();
    }
}