# Kiểm thử Hộp trắng - QLNS Philong
## Phương pháp Kiểm thử đường thi hành cơ bản (Basic Execution Path Testing)

---

## 1. Hàm Backend: `generate_employee_id`

### 1.1 Phân tích Mã
```python
def generate_employee_id(db: Session) -> str:
    # Node 1: Điểm vào hàm
    last_employee = db.query(Employee).order_by(Employee.manv.desc()).first()
    
    # Node 2: Kiểm tra điều kiện
    if not last_employee:
        # Node 3: Trả về NV001
        return "NV001"
    
    # Node 4: Điểm vào khối try
    try:
        # Node 5: Trích xuất số
        last_number = int(last_employee.manv[2:])
        # Node 6: Tính số tiếp theo
        next_number = last_number + 1
        # Node 7: Trả về ID đã định dạng
        return f"NV{str(next_number).zfill(3)}"
    except ValueError:
        # Node 8: Xử lý ngoại lệ
        return f"NV{str(uuid.uuid4().int % 1000000).zfill(6)}"
    # Node 9: Điểm thoát hàm
```

### 1.2 Đồ thị Luồng Điều khiển
```
    1 (Điểm vào)
    ↓
    2 (if not last_employee)
    ↓
    ├─ True → 3 (return "NV001") → 9 (Thoát)
    └─ False → 4 (khối try)
              ↓
              5 (trích xuất số)
              ↓
              6 (tính tiếp theo)
              ↓
              7 (return đã định dạng) → 9 (Thoát)
              ↓
              8 (except) → 9 (Thoát)
```

### 1.3 Tính toán Độ phức tạp Cyclomatic
- **E (Cạnh):** 10
- **N (Nút):** 9
- **V(G) = E - N + 2 = 10 - 9 + 2 = 3**

**Số đường kiểm thử độc lập:** 3

### 1.4 Đường Kiểm thử
1. **Đường 1:** 1 → 2 → 3 → 9 (Cơ sở dữ liệu trống)
2. **Đường 2:** 1 → 2 → 4 → 5 → 6 → 7 → 9 (Thành công)
3. **Đường 3:** 1 → 2 → 4 → 5 → 8 → 9 (Có ngoại lệ)

### 1.5 Bảng Trường hợp Kiểm thử

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.3.9 | `last_employee = None` | `"NV001"` |
| 1.2.4.5.6.7.9 | `last_employee.manv = "NV001"` | `"NV002"` |
| 1.2.4.5.8.9 | `last_employee.manv = "NVABC"` | `"NV123456"` (UUID) |

---

## 2. Hàm Backend: `create_employee` (Thêm nhân viên)

### 2.1 Phân tích Mã
```python
@router.post("/", response_model=EmployeeResponse, status_code=201)
def create_employee(employee_data: EmployeeCreate, db: Session = Depends(get_db)):
    # Node 1: Điểm vào hàm
    # Node 2: Kiểm tra email tồn tại
    existing_email = db.query(Employee).filter(Employee.email == employee_data.email).first()
    if existing_email:
        # Node 3: Email tồn tại - trả về lỗi
        raise HTTPException(status_code=400, detail="Email already exists")
    
    # Node 4: Tạo ID nhân viên
    manv = generate_employee_id(db)
    
    # Node 5: Tạo đối tượng nhân viên
    employee = Employee(manv=manv, **employee_data.dict())
    
    # Node 6: Thêm vào cơ sở dữ liệu
    db.add(employee)
    # Node 7: Commit giao dịch
    db.commit()
    # Node 8: Làm mới đối tượng
    db.refresh(employee)
    # Node 9: Trả về nhân viên
    return employee
    # Node 10: Điểm thoát hàm
```

### 2.2 Đồ thị Luồng Điều khiển
```
    1 (Điểm vào)
    ↓
    2 (Kiểm tra email tồn tại)
    ↓
    ├─ True → 3 (HTTPException) → 10 (Thoát)
    └─ False → 4 (Tạo ID)
              ↓
              5 (Tạo đối tượng)
              ↓
              6 (Thêm vào DB)
              ↓
              7 (Commit)
              ↓
              8 (Làm mới)
              ↓
              9 (Trả về) → 10 (Thoát)
```

