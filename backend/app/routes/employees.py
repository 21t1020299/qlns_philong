from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List, Optional
import uuid
import re

from app.database.connection import get_db
from app.models.employee import Employee
from app.schemas.employee import (
    EmployeeCreate, 
    EmployeeUpdate, 
    EmployeeResponse, 
    EmployeeListResponse
)

router = APIRouter(prefix="/api/employees", tags=["employees"])

def generate_employee_id(db: Session) -> str:
    """Generate unique employee ID (NV001, NV002, ...)"""
    # Get the last employee ID
    last_employee = db.query(Employee).order_by(Employee.manv.desc()).first()
    
    if not last_employee:
        return "NV001"
    
    # Extract number from last ID
    match = re.match(r'NV(\d+)', last_employee.manv)
    if not match:
        return "NV001"
    
    last_number = int(match.group(1))
    next_number = last_number + 1
    
    return f"NV{next_number:03d}"

@router.get("/", response_model=EmployeeListResponse)
def get_employees(
    skip: int = Query(0, ge=0),
    limit: int = Query(10, ge=1, le=100),
    search: Optional[str] = None,
    gender: Optional[str] = None,
    db: Session = Depends(get_db)
):
    """Get all employees with pagination and filtering"""
    query = db.query(Employee)
    
    # Apply search filter
    if search:
        search_term = f"%{search}%"
        query = query.filter(
            (Employee.tennv.ilike(search_term)) |
            (Employee.email.ilike(search_term)) |
            (Employee.manv.ilike(search_term))
        )
    
    # Apply gender filter
    if gender:
        query = query.filter(Employee.gtinh == gender)
    
    # Get total count
    total = query.count()
    
    # Apply pagination
    employees = query.offset(skip).limit(limit).all()
    
    return EmployeeListResponse(
        employees=employees,
        total=total,
        page=skip // limit + 1,
        size=limit
    )

@router.get("/{employee_id}", response_model=EmployeeResponse)
def get_employee(employee_id: str, db: Session = Depends(get_db)):
    """Get employee by ID"""
    employee = db.query(Employee).filter(Employee.id == employee_id).first()
    if not employee:
        raise HTTPException(status_code=404, detail="Employee not found")
    return employee

@router.post("/", response_model=EmployeeResponse, status_code=201)
def create_employee(employee_data: EmployeeCreate, db: Session = Depends(get_db)):
    """Create new employee"""
    # Check if email already exists
    existing_email = db.query(Employee).filter(Employee.email == employee_data.email).first()
    if existing_email:
        raise HTTPException(status_code=400, detail="Email already exists")
    
    # Generate unique employee ID
    manv = generate_employee_id(db)
    
    # Create employee object
    employee = Employee(
        id=str(uuid.uuid4()),
        manv=manv,
        **employee_data.dict()
    )
    
    db.add(employee)
    db.commit()
    db.refresh(employee)
    
    return employee

@router.put("/{employee_id}", response_model=EmployeeResponse)
def update_employee(
    employee_id: str, 
    employee_data: EmployeeUpdate, 
    db: Session = Depends(get_db)
):
    """Update employee"""
    employee = db.query(Employee).filter(Employee.id == employee_id).first()
    if not employee:
        raise HTTPException(status_code=404, detail="Employee not found")
    
    # Check if email already exists (if being updated)
    if employee_data.email and employee_data.email != employee.email:
        existing_email = db.query(Employee).filter(Employee.email == employee_data.email).first()
        if existing_email:
            raise HTTPException(status_code=400, detail="Email already exists")
    
    # Update only provided fields
    update_data = employee_data.dict(exclude_unset=True)
    for field, value in update_data.items():
        setattr(employee, field, value)
    
    db.commit()
    db.refresh(employee)
    
    return employee

@router.delete("/{employee_id}", status_code=204)
def delete_employee(employee_id: str, db: Session = Depends(get_db)):
    """Delete employee"""
    employee = db.query(Employee).filter(Employee.id == employee_id).first()
    if not employee:
        raise HTTPException(status_code=404, detail="Employee not found")
    
    db.delete(employee)
    db.commit()
    
    return None

@router.get("/stats/summary")
def get_employee_stats(db: Session = Depends(get_db)):
    """Get employee statistics"""
    total_employees = db.query(Employee).count()
    male_count = db.query(Employee).filter(Employee.gtinh == "Nam").count()
    female_count = db.query(Employee).filter(Employee.gtinh == "Nữ").count()
    
    return {
        "total": total_employees,
        "male": male_count,
        "female": female_count,
        "male_percentage": round((male_count / total_employees * 100) if total_employees > 0 else 0, 1),
        "female_percentage": round((female_count / total_employees * 100) if total_employees > 0 else 0, 1)
    }