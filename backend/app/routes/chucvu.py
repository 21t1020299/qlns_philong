from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from app.database.connection import get_db
from app.models.chucvu import ChucVu
from app.schemas.chucvu import ChucVuCreate, ChucVuUpdate, ChucVuResponse

router = APIRouter(prefix="/api/chucvu", tags=["chucvu"])

@router.get("/", response_model=List[ChucVuResponse])
def get_chucvu(db: Session = Depends(get_db)):
    """Get all chức vụ"""
    chucvu_list = db.query(ChucVu).all()
    return chucvu_list

@router.post("/", response_model=ChucVuResponse, status_code=201)
def create_chucvu(chucvu_data: ChucVuCreate, db: Session = Depends(get_db)):
    """Create new chức vụ"""
    # Check if macv already exists
    existing = db.query(ChucVu).filter(ChucVu.macv == chucvu_data.macv).first()
    if existing:
        raise HTTPException(status_code=400, detail="Mã chức vụ đã tồn tại")
    
    chucvu = ChucVu(**chucvu_data.dict())
    db.add(chucvu)
    db.commit()
    db.refresh(chucvu)
    return chucvu

@router.post("/init")
def init_chucvu(db: Session = Depends(get_db)):
    """Initialize default chức vụ"""
    default_chucvu = [
        {"macv": "CV001", "tencv": "Nhân viên", "mota": "Nhân viên cơ bản"},
        {"macv": "CV002", "tencv": "Trưởng nhóm", "mota": "Trưởng nhóm dự án"},
        {"macv": "CV003", "tencv": "Quản lý", "mota": "Quản lý phòng ban"},
        {"macv": "CV004", "tencv": "Giám đốc", "mota": "Giám đốc công ty"},
    ]
    
    for cv_data in default_chucvu:
        existing = db.query(ChucVu).filter(ChucVu.macv == cv_data["macv"]).first()
        if not existing:
            chucvu = ChucVu(**cv_data)
            db.add(chucvu)
    
    db.commit()
    return {"message": "Chức vụ đã được khởi tạo"} 