### 2.3 Tính toán Độ phức tạp Cyclomatic
- **E (Cạnh):** 10
- **N (Nút):** 10
- **V(G) = E - N + 2 = 10 - 10 + 2 = 2**

**Số đường kiểm thử độc lập:** 2

### 2.4 Đường Kiểm thử
1. **Đường 1:** 1 → 2 → 3 → 10 (Email đã tồn tại)
2. **Đường 2:** 1 → 2 → 4 → 5 → 6 → 7 → 8 → 9 → 10 (Thêm thành công)

### 2.5 Bảng Trường hợp Kiểm thử

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.3.10 | Email đã tồn tại | HTTP 400: "Email already exists" |
| 1.2.4.5.6.7.8.9.10 | Email mới, dữ liệu hợp lệ | HTTP 201: Employee object |

---

## 3. Hàm Backend: `validate_email_domain` (Xác thực Email - Phức tạp)

### 3.1 Phân tích Mã
```python
@validator('email')
def validate_email_domain(cls, v):
    # Node 1: Điểm vào hàm
    # Node 2: Kiểm tra định dạng cơ bản
    if not re.match(r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$', v):
        # Node 3: Định dạng không hợp lệ
        raise ValueError('Email không đúng định dạng')
    
    # Node 4: Kiểm tra ký tự đặc biệt
    if re.search(r'[<>"\']', v):
        # Node 5: Ký tự không hợp lệ
        raise ValueError('Email không được chứa ký tự đặc biệt: < > " \'')
    
    # Node 6: Kiểm tra độ dài email
    if len(v) > 254:
        # Node 7: Quá dài
        raise ValueError('Email quá dài (tối đa 254 ký tự)')
    
    # Node 8: Tách các phần email
    parts = v.split('@')
    if len(parts) != 2:
        # Node 9: Các phần không hợp lệ
        raise ValueError('Email không hợp lệ')
    
    # Node 10: Trích xuất phần cục bộ và miền
    local_part = parts[0]
    domain = parts[1]
    
    # Node 11: Kiểm tra độ dài phần cục bộ
    if len(local_part) > 64:
        # Node 12: Phần cục bộ quá dài
        raise ValueError('Phần trước @ quá dài (tối đa 64 ký tự)')
    
    # Node 13: Kiểm tra dấu chấm phần cục bộ
    if local_part.startswith('.') or local_part.endswith('.'):
        # Node 14: Phần cục bộ không hợp lệ
        raise ValueError('Phần trước @ không được bắt đầu hoặc kết thúc bằng dấu chấm')
    
    # Node 15: Tách các phần miền
    domain_parts = domain.split('.')
    if len(domain_parts) < 2 or len(domain_parts) > 4:
        # Node 16: Các phần miền không hợp lệ
        raise ValueError('Domain phải có 2-4 phần')
    
    # Node 17: Kiểm tra TLD
    tld = domain_parts[-1]
    if len(tld) < 2:
        # Node 18: TLD không hợp lệ
        raise ValueError('Domain cấp cao nhất phải có ít nhất 2 ký tự')
    
    # Node 19: Trường hợp thành công
    return v.lower()
    # Node 20: Điểm thoát hàm
```

### 3.2 Đồ thị Luồng Điều khiển
```
    1 (Điểm vào)
    ↓
    2 (if !regex.match())
    ↓
    ├─ True → 3 (raise error) → 20 (Thoát)
    └─ False → 4 (if re.search())
              ↓
              ├─ True → 5 (raise error) → 20 (Thoát)
              └─ False → 6 (if len > 254)
                        ↓
                        ├─ True → 7 (raise error) → 20 (Thoát)
                        └─ False → 8 (tách các phần)
                                  ↓
                                  9 (if len != 2)
                                  ↓
                                  ├─ True → 9 (raise error) → 20 (Thoát)
                                  └─ False → 10 (trích xuất các phần)
                                            ↓
                                            11 (if local > 64)
                                            ↓
                                            ├─ True → 12 (raise error) → 20 (Thoát)
                                            └─ False → 13 (if dots)
                                                      ↓
                                                      ├─ True → 14 (raise error) → 20 (Thoát)
                                                      └─ False → 15 (tách miền)
                                                                ↓
                                                                16 (if domain parts < 2 or > 4)
                                                                ↓
                                                                ├─ True → 16 (raise error) → 20 (Thoát)
                                                                └─ False → 17 (kiểm tra TLD)
                                                                          ↓
                                                                          18 (if TLD < 2)
                                                                          ↓
                                                                          ├─ True → 18 (raise error) → 20 (Thoát)
                                                                          └─ False → 19 (return) → 20 (Thoát)
```

