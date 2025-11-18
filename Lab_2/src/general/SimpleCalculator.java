package general;

import java.util.*;

/**
 * Класс для вычисления математических выражений.
 * Поддерживает базовые арифметические операции, функции, переменные и скобки.
 * 
 * @author Кристина
 */
public class SimpleCalculator {
    private Map<String, Double> variables;
    
    /**
     * Конструктор по умолчанию. Создает пустой калькулятор.
     */
    public SimpleCalculator() {
        variables = new HashMap<>();
    }
    
    /**
     * Проверяет, есть ли в текущем выражении переменные.
     * 
     * @return true если есть переменные, иначе false
     */
    public boolean hasVariables() {
        return !variables.isEmpty();
    }
    
    /**
     * Возвращает множество имен переменных, найденных в выражении.
     * 
     * @return множество имен переменных
     */
    public Set<String> getVariableNames() {
        return new HashSet<>(variables.keySet());
    }
    
    /**
     * Основной метод для вычисления математического выражения.
     * Выполняет проверку корректности, вычисление функций, подстановку переменных и вычисление.
     * 
     * @param expression математическое выражение для вычисления
     * @return результат вычисления выражения
     * @throws IllegalArgumentException если выражение некорректно
     * @throws ArithmeticException при делении на ноль
     */
    public double calculate(String expression) {
        System.out.println("Вычисляем: " + expression);
        
        if (!isValidExpression(expression)) {
            throw new IllegalArgumentException("Выражение записано некорректно");
        }
        
        String step1 = calculateFunctions(expression);
        System.out.println("После функций: " + step1);
        
        String step2 = replaceVariables(step1);
        System.out.println("После переменных: " + step2);
        
        String step3 = calculateBrackets(step2);
        System.out.println("После скобок: " + step3);
        
        double result = calculateSimple(step3);
        System.out.println("Финальный результат: " + result);
        
        return result;
    }
    
    /**
     * Устанавливает значение переменной.
     * 
     * @param name имя переменной
     * @param value значение переменной
     */
    public void setVariable(String name, double value) {
        variables.put(name, value);
    }
    
