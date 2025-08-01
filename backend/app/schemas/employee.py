from pydantic import BaseModel, EmailStr, validator
from typing import Optional
from datetime import date, datetime
import re
import uuid

# Base Employee Schema for new employees (strict validation)
class EmployeeBase(BaseModel):
    tennv: str
    gtinh: str
    email: EmailStr
    sdt: str
    ngsinh: date
    dchi: str
    dchithuongtru: str
    noidkhktt: str
    dtoc: str
    trinhdo: str
    qtich: str
    skhoe: str
    macv: str
    hotencha: str
    hotenme: str

    @validator('tennv')
    def validate_tennv(cls, v):
        if not v.strip():
            raise ValueError('Họ tên không được để trống')
        if not re.match(r'^[a-zA-ZÀ-ỹ\s\-]+$', v):
            raise ValueError('Họ tên chỉ được chứa chữ cái, dấu tiếng Việt và khoảng trắng')
        return v.strip()

    @validator('sdt')
    def validate_sdt(cls, v):
        if not re.match(r'^0[0-9]{9}$', v):
            raise ValueError('Số điện thoại phải có 10 số và bắt đầu bằng 0')
        return v

    @validator('ngsinh')
    def validate_ngsinh(cls, v):
        # Kiểm tra ngày sinh không được trong tương lai
        today = date.today()
        if v > today:
            raise ValueError('Ngày sinh không được trong tương lai')
        
        # Kiểm tra ngày sinh không được quá xa trong quá khứ (trước 1900)
        if v.year < 1900:
            raise ValueError('Ngày sinh không hợp lệ (trước năm 1900)')
        
        # Tính tuổi
        age = today.year - v.year - ((today.month, today.day) < (v.month, v.day))
        if age < 18 or age >= 66:
            raise ValueError('Tuổi phải từ 18-65')
        return v

    @validator('dchi', 'dchithuongtru')
    def validate_address(cls, v):
        if len(v) > 200:
            raise ValueError('Địa chỉ không được quá 200 ký tự')
        # Kiểm tra ký tự đặc biệt không hợp lệ trong địa chỉ
        if re.search(r'[<>"\']', v):
            raise ValueError('Địa chỉ không được chứa ký tự đặc biệt: < > " \'')
        return v

    @validator('hotencha', 'hotenme')
    def validate_parent_name(cls, v):
        if len(v) > 100:
            raise ValueError('Họ tên cha/mẹ không được quá 100 ký tự')
        if not re.match(r'^[a-zA-ZÀ-ỹ\s\-]+$', v):
            raise ValueError('Họ tên cha/mẹ chỉ được chứa chữ cái')
        return v

    @validator('email')
    def validate_email_domain(cls, v):
        # Kiểm tra format email cơ bản
        if not re.match(r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$', v):
            raise ValueError('Email không đúng định dạng')
        
        # Kiểm tra ký tự đặc biệt không hợp lệ
        if re.search(r'[<>"\']', v):
            raise ValueError('Email không được chứa ký tự đặc biệt: < > " \'')
        
        # Kiểm tra độ dài email
        if len(v) > 254:
            raise ValueError('Email quá dài (tối đa 254 ký tự)')
        
        parts = v.split('@')
        if len(parts) != 2:
            raise ValueError('Email không hợp lệ')
        
        local_part = parts[0]
        domain = parts[1]
        
        # Kiểm tra local part
        if len(local_part) > 64:
            raise ValueError('Phần trước @ quá dài (tối đa 64 ký tự)')
        if local_part.startswith('.') or local_part.endswith('.'):
            raise ValueError('Phần trước @ không được bắt đầu hoặc kết thúc bằng dấu chấm')
        
        # Kiểm tra domain
        domain_parts = domain.split('.')
        if len(domain_parts) < 2 or len(domain_parts) > 4:
            raise ValueError('Domain phải có 2-4 phần')
        
        # Kiểm tra TLD (top-level domain)
        tld = domain_parts[-1]
        if len(tld) < 2:
            raise ValueError('Domain cấp cao nhất phải có ít nhất 2 ký tự')
        
        return v.lower()  # Chuyển về lowercase để chuẩn hóa

# Employee Schema for loading from database (no strict validation)
class EmployeeDB(BaseModel):
    tennv: str
    gtinh: str
    email: str  # Use str instead of EmailStr for database data
    sdt: str
    ngsinh: date
    dchi: str
    dchithuongtru: str
    noidkhktt: str
    dtoc: str
    trinhdo: str
    qtich: str
    skhoe: str
    macv: str
    hotencha: str
    hotenme: str

    class Config:
        from_attributes = True

# Create Employee Schema
class EmployeeCreate(EmployeeBase):
    pass

# Update Employee Schema
class EmployeeUpdate(BaseModel):
    tennv: Optional[str] = None
    gtinh: Optional[str] = None
    email: Optional[EmailStr] = None
    sdt: Optional[str] = None
    ngsinh: Optional[date] = None
    dchi: Optional[str] = None
    dchithuongtru: Optional[str] = None
    noidkhktt: Optional[str] = None
    dtoc: Optional[str] = None
    trinhdo: Optional[str] = None
    qtich: Optional[str] = None
    skhoe: Optional[str] = None
    macv: Optional[str] = None
    hotencha: Optional[str] = None
    hotenme: Optional[str] = None
    anhchandung: Optional[str] = None
    anhcmnd: Optional[str] = None

# Employee Response Schema for new employees
class EmployeeResponse(EmployeeBase):
    manv: str
    anhchandung: Optional[str] = None
    anhcmnd: Optional[str] = None

    class Config:
        from_attributes = True

# Employee Response Schema for database data
class EmployeeDBResponse(EmployeeDB):
    manv: str
    anhchandung: Optional[str] = None
    anhcmnd: Optional[str] = None

    class Config:
        from_attributes = True

# Employee List Response
class EmployeeListResponse(BaseModel):
    employees: list[EmployeeDBResponse]  # Use DB response for loading data
    total: int
    page: int
    size: int

# Employee Statistics Schema
class EmployeeStats(BaseModel):
    total: int
    male_count: int
    female_count: int
    education_distribution: dict[str, int]
    age_distribution: dict[str, int]
    recent_additions: int
    last_updated: datetime