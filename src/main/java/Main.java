public class Main {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        if (args[0].equals("add")) {
            System.out.println(calc.add(Integer.parseInt(args[1]),Integer.parseInt(args[2])));
        }
        if (args[0].equals("subtract")) {
            System.out.println(calc.subtract(Integer.parseInt(args[1]),Integer.parseInt(args[2])));
        }
        if (args[0].equals("divide")) {
            System.out.println(calc.divide(Integer.parseInt(args[1]),Integer.parseInt(args[2])));
        }
        if (args[0].equals("fibonacci")) {
            System.out.println(calc.fibonacciNumberFinder(Integer.parseInt(args[1])));
        }
        if (args[0].equals("binary")) {
            System.out.println(calc.intToBinaryNumber(Integer.parseInt(args[1])));
        }
    }
}