### 3.3 Tính toán Độ phức tạp Cyclomatic
- **E (Cạnh):** 22
- **N (Nút):** 20
- **V(G) = E - N + 2 = 22 - 20 + 2 = 4**

**Số đường kiểm thử độc lập:** 4

### 3.4 Đường Kiểm thử
1. **Đường 1:** 1 → 2 → 3 → 20 (Sai định dạng cơ bản)
2. **Đường 2:** 1 → 2 → 4 → 5 → 20 (Có ký tự đặc biệt)
3. **Đường 3:** 1 → 2 → 4 → 6 → 7 → 20 (Email quá dài)
4. **Đường 4:** 1 → 2 → 4 → 6 → 8 → 9 → 20 (Sai cấu trúc @)
5. **Đường 5:** 1 → 2 → 4 → 6 → 8 → 10 → 11 → 12 → 20 (Phần cục bộ quá dài)
6. **Đường 6:** 1 → 2 → 4 → 6 → 8 → 10 → 11 → 13 → 14 → 20 (Phần cục bộ có dấu chấm)
7. **Đường 7:** 1 → 2 → 4 → 6 → 8 → 10 → 11 → 13 → 15 → 16 → 20 (Miền sai phần)
8. **Đường 8:** 1 → 2 → 4 → 6 → 8 → 10 → 11 → 13 → 15 → 17 → 18 → 20 (TLD quá ngắn)
9. **Đường 9:** 1 → 2 → 4 → 6 → 8 → 10 → 11 → 13 → 15 → 17 → 19 → 20 (Email hợp lệ)

### 3.5 Bảng Trường hợp Kiểm thử

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

## 4. Hàm Xác thực: `validate_tennv` (Họ tên)

### 4.1 Phân tích Mã
```python
@validator('tennv')
def validate_tennv(cls, v):
    # Node 1: Điểm vào hàm
    if not v.strip():
        # Node 2: Kiểm tra rỗng
        raise ValueError('Họ tên không được để trống')
    if not re.match(r'^[a-zA-ZÀ-ỹ\s\-]+$', v):
        # Node 3: Kiểm tra định dạng
        raise ValueError('Họ tên chỉ được chứa chữ cái, dấu tiếng Việt và khoảng trắng')
    # Node 4: Trường hợp thành công
    return v.strip()
    # Node 5: Điểm thoát hàm
```

### 4.2 Đồ thị Luồng Điều khiển
```
    1 (Điểm vào)
    ↓
    2 (if not v.strip())
    ↓
    ├─ True → raise ValueError → 5 (Thoát)
    └─ False → 3 (if !regex.match())
              ↓
              ├─ True → raise ValueError → 5 (Thoát)
              └─ False → 4 (return v.strip()) → 5 (Thoát)
```

### 4.3 Tính toán Độ phức tạp Cyclomatic
- **E (Cạnh):** 6
- **N (Nút):** 5
- **V(G) = E - N + 2 = 6 - 5 + 2 = 3**

**Số đường kiểm thử độc lập:** 3

### 4.4 Đường Kiểm thử
1. **Đường 1:** 1 → 2 → raise error → 5 (Tên rỗng)
2. **Đường 2:** 1 → 2 → 3 → raise error → 5 (Tên có ký tự đặc biệt)
3. **Đường 3:** 1 → 2 → 3 → 4 → 5 (Tên hợp lệ)

### 4.5 Bảng Trường hợp Kiểm thử

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.5 | `""` | `ValueError: Họ tên không được để trống` |
| 1.2.3.error.5 | `"Nguyen123"` | `ValueError: Họ tên chỉ được chứa chữ cái...` |
| 1.2.3.4.5 | `"Nguyễn Văn A"` | `"Nguyễn Văn A"` |

