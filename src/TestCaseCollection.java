import java.util.ArrayList;
import java.util.List;

public class TestCaseCollection {
    String name;
    List<TestCase> testCases;

    public TestCaseCollection(){
         testCases = new ArrayList<TestCase>();
    }

    public void AddTestcase(TestCase tc) {
        testCases.add(tc);
    }

    public void removeTestcase(TestCase tc) {
        testCases.remove(tc);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
