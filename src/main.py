import os
import csv


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