---

## 5. Hàm Xác thực: `validate_sdt` (Số điện thoại)

### 5.1 Phân tích Mã
```python
@validator('sdt')
def validate_sdt(cls, v):
    # Node 1: Điểm vào hàm
    if not re.match(r'^0[0-9]{9}$', v):
        # Node 2: Kiểm tra định dạng
        raise ValueError('Số điện thoại phải có 10 số và bắt đầu bằng 0')
    # Node 3: Trường hợp thành công
    return v
    # Node 4: Điểm thoát hàm
```

### 5.2 Đồ thị Luồng Điều khiển
```
    1 (Điểm vào)
    ↓
    2 (if !regex.match())
    ↓
    ├─ True → raise ValueError → 4 (Thoát)
    └─ False → 3 (return v) → 4 (Thoát)
```

### 5.3 Tính toán Độ phức tạp Cyclomatic
- **E (Cạnh):** 4
- **N (Nút):** 4
- **V(G) = E - N + 2 = 4 - 4 + 2 = 2**

**Số đường kiểm thử độc lập:** 2

### 5.4 Đường Kiểm thử
1. **Đường 1:** 1 → 2 → raise error → 4 (SĐT không hợp lệ)
2. **Đường 2:** 1 → 2 → 3 → 4 (SĐT hợp lệ)

### 5.5 Bảng Trường hợp Kiểm thử

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.4 | `"1234567890"` | `ValueError: Số điện thoại phải có 10 số và bắt đầu bằng 0` |
| 1.2.3.4 | `"0123456789"` | `"0123456789"` |

---

## 6. Hàm Xác thực: `validate_ngsinh` (Ngày sinh)

### 6.1 Phân tích Mã
```python
@validator('ngsinh')
def validate_ngsinh(cls, v):
    # Node 1: Điểm vào hàm
    today = date.today()
    if v > today:
        # Node 2: Kiểm tra ngày tương lai
        raise ValueError('Ngày sinh không được trong tương lai')
    if v.year < 1900:
        # Node 3: Kiểm tra ngày quá khứ
        raise ValueError('Ngày sinh không hợp lệ (trước năm 1900)')
    age = today.year - v.year - ((today.month, today.day) < (v.month, v.day))
    if age < 18 or age >= 66:
        # Node 4: Kiểm tra tuổi
        raise ValueError('Tuổi phải từ 18-65')
    # Node 5: Trường hợp thành công
    return v
    # Node 6: Điểm thoát hàm
```

### 6.2 Đồ thị Luồng Điều khiển
```
    1 (Điểm vào)
    ↓
    2 (if v > today)
    ↓
    ├─ True → raise error → 6 (Thoát)
    └─ False → 3 (if v.year < 1900)
              ↓
              ├─ True → raise error → 6 (Thoát)
              └─ False → 4 (if age < 18 or age >= 66)
                        ↓
                        ├─ True → raise error → 6 (Thoát)
                        └─ False → 5 (return v) → 6 (Thoát)
```

### 6.3 Tính toán Độ phức tạp Cyclomatic
- **E (Cạnh):** 8
- **N (Nút):** 6
- **V(G) = E - N + 2 = 8 - 6 + 2 = 4**

**Số đường kiểm thử độc lập:** 4

### 6.4 Đường Kiểm thử
1. **Đường 1:** 1 → 2 → raise error → 6 (Ngày sinh trong tương lai)
2. **Đường 2:** 1 → 2 → 3 → raise error → 6 (Ngày sinh trước 1900)
3. **Đường 3:** 1 → 2 → 3 → 4 → raise error → 6 (Tuổi không hợp lệ)
4. **Đường 4:** 1 → 2 → 3 → 4 → 5 → 6 (Ngày sinh hợp lệ)

