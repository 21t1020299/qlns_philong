#!/usr/bin/env python3
"""
Script to connect to Supabase and query nhanvien table
"""

import psycopg2
import os
from dotenv import load_dotenv

# Load environment variables
load_dotenv('backend/.env')

def connect_to_supabase():
    """Connect to Supabase database"""
    try:
        # Supabase connection details
        DATABASE_URL = "postgresql://postgres:PhiLong2025!@db.wsisepsdhioaikdzrnmd.supabase.co:5432/postgres"
        
        print("🔗 Connecting to Supabase...")
        conn = psycopg2.connect(
            DATABASE_URL,
            sslmode='require'
        )
        print("✅ Connected successfully!")
        return conn
    except Exception as e:
        print(f"❌ Connection failed: {e}")
        return None

def list_tables(conn):
    """List all tables in the database"""
    try:
        cursor = conn.cursor()
        cursor.execute("""
            SELECT table_name 
            FROM information_schema.tables 
            WHERE table_schema = 'public'
            ORDER BY table_name;
        """)
        tables = cursor.fetchall()
        cursor.close()
        
        print("\n📋 Tables in database:")
        for table in tables:
            print(f"  - {table[0]}")
        return [table[0] for table in tables]
    except Exception as e:
        print(f"❌ Error listing tables: {e}")
        return []

def query_nhanvien_table(conn):
    """Query the nhanvien table"""
    try:
        cursor = conn.cursor()
        
        # Check if table exists
        cursor.execute("""
            SELECT EXISTS (
                SELECT FROM information_schema.tables 
                WHERE table_schema = 'public' 
                AND table_name = 'nhanvien'
            );
        """)
        table_exists = cursor.fetchone()[0]
        
        if not table_exists:
            print("❌ Table 'nhanvien' does not exist!")
            return
        
        # Get table structure
        print("\n📊 Table structure:")
        cursor.execute("""
            SELECT column_name, data_type, is_nullable
            FROM information_schema.columns
            WHERE table_name = 'nhanvien'
            ORDER BY ordinal_position;
        """)
        columns = cursor.fetchall()
        
        for col in columns:
            print(f"  - {col[0]}: {col[1]} ({'NULL' if col[2] == 'YES' else 'NOT NULL'})")
        
        # Count records
        cursor.execute("SELECT COUNT(*) FROM nhanvien;")
        count = cursor.fetchone()[0]
        print(f"\n📈 Total records: {count}")
        
        if count > 0:
            # Get first 5 records
            print("\n📋 First 5 records:")
            cursor.execute("SELECT * FROM nhanvien LIMIT 5;")
            records = cursor.fetchall()
            
            # Get column names
            col_names = [desc[0] for desc in cursor.description]
            print(f"Columns: {', '.join(col_names)}")
            
            for i, record in enumerate(records, 1):
                print(f"\nRecord {i}:")
                for j, value in enumerate(record):
                    print(f"  {col_names[j]}: {value}")
        else:
            print("📭 No records found in nhanvien table")
        
        cursor.close()
        
    except Exception as e:
        print(f"❌ Error querying nhanvien table: {e}")

def query_employees_table(conn):
    """Query the employees table (if exists)"""
    try:
        cursor = conn.cursor()
        
        # Check if table exists
        cursor.execute("""
            SELECT EXISTS (
                SELECT FROM information_schema.tables 
                WHERE table_schema = 'public' 
                AND table_name = 'employees'
            );
        """)
        table_exists = cursor.fetchone()[0]
        
        if not table_exists:
            print("❌ Table 'employees' does not exist!")
            return
        
        # Count records
        cursor.execute("SELECT COUNT(*) FROM employees;")
        count = cursor.fetchone()[0]
        print(f"\n📈 Total employees records: {count}")
        
        if count > 0:
            # Get first 3 records
            cursor.execute("SELECT * FROM employees LIMIT 3;")
            records = cursor.fetchall()
            
            # Get column names
            col_names = [desc[0] for desc in cursor.description]
            print(f"Columns: {', '.join(col_names)}")
            
            for i, record in enumerate(records, 1):
                print(f"\nEmployee {i}:")
                for j, value in enumerate(record):
                    print(f"  {col_names[j]}: {value}")
        
        cursor.close()
        
    except Exception as e:
        print(f"❌ Error querying employees table: {e}")

def main():
    """Main function"""
    print("🗄️ Supabase Database Query Tool")
    print("=" * 40)
    
    # Connect to database
    conn = connect_to_supabase()
    if not conn:
        return
    
    try:
        # List all tables
        tables = list_tables(conn)
        
        # Query nhanvien table
        print("\n" + "=" * 40)
        print("🔍 Querying nhanvien table...")
        query_nhanvien_table(conn)
        
        # Query employees table
        print("\n" + "=" * 40)
        print("🔍 Querying employees table...")
        query_employees_table(conn)
        
    finally:
        conn.close()
        print("\n🔌 Connection closed")

if __name__ == "__main__":
    main() 