    /**
     * Находит все переменные в выражении и добавляет их в хранилище.
     * Игнорирует зарезервированные имена функций.
     * 
     * @param expression выражение для анализа
     */
    public void findVariables(String expression) {
        variables.clear();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isLetter(c)) {
                StringBuilder varName = new StringBuilder();
                while (i < expression.length() && Character.isLetter(expression.charAt(i))) {
                    varName.append(expression.charAt(i));
                    i++;
                }
                String name = varName.toString();
                if (!name.equals("sin") && !name.equals("cos") && 
                    !name.equals("tan") && !name.equals("sqrt")) {
                    variables.put(name, 0.0);
                }
                i--;
            }
        }
    }
    
    /**
     * Запрашивает у пользователя значения всех найденных переменных.
     * 
     * @param scanner объект Scanner для ввода данных
     */
    public void askForVariables(Scanner scanner) {
        System.out.println("Найдены переменные: " + getVariableNames());
        
        for (String varName : getVariableNames()) {
            while (true) {
                System.out.print("Введите значение для " + varName + ": ");
                try {
                    double value = scanner.nextDouble();
                    setVariable(varName, value);
                    break;
                } catch (Exception e) {
                    System.out.println("Ошибка! Введите число.");
                    scanner.nextLine();
                }
            }
        }
        scanner.nextLine();
    }
    
    //ПРИВАТНЫЕ МЕТОДЫ 
    
    /**
     * Проверяет корректность математического выражения.
     * 
     * @param expression выражение для проверки
     * @return true если выражение корректно, иначе false
     */
    private boolean isValidExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }
        
        String expr = expression.replaceAll("\\s+", "");
        
        // Проверка на допустимые символы
        if (!expr.matches("[0-9a-zA-Z+\\-*/().]+")) {
            return false;
        }
        
        // Проверка баланса скобок
        int bracketCount = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') {
                bracketCount++;
            } else if (c == ')') {
                bracketCount--;
                if (bracketCount < 0) {
                    return false;
                }
            }
        }
        if (bracketCount != 0) {
            return false;
        }
        
        // Проверка что выражение не начинается/не заканчивается оператором
        if (isOperator(expr.charAt(0)) && expr.charAt(0) != '-') {
            return false;
        }
        if (isOperator(expr.charAt(expr.length() - 1))) {
            return false;
        }
        
        // Проверка на два оператора подряд
        for (int i = 0; i < expr.length() - 1; i++) {
            char current = expr.charAt(i);
            char next = expr.charAt(i + 1);
            if (isOperator(current) && isOperator(next) && !(current == '-' && Character.isDigit(next))) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Вычисляет все математические функции в выражении.
     * 
     * @param expression выражение с функциями
     * @return выражение с замененными функциями на их значения
     */
    private String calculateFunctions(String expression) {
        String result = expression;
        String[] functions = {"sin", "cos", "tan", "sqrt"};
        
        for (String func : functions) {
            while (result.contains(func)) {
                int funcStart = result.indexOf(func);
                int bracketStart = result.indexOf("(", funcStart);
                
                // Проверяем что после имени функции сразу идет скобка
                if (bracketStart != funcStart + func.length()) {
                    throw new IllegalArgumentException("Некорректный вызов функции " + func);
                }
                
                int bracketEnd = findMatchingBracket(result, bracketStart);
                
                if (bracketEnd == -1) {
                    throw new IllegalArgumentException("Непарные скобки у функции " + func);
                }
                
                // Вычисляем аргумент функции рекурсивно
                String insideFunc = result.substring(bracketStart + 1, bracketEnd);
                double insideValue = calculate(insideFunc);
                double funcResult = applyFunction(func, insideValue);
                
                // Заменяем вызов функции на результат
                String before = result.substring(0, funcStart);
                String after = result.substring(bracketEnd + 1);
                result = before + funcResult + after;
            }
        }
        
        return result;
    }
    
    /**
     * Находит парную закрывающую скобку для открывающей.
     * 
     * @param expression выражение со скобками
     * @param startBracket позиция открывающей скобки
     * @return позиция закрывающей скобки или -1 если не найдена
     */
    private int findMatchingBracket(String expression, int startBracket) {
        int count = 1;
        for (int i = startBracket + 1; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '(') count++;
            else if (c == ')') count--;
            
            if (count == 0) return i;
        }
        return -1;
    }
    
    /**
     * Применяет математическую функцию к числу.
     * 
     * @param funcName имя функции (sin, cos, tan, sqrt)
     * @param value аргумент функции
     * @return результат применения функции
     * @throws IllegalArgumentException при неизвестной функции или корне из отрицательного числа
     */
    private double applyFunction(String funcName, double value) {
        switch (funcName) {
            case "sin": return Math.sin(value);
            case "cos": return Math.cos(value);
            case "tan": return Math.tan(value);
            case "sqrt":
                if (value < 0) throw new IllegalArgumentException("Корень из отрицательного числа");
                return Math.sqrt(value);
            default: throw new IllegalArgumentException("Неизвестная функция: " + funcName);
        }
    }
    
    /**
     * Заменяет имена переменных на их числовые значения.
     * 
     * @param expression выражение с переменными
     * @return выражение с замененными переменными на числа
     */
    private String replaceVariables(String expression) {
        String result = expression;
        for (String varName : variables.keySet()) {
            double value = variables.get(varName);
            result = result.replace(varName, String.valueOf(value));
        }
        return result;
    }
    
    /**
     * Вычисляет все выражения в скобках.
     * 
     * @param expression выражение со скобками
     * @return выражение с вычисленными скобками
     */
    private String calculateBrackets(String expression) {
        String result = expression;
        
        // Обрабатываем самые внутренние скобки 
        while (result.contains("(")) {
            int openBracket = result.lastIndexOf("(");
            int closeBracket = findMatchingBracket(result, openBracket);
            
            if (closeBracket == -1) {
                throw new IllegalArgumentException("Непарные скобки в выражении");
            }
            
            String insideBrackets = result.substring(openBracket + 1, closeBracket);
            double bracketResult = calculateSimple(insideBrackets);
            
            // Заменяем скобки на результат
            String before = result.substring(0, openBracket);
            String after = result.substring(closeBracket + 1);
            result = before + bracketResult + after;
        }
        
        return result;
    }
    
    /**
     * Вычисляет простое выражение без скобок и функций.
     * Сначала выполняет умножение и деление, затем сложение и вычитание.
     * 
     * @param expression простое выражение
     * @return результат вычисления
     */
    private double calculateSimple(String expression) {
        expression = expression.replaceAll(" ", "");
        
        if (expression.isEmpty()) {
            return 0;
        }
        
        List<String> parts = splitExpression(expression);
        parts = calculateAllMultiplicationDivision(parts);
        return calculateAllAdditionSubtraction(parts);
    }
    
    /**
     * Выполняет все операции умножения и деления в списке частей выражения.
     * 
     * @param parts список частей выражения (числа и операторы)
     * @return список с выполненными операциями умножения и деления
     */
    private List<String> calculateAllMultiplicationDivision(List<String> parts) {
        List<String> result = new ArrayList<>(parts);
        
        int i = 1;
        while (i < result.size() - 1) {
            String operator = result.get(i);
            
            if (operator.equals("*") || operator.equals("/")) {
                double left = Double.parseDouble(result.get(i - 1));
                double right = Double.parseDouble(result.get(i + 1));
                double operationResult;
                
                if (operator.equals("*")) {
                    operationResult = left * right;
                } else {
                    if (right == 0) throw new ArithmeticException("Деление на ноль!");
                    operationResult = left / right;
                }
                
                // Заменяем три элемента на результат операции
                result.set(i - 1, String.valueOf(operationResult));
                result.remove(i); // удаляем оператор
                result.remove(i); // удаляем правый операнд
            } else {
                i += 2; // переходим к следующему оператору
            }
        }
        
        return result;
    }
    
    /**
     * Выполняет все операции сложения и вычитания.
     * 
     * @param parts список частей выражения (после умножения/деления)
     * @return финальный результат вычисления
     */
    private double calculateAllAdditionSubtraction(List<String> parts) {
        double result = Double.parseDouble(parts.get(0));
        
        for (int i = 1; i < parts.size(); i += 2) {
            String operator = parts.get(i);
            double number = Double.parseDouble(parts.get(i + 1));
            
            if (operator.equals("+")) {
                result += number;
            } else if (operator.equals("-")) {
                result -= number;
            }
        }
        
        return result;
    }
    
    /**
     * Разбивает выражение на числа и операторы.
     * 
     * @param expression выражение для разбора
     * @return список чисел и операторов в порядке их следования
     */
    private List<String> splitExpression(String expression) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentNumber = new StringBuilder();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (currentNumber.length() > 0) {
                    parts.add(currentNumber.toString());
                    currentNumber = new StringBuilder();
                }
                parts.add(String.valueOf(c));
            } else {
                currentNumber.append(c);
            }
        }
        
        if (currentNumber.length() > 0) {
            parts.add(currentNumber.toString());
        }
        
        return parts;
    }
    
    /**
     * Проверяет, является ли символ математическим оператором.
     * 
     * @param c символ для проверки
     * @return true если символ является оператором, иначе false
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}