### 6.5 Bảng Trường hợp Kiểm thử

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.6 | `date(2025, 1, 1)` | `ValueError: Ngày sinh không được trong tương lai` |
| 1.2.3.error.6 | `date(1899, 1, 1)` | `ValueError: Ngày sinh không hợp lệ (trước năm 1900)` |
| 1.2.3.4.error.6 | `date(2010, 1, 1)` | `ValueError: Tuổi phải từ 18-65` |
| 1.2.3.4.5.6 | `date(1990, 1, 1)` | `date(1990, 1, 1)` |

---

## 7. Hàm Frontend: `validateField` (trường hợp tennv)

### 7.1 Phân tích Mã
```javascript
case 'tennv':
    // Node 1: Điểm vào switch case
    if (!value.trim()) 
        // Node 2: Kiểm tra rỗng
        return 'Họ tên không được để trống';
    if (!/^[a-zA-ZÀ-ỹ\s-]+$/.test(value)) 
        // Node 3: Kiểm tra định dạng
        return 'Họ tên chỉ được chứa chữ cái';
    if (value.length > 100) 
        // Node 4: Kiểm tra độ dài
        return 'Họ tên không được quá 100 ký tự';
    // Node 5: Trường hợp thành công
    break;
// Node 6: Trả về chuỗi rỗng
return '';
```

### 7.2 Đồ thị Luồng Điều khiển
```
    1 (Switch case)
    ↓
    2 (if !value.trim())
    ↓
    ├─ True → return error → 6 (Thoát)
    └─ False → 3 (if !regex.test())
              ↓
              ├─ True → return error → 6 (Thoát)
              └─ False → 4 (if length > 100)
                        ↓
                        ├─ True → return error → 6 (Thoát)
                        └─ False → 5 (thành công) → 6 (Thoát)
```

### 7.3 Tính toán Độ phức tạp Cyclomatic
- **E (Cạnh):** 8
- **N (Nút):** 6
- **V(G) = E - N + 2 = 8 - 6 + 2 = 4**

**Số đường kiểm thử độc lập:** 4

### 7.4 Đường Kiểm thử
1. **Đường 1:** 1 → 2 → return error → 6 (Tên rỗng)
2. **Đường 2:** 1 → 2 → 3 → return error → 6 (Định dạng không hợp lệ)
3. **Đường 3:** 1 → 2 → 3 → 4 → return error → 6 (Quá dài)
4. **Đường 4:** 1 → 2 → 3 → 4 → 5 → 6 (Tên hợp lệ)

### 7.5 Bảng Trường hợp Kiểm thử

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.6 | `""` | `"Họ tên không được để trống"` |
| 1.2.3.error.6 | `"Nguyen123"` | `"Họ tên chỉ được chứa chữ cái"` |
| 1.2.3.4.error.6 | `"A".repeat(101)` | `"Họ tên không được quá 100 ký tự"` |
| 1.2.3.4.5.6 | `"Nguyễn Văn A"` | `""` (thành công) |

---

## 8. Hàm Frontend: `validateField` (trường hợp ngsinh)

### 8.1 Phân tích Mã
```javascript
case 'ngsinh':
    // Node 1: Điểm vào switch case
    if (!value) 
        // Node 2: Kiểm tra rỗng
        return 'Ngày sinh không được để trống';
    if (!/^\d{4}-\d{2}-\d{2}$/.test(value)) 
        // Node 3: Kiểm tra định dạng
        return 'Ngày sinh phải theo định dạng YYYY-MM-DD';
    const birthDate = new Date(value);
    // Node 4: Xác thực ngày
    if (isNaN(birthDate.getTime())) 
        // Node 5: Ngày không hợp lệ
        return 'Ngày sinh không hợp lệ';
    if (birthDate > new Date()) 
        // Node 6: Kiểm tra ngày tương lai
        return 'Ngày sinh không được trong tương lai';
    // Node 7: Trường hợp thành công
    break;
// Node 8: Trả về chuỗi rỗng
return '';
```

### 8.2 Đồ thị Luồng Điều khiển
```
    1 (Switch case)
    ↓
    2 (if !value)
    ↓
    ├─ True → return error → 8 (Thoát)
    └─ False → 3 (if !regex.test())
              ↓
              ├─ True → return error → 8 (Thoát)
              └─ False → 4 (new Date())
                        ↓
                        5 (if isNaN())
                        ↓
                        ├─ True → return error → 8 (Thoát)
                        └─ False → 6 (if > today)
                                  ↓
                                  ├─ True → return error → 8 (Thoát)
                                  └─ False → 7 (thành công) → 8 (Thoát)
```

