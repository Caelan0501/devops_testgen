import unittest
import File_to_Test

class MyTestCase(unittest.TestCase):
    def test_something(self):
        self.assertEqual(File_to_Test.five(), 5)


if __name__ == '__main__':
    unittest.main()
