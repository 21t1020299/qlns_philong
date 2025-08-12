import unittest
from unittest.mock import Mock
import sys
import os
import requests
import json
from datetime import date

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

class TestEmployeeSafeDelete(unittest.TestCase):
    """Test cases for safe employee deletion with confirmation"""
    
    BASE_URL = "http://localhost:8000/api/employees"
    
    def setUp(self):
        """Set up test data"""
        self.test_employee = {
            "tennv": "Nguyễn Văn Test",
            "gtinh": "Nam",
            "email": "test.delete@example.com",
            "sdt": "0123456789",
            "ngsinh": "1990-01-01",
            "dchi": "123 Test Street",
            "dchithuongtru": "123 Test Street",
            "noidkhktt": "Test District",
            "dtoc": "Kinh",
            "trinhdo": "Đại học",
            "qtich": "Việt Nam",
            "skhoe": "Tốt",
            "macv": "NV001",
            "hotencha": "Nguyễn Văn Cha",
            "hotenme": "Nguyễn Thị Mẹ"
        }
        
        # Create test employee
        response = requests.post(f"{self.BASE_URL}/", json=self.test_employee)
        if response.status_code == 201:
            self.employee_data = response.json()
            self.employee_id = self.employee_data["manv"]
        else:
            # If creation fails, try to get existing employee
            response = requests.get(f"{self.BASE_URL}/")
            if response.status_code == 200:
                employees = response.json()["employees"]
                if employees:
                    self.employee_id = employees[0]["manv"]
                else:
                    self.skipTest("No employees available for testing")
            else:
                self.skipTest("Cannot access employee data")
    
    def test_delete_without_confirmation(self):
        """Test delete without confirmation should fail"""
        response = requests.delete(f"{self.BASE_URL}/{self.employee_id}")
        self.assertEqual(response.status_code, 422)  # Validation error
    
    def test_delete_with_wrong_confirmation(self):
        """Test delete with wrong confirmation text should fail"""
        response = requests.delete(
            f"{self.BASE_URL}/{self.employee_id}",
            params={"confirmation": "SAI"}
        )
        self.assertEqual(response.status_code, 400)
        self.assertIn("Xác nhận không đúng", response.json()["detail"])
    
    def test_delete_with_empty_confirmation(self):
        """Test delete with empty confirmation should fail"""
        response = requests.delete(
            f"{self.BASE_URL}/{self.employee_id}",
            params={"confirmation": ""}
        )
        self.assertEqual(response.status_code, 400)
        self.assertIn("Xác nhận không đúng", response.json()["detail"])
    
    def test_delete_with_partial_confirmation(self):
        """Test delete with partial confirmation should fail"""
        response = requests.delete(
            f"{self.BASE_URL}/{self.employee_id}",
            params={"confirmation": "TÔI"}
        )
        self.assertEqual(response.status_code, 400)
        self.assertIn("Xác nhận không đúng", response.json()["detail"])
    
    def test_delete_with_case_sensitive_confirmation(self):
        """Test delete with case-sensitive confirmation should fail"""
        response = requests.delete(
            f"{self.BASE_URL}/{self.employee_id}",
            params={"confirmation": "tôi hiểu"}
        )
        self.assertEqual(response.status_code, 400)
        self.assertIn("Xác nhận không đúng", response.json()["detail"])
    
    def test_delete_with_extra_spaces_confirmation(self):
        """Test delete with extra spaces should fail"""
        response = requests.delete(
            f"{self.BASE_URL}/{self.employee_id}",
            params={"confirmation": " TÔI HIỂU "}
        )
        self.assertEqual(response.status_code, 400)
        self.assertIn("Xác nhận không đúng", response.json()["detail"])
    
    def test_delete_with_correct_confirmation(self):
        """Test delete with correct confirmation should succeed"""
        response = requests.delete(
            f"{self.BASE_URL}/{self.employee_id}",
            params={"confirmation": "TÔI HIỂU"}
        )
        self.assertEqual(response.status_code, 204)
        
        # Verify employee is deleted
        get_response = requests.get(f"{self.BASE_URL}/{self.employee_id}")
        self.assertEqual(get_response.status_code, 404)
    
    def test_delete_nonexistent_employee(self):
        """Test delete nonexistent employee should fail"""
        response = requests.delete(
            f"{self.BASE_URL}/NV999",
            params={"confirmation": "TÔI HIỂU"}
        )
        self.assertEqual(response.status_code, 404)
        self.assertIn("Employee not found", response.json()["detail"])
    
    def test_delete_with_invalid_employee_id_format(self):
        """Test delete with invalid employee ID format should fail"""
        response = requests.delete(
            f"{self.BASE_URL}/INVALID",
            params={"confirmation": "TÔI HIỂU"}
        )
        self.assertEqual(response.status_code, 404)
        self.assertIn("Employee not found", response.json()["detail"])
    
    def test_delete_last_employee_prevention(self):
        """Test that system prevents deletion of last employee"""
        # Get total employee count
        response = requests.get(f"{self.BASE_URL}/")
        if response.status_code == 200:
            total_employees = response.json()["total"]
            
            if total_employees <= 1:
                # Try to delete the last employee
                employees = response.json()["employees"]
                if employees:
                    last_employee_id = employees[0]["manv"]
                    delete_response = requests.delete(
                        f"{self.BASE_URL}/{last_employee_id}",
                        params={"confirmation": "TÔI HIỂU"}
                    )
                    self.assertEqual(delete_response.status_code, 400)
                    self.assertIn("Không thể xóa nhân viên cuối cùng", delete_response.json()["detail"])
    
    def test_delete_audit_log(self):
        """Test that deletion is logged (this would require checking logs)"""
        # This test would require access to logs
        # For now, we just verify the deletion works
        response = requests.delete(
            f"{self.BASE_URL}/{self.employee_id}",
            params={"confirmation": "TÔI HIỂU"}
        )
        self.assertEqual(response.status_code, 204)
    
    def test_delete_confirmation_parameter_required(self):
        """Test that confirmation parameter is required"""
        response = requests.delete(f"{self.BASE_URL}/{self.employee_id}")
        self.assertEqual(response.status_code, 422)  # Validation error for missing parameter