### 8.3 Tính toán Độ phức tạp Cyclomatic
- **E (Cạnh):** 10
- **N (Nút):** 8
- **V(G) = E - N + 2 = 10 - 8 + 2 = 4**

**Số đường kiểm thử độc lập:** 4

### 8.4 Đường Kiểm thử
1. **Đường 1:** 1 → 2 → return error → 8 (Ngày rỗng)
2. **Đường 2:** 1 → 2 → 3 → return error → 8 (Định dạng không hợp lệ)
3. **Đường 3:** 1 → 2 → 3 → 4 → 5 → return error → 8 (Ngày không hợp lệ)
4. **Đường 4:** 1 → 2 → 3 → 4 → 5 → 6 → return error → 8 (Ngày tương lai)
5. **Đường 5:** 1 → 2 → 3 → 4 → 5 → 6 → 7 → 8 (Ngày hợp lệ)

### 8.5 Bảng Trường hợp Kiểm thử

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.8 | `""` | `"Ngày sinh không được để trống"` |
| 1.2.3.error.8 | `"2023/12/31"` | `"Ngày sinh phải theo định dạng YYYY-MM-DD"` |
| 1.2.3.4.5.error.8 | `"2023-13-45"` | `"Ngày sinh không hợp lệ"` |
| 1.2.3.4.5.6.error.8 | `"2025-01-01"` | `"Ngày sinh không được trong tương lai"` |
| 1.2.3.4.5.6.7.8 | `"1990-01-01"` | `""` (thành công) |

---

## 8. Hàm Backend: `delete_employee` (Xóa nhân viên)

### 8.1 Phân tích Mã
```python
@router.delete("/{employee_id}", status_code=204)
def delete_employee(
    employee_id: str, 
    confirmation: str = Query(..., description="Xác nhận xóa bằng cách nhập 'TÔI HIỂU'"),
    db: Session = Depends(get_db)
):
    # Node 1: Điểm vào hàm
    # Node 2: Kiểm tra xác nhận
    if confirmation != "TÔI HIỂU":
        # Node 3: Xác nhận không đúng - trả về lỗi
        raise HTTPException(
            status_code=400, 
            detail="Xác nhận không đúng. Vui lòng nhập 'TÔI HIỂU' để xác nhận xóa."
        )
    
    # Node 4: Kiểm tra nhân viên tồn tại
    employee = db.query(Employee).filter(Employee.manv == employee_id).first()
    if not employee:
        # Node 5: Nhân viên không tồn tại - trả về lỗi
        raise HTTPException(status_code=404, detail="Employee not found")
    
    # Node 6: Kiểm tra số lượng nhân viên
    total_employees = db.query(Employee).count()
    if total_employees <= 1:
        # Node 7: Không thể xóa nhân viên cuối cùng - trả về lỗi
        raise HTTPException(
            status_code=400, 
            detail="Không thể xóa nhân viên cuối cùng trong hệ thống."
        )
    
    # Node 8: Log xóa nhân viên
    print(f"⚠️ DELETING EMPLOYEE: {employee.manv} - {employee.tennv} - {employee.email}")
    
    # Node 9: Thực hiện xóa
    db.delete(employee)
    # Node 10: Commit giao dịch
    db.commit()
    # Node 11: Trả về None
    return None
    # Node 12: Điểm thoát hàm
```

### 8.2 Đồ thị Luồng Điều khiển
```
    1 (Điểm vào)
    ↓
    2 (if confirmation != "TÔI HIỂU")
    ↓
    ├─ True → 3 (return error) → 12 (Thoát)
    └─ False → 4 (query employee)
              ↓
              5 (if not employee)
              ↓
              ├─ True → 5 (return error) → 12 (Thoát)
              └─ False → 6 (count total)
                        ↓
                        7 (if total <= 1)
                        ↓
                        ├─ True → 7 (return error) → 12 (Thoát)
                        └─ False → 8 (log)
                                  ↓
                                  9 (delete)
                                  ↓
                                  10 (commit)
                                  ↓
                                  11 (return None) → 12 (Thoát)
```

