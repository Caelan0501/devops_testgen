import random
import test.File_to_Test
from abc import ABC, abstractmethod
from enum import Enum

class Allowed_chars(Enum):
    alpha = 1
    alnum = 2
    digit = 3

class ValuePartition(ABC):
    @abstractmethod
    def __init__(self, name, type):
        self.name = name
        self.type = type

class StringPartition(ValuePartition):
    def __init__(self, name, type, allowed_chars = Allowed_chars.alnum, forbidden_chars = None):
        super().__init__(name, type)
        self.allowed_chars = allowed_chars
        self.forbidden_chars = forbidden_chars

class NumberPartition(ValuePartition):
    def __init__(self, name, type, min_val = None, max_val = None):
        super().__init__(name, type)
        self.min_val = min_val
        self.max_val = max_val

class EnumPartition(ValuePartition):
    def __init__(self, name, type, values):
        super().__init__(name, type)
        self.values = values

class BooleanPartition(ValuePartition):
    def __init__(self, name, type):
        super().__init__(name, type)

def main():
    print("How many inputs will do you have?")
    numInputs = input()
    for i in range(int(numInputs)):
        print("Place Enter Datatype of the input")
        datatype = input()
        match datatype:
            case "bool" | "boolean":
                print("Placeholder")
            case "int" | "integer" | "float" | "complex":
                print("What is the minimum valid number")
                minimum = input()
                minimumErrorMessage = input()
                print("What is the maximum valid number")
                maximum = input()
                generateNumberTestCase(datatype, minimum, minimumErrorMessage, maximum)
            case "str" | "string":
                generateStringTestCase()
            case _:
                print("Please enter a valid data type")

def generateNumberTestCase(datatype, minimum, minimumErrorMessage, maximum):
    print("------------------------")
    print("import unittest")
    print("class MyTestCase(unittest.TestCase):")
    if minimum != "None":
        minimum = int(minimum)
        randomNumber = random.randint(minimum - 100, minimum - 1)
        print("    def test_minimum(self):")
        print("        self.assertEqual(True, False)  # add assertion here")

def generateStringTestCase():
    print("What is the longest possible string")
    longest = input()
    print("What is the shortest possible string")
    shortest = input()
    print("Which characters would you like to exclude?")
    exclude = input()
