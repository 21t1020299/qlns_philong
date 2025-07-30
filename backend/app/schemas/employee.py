from pydantic import BaseModel, EmailStr, validator
from typing import Optional
from datetime import date, datetime
import re
import uuid

# Base Employee Schema
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
        today = date.today()
        age = today.year - v.year - ((today.month, today.day) < (v.month, v.day))
        if age < 18 or age >= 55:
            raise ValueError('Tuổi phải từ 18-54')
        return v

    @validator('dchi', 'dchithuongtru')
    def validate_address(cls, v):
        if len(v) > 200:
            raise ValueError('Địa chỉ không được quá 200 ký tự')
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
        parts = v.split('@')
        if len(parts) != 2:
            raise ValueError('Email không hợp lệ')
        
        domain = parts[1]
        domain_parts = domain.split('.')
        if len(domain_parts) < 2 or len(domain_parts) > 4:
            raise ValueError('Domain tối đa 4 cấp')
        
        return v

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

# Employee Response Schema
class EmployeeResponse(EmployeeBase):
    id: str
    manv: str
    created_at: datetime
    updated_at: datetime

    class Config:
        from_attributes = True

# Employee List Response
class EmployeeListResponse(BaseModel):
    employees: list[EmployeeResponse]
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