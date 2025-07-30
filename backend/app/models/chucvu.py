from sqlalchemy import Column, String, Text
from app.database.connection import Base

class ChucVu(Base):
    __tablename__ = "chucvu"
    
    # Primary key
    macv = Column(String(10), primary_key=True, index=True)
    
    # Basic information
    tencv = Column(String(100), nullable=False)  # Tên chức vụ
    mota = Column(Text, nullable=True)  # Mô tả 