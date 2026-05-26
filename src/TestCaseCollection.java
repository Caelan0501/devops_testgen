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

    public void RemoveTestcase(TestCase tc) {
        testCases.remove(tc);
    }

    public TestCase Get_Testcase(int index) {
        return testCases.get(index);
    }

    public int size() {
        return testCases.size();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
