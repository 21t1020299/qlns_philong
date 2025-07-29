from sqlalchemy import Column, String, Date, Text, DateTime
from sqlalchemy.sql import func
from app.database.connection import Base

class Employee(Base):
    __tablename__ = "employees"
    
    # Primary key
    id = Column(String(36), primary_key=True, index=True)
    
    # Employee ID (auto-generated)
    manv = Column(String(10), unique=True, index=True, nullable=False)
    
    # Basic information
    tennv = Column(String(100), nullable=False)
    gtinh = Column(String(10), nullable=False)  # Nam/Nữ
    email = Column(String(100), unique=True, nullable=False)
    sdt = Column(String(15), nullable=False)
    ngsinh = Column(Date, nullable=False)
    
    # Address information
    dchi = Column(Text, nullable=False)  # Địa chỉ hiện tại
    dchithuongtru = Column(Text, nullable=False)  # Địa chỉ thường trú
    noidkhktt = Column(String(100), nullable=False)  # Nơi đăng ký HKTT
    
    # Personal information
    dtoc = Column(String(50), nullable=False)  # Dân tộc
    trinhdo = Column(String(50), nullable=False)  # Trình độ
    qtich = Column(String(50), nullable=False)  # Quốc tịch
    skhoe = Column(String(50), nullable=False)  # Sức khỏe
    
    # Work information
    macv = Column(String(10), nullable=False)  # Mã chức vụ
    
    # Family information
    hotencha = Column(String(100), nullable=False)  # Họ tên cha
    hotenme = Column(String(100), nullable=False)  # Họ tên mẹ
    
    # Timestamps
    created_at = Column(DateTime(timezone=True), server_default=func.now())
    updated_at = Column(DateTime(timezone=True), server_default=func.now(), onupdate=func.now())