class TestEmployeeDeleteBoundaryValues(unittest.TestCase):
    """Test boundary values for employee deletion"""
    
    BASE_URL = "http://localhost:8000/api/employees"
    
    def test_delete_with_very_long_confirmation(self):
        """Test delete with very long confirmation text"""
        long_confirmation = "TÔI HIỂU" + "A" * 1000
        response = requests.delete(
            f"{self.BASE_URL}/NV001",
            params={"confirmation": long_confirmation}
        )
        self.assertEqual(response.status_code, 400)
        self.assertIn("Xác nhận không đúng", response.json()["detail"])
    
    def test_delete_with_special_characters_confirmation(self):
        """Test delete with special characters in confirmation"""
        special_confirmation = "TÔI HIỂU@#$%"
        response = requests.delete(
            f"{self.BASE_URL}/NV001",
            params={"confirmation": special_confirmation}
        )
        self.assertEqual(response.status_code, 400)
        self.assertIn("Xác nhận không đúng", response.json()["detail"])
    
    def test_delete_with_unicode_confirmation(self):
        """Test delete with unicode characters in confirmation"""
        unicode_confirmation = "TÔI HIỂUñáéíóú"
        response = requests.delete(
            f"{self.BASE_URL}/NV001",
            params={"confirmation": unicode_confirmation}
        )
        self.assertEqual(response.status_code, 400)
        self.assertIn("Xác nhận không đúng", response.json()["detail"])

if __name__ == "__main__":
    # Run tests
    unittest.main(verbosity=2) 