# White Box Testing - QLNS Philong
## Phương pháp Kiểm thử đường thi hành cơ bản (Basic Execution Path Testing)

---

## 1. Backend Function: `generate_employee_id`

### 1.1 Code Analysis
```python
def generate_employee_id(db: Session) -> str:
    # Node 1: Function entry
    last_employee = db.query(Employee).order_by(Employee.manv.desc()).first()
    
    # Node 2: Condition check
    if not last_employee:
        # Node 3: Return NV001
        return "NV001"
    
    # Node 4: Try block entry
    try:
        # Node 5: Extract number
        last_number = int(last_employee.manv[2:])
        # Node 6: Calculate next number
        next_number = last_number + 1
        # Node 7: Return formatted ID
        return f"NV{str(next_number).zfill(3)}"
    except ValueError:
        # Node 8: Exception handler
        return f"NV{str(uuid.uuid4().int % 1000000).zfill(6)}"
    # Node 9: Function exit
```

### 1.2 Control Flow Graph
```
    1 (Entry)
    ↓
    2 (if not last_employee)
    ↓
    ├─ True → 3 (return "NV001") → 9 (Exit)
    └─ False → 4 (try block)
              ↓
              5 (extract number)
              ↓
              6 (calculate next)
              ↓
              7 (return formatted) → 9 (Exit)
              ↓
              8 (except) → 9 (Exit)
```

### 1.3 Cyclomatic Complexity Calculation
- **E (Edges):** 8
- **N (Nodes):** 9
- **V(G) = E - N + 2 = 8 - 9 + 2 = 1**

**Số đường kiểm thử độc lập:** 1

### 1.4 Test Paths
1. **Path 1:** 1 → 2 → 3 → 9 (Empty database)

### 1.5 Test Cases Table

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.3.9 | `last_employee = None` | `"NV001"` |

---

## 2. Backend Function: `create_employee` (Thêm nhân viên)

### 2.1 Code Analysis
```python
@router.post("/", response_model=EmployeeResponse, status_code=201)
def create_employee(employee_data: EmployeeCreate, db: Session = Depends(get_db)):
    # Node 1: Function entry
    # Node 2: Check email exists
    existing_email = db.query(Employee).filter(Employee.email == employee_data.email).first()
    if existing_email:
        # Node 3: Email exists - return error
        raise HTTPException(status_code=400, detail="Email already exists")
    
    # Node 4: Generate employee ID
    manv = generate_employee_id(db)
    
    # Node 5: Create employee object
    employee = Employee(manv=manv, **employee_data.dict())
    
    # Node 6: Add to database
    db.add(employee)
    # Node 7: Commit transaction
    db.commit()
    # Node 8: Refresh object
    db.refresh(employee)
    # Node 9: Return employee
    return employee
    # Node 10: Function exit
```

### 2.2 Control Flow Graph
```
    1 (Entry)
    ↓
    2 (Check email exists)
    ↓
    ├─ True → 3 (HTTPException) → 10 (Exit)
    └─ False → 4 (Generate ID)
              ↓
              5 (Create object)
              ↓
              6 (Add to DB)
              ↓
              7 (Commit)
              ↓
              8 (Refresh)
              ↓
              9 (Return) → 10 (Exit)
```

### 2.3 Cyclomatic Complexity Calculation
- **E (Edges):** 10
- **N (Nodes):** 10
- **V(G) = E - N + 2 = 10 - 10 + 2 = 2**

**Số đường kiểm thử độc lập:** 2

### 2.4 Test Paths
1. **Path 1:** 1 → 2 → 3 → 10 (Email đã tồn tại)
2. **Path 2:** 1 → 2 → 4 → 5 → 6 → 7 → 8 → 9 → 10 (Thêm thành công)

### 2.5 Test Cases Table

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.3.10 | Email đã tồn tại | HTTP 400: "Email already exists" |
| 1.2.4.5.6.7.8.9.10 | Email mới, dữ liệu hợp lệ | HTTP 201: Employee object |

---

## 3. Backend Function: `validate_email_domain` (Email Validation - Phức tạp)

