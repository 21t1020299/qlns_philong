import unittest
from unittest.mock import Mock
import sys
import os

# Add the app directory to the path
sys.path.append(os.path.join(os.path.dirname(__file__), 'app'))

from routes.employees import generate_employee_id
from models.employee import Employee

class SimpleWhiteBoxTest(unittest.TestCase):
    """Simple White Box Test for generate_employee_id function"""
    
    def setUp(self):
        self.mock_db = Mock()
        self.mock_query = Mock()
        self.mock_db.query.return_value = self.mock_query
    
    def test_empty_database(self):
        """Test: Empty database -> return NV001"""
        self.mock_query.order_by.return_value.first.return_value = None
        result = generate_employee_id(self.mock_db)
        self.assertEqual(result, "NV001")
    
    def test_normal_increment(self):
        """Test: NV001 -> NV002"""
        mock_employee = Mock()
        mock_employee.manv = "NV001"
        self.mock_query.order_by.return_value.first.return_value = mock_employee
        result = generate_employee_id(self.mock_db)
        self.assertEqual(result, "NV002")
    
    def test_invalid_format(self):
        """Test: Invalid format -> UUID fallback"""
        mock_employee = Mock()
        mock_employee.manv = "INVALID"
        self.mock_query.order_by.return_value.first.return_value = mock_employee
        result = generate_employee_id(self.mock_db)
        self.assertTrue(result.startswith("NV"))
        self.assertEqual(len(result), 9)

if __name__ == '__main__':
    unittest.main(verbosity=2) 