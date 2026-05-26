import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.lang.model.SourceVersion;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for the TestCase class")
public class TestCaseTest {

    @DisplayName("Sanity Checks")
    @Nested
    class SanityTests {
        TestCase tc;
        @BeforeEach
        public void setup() {
            tc = new TestCase();
        }

        @DisplayName("Construction Does not Throw")
        @Test
        void ConstructionDoesNotThrow(){
            tc = new TestCase();
        }

        @Test
        void SetNameDoesNotThrow(){
            assertDoesNotThrow(() -> tc.set_name("Test"));
        }

        @Test
        void SetModuleNameDoesNotThrow(){
            assertDoesNotThrow(() -> tc.set_module_name("Test"));
        }

        @Test
        void SetFunctionNameDoesNotThrow(){
            assertDoesNotThrow(() -> tc.set_function_name("Test"));
        }

        @Test
        void AddArgumentDoesNotThrow(){
            assertDoesNotThrow(() -> tc.add_argument("True", "Boolean"));
        }
        @Test
        void SetAssertionDoesNotThrow(){
            assertDoesNotThrow(() -> tc.set_assertion("true"));
        }
        @Test
        void SetExpectedDoesNotThrow(){
            assertDoesNotThrow(() -> tc.set_expected("True", "Boolean"));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("name, module_name, and function_name Tests")
    @Nested
    class NameTests {
        static Stream<BiConsumer<TestCase, String>> methods() {
            return Stream.of(
                    TestCase::set_name,
                    TestCase::set_module_name,
                    TestCase::set_function_name
            );
        }

        static Stream<String> legalStrings(){
            return Stream.of(
                    "test", "Test", "_Test", "$test", "test1"
            );
        }

        static Stream<String> illegalStrings(){
            return Stream.of(
                    "1test", "Te st", "!test", "@test", "#test1", "%test", "^test", "&test", "*test", "(test", ")test", ""
            );
        }

        static Stream<String> javaKeywords() {
            return Stream.of(
                    "abstract", "assert", "boolean", "break", "byte",
                    "case", "catch", "char", "class", "const",
                    "continue", "default", "do", "double", "else",
                    "enum", "extends", "final", "finally", "float",
                    "for", "goto", "if", "implements", "import",
                    "instanceof", "int", "interface", "long",
                    "native", "new", "package", "private", "protected",
                    "public", "return", "short", "static", "strictfp",
                    "super", "switch", "synchronized", "this", "throw",
                    "throws", "transient", "try", "void", "volatile", "while",
                    "record", "sealed", "permits", "yield", "var"
            ).filter(SourceVersion::isKeyword);
        }

        Stream<Arguments> legalArguments(){
            return legalStrings().flatMap(s -> methods().map(method -> Arguments.of(method, s)));
        }

        Stream<Arguments> illegalArguments(){
            return illegalStrings().flatMap(s -> methods().map(method -> Arguments.of(method, s)));
        }

        Stream<Arguments> KeywordArguments(){
            return javaKeywords().flatMap(s -> methods().map(method -> Arguments.of(method, s)));
        }

        @ParameterizedTest
        @MethodSource("legalArguments")
        void isLegalTest(BiConsumer<TestCase, String> method, String input){
            TestCase tc = new TestCase();
            assertDoesNotThrow(() -> method.accept(tc, input));
        }

        @ParameterizedTest
        @MethodSource("illegalArguments")
        void isIllegalTest(BiConsumer<TestCase, String> method, String input){
            TestCase tc = new TestCase();
            assertThrows(IllegalArgumentException.class, () -> method.accept(tc, input));
        }

        @DisplayName("Is not a Keyword")
        @ParameterizedTest(name = "keyword ''{0}'' is rejected")
        @MethodSource("KeywordArguments")
        void javaKeywordsAreRejected(BiConsumer<TestCase, String> method, String input) {
            TestCase tc = new TestCase();
            assertThrows(IllegalArgumentException.class, () -> method.accept(tc, input));
        }
    }
}