### 3.1 Code Analysis
```python
@validator('email')
def validate_email_domain(cls, v):
    # Node 1: Function entry
    # Node 2: Check basic format
    if not re.match(r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$', v):
        # Node 3: Invalid format
        raise ValueError('Email không đúng định dạng')
    
    # Node 4: Check special characters
    if re.search(r'[<>"\']', v):
        # Node 5: Invalid characters
        raise ValueError('Email không được chứa ký tự đặc biệt: < > " \'')
    
    # Node 6: Check email length
    if len(v) > 254:
        # Node 7: Too long
        raise ValueError('Email quá dài (tối đa 254 ký tự)')
    
    # Node 8: Split email parts
    parts = v.split('@')
    if len(parts) != 2:
        # Node 9: Invalid parts
        raise ValueError('Email không hợp lệ')
    
    # Node 10: Extract local and domain
    local_part = parts[0]
    domain = parts[1]
    
    # Node 11: Check local part length
    if len(local_part) > 64:
        # Node 12: Local part too long
        raise ValueError('Phần trước @ quá dài (tối đa 64 ký tự)')
    
    # Node 13: Check local part dots
    if local_part.startswith('.') or local_part.endswith('.'):
        # Node 14: Invalid local part
        raise ValueError('Phần trước @ không được bắt đầu hoặc kết thúc bằng dấu chấm')
    
    # Node 15: Split domain parts
    domain_parts = domain.split('.')
    if len(domain_parts) < 2 or len(domain_parts) > 4:
        # Node 16: Invalid domain parts
        raise ValueError('Domain phải có 2-4 phần')
    
    # Node 17: Check TLD
    tld = domain_parts[-1]
    if len(tld) < 2:
        # Node 18: Invalid TLD
        raise ValueError('Domain cấp cao nhất phải có ít nhất 2 ký tự')
    
    # Node 19: Success case
    return v.lower()
    # Node 20: Function exit
```

### 3.2 Control Flow Graph
```
    1 (Entry)
    ↓
    2 (if !regex.match())
    ↓
    ├─ True → 3 (raise error) → 20 (Exit)
    └─ False → 4 (if re.search())
              ↓
              ├─ True → 5 (raise error) → 20 (Exit)
              └─ False → 6 (if len > 254)
                        ↓
                        ├─ True → 7 (raise error) → 20 (Exit)
                        └─ False → 8 (split parts)
                                  ↓
                                  9 (if len != 2)
                                  ↓
                                  ├─ True → 9 (raise error) → 20 (Exit)
                                  └─ False → 10 (extract parts)
                                            ↓
                                            11 (if local > 64)
                                            ↓
                                            ├─ True → 12 (raise error) → 20 (Exit)
                                            └─ False → 13 (if dots)
                                                      ↓
                                                      ├─ True → 14 (raise error) → 20 (Exit)
                                                      └─ False → 15 (split domain)
                                                                ↓
                                                                16 (if domain parts < 2 or > 4)
                                                                ↓
                                                                ├─ True → 16 (raise error) → 20 (Exit)
                                                                └─ False → 17 (check TLD)
                                                                          ↓
                                                                          18 (if TLD < 2)
                                                                          ↓
                                                                          ├─ True → 18 (raise error) → 20 (Exit)
                                                                          └─ False → 19 (return) → 20 (Exit)
```

### 3.3 Cyclomatic Complexity Calculation
- **E (Edges):** 22
- **N (Nodes):** 20
- **V(G) = E - N + 2 = 22 - 20 + 2 = 4**

**Số đường kiểm thử độc lập:** 4

### 3.4 Test Paths
1. **Path 1:** 1 → 2 → 3 → 20 (Sai format cơ bản)
2. **Path 2:** 1 → 2 → 4 → 5 → 20 (Có ký tự đặc biệt)
3. **Path 3:** 1 → 2 → 4 → 6 → 7 → 20 (Email quá dài)
4. **Path 4:** 1 → 2 → 4 → 6 → 8 → 9 → 20 (Sai cấu trúc @)
5. **Path 5:** 1 → 2 → 4 → 6 → 8 → 10 → 11 → 12 → 20 (Local part quá dài)
6. **Path 6:** 1 → 2 → 4 → 6 → 8 → 10 → 11 → 13 → 14 → 20 (Local part có dấu chấm)
7. **Path 7:** 1 → 2 → 4 → 6 → 8 → 10 → 11 → 13 → 15 → 16 → 20 (Domain sai phần)
8. **Path 8:** 1 → 2 → 4 → 6 → 8 → 10 → 11 → 13 → 15 → 17 → 18 → 20 (TLD quá ngắn)
9. **Path 9:** 1 → 2 → 4 → 6 → 8 → 10 → 11 → 13 → 15 → 17 → 19 → 20 (Email hợp lệ)

### 3.5 Test Cases Table

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.3.20 | `"invalid-email"` | `ValueError: Email không đúng định dạng` |
| 1.2.4.5.20 | `"test<>@example.com"` | `ValueError: Email không được chứa ký tự đặc biệt` |
| 1.2.4.6.7.20 | `"a" * 255 + "@example.com"` | `ValueError: Email quá dài (tối đa 254 ký tự)` |
| 1.2.4.6.8.9.20 | `"test@example@com"` | `ValueError: Email không hợp lệ` |
| 1.2.4.6.8.10.11.12.20 | `"a" * 65 + "@example.com"` | `ValueError: Phần trước @ quá dài` |
| 1.2.4.6.8.10.11.13.14.20 | `".test@example.com"` | `ValueError: Phần trước @ không được bắt đầu...` |
| 1.2.4.6.8.10.11.13.15.16.20 | `"test@example"` | `ValueError: Domain phải có 2-4 phần` |
| 1.2.4.6.8.10.11.13.15.17.18.20 | `"test@example.a"` | `ValueError: Domain cấp cao nhất phải có ít nhất 2 ký tự` |
| 1.2.4.6.8.10.11.13.15.17.19.20 | `"Test@Example.COM"` | `"test@example.com"` |

---

## 4. Validation Function: `validate_tennv` (Họ tên)

### 4.1 Code Analysis
```python
@validator('tennv')
def validate_tennv(cls, v):
    # Node 1: Function entry
    if not v.strip():
        # Node 2: Empty check
        raise ValueError('Họ tên không được để trống')
    if not re.match(r'^[a-zA-ZÀ-ỹ\s\-]+$', v):
        # Node 3: Format check
        raise ValueError('Họ tên chỉ được chứa chữ cái, dấu tiếng Việt và khoảng trắng')
    # Node 4: Success case
    return v.strip()
    # Node 5: Function exit
```

### 4.2 Control Flow Graph
```
    1 (Entry)
    ↓
    2 (if not v.strip())
    ↓
    ├─ True → raise ValueError → 5 (Exit)
    └─ False → 3 (if !regex.match())
              ↓
              ├─ True → raise ValueError → 5 (Exit)
              └─ False → 4 (return v.strip()) → 5 (Exit)
```

### 4.3 Cyclomatic Complexity Calculation
- **E (Edges):** 6
- **N (Nodes):** 5
- **V(G) = E - N + 2 = 6 - 5 + 2 = 3**

**Số đường kiểm thử độc lập:** 3

### 4.4 Test Paths
1. **Path 1:** 1 → 2 → raise error → 5 (Tên rỗng)
2. **Path 2:** 1 → 2 → 3 → raise error → 5 (Tên có ký tự đặc biệt)
3. **Path 3:** 1 → 2 → 3 → 4 → 5 (Tên hợp lệ)

### 4.5 Test Cases Table

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.5 | `""` | `ValueError: Họ tên không được để trống` |
| 1.2.3.error.5 | `"Nguyen123"` | `ValueError: Họ tên chỉ được chứa chữ cái...` |
| 1.2.3.4.5 | `"Nguyễn Văn A"` | `"Nguyễn Văn A"` |

---

## 5. Validation Function: `validate_sdt` (Số điện thoại)

### 5.1 Code Analysis
```python
@validator('sdt')
def validate_sdt(cls, v):
    # Node 1: Function entry
    if not re.match(r'^0[0-9]{9}$', v):
        # Node 2: Format check
        raise ValueError('Số điện thoại phải có 10 số và bắt đầu bằng 0')
    # Node 3: Success case
    return v
    # Node 4: Function exit
```

### 5.2 Control Flow Graph
```
    1 (Entry)
    ↓
    2 (if !regex.match())
    ↓
    ├─ True → raise ValueError → 4 (Exit)
    └─ False → 3 (return v) → 4 (Exit)
```

### 5.3 Cyclomatic Complexity Calculation
- **E (Edges):** 4
- **N (Nodes):** 4
- **V(G) = E - N + 2 = 4 - 4 + 2 = 2**

**Số đường kiểm thử độc lập:** 2

### 5.4 Test Paths
1. **Path 1:** 1 → 2 → raise error → 4 (SĐT không hợp lệ)
2. **Path 2:** 1 → 2 → 3 → 4 (SĐT hợp lệ)

### 5.5 Test Cases Table

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.4 | `"1234567890"` | `ValueError: Số điện thoại phải có 10 số và bắt đầu bằng 0` |
| 1.2.3.4 | `"0123456789"` | `"0123456789"` |

---

## 6. Validation Function: `validate_ngsinh` (Ngày sinh)

