from fastapi import APIRouter, Depends, HTTPException, status, Query
from sqlalchemy.orm import Session
import uuid
import re
from datetime import date, datetime, timedelta
from sqlalchemy import func
from app.database.connection import get_db
from app.models.employee import Employee
from app.schemas.employee import (
    EmployeeCreate,
    EmployeeUpdate,
    EmployeeResponse,
    EmployeeListResponse,
    EmployeeStats
)

router = APIRouter(
    prefix="/api/employees",
    tags=["employees"]
)

def generate_employee_id(db: Session) -> str:
    """Generate unique employee ID"""
    while True:
        # Generate a random 6-digit number
        employee_id = f"NV{str(uuid.uuid4().int % 1000000).zfill(6)}"
        
        # Check if it already exists
        existing = db.query(Employee).filter(Employee.manv == employee_id).first()
        if not existing:
            return employee_id

@router.get("/", response_model=EmployeeListResponse)
def get_employees(
    skip: int = Query(0, ge=0),
    limit: int = Query(10, ge=1, le=100),
    search: str = Query(None),
    gender: str = Query(None),
    db: Session = Depends(get_db)
):
    """Get all employees with pagination and search"""
    query = db.query(Employee)
    
    # Apply search filter
    if search:
        search_term = f"%{search}%"
        query = query.filter(
            (Employee.tennv.ilike(search_term)) |
            (Employee.email.ilike(search_term)) |
            (Employee.sdt.ilike(search_term)) |
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

@router.get("/stats", response_model=EmployeeStats)
def get_employee_stats(db: Session = Depends(get_db)):
    """Get employee statistics"""
    try:
        # Get total count
        total = db.query(Employee).count()
        
        # Get gender distribution
        male_count = db.query(Employee).filter(Employee.gtinh == "Nam").count()
        female_count = db.query(Employee).filter(Employee.gtinh == "Nữ").count()
        
        # Get education level distribution
        education_stats = db.query(
            Employee.trinhdo,
            func.count(Employee.id).label('count')
        ).group_by(Employee.trinhdo).all()
        
        # Get age distribution
        today = date.today()
        age_stats = db.query(
            func.extract('year', today) - func.extract('year', Employee.ngsinh)
        ).all()
        
        # Calculate age ranges
        age_ranges = {
            "18-25": 0,
            "26-35": 0,
            "36-45": 0,
            "46-54": 0
        }
        
        for age_tuple in age_stats:
            age = age_tuple[0]
            if age and 18 <= age <= 25:
                age_ranges["18-25"] += 1
            elif age and 26 <= age <= 35:
                age_ranges["26-35"] += 1
            elif age and 36 <= age <= 45:
                age_ranges["36-45"] += 1
            elif age and 46 <= age <= 54:
                age_ranges["46-54"] += 1
        
        # Get recent employees (last 30 days)
        thirty_days_ago = datetime.now() - timedelta(days=30)
        recent_count = db.query(Employee).filter(
            Employee.created_at >= thirty_days_ago
        ).count()
        
        return EmployeeStats(
            total=total,
            male_count=male_count,
            female_count=female_count,
            education_distribution={item.trinhdo: item.count for item in education_stats},
            age_distribution=age_ranges,
            recent_additions=recent_count,
            last_updated=datetime.now()
        )
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error getting statistics: {str(e)}")

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