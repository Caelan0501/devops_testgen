import java.util.ArrayList;
import java.lang.String;
import java.lang.IllegalArgumentException;

public class TestCase {
    //Defined Types to use
    record variable(String value, types type){}
    enum types{
        BOOLEAN,
        BYTE,
        SHORT,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        CHARACTER,
        STRING,
        ENUM
    }
    enum condition {
        Equal,
        NotEqual,
        Less,
        Greater
    }

    //Fields
    private String name;
    private String module_name;
    private String function_name;
    private final ArrayList<variable> arguments;
    private variable expected;
    private condition assertion;

    //Constructors
    TestCase(String name) {
        set_name(name);
        arguments = new ArrayList<>();
    }

    TestCase(String module_name, String function_name){
        set_name(module_name + "_" + function_name);
        set_module_name(module_name);
        set_function_name(function_name);
        arguments = new ArrayList<>();
    }

    TestCase(String name, String module_name, String function_name) {
        set_name(name);
        set_module_name(module_name);
        set_function_name(function_name);
        arguments = new ArrayList<>();
    }

    TestCase(TestCase testCase) {
        this.name = testCase.name;
        this.module_name = testCase.module_name;
        this.function_name = testCase.function_name;
        this.arguments = testCase.arguments;
        this.assertion = testCase.assertion;
        this.expected = testCase.expected;
    }

    //Accessors
    public void set_name(String name) {
        VerifyProgrammingTerm(name);
        this.name = name;
    }

    public void set_module_name(String module_name) {
        VerifyProgrammingTerm(module_name);
        this.module_name = module_name;
    }

    public void set_function_name(String function_name) {
        VerifyProgrammingTerm(function_name);
        this.function_name = function_name;
    }

    public int getNumberOfArguments() {
        return arguments.size();
    }

    public String get_arguments() {
        StringBuilder sb = new StringBuilder();
        for(variable args : arguments) {
            sb.append(args.type).append(" ").append(args.value).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public void add_argument(String value, String type) {
        variable v = new variable(value, processType(type));
        arguments.add(v);
    }

    public void add_argument(String value, String type, int index) {
        variable v = new variable(value, processType(type));
        arguments.add(index, v);
    }

    public void remove_argument(int index) {
        arguments.remove(index);
    }

    public void set_expected(String value, String type) {
        expected = new variable(value, processType(type));
    }

    public void set_assertion(String value, String type) {

    }

    //Service Method
    public String generateTestCode() {
        //Verify All fields are filled in
        if (name == null) {
            throw new IllegalStateException("name is null");
        }
        else if (module_name == null) {
            throw new IllegalStateException("module_name is null");
        }
        else if (function_name == null) {
            throw new IllegalStateException("function_name is null");
        }
        else if (expected == null) {
            throw new IllegalStateException("expected is null");
        }
        else if (assertion == null) {
            throw new IllegalStateException("assertion is null");
        }

        //Start Process
        StringBuilder testCode = new StringBuilder();
        testCode.append("\t@Test\n").append("\tvoid").append(name).append("() {\n");

        StringBuilder argsSb = new StringBuilder();
        for (int i = 0; i < arguments.size(); i++) {
            argsSb.append(arguments.get(i).value);
            if (i < arguments.size() - 1) {
                argsSb.append(", ");
            }
        }
        String functionCall = module_name + "." + function_name + "(" + argsSb + ")";
        String test = switch (expected.type) {
            case BOOLEAN -> (expected.value.equals("false") ? "\t\tassertFalse(" + functionCall + ")" : "\t\tassertTrue(" + functionCall + ")");
            case INTEGER, DOUBLE, FLOAT, BYTE, SHORT, LONG -> switch (assertion) {
                case Equal -> "\t\tassertEqual" + "(" + functionCall + ", " + expected + ")";
                case NotEqual -> "\t\tassertNotEqual" + "(" + functionCall + ", " + expected + ")";
                case Less -> "\t\tassertTrue" + "(" + functionCall + " < " + expected + ")";
                case Greater -> "\t\tassertTrue" + "(" + functionCall + " > " + expected + ")";
            };
            case CHARACTER, STRING, ENUM -> switch (assertion) {
                case Equal -> "\t\tassertEqual" + "(" + functionCall + ", " + expected + ")";
                case NotEqual -> "\t\tassertNotEqual" + "(" + functionCall + ", " + expected + ")";
                default -> throw new IllegalArgumentException("Cannot compare " + functionCall + " with " + expected);
            };
        };
        return testCode.append(test).toString();
    }

    //Helper Functions
    //Converts Strings to the enum identifier
    private types processType(String type) {
        type = type.toLowerCase();
        return switch(type) {
            case "boolean" -> types.BOOLEAN;
            case "byte" -> types.BYTE;
            case "short" -> types.SHORT;
            case "int", "integer" -> types.INTEGER;
            case "long" -> types.LONG;
            case "float" -> types.FLOAT;
            case "double" -> types.DOUBLE;
            case "character", "char" -> types.CHARACTER;
            case "string", "str" -> types.STRING;
            case "enum" -> types.ENUM;
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    //Function throws errors if the term breaks programming syntax
    private void VerifyProgrammingTerm(String term){
        char firstCharacter = term.charAt(0);
        if(Character.isDigit(firstCharacter)){
            throw new IllegalArgumentException("First character must not be a number.");
        }
        if(term.contains(" ")) {
            throw new IllegalArgumentException("Name must not contain spaces.");
        }
    }
}

