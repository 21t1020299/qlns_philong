from fastapi import APIRouter, UploadFile, File, HTTPException, Depends
from fastapi.responses import FileResponse
from sqlalchemy.orm import Session
from app.database.connection import get_db
from app.models.employee import Employee
from app.utils.image_utils import (
    save_avatar_image,
    save_id_card_image,
    delete_image_file,
    get_image_url
)
from pathlib import Path
import os

router = APIRouter(
    prefix="/api/upload",
    tags=["upload"]
)

@router.post("/avatar/{employee_id}")
async def upload_avatar(
    employee_id: str,
    file: UploadFile = File(...),
    db: Session = Depends(get_db)
):
    """Upload avatar image for employee"""
    try:
        # Check if employee exists
        employee = db.query(Employee).filter(Employee.manv == employee_id).first()
        if not employee:
            raise HTTPException(status_code=404, detail="Employee not found")
        
        # Delete old avatar if exists
        if employee.anhchandung:
            await delete_image_file(employee.anhchandung)
        
        # Save new avatar
        file_path = await save_avatar_image(file)
        
        # Update employee record
        employee.anhchandung = file_path
        db.commit()
        
        return {
            "message": "Avatar uploaded successfully",
            "file_path": file_path,
            "url": get_image_url(file_path)
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error uploading avatar: {str(e)}")

@router.post("/id-card/{employee_id}")
async def upload_id_card(
    employee_id: str,
    file: UploadFile = File(...),
    db: Session = Depends(get_db)
):
    """Upload ID card image for employee"""
    try:
        # Check if employee exists
        employee = db.query(Employee).filter(Employee.manv == employee_id).first()
        if not employee:
            raise HTTPException(status_code=404, detail="Employee not found")
        
        # Delete old ID card image if exists
        if employee.anhcmnd:
            await delete_image_file(employee.anhcmnd)
        
        # Save new ID card image
        file_path = await save_id_card_image(file)
        
        # Update employee record
        employee.anhcmnd = file_path
        db.commit()
        
        return {
            "message": "ID card image uploaded successfully",
            "file_path": file_path,
            "url": get_image_url(file_path)
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error uploading ID card image: {str(e)}")

@router.delete("/avatar/{employee_id}")
async def delete_avatar(
    employee_id: str,
    db: Session = Depends(get_db)
):
    """Delete avatar image for employee"""
    try:
        # Check if employee exists
        employee = db.query(Employee).filter(Employee.manv == employee_id).first()
        if not employee:
            raise HTTPException(status_code=404, detail="Employee not found")
        
        # Delete file if exists
        if employee.anhchandung:
            await delete_image_file(employee.anhchandung)
            employee.anhchandung = None
            db.commit()
        
        return {"message": "Avatar deleted successfully"}
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting avatar: {str(e)}")

@router.delete("/id-card/{employee_id}")
async def delete_id_card(
    employee_id: str,
    db: Session = Depends(get_db)
):
    """Delete ID card image for employee"""
    try:
        # Check if employee exists
        employee = db.query(Employee).filter(Employee.manv == employee_id).first()
        if not employee:
            raise HTTPException(status_code=404, detail="Employee not found")
        
        # Delete file if exists
        if employee.anhcmnd:
            await delete_image_file(employee.anhcmnd)
            employee.anhcmnd = None
            db.commit()
        
        return {"message": "ID card image deleted successfully"}
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting ID card image: {str(e)}")

@router.get("/image/{file_path:path}")
async def get_image(file_path: str):
    """Serve image files"""
    try:
        # Security: prevent directory traversal
        if ".." in file_path or file_path.startswith("/"):
            raise HTTPException(status_code=400, detail="Invalid file path")
        
        # Check if file exists
        full_path = Path("uploads") / file_path
        if not full_path.exists() or not full_path.is_file():
            raise HTTPException(status_code=404, detail="Image not found")
        
        return FileResponse(full_path)
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error serving image: {str(e)}") 