import java.util.ArrayList;
import java.lang.String;
import java.lang.IllegalArgumentException;
import java.util.EnumSet;
import java.util.Map;

public class TestCase {
    //Defined Types to use
    private record variable(String value, Type type){}
    private enum Type {
        BOOLEAN,
        BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE,
        STRING,
        CHARACTER,
        ENUM
    }
    private static final EnumSet<Type> numericTypes = EnumSet.of(Type.BYTE, Type.SHORT, Type.INTEGER, Type.LONG, Type.FLOAT, Type.DOUBLE);
    private enum Assertion {
        Equal, NotEqual, Null, NotNull, Same, NotSame,
        True, False,
        Less, Greater, LessEqual, GreaterEqual, Between,
        Letter, Digit, Whitespace, Uppercase, Lowercase, Alphanumeric,
        Contains, NotContains, StartsWith, EndsWith, LengthEqual, Empty, NotEmpty
    }

    //Used to check Compatability between Expected and Assertion Variables
    private static final Map<Assertion, EnumSet<Type>> ASSERTION_COMPATIBILITY = Map.ofEntries(
            Map.entry(Assertion.Equal, EnumSet.allOf(Type.class)),
            Map.entry(Assertion.NotEqual, EnumSet.allOf(Type.class)),
            Map.entry(Assertion.Null, EnumSet.allOf(Type.class)),
            Map.entry(Assertion.NotNull, EnumSet.allOf(Type.class)),
            Map.entry(Assertion.Same, EnumSet.allOf(Type.class)),
            Map.entry(Assertion.NotSame, EnumSet.allOf(Type.class)),

            Map.entry(Assertion.True, EnumSet.of(Type.BOOLEAN)),
            Map.entry(Assertion.False, EnumSet.of(Type.BOOLEAN)),

            Map.entry(Assertion.Greater, numericTypes),
            Map.entry(Assertion.Less, numericTypes),
            Map.entry(Assertion.LessEqual, numericTypes),
            Map.entry(Assertion.GreaterEqual, numericTypes),
            Map.entry(Assertion.Between, numericTypes),

            Map.entry(Assertion.Letter, EnumSet.of(Type.CHARACTER)),
            Map.entry(Assertion.Digit, EnumSet.of(Type.CHARACTER)),
            Map.entry(Assertion.Whitespace, EnumSet.of(Type.CHARACTER)),
            Map.entry(Assertion.Uppercase, EnumSet.of(Type.CHARACTER)),
            Map.entry(Assertion.Lowercase, EnumSet.of(Type.CHARACTER)),
            Map.entry(Assertion.Alphanumeric, EnumSet.of(Type.CHARACTER)),

            Map.entry(Assertion.Contains, EnumSet.of(Type.STRING)),
            Map.entry(Assertion.NotContains, EnumSet.of(Type.STRING)),
            Map.entry(Assertion.StartsWith, EnumSet.of(Type.STRING)),
            Map.entry(Assertion.EndsWith, EnumSet.of(Type.STRING)),
            Map.entry(Assertion.LengthEqual, EnumSet.of(Type.STRING)),
            Map.entry(Assertion.Empty, EnumSet.of(Type.STRING)),
            Map.entry(Assertion.NotEmpty, EnumSet.of(Type.STRING))
    );

    //Fields
    private String name;
    private String module_name;
    private String function_name;
    private final ArrayList<variable> arguments;
    private variable expected;
    private Assertion assertion;

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

    public void set_assertion(String assertionString) {
        Assertion assertion = switch (assertionString) {
            case "true" -> Assertion.True;
            case "false" -> Assertion.False;
            case "null" -> Assertion.Null;
            case "notnull" -> Assertion.NotNull;
            case "same" -> Assertion.Same;
            case "notsame" -> Assertion.NotSame;
            //Add More Here
            default -> throw new IllegalArgumentException("Invalid assertion string: " + assertionString);
        };
        if (expected != null) {
            validateCompatibility(expected.type,  assertion);
        }
        this.assertion = assertion;
    }

    //Service Method

    //Rework needed
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
    private Type processType(String type) {
        type = type.toLowerCase();
        return switch(type) {
            case "boolean" -> Type.BOOLEAN;
            case "byte" -> Type.BYTE;
            case "short" -> Type.SHORT;
            case "int", "integer" -> Type.INTEGER;
            case "long" -> Type.LONG;
            case "float" -> Type.FLOAT;
            case "double" -> Type.DOUBLE;
            case "character", "char" -> Type.CHARACTER;
            case "string", "str" -> Type.STRING;
            case "enum" -> Type.ENUM;
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

    //Check Usability
    //Function helps validate Compatability between the expected type and the assertion given.
    private void validateCompatibility(Type type, Assertion assertion) {
        EnumSet<Type> compatibleTypes = ASSERTION_COMPATIBILITY.getOrDefault(assertion, EnumSet.noneOf(Type.class));
        if (!compatibleTypes.contains(type)) {
            throw new IllegalArgumentException("Assertion " + assertion + " not valid for type " + type);
        }
    }
}