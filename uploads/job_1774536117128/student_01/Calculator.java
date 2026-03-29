public class Calculator {

    public int performCalculation(int a, int b) {
        int result = a + b;
        System.out.println("Adding numbers: " + a + " and " + b);
        return result;
    }

    public void greetUser(String name) {
        System.out.println("Hello, " + name + "!");
    }

    public static void main(String[] args) {
        Calculator c = new Calculator();
        c.performCalculation(5, 3);
        c.greetUser("Student");
    }
}
