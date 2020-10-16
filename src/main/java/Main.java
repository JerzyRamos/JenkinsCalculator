import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        Scanner myObj = new Scanner(System.in);
        System.out.println("Hi! Welcome to Jenkins Calculator!");
        System.out.println("Please enter a command: ");
        String line = myObj.nextLine();
        String[] input = line.split(" ");
        while (!line.equals("exit")) {
            if (input[0].equals("add")) {
                System.out.println(calc.add(Integer.parseInt(input[1]),Integer.parseInt(input[2])));
            }
            if (input[0].equals("subtract")) {
                System.out.println(calc.subtract(Integer.parseInt(input[1]),Integer.parseInt(input[2])));
            }
            if (input[0].equals("divide")) {
                System.out.println(calc.divide(Integer.parseInt(input[1]),Integer.parseInt(input[2])));
            }
            if (input[0].equals("fibonacci")) {
                System.out.println(calc.fibonacciNumberFinder(Integer.parseInt(input[1])));
            }
            if (input[0].equals("binary")) {
                System.out.println(calc.intToBinaryNumber(Integer.parseInt(input[1])));
            }
            line = myObj.nextLine();
            input = line.split(" ");
        }

        System.exit(0);

    }
}
