import java.util.ArrayList;
import java.lang.String;
import java.lang.IllegalArgumentException;
import java.util.EnumSet;
import java.util.Map;

public class TestCase {
    //Defined Types to use
    static final EnumSet<Type> numericTypes = EnumSet.of(Type.BYTE, Type.SHORT, Type.INTEGER, Type.LONG, Type.FLOAT, Type.DOUBLE);
    //Used to check Compatability between Expected and Assertion Variables
    static final Map<Assertion, EnumSet<Type>> ASSERTION_COMPATIBILITY = Map.ofEntries(
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
    String name;
    String module_name;
    String function_name;
    final ArrayList<Variable> arguments;
    Variable expected;
    Assertion assertion;

    //Constructors
    TestCase() {
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
    public String get_name(){
        return name;
    }

    public void set_name(String name) {
        VerifyProgrammingTerm(name);
        this.name = name;
    }

    public String get_module_name(){
        return module_name;
    }

    public void set_module_name(String module_name) {
        VerifyProgrammingTerm(module_name);
        this.module_name = module_name;
    }

    public String get_function_name(){
        return function_name;
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
        for(Variable args : arguments) {
            sb.append(args.type()).append(" ").append(args.value()).append(",");
        }
        if (!sb.isEmpty()) {
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    public void add_argument(String value, String type) {
        add_argument(new Variable(value, processType(type)));
    }

    void add_argument(Variable v) {
        arguments.add(v);
    }

    public void add_argument(String value, String type, int index) {
        add_argument(new Variable(value, processType(type)),  index);
    }

    void add_argument(Variable v, int index) {
        arguments.add(index, v);
    }

    public void remove_argument(int index) {
        arguments.remove(index);
    }

    public String get_expected() {
        if (expected == null) {
            return "";
        }
        return expected.type().toString() + " " + expected.value();
    }

    public void set_expected(String value, String type) {
        Type t = processType(type);
        switch (t) {
            case BOOLEAN -> value = Boolean.toString(Boolean.parseBoolean(value));
            case BYTE -> value = Byte.toString(Byte.parseByte(value));
            case SHORT -> value = Short.toString((short) Short.parseShort(value));
            case INTEGER -> value = Integer.toString((int) Short.parseShort(value));
            case LONG -> value = Long.toString((long) Short.parseShort(value));
            case FLOAT -> value = Float.toString(Float.parseFloat(value));
            case DOUBLE -> value = Double.toString(Double.parseDouble(value));
            case CHARACTER -> value = Character.toString(value.charAt(0));
            case ENUM, STRING -> value = value;
            default -> throw new IllegalArgumentException("Invalid type " + t);
        }
        set_expected(new Variable(value, t));
    }

    void set_expected(Variable expect) {
        expected = expect;
    }

    public String get_assertion() {
        if (assertion == null) {
            return "";
        }
        return assertion.toString();
    }

    public void set_assertion(String assertionString) {
        assertionString = assertionString.toLowerCase();
        Assertion assertion = switch (assertionString) {
            case "true" -> Assertion.True;
            case "false" -> Assertion.False;
            case "null" -> Assertion.Null;
            case "notnull" -> Assertion.NotNull;
            case "same" -> Assertion.Same;
            case "notsame" -> Assertion.NotSame;
            case "alphanumeric" -> Assertion.Alphanumeric;
            case "contain" -> Assertion.Contains;
            case "notcontain" -> Assertion.NotContains;
            case "startswith" -> Assertion.StartsWith;
            case "endswith" -> Assertion.EndsWith;
            //Add More Here
            default -> throw new IllegalArgumentException("Invalid assertion string: " + assertionString);
        };
        set_assertion(assertion);
    }

    void set_assertion(Assertion assertion) {
        if (expected != null) {
            validateCompatibility(expected.type(),  assertion);
        }
        this.assertion = assertion;
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