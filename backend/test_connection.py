#!/usr/bin/env python3
"""
Test database connection
"""

from app.database.connection import engine
from sqlalchemy import text

def test_connection():
    try:
        print("Testing database connection...")
        with engine.connect() as conn:
            result = conn.execute(text("SELECT 1"))
            print("✅ Database connection successful!")
            
            # Test querying employees
            print("Testing employee query...")
            result = conn.execute(text("SELECT COUNT(*) FROM nhanvien"))
            count = result.fetchone()[0]
            print(f"✅ Found {count} employees in database")
            
            # Test querying chucvu
            print("Testing chucvu query...")
            result = conn.execute(text("SELECT COUNT(*) FROM chucvu"))
            count = result.fetchone()[0]
            print(f"✅ Found {count} chucvu in database")
            
    except Exception as e:
        print(f"❌ Database connection failed: {e}")

if __name__ == "__main__":
    test_connection() 