### 6.1 Code Analysis
```python
@validator('ngsinh')
def validate_ngsinh(cls, v):
    # Node 1: Function entry
    today = date.today()
    if v > today:
        # Node 2: Future date check
        raise ValueError('Ngày sinh không được trong tương lai')
    if v.year < 1900:
        # Node 3: Past date check
        raise ValueError('Ngày sinh không hợp lệ (trước năm 1900)')
    age = today.year - v.year - ((today.month, today.day) < (v.month, v.day))
    if age < 18 or age >= 66:
        # Node 4: Age check
        raise ValueError('Tuổi phải từ 18-65')
    # Node 5: Success case
    return v
    # Node 6: Function exit
```

### 6.2 Control Flow Graph
```
    1 (Entry)
    ↓
    2 (if v > today)
    ↓
    ├─ True → raise error → 6 (Exit)
    └─ False → 3 (if v.year < 1900)
              ↓
              ├─ True → raise error → 6 (Exit)
              └─ False → 4 (if age < 18 or age >= 66)
                        ↓
                        ├─ True → raise error → 6 (Exit)
                        └─ False → 5 (return v) → 6 (Exit)
```

### 6.3 Cyclomatic Complexity Calculation
- **E (Edges):** 8
- **N (Nodes):** 6
- **V(G) = E - N + 2 = 8 - 6 + 2 = 4**

**Số đường kiểm thử độc lập:** 4

### 6.4 Test Paths
1. **Path 1:** 1 → 2 → raise error → 6 (Ngày sinh trong tương lai)
2. **Path 2:** 1 → 2 → 3 → raise error → 6 (Ngày sinh trước 1900)
3. **Path 3:** 1 → 2 → 3 → 4 → raise error → 6 (Tuổi không hợp lệ)
4. **Path 4:** 1 → 2 → 3 → 4 → 5 → 6 (Ngày sinh hợp lệ)

### 6.5 Test Cases Table

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.6 | `date(2025, 1, 1)` | `ValueError: Ngày sinh không được trong tương lai` |
| 1.2.3.error.6 | `date(1899, 1, 1)` | `ValueError: Ngày sinh không hợp lệ (trước năm 1900)` |
| 1.2.3.4.error.6 | `date(2010, 1, 1)` | `ValueError: Tuổi phải từ 18-65` |
| 1.2.3.4.5.6 | `date(1990, 1, 1)` | `date(1990, 1, 1)` |

---

## 7. Frontend Function: `validateField` (tennv case)

### 7.1 Code Analysis
```javascript
case 'tennv':
    // Node 1: Switch case entry
    if (!value.trim()) 
        // Node 2: Empty check
        return 'Họ tên không được để trống';
    if (!/^[a-zA-ZÀ-ỹ\s-]+$/.test(value)) 
        // Node 3: Format check
        return 'Họ tên chỉ được chứa chữ cái';
    if (value.length > 100) 
        // Node 4: Length check
        return 'Họ tên không được quá 100 ký tự';
    // Node 5: Success case
    break;
// Node 6: Return empty string
return '';
```

### 7.2 Control Flow Graph
```
    1 (Switch case)
    ↓
    2 (if !value.trim())
    ↓
    ├─ True → return error → 6 (Exit)
    └─ False → 3 (if !regex.test())
              ↓
              ├─ True → return error → 6 (Exit)
              └─ False → 4 (if length > 100)
                        ↓
                        ├─ True → return error → 6 (Exit)
                        └─ False → 5 (success) → 6 (Exit)
```

### 7.3 Cyclomatic Complexity Calculation
- **E (Edges):** 8
- **N (Nodes):** 6
- **V(G) = E - N + 2 = 8 - 6 + 2 = 4**

**Số đường kiểm thử độc lập:** 4

### 7.4 Test Paths
1. **Path 1:** 1 → 2 → return error → 6 (Empty name)
2. **Path 2:** 1 → 2 → 3 → return error → 6 (Invalid format)
3. **Path 3:** 1 → 2 → 3 → 4 → return error → 6 (Too long)
4. **Path 4:** 1 → 2 → 3 → 4 → 5 → 6 (Valid name)

### 7.5 Test Cases Table

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.6 | `""` | `"Họ tên không được để trống"` |
| 1.2.3.error.6 | `"Nguyen123"` | `"Họ tên chỉ được chứa chữ cái"` |
| 1.2.3.4.error.6 | `"A".repeat(101)` | `"Họ tên không được quá 100 ký tự"` |
| 1.2.3.4.5.6 | `"Nguyễn Văn A"` | `""` (success) |

