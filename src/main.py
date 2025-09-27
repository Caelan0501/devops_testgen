import os
import csv

class TestCase:
    def __init__(self, name, module_name, function_name, args, test_arg, condition, expected):
        self.name = name
        self.module_name = module_name
        self.function_name = function_name
        self.args = args
        self.test_arg = test_arg
        self.condition = condition
        self.expected_value = expected

    def createCopy(self):
        return TestCase(self.name, self.module_name, self.function_name, self.args, self.test_arg, self.condition, self.expected_value)

    def generatePyTest(self):
        pass

def QuestionnaireManualTestCaseGen():
    module_name = input("Please enter the name of the module you wish to use: ")
    pass


def generate_tests_from_csv(source_file, func_name, csv_file):
    module_name = os.path.splitext(os.path.basename(source_file))[0]

    with open(csv_file, newline="") as f:
        reader = csv.DictReader(f)
        cases = list(reader)

    tests = ["import pytest\n", f"import {module_name}\n\n"]

    for case in cases:
        test_no = case["Test_No"]
        expected = case["Expected"]

        # Grab all columns except Test_No and Expected
        inputs = [v for k, v in case.items() if k not in ("Test_No", "Expected")]
        inputs_str = ", ".join(inputs)

        test_func = f"""def test_{func_name}_{test_no}():
    result = {module_name}.{func_name}({inputs_str})
    assert result == {expected}

"""
        tests.append(test_func)

    test_file = f"test_{module_name}_{func_name}.py"
    with open(test_file, "w") as f:
        f.writelines(tests)

    print(f"Generated {test_file} with {len(cases)} test cases.")