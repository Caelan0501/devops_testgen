from abc import ABC, abstractmethod
from enum import Enum
import os

class AllowedChars(Enum):
    alpha = 1
    alnum = 2
    digit = 3

class ValuePartition(ABC):
    @abstractmethod
    def __init__(self, name, datatype):
        self.name = name
        self.datatype = datatype

class StringPartition(ValuePartition):
    def __init__(self, name, datatype, min_length = None, max_length = None, allowed_chars = AllowedChars.alnum, forbidden_chars = None):
        super().__init__(name, datatype)
        self.minLength = min_length
        self.maxLength = max_length
        self.allowed_chars = allowed_chars
        self.forbidden_chars = forbidden_chars

class NumberPartition(ValuePartition):
    def __init__(self, name, datatype, min_val = None, min_error_msg = None, max_val = None, max_error_msg = None):
        super().__init__(name, datatype)
        self.min_val = min_val
        self.min_error_msg = min_error_msg
        self.max_val = max_val
        self.max_error_msg = max_error_msg

class EnumPartition(ValuePartition):
    def __init__(self, name, datatype, values):
        super().__init__(name, datatype)
        self.values = values

class BooleanPartition(ValuePartition):
    def __init__(self, name, datatype):
        super().__init__(name, datatype)

def main():
    consoleQuestionnaire()

def consoleQuestionnaire():
    source_file = input("What is the source code file?")
    module_name = os.path.splitext(os.path.basename(source_file))[0]
    function_name = input("What is the funtion name?")

    partitions = []
    num_inputs = input("How many inputs will do you have?")
    for i in range(int(num_inputs)):
        name = input("Enter the name of the input")
        datatype = input("Place Enter Datatype of the input")
        match datatype:
            case "bool" | "boolean":
                partitions.append(BooleanPartition(name, datatype))
            case "int" | "integer" | "float" | "complex":
                minimum = input("What is the minimum valid number")
                min_error_msg = input("What is the Error message")
                maximum = input("What is the maximum valid number")
                max_error_msg = input("What is the Error message")
                partitions.append(NumberPartition(name, datatype, minimum, min_error_msg, maximum, max_error_msg))
            case "str" | "string":
                minimum = input("What is the minimum length")
                min_error_msg = input("What is the Error message")
                maximum = input("What is the maximum length")
                max_error_msg = input("What is the Error message")
                allowed_chars = input("What is the allowed characters")
                allowed_chars_error_msg = input("What is the error message")
                forbidden_chars = input("What is the forbidden characters")
                forbidden_chars_error_msg = input("What is the error message")

                partitions.append(StringPartition(name, datatype,minimum, maximum, allowed_chars, forbidden_chars))
            case "enum" | "enumerate":
                values = input("Placeholder")
                partitions.append(EnumPartition(name, datatype, values))
            case _:
                print("Please enter a valid data type")
    generateTestCases(partitions, module_name, function_name)

def generateTestCases(partitions, module_name, function_name):
    inputs_str = ""
    expected = ""
    tests = ["import pytest\n", f"import {module_name}\n\n"]
    for partition in partitions:
        if isinstance(partition, BooleanPartition):
            tests.append(generateBooleanTestCases(partition, module_name, function_name, inputs_str))
        elif isinstance(partition, NumberPartition):
            tests.append(generateNumberTestCases(partition, module_name, function_name, inputs_str))
        elif isinstance(partition, EnumPartition):
            tests.append(generateEnumTestCase(partition, module_name, function_name, inputs_str))
        elif isinstance(partition, StringPartition):
            tests.append(generateStringTestCase(partition, module_name, function_name, inputs_str))

        test_file = f"test_{module_name}_{function_name}.py"
        with open(test_file, "w") as f:
            f.writelines(tests)

def generateBooleanTestCases(partition, module_name, function_name, inputs_str):
    return f"""def test_{function_name}_{partition.name}():
        {module_name}.{function_name}({inputs_str})

    """

def generateNumberTestCases(partition, module_name, function_name, inputs_str):
    assert isinstance(partition, NumberPartition)
    tests = []
    if partition.min_val is not None:
        test_func = f"""def test_{function_name}_minimum():
            result = {module_name}.{function_name}({inputs_str})#adjust inputs_str
            assert result == {partition.min_error_msg}

        """
    if partition.max_val is not None:
        pass #Generate Above Maximum Test Case
    pass #Generate Valid TestCase

def generateStringTestCase(partition, module_name, function_name, inputs_str):
    assert isinstance(partition, StringPartition)
    if partition.minLength is not None:
        pass
    if partition.maxLength is not None:
        pass
    if partition.allowed_chars is not None:
        pass
    if partition.forbidden_chars is not None:
        pass
    pass
def generateEnumTestCase(partition, module_name, function_name, inputs_str):
    assert isinstance(partition, EnumPartition)
    #generate test cases for each value
    #Then Generate a random one to check for the invalid
    pass