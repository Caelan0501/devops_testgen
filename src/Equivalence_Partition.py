import random
import test.File_to_Test

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
