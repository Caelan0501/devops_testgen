import java.util.List;
import java.lang.String;
import java.lang.IllegalArgumentException;

public class TestCase {
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

    String name;
    String module_name;
    String function_name;
    List<variable> arguments;
    variable expected;
    condition assertion;

    TestCase() {
        this.name = "Default";
    }

    TestCase(String name) {
        this.name = name;
    }

    TestCase(String module_name, String function_name){
        this.name = module_name + "." + function_name;
        this.module_name = module_name;
        this.function_name = function_name;
    }

    TestCase(String name, String module_name, String function_name) {
        this.name = name;
        this.module_name = module_name;
        this.function_name = function_name;
    }

    TestCase(TestCase testCase) {
        this.name = testCase.name;
        this.module_name = testCase.module_name;
        this.function_name = testCase.function_name;
        this.arguments = testCase.arguments;
        this.assertion = testCase.assertion;
        this.expected = testCase.expected;
    }

    String generateTestCode() {
        return "\t@Test\n" +
                "\tvoid" + name + "() {\n" +
                printCondition();
    }

    private String printCondition() {
        StringBuilder argsSb = new StringBuilder();
        for (int i = 0; i < arguments.size(); i++) {
            argsSb.append(arguments.get(i).value);
            if (i < arguments.size() - 1) {
                argsSb.append(", ");
            }
        }
        String functionCall = module_name + "." + function_name + "(" + argsSb + ")";
        String cond;
        return switch (expected.type) {
            case BOOLEAN -> {
                if (expected.value.equals("false")) {
                    cond = "assertFalse";
                } else {
                    cond = "assertTrue";
                }
                yield "\t\t" + cond + "(" + functionCall + ")";
            }
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
    }
}

