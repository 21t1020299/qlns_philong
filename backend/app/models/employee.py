from sqlalchemy import Column, String, Date, Text, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from app.database.connection import Base

class Employee(Base):
    __tablename__ = "nhanvien"
    
    # Primary key - use manv as primary key
    manv = Column(String(10), primary_key=True, index=True)
    
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
    macv = Column(String(10), ForeignKey("chucvu.macv"), nullable=False)  # Mã chức vụ
    chucvu = relationship("ChucVu", backref="nhanvien")
    
    # Family information
    hotencha = Column(String(100), nullable=False)  # Họ tên cha
    hotenme = Column(String(100), nullable=False)  # Họ tên mẹ
    
    # Images
    anhchandung = Column(String(255), nullable=True)  # Ảnh chân dung
    anhcmnd = Column(String(255), nullable=True)  # Ảnh CMND