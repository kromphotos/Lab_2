package general;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для класса SimpleCalculator с JUnit 5
 */
public class SimpleCalculatorTest {
    
    // ========== ТЕСТЫ БАЗОВЫХ ОПЕРАЦИЙ ==========
    
    @Test
    public void testSimpleAddition() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("2 + 3");
        assertEquals(5.0, result, 0.001);
    }
    
    @Test
    public void testSimpleSubtraction() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("5 - 3");
        assertEquals(2.0, result, 0.001);
    }
    
    @Test
    public void testSimpleMultiplication() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("2 * 3");
        assertEquals(6.0, result, 0.001);
    }
    
    @Test
    public void testSimpleDivision() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("6 / 2");
        assertEquals(3.0, result, 0.001);
    }
    
    // ========== ТЕСТЫ ПРИОРИТЕТА ОПЕРАЦИЙ ==========
    
    @Test
    public void testMultiplicationBeforeAddition() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("2 + 3 * 4");
        assertEquals(14.0, result, 0.001);
    }
    
    @Test
    public void testBracketsChangePriority() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("(2 + 3) * 4");
        assertEquals(20.0, result, 0.001);
    }
    
    // ========== ТЕСТЫ ФУНКЦИЙ ==========
    
    @Test
    public void testSinFunction() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("sin(0)");
        assertEquals(0.0, result, 0.001);
    }
    
    @Test
    public void testCosFunction() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("cos(0)");
        assertEquals(1.0, result, 0.001);
    }
    
    @Test
    public void testSqrtFunction() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("sqrt(16)");
        assertEquals(4.0, result, 0.001);
    }
    
    // ========== ТЕСТЫ ПЕРЕМЕННЫХ ==========
    
    @Test
    public void testVariables() {
        SimpleCalculator calculator = new SimpleCalculator();
        calculator.setVariable("x", 5.0);
        calculator.setVariable("y", 3.0);
        double result = calculator.calculate("x + y");
        assertEquals(8.0, result, 0.001);
    }
    
    // ========== ТЕСТЫ ОШИБОК ==========
    
    @Test
    public void testDivisionByZero() {
        SimpleCalculator calculator = new SimpleCalculator();
        Exception exception = assertThrows(ArithmeticException.class, () -> {
            calculator.calculate("5 / 0");
        });
        assertTrue(exception.getMessage().contains("Деление на ноль"));
    }
    
    @Test
    public void testSqrtOfNegativeNumber() {
        SimpleCalculator calculator = new SimpleCalculator();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate("sqrt(-1)");
        });
        assertTrue(exception.getMessage().contains("Корень из отрицательного"));
    }
    
    @Test
    public void testInvalidExpression() {
        SimpleCalculator calculator = new SimpleCalculator();
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate("2 + + 3");
        });
    }
    
    @Test
    public void testUnmatchedBrackets() {
        SimpleCalculator calculator = new SimpleCalculator();
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate("(2 + 3");
        });
    }
    
    // ========== ТЕСТЫ СЛОЖНЫХ ВЫРАЖЕНИЙ ==========
    
    @Test
    public void testComplexExpression() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("(2 + 3 * 4) / (5 - 1) + sqrt(9)");
        assertEquals(6.5, result, 0.001);
    }
    
    @Test
    public void testMultipleFunctions() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("sin(0) + cos(0) * sqrt(4)");
        assertEquals(2.0, result, 0.001);
    }
    
    @Test
    public void testDecimalNumbers() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("3.5 * 2.0 + 1.5");
        assertEquals(8.5, result, 0.001);
    }
    
    // ========== ТЕСТЫ ВЫРАЖЕНИЙ С ПРОБЕЛАМИ ==========
    
    @Test
    public void testExpressionWithSpaces() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("  2   +   3   *   4  ");
        assertEquals(14.0, result, 0.001);
    }
    
    @Test
    public void testExpressionWithoutSpaces() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("2+3*4");
        assertEquals(14.0, result, 0.001);
    }
    
    // ========== ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ ==========
    
    @Test
    public void testSingleNumber() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("42");
        assertEquals(42.0, result, 0.001);
    }
    
    @Test
    public void testSingleNegativeNumber() {
        SimpleCalculator calculator = new SimpleCalculator();
        double result = calculator.calculate("-5");
        assertEquals(-5.0, result, 0.001);
    }
    
    @Test
    public void testFindVariables() {
        SimpleCalculator calculator = new SimpleCalculator();
        calculator.findVariables("x + y * width");
        assertTrue(calculator.hasVariables());
        assertTrue(calculator.getVariableNames().contains("x"));
        assertTrue(calculator.getVariableNames().contains("y"));
        assertTrue(calculator.getVariableNames().contains("width"));
    }
    
    @Test
    public void testFunctionsNotTreatedAsVariables() {
        SimpleCalculator calculator = new SimpleCalculator();
        calculator.findVariables("sin(x) + cos(y)");
        assertTrue(calculator.getVariableNames().contains("x"));
        assertTrue(calculator.getVariableNames().contains("y"));
        assertFalse(calculator.getVariableNames().contains("sin"));
        assertFalse(calculator.getVariableNames().contains("cos"));
    }
    
    @Test
    public void testEmptyExpression() {
        SimpleCalculator calculator = new SimpleCalculator();
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate("");
        });
    }
    
    @Test
    public void testExpressionWithInvalidCharacters() {
        SimpleCalculator calculator = new SimpleCalculator();
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate("2 @ 3");
        });
    }
    
    @Test
    public void testComplexVariableExpression() {
        SimpleCalculator calculator = new SimpleCalculator();
        calculator.setVariable("a", 2.0);
        calculator.setVariable("b", 3.0);
        calculator.setVariable("c", 4.0);
        double result = calculator.calculate("(a + b) * c");
        assertEquals(20.0, result, 0.001);
    }
}