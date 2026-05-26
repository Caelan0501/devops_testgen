import jdk.jshell.spi.ExecutionControl;

import java.lang.String;

public class Junit5Generator implements CodeGenerator {

    @Override
    public String generate_code(TestCaseCollection collection) throws ExecutionControl.NotImplementedException {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < collection.size(); i++) {
            TestCase tc = collection.Get_Testcase(i);
            ValidateTestCase(tc);
            sb.append(generateTestCase(tc));
        }
        return sb.toString();
    }

    //Verify All fields are filled in
    private void ValidateTestCase(TestCase tc){
        if (tc.name == null) {
            throw new IllegalStateException("name is null");
        }
        else if (tc.module_name == null) {
            throw new IllegalStateException("module_name is null");
        }
        else if (tc.function_name == null) {
            throw new IllegalStateException("function_name is null");
        }
        else if (tc.expected == null) {
            throw new IllegalStateException("expected is null");
        }
        else if (tc.assertion == null) {
            throw new IllegalStateException("assertion is null");
        }
    }

    public String generateTestCase(TestCase tc) throws ExecutionControl.NotImplementedException {
        //Start Process
        StringBuilder testCode = new StringBuilder();
        testCode.append("\t@Test\n").append("\tvoid").append(tc.name).append("() {\n");

        //Build FunctionCall
        StringBuilder argsSb = new StringBuilder();
        for (int i = 0; i < tc.arguments.size(); i++) {
            argsSb.append(tc.arguments.get(i).value());
            if (i < tc.arguments.size() - 1) {
                argsSb.append(", ");
            }
        }
        String functionCall = tc.module_name + "." + tc.function_name + "(" + argsSb + ")";

        String test;
        if (tc.assertion == Assertion.Equal || tc.assertion == Assertion.NotEqual || tc.assertion == Assertion.Null || tc.assertion == Assertion.NotNull || tc.assertion == Assertion.Same || tc.assertion == Assertion.NotSame) {
            test = UniversalAssertions(tc, functionCall);
        }
        else {
            test = switch (tc.expected.type()) {
                case BOOLEAN -> BooleanAssertions(tc, functionCall);
                case INTEGER, DOUBLE, FLOAT, BYTE, SHORT, LONG -> NumericalAssertions(tc, functionCall);
                case CHARACTER -> CharAssertions(tc, functionCall);
                case STRING -> StringAssertions(tc, functionCall);
                default -> throw new ExecutionControl.NotImplementedException(tc.expected.type() + "is not Implemented");
            };
        }
        return testCode.append(test).toString();
    }

    private String UniversalAssertions(TestCase tc, String functionCall) {
        return switch (tc.assertion){
            case Equal -> String.format("\t\tassertEqual(%s, %s)", functionCall, tc.expected.value());
            case NotEqual -> String.format("\t\tassertNotEqual(%s, %s)", functionCall, tc.expected.value());
            case Null -> String.format("\t\tassertNull(%s)", functionCall);
            case NotNull -> String.format("\t\tassertNotNull(%s)", functionCall);
            case Same -> String.format("\t\tassertSame(%s,%s)", functionCall, tc.expected.value());
            case NotSame -> String.format("\t\tassertNotSame(%s, %s)", functionCall, tc.expected.value());
            default -> throw new IllegalArgumentException("Cannot compare " + functionCall + " with " + tc.expected);
        };
    }

    private String BooleanAssertions(TestCase tc, String functionCall){
        return switch (tc.assertion){
            case True -> String.format("\t\tassertTrue(%s)", functionCall);
            case False -> String.format("\t\tassertFalse(%s)", functionCall);
            default -> throw new IllegalArgumentException("Cannot compare " + functionCall + " with " + tc.expected);
        };
    }

    private String NumericalAssertions(TestCase tc, String functionCall) throws ExecutionControl.NotImplementedException {
        return switch (tc.assertion){
            case Greater -> String.format("\t\tassertTrue(%s > %s)", functionCall, tc.expected.value());
            case GreaterEqual -> String.format("\t\tassertTrue(%s >= %s)", functionCall, tc.expected.value());
            case Less -> String.format("\t\tassertTrue(%s < %s)", functionCall, tc.expected.value());
            case LessEqual -> String.format("\t\tassertTrue(%s <= %s)", functionCall, tc.expected.value());
            case Between -> throw new ExecutionControl.NotImplementedException("Between Assertion is not implemented");
            default -> throw new IllegalArgumentException("Cannot compare " + functionCall + " with " + tc.expected);
        };
    }

    private String CharAssertions(TestCase tc, String functionCall) throws ExecutionControl.NotImplementedException {
        return switch (tc.assertion){
            case Letter -> String.format("\t\tassertTrue(%s.isLetter())", functionCall);
            case Digit -> String.format("\t\tassertTrue(%s.isDigit())", functionCall);
            case Whitespace -> String.format("\t\tassertTrue(%s.isWhitespace())", functionCall);
            case Uppercase -> String.format("\t\tassertTrue(%s.isUppercase())", functionCall);
            case Lowercase -> String.format("\t\tassertTrue(%s.isLowercase())", functionCall);
            case Alphanumeric -> String.format("\t\tassertTrue(%s.isLetterOrDigit())", functionCall);
            default -> throw new IllegalArgumentException("Cannot compare " + functionCall + " with " + tc.expected);
        };
    }

    private String StringAssertions(TestCase tc, String functionCall){
        return switch (tc.assertion){
            case Contains -> String.format("\t\tassertTrue(%s.contains(%s))", functionCall, tc.expected.value());
            case NotContains -> String.format("\t\tassertFalse(%s.contains(%s))", functionCall, tc.expected.value());
            case StartsWith -> String.format("\t\tassertTrue(%s.startsWith(%s))", functionCall, tc.expected.value());
            case EndsWith -> String.format("\t\tassertTrue(%s.endsWith(%s))", functionCall, tc.expected.value());
            case LengthEqual -> String.format("\t\tassertTrue(%s.size() == %s)", functionCall, tc.expected.value());
            case Empty -> String.format("\t\tassertTrue(%s.isEmpty())", functionCall);
            case NotEmpty -> String.format("\t\tassertFalse(%s.isEmpty())", functionCall);
            default -> throw new IllegalArgumentException("Cannot compare " + functionCall + " with " + tc.expected);
        };
    }

}