### 8.3 Tính toán Độ phức tạp Cyclomatic
- **E (Cạnh):** 8
- **N (Nút):** 12
- **V(G) = E - N + 2 = 8 - 12 + 2 = -2**

**Sửa lại:** Có 3 điều kiện if, mỗi điều kiện tạo 2 cạnh
- **E (Cạnh):** 12 (3 điều kiện × 2 + 6 cạnh khác)
- **N (Nút):** 12
- **V(G) = E - N + 2 = 12 - 12 + 2 = 2**

**Số đường kiểm thử độc lập:** 4

### 8.4 Đường Kiểm thử
1. **Đường 1:** 1 → 2 → 3 → 12 (Xác nhận không đúng)
2. **Đường 2:** 1 → 2 → 4 → 5 → 12 (Nhân viên không tồn tại)
3. **Đường 3:** 1 → 2 → 4 → 5 → 6 → 7 → 12 (Không thể xóa nhân viên cuối cùng)
4. **Đường 4:** 1 → 2 → 4 → 5 → 6 → 7 → 8 → 9 → 10 → 11 → 12 (Xóa thành công)

### 8.5 Bảng Trường hợp Kiểm thử

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.3.12 | `confirmation = "SAI"` | `HTTP 400: "Xác nhận không đúng"` |
| 1.2.4.5.12 | `employee_id = "NV999"` (không tồn tại) | `HTTP 404: "Employee not found"` |
| 1.2.4.5.6.7.12 | `total_employees = 1` | `HTTP 400: "Không thể xóa nhân viên cuối cùng"` |
| 1.2.4.5.6.7.8.9.10.11.12 | `confirmation = "TÔI HIỂU"`, `employee_id = "NV001"` (tồn tại), `total_employees > 1` | `HTTP 204: Xóa thành công` |

### 8.6 Phân lớp Tương đương cho Chức năng Xóa

#### **Phân lớp Hợp lệ:**
- **confirmation:** `"TÔI HIỂU"` (chính xác)
- **employee_id:** Mã nhân viên tồn tại trong hệ thống
- **total_employees:** Số lượng nhân viên > 1

#### **Phân lớp Không hợp lệ:**
- **confirmation:** 
  - Chuỗi rỗng `""`
  - Chuỗi khác `"TÔI HIỂU"` (ví dụ: `"SAI"`, `"Tôi hiểu"`, `"TOI HIEU"`)
  - Chuỗi có khoảng trắng `" TÔI HIỂU "`
- **employee_id:**
  - Mã nhân viên không tồn tại
  - Mã nhân viên rỗng
  - Mã nhân viên không đúng định dạng
- **total_employees:**
  - Số lượng nhân viên = 1 (nhân viên cuối cùng)
  - Số lượng nhân viên = 0 (hệ thống trống)

### 8.7 Giá trị Biên
- **confirmation:** 
  - Biên dưới: `""` (rỗng)
  - Biên trên: `"TÔI HIỂU"` (chính xác)
  - Giá trị gần biên: `"TÔI HIỂU "` (có khoảng trắng)
- **total_employees:**
  - Biên dưới: `1` (không thể xóa)
  - Biên trên: `2` (có thể xóa)
  - Giá trị gần biên: `3` (nhiều nhân viên)

---

## 9. Hàm Frontend: `validateField` (ngsinh)

### 9.1 Phân tích Mã
```javascript
case 'ngsinh':
  if (!value) {
    return 'Ngày sinh không được để trống';
  }
  const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
  if (!dateRegex.test(value)) {
    return 'Ngày sinh phải theo định dạng YYYY-MM-DD';
  }
  const date = new Date(value);
  if (isNaN(date.getTime())) {
    return 'Ngày sinh không hợp lệ';
  }
  const today = new Date();
  if (date > today) {
    return 'Ngày sinh không được trong tương lai';
  }
  return '';
```

### 9.2 Đồ thị Luồng Điều khiển
```
    1 (Switch case)
    ↓
    2 (if !value)
    ↓
    ├─ True → return error → 8 (Thoát)
    └─ False → 3 (if !regex.test())
              ↓
              ├─ True → return error → 8 (Thoát)
              └─ False → 4 (new Date())
                        ↓
                        5 (if isNaN())
                        ↓
                        ├─ True → return error → 8 (Thoát)
                        └─ False → 6 (if > today)
                                  ↓
                                  ├─ True → return error → 8 (Thoát)
                                  └─ False → 7 (thành công) → 8 (Thoát)
```

### 9.3 Tính toán Độ phức tạp Cyclomatic
- **E (Cạnh):** 10
- **N (Nút):** 8
- **V(G) = E - N + 2 = 10 - 8 + 2 = 4**

**Số đường kiểm thử độc lập:** 4

### 9.4 Đường Kiểm thử
1. **Đường 1:** 1 → 2 → return error → 8 (Ngày rỗng)
2. **Đường 2:** 1 → 2 → 3 → return error → 8 (Định dạng không hợp lệ)
3. **Đường 3:** 1 → 2 → 3 → 4 → 5 → return error → 8 (Ngày không hợp lệ)
4. **Đường 4:** 1 → 2 → 3 → 4 → 5 → 6 → return error → 8 (Ngày tương lai)
5. **Đường 5:** 1 → 2 → 3 → 4 → 5 → 6 → 7 → 8 (Ngày hợp lệ)

### 9.5 Bảng Trường hợp Kiểm thử

| Đường kiểm thử | Giá trị đầu vào | Kết quả mong đợi |
|----------------|-----------------|-------------------|
| 1.2.error.8 | `""` | `"Ngày sinh không được để trống"` |
| 1.2.3.error.8 | `"2023/12/31"` | `"Ngày sinh phải theo định dạng YYYY-MM-DD"` |
| 1.2.3.4.5.error.8 | `"2023-13-45"` | `"Ngày sinh không hợp lệ"` |
| 1.2.3.4.5.6.error.8 | `"2025-01-01"` | `"Ngày sinh không được trong tương lai"` |
| 1.2.3.4.5.6.7.8 | `"1990-01-01"` | `""` (thành công) |

---

## 10. Tổng kết Coverage

### 10.1 Hàm Backend
- **generate_employee_id:** 3 đường, 3 trường hợp kiểm thử
- **create_employee:** 2 đường, 2 trường hợp kiểm thử
- **delete_employee:** 4 đường, 4 trường hợp kiểm thử
- **validate_email_domain:** 9 đường, 9 trường hợp kiểm thử ⭐ **PHỨC TẠP NHẤT**
- **validate_tennv:** 3 đường, 3 trường hợp kiểm thử
- **validate_sdt:** 2 đường, 2 trường hợp kiểm thử
- **validate_ngsinh:** 4 đường, 4 trường hợp kiểm thử

### 10.2 Hàm Frontend
- **validateField (tennv):** 4 đường, 4 trường hợp kiểm thử
- **validateField (ngsinh):** 5 đường, 5 trường hợp kiểm thử

### 10.3 Tổng cộng
- **Tổng Trường hợp Kiểm thử:** 37
- **Branch Coverage:** 100%
- **Statement Coverage:** 100%
- **Path Coverage:** 100%

### 10.4 Các trường bắt buộc đã kiểm thử:
- ✅ **tennv** (Họ tên)
- ✅ **sdt** (Số điện thoại)
- ✅ **ngsinh** (Ngày sinh)
- ✅ **email** (Email - kiểm tra trùng lặp + xác thực phức tạp) ⭐
- ✅ **Logic tạo nhân viên** (create_employee)

### 10.5 Các trường có thể bỏ trống:
- **dchithuongtru** (Địa chỉ thường trú)
- **noidkhktt** (Nơi đăng ký HKTT)
- **trinhdo** (Trình độ)
- **qtich** (Quốc tịch)
- **skhoe** (Sức khỏe)
- **hotencha** (Họ tên cha)
- **hotenme** (Họ tên mẹ)

---

## 11. Cách chạy Test

### 11.1 Backend Test
```bash
cd backend
python simple_test.py
```

### 11.2 Frontend Test
```bash
cd frontend
node simple_test.js
``` 