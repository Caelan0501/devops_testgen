import java.util.Objects;
import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        System.out.println("This is the Console Platform for Exploration Testing via Console.");
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        System.out.println("Welcome to my test case generator, Lets start out with a Test case name");
        TestCase tc = new TestCase(scanner.nextLine());
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
                    System.out.println("Do you want to name the test case after the module and function names?");
                    if(Objects.equals(scanner.nextLine(), "y")) {
                        System.out.println("What is the name of the module?");
                        String module = scanner.nextLine();
                        System.out.println("What is the name of the function?");
                        String function = scanner.nextLine();
                        tc = new TestCase(module, function);
                    }
                    else {
                        System.out.println("What is the name of the test?");
                        String name = scanner.nextLine();
                        System.out.println("Would you like to add a module and function name?");
                        if(Objects.equals(scanner.nextLine(), "y")) {
                            System.out.println("What is the name of the module?");
                            String module = scanner.nextLine();
                            System.out.println("What is the name of the function?");
                            String function = scanner.nextLine();
                            tc = new TestCase(name, module, function);
                        }
                        else {
                            tc = new TestCase(name);
                        }
                    }
                    break;
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
                    System.out.println("What is the type of the argument?");
                    String type = scanner.nextLine();
                    System.out.println("What is the value of the argument?");
                    String value = scanner.nextLine();
                    tc.add_argument(type, value);
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
                    tc.change_expected(type, value);

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
