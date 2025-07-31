#!/usr/bin/env python3
"""
Script to initialize database and add sample data
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from app.database.connection import engine
from app.models import employee, chucvu
from app.models.employee import Employee
from app.models.chucvu import ChucVu
from sqlalchemy.orm import sessionmaker

def init_database():
    """Initialize database tables"""
    print("Creating database tables...")
    employee.Base.metadata.create_all(bind=engine)
    chucvu.Base.metadata.create_all(bind=engine)
    print("✅ Database tables created successfully")

def add_sample_data():
    """Add sample data to database"""
    SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
    db = SessionLocal()
    
    try:
        # Add sample chức vụ
        print("Adding sample chức vụ...")
        sample_chucvu = [
            {"macv": "CV001", "tencv": "Nhân viên"},
            {"macv": "CV002", "tencv": "Trưởng nhóm"},
            {"macv": "CV003", "tencv": "Quản lý"},
            {"macv": "CV004", "tencv": "Giám đốc"},
        ]
        
        for cv_data in sample_chucvu:
            existing = db.query(ChucVu).filter(ChucVu.macv == cv_data["macv"]).first()
            if not existing:
                chucvu = ChucVu(**cv_data)
                db.add(chucvu)
                print(f"✅ Added chức vụ: {cv_data['macv']} - {cv_data['tencv']}")
        
        # Add sample employees
        print("Adding sample employees...")
        sample_employees = [
            {
                "tennv": "Nguyễn Văn An",
                "gtinh": "Nam",
                "email": "an.nguyen@philong.com",
                "sdt": "0123456789",
                "ngsinh": "1990-05-15",
                "dchi": "123 Đường ABC, Quận 1, TP.HCM",
                "dchithuongtru": "123 Đường ABC, Quận 1, TP.HCM",
                "noidkhktt": "Quận 1, TP.HCM",
                "dtoc": "Kinh",
                "trinhdo": "Đại học",
                "qtich": "Việt Nam",
                "skhoe": "Tốt",
                "macv": "CV001",
                "hotencha": "Nguyễn Văn Bố",
                "hotenme": "Trần Thị Mẹ"
            },
            {
                "tennv": "Trần Thị Bình",
                "gtinh": "Nữ",
                "email": "binh.tran@philong.com",
                "sdt": "0987654321",
                "ngsinh": "1992-08-20",
                "dchi": "456 Đường XYZ, Quận 2, TP.HCM",
                "dchithuongtru": "456 Đường XYZ, Quận 2, TP.HCM",
                "noidkhktt": "Quận 2, TP.HCM",
                "dtoc": "Kinh",
                "trinhdo": "Cao đẳng",
                "qtich": "Việt Nam",
                "skhoe": "Tốt",
                "macv": "CV002",
                "hotencha": "Trần Văn Bố",
                "hotenme": "Nguyễn Thị Mẹ"
            },
            {
                "tennv": "Lê Văn Cường",
                "gtinh": "Nam",
                "email": "cuong.le@philong.com",
                "sdt": "0111222333",
                "ngsinh": "1988-12-10",
                "dchi": "789 Đường DEF, Quận 3, TP.HCM",
                "dchithuongtru": "789 Đường DEF, Quận 3, TP.HCM",
                "noidkhktt": "Quận 3, TP.HCM",
                "dtoc": "Kinh",
                "trinhdo": "Đại học",
                "qtich": "Việt Nam",
                "skhoe": "Tốt",
                "macv": "CV003",
                "hotencha": "Lê Văn Bố",
                "hotenme": "Phạm Thị Mẹ"
            },
            {
                "tennv": "Phạm Thị Dung",
                "gtinh": "Nữ",
                "email": "dung.pham@philong.com",
                "sdt": "0444555666",
                "ngsinh": "1995-03-25",
                "dchi": "321 Đường GHI, Quận 4, TP.HCM",
                "dchithuongtru": "321 Đường GHI, Quận 4, TP.HCM",
                "noidkhktt": "Quận 4, TP.HCM",
                "dtoc": "Kinh",
                "trinhdo": "Trung cấp",
                "qtich": "Việt Nam",
                "skhoe": "Khá",
                "macv": "CV001",
                "hotencha": "Phạm Văn Bố",
                "hotenme": "Lê Thị Mẹ"
            },
            {
                "tennv": "Hoàng Văn Em",
                "gtinh": "Nam",
                "email": "em.hoang@philong.com",
                "sdt": "0777888999",
                "ngsinh": "1991-07-08",
                "dchi": "654 Đường JKL, Quận 5, TP.HCM",
                "dchithuongtru": "654 Đường JKL, Quận 5, TP.HCM",
                "noidkhktt": "Quận 5, TP.HCM",
                "dtoc": "Kinh",
                "trinhdo": "Đại học",
                "qtich": "Việt Nam",
                "skhoe": "Tốt",
                "macv": "CV002",
                "hotencha": "Hoàng Văn Bố",
                "hotenme": "Vũ Thị Mẹ"
            }
        ]
        
        for emp_data in sample_employees:
            # Check if email already exists
            existing = db.query(Employee).filter(Employee.email == emp_data["email"]).first()
            if not existing:
                # Generate employee ID
                last_employee = db.query(Employee).order_by(Employee.manv.desc()).first()
                if not last_employee:
                    manv = "NV001"
                else:
                    try:
                        last_number = int(last_employee.manv[2:])
                        next_number = last_number + 1
                        manv = f"NV{str(next_number).zfill(3)}"
                    except ValueError:
                        manv = f"NV{str(len(sample_employees) + 1).zfill(3)}"
                
                emp_data["manv"] = manv
                employee = Employee(**emp_data)
                db.add(employee)
                print(f"✅ Added employee: {manv} - {emp_data['tennv']}")
        
        db.commit()
        print("✅ Sample data added successfully")
        
    except Exception as e:
        print(f"❌ Error adding sample data: {e}")
        db.rollback()
    finally:
        db.close()

def main():
    """Main function"""
    print("🚀 Initializing PhiLong Employee Management System Database...")
    
    try:
        init_database()
        add_sample_data()
        print("🎉 Database initialization completed successfully!")
        
        # Show summary
        SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
        db = SessionLocal()
        
        employee_count = db.query(Employee).count()
        chucvu_count = db.query(ChucVu).count()
        
        print(f"\n📊 Database Summary:")
        print(f"   - Employees: {employee_count}")
        print(f"   - Chức vụ: {chucvu_count}")
        
        db.close()
        
    except Exception as e:
        print(f"❌ Error initializing database: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main() 