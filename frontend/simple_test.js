// Simple White Box Test for validateField function

// Mock validateField function
const validateField = (name, value, isFromDB = false) => {
  switch (name) {
    case 'tennv':
      if (!value.trim()) return 'Họ tên không được để trống';
      if (!/^[a-zA-ZÀ-ỹ\s-]+$/.test(value)) return 'Họ tên chỉ được chứa chữ cái';
      if (value.length > 100) return 'Họ tên không được quá 100 ký tự';
      break;
    
    case 'email':
      if (!value.trim()) return 'Email không được để trống';
      if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) return 'Email không hợp lệ';
      break;
    
    case 'ngsinh':
      if (!value) return 'Ngày sinh không được để trống';
      if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) return 'Ngày sinh phải theo định dạng YYYY-MM-DD';
      const birthDate = new Date(value);
      if (isNaN(birthDate.getTime())) return 'Ngày sinh không hợp lệ';
      if (birthDate > new Date()) return 'Ngày sinh không được trong tương lai';
      break;
  }
  return '';
};

// Test cases
console.log('=== Simple White Box Test ===');

// Test 1: Empty name
console.log('Test 1:', validateField('tennv', '') === 'Họ tên không được để trống' ? 'PASS' : 'FAIL');

// Test 2: Valid name
console.log('Test 2:', validateField('tennv', 'Nguyễn Văn A') === '' ? 'PASS' : 'FAIL');

// Test 3: Invalid email
console.log('Test 3:', validateField('email', 'invalid') === 'Email không hợp lệ' ? 'PASS' : 'FAIL');

// Test 4: Valid email
console.log('Test 4:', validateField('email', 'test@example.com') === '' ? 'PASS' : 'FAIL');

// Test 5: Invalid date
console.log('Test 5:', validateField('ngsinh', '2023/12/31') === 'Ngày sinh phải theo định dạng YYYY-MM-DD' ? 'PASS' : 'FAIL');

// Test 6: Valid date
console.log('Test 6:', validateField('ngsinh', '1990-01-01') === '' ? 'PASS' : 'FAIL');

console.log('=== Test Complete ==='); 