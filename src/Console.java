import jdk.jshell.spi.ExecutionControl;

import java.util.Objects;
import java.util.Scanner;

public class Console {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ExecutionControl.NotImplementedException {
        System.out.println("This is the Console Platform for Exploration Testing via Console.");
        System.out.println("Welcome to my test case generator, Lets start out with a Test case name");
        TestCase tc = new TestCase();
        boolean quit = false;
        while (!quit) {
            String input = scanner.nextLine();
            input = input.trim();
            input = input.toLowerCase();
            switch (input) {
                case "exit", "quit", "q": {
                    quit = true;
                    break;
                }
                case "help", "h": {
                    break;
                }
                case "new", "n": {
                    tc = new TestCase();
                }
                case "set module", "sm": {
                    System.out.println("What is the name of the module?");
                    tc.set_module_name(scanner.nextLine());
                    break;
                }
                case "set function", "sf": {
                    System.out.println("What is the name of the function?");
                    tc.set_function_name(scanner.nextLine());
                    break;
                }
                case "add argument", "aa": {
                    System.out.println("Here are the arguments: " + tc.get_arguments());
                    System.out.println("What is the type of the argument?");
                    String type = scanner.nextLine();
                    System.out.println("What is the value of the argument?");
                    String value = scanner.nextLine();
                    System.out.println("What is the index of the argument?");
                    int index = scanner.nextInt();
                    if (index >= tc.getNumberOfArguments()) {
                        tc.add_argument(value, type);
                        break;
                    }
                    tc.add_argument(value, type, index);
                    break;
                }
                case "remove argument", "ra": {
                    System.out.println("Which argument do you want to remove?");
                    tc.remove_argument(scanner.nextInt());
                    break;
                }
                case "set expected value", "se": {
                    System.out.println("What is the expected type?");
                    String type = scanner.nextLine();
                    System.out.println("What is the expected value?");
                    String value = scanner.nextLine();
                    tc.set_expected(type, value);

                }
                case "print", "p":{
                    System.out.println(tc.generateTestCode());
                }
                default: {
                    System.out.println("Invalid input: Try again.");
                }
            }
        }
    }
}
