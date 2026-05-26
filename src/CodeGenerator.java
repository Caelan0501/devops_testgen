import jdk.jshell.spi.ExecutionControl;

import java.lang.String;

public interface CodeGenerator {
    String generate_code(TestCaseCollection collection) throws ExecutionControl.NotImplementedException;
}