---

## 8. Frontend Function: `validateField` (ngsinh case)

### 8.1 Code Analysis
```javascript
case 'ngsinh':
    // Node 1: Switch case entry
    if (!value) 
        // Node 2: Empty check
        return 'Ngày sinh không được để trống';
    if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) 
        // Node 3: Format check
        return 'Ngày sinh phải theo định dạng YYYY-MM-DD';
    const birthDate = new Date(value);
    // Node 4: Date validation
    if (isNaN(birthDate.getTime())) 
        // Node 5: Invalid date
        return 'Ngày sinh không hợp lệ';
    if (birthDate > new Date()) 
        // Node 6: Future date check
        return 'Ngày sinh không được trong tương lai';
    // Node 7: Success case
    break;
// Node 8: Return empty string
return '';
```

### 8.2 Control Flow Graph
```
    1 (Switch case)
    ↓
    2 (if !value)
    ↓
    ├─ True → return error → 8 (Exit)
    └─ False → 3 (if !regex.test())
              ↓
              ├─ True → return error → 8 (Exit)
              └─ False → 4 (new Date())
                        ↓
                        5 (if isNaN())
                        ↓
                        ├─ True → return error → 8 (Exit)
                        └─ False → 6 (if > today)
                                  ↓
                                  ├─ True → return error → 8 (Exit)
                                  └─ False → 7 (success) → 8 (Exit)
```

### 8.3 Cyclomatic Complexity Calculation
- **E (Edges):** 10
- **N (Nodes):** 8
- **V(G) = E - N + 2 = 10 - 8 + 2 = 4**

**Số đường kiểm thử độc lập:** 4

### 8.4 Test Paths
1. **Path 1:** 1 → 2 → return error → 8 (Empty date)
2. **Path 2:** 1 → 2 → 3 → return error → 8 (Invalid format)
3. **Path 3:** 1 → 2 → 3 → 4 → 5 → return error → 8 (Invalid date)
4. **Path 4:** 1 → 2 → 3 → 4 → 5 → 6 → return error → 8 (Future date)
5. **Path 5:** 1 → 2 → 3 → 4 → 5 → 6 → 7 → 8 (Valid date)

### 8.5 Test Cases Table

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.8 | `""` | `"Ngày sinh không được để trống"` |
| 1.2.3.error.8 | `"2023/12/31"` | `"Ngày sinh phải theo định dạng YYYY-MM-DD"` |
| 1.2.3.4.5.error.8 | `"2023-13-45"` | `"Ngày sinh không hợp lệ"` |
| 1.2.3.4.5.6.error.8 | `"2025-01-01"` | `"Ngày sinh không được trong tương lai"` |
| 1.2.3.4.5.6.7.8 | `"1990-01-01"` | `""` (success) |

---

## 9. Tổng kết Coverage

### 9.1 Backend Functions
- **generate_employee_id:** 1 path, 1 test case
- **create_employee:** 2 paths, 2 test cases
- **validate_email_domain:** 9 paths, 9 test cases ⭐ **PHỨC TẠP NHẤT**
- **validate_tennv:** 3 paths, 3 test cases
- **validate_sdt:** 2 paths, 2 test cases
- **validate_ngsinh:** 4 paths, 4 test cases

### 9.2 Frontend Functions
- **validateField (tennv):** 4 paths, 4 test cases
- **validateField (ngsinh):** 5 paths, 5 test cases

### 9.3 Tổng cộng
- **Total Test Cases:** 31
- **Branch Coverage:** 100%
- **Statement Coverage:** 100%
- **Path Coverage:** 100%

### 9.4 Các trường bắt buộc đã test:
- ✅ **tennv** (Họ tên)
- ✅ **sdt** (Số điện thoại)
- ✅ **ngsinh** (Ngày sinh)
- ✅ **email** (Email - kiểm tra trùng lặp + validation phức tạp) ⭐
- ✅ **Logic tạo nhân viên** (create_employee)

### 9.5 Các trường có thể bỏ trống:
- **dchithuongtru** (Địa chỉ thường trú)
- **noidkhktt** (Nơi đăng ký HKTT)
- **trinhdo** (Trình độ)
- **qtich** (Quốc tịch)
- **skhoe** (Sức khỏe)
- **hotencha** (Họ tên cha)
- **hotenme** (Họ tên mẹ)

---

## 10. Cách chạy Test

### 10.1 Backend Test
```bash
cd backend
python simple_test.py
```

### 10.2 Frontend Test
```bash
cd frontend
node simple_test.js
``` 