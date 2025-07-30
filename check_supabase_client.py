#!/usr/bin/env python3
"""
Script to connect to Supabase using client and query data
"""

import requests
import json

def query_supabase():
    """Query Supabase using REST API"""
    
    # Supabase configuration
    SUPABASE_URL = "https://wsisepsdhioaikdzrnmd.supabase.co"
    SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndzaXNlcHNkaGlvYWlrZHpybm1kIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM2NjM3MDIsImV4cCI6MjA2OTIzOTcwMn0.kwveMFa2IIwT519B0D1eQr9ZASQm-TmQk90u68kUqQo"
    
    headers = {
        "apikey": SUPABASE_ANON_KEY,
        "Authorization": f"Bearer {SUPABASE_ANON_KEY}",
        "Content-Type": "application/json"
    }
    
    print("🗄️ Supabase REST API Query Tool")
    print("=" * 40)
    
    # Test connection
    print("🔗 Testing connection...")
    try:
        response = requests.get(f"{SUPABASE_URL}/rest/v1/", headers=headers)
        print(f"✅ Connection status: {response.status_code}")
    except Exception as e:
        print(f"❌ Connection failed: {e}")
        return
    
    # Try to query nhanvien table
    print("\n🔍 Querying nhanvien table...")
    try:
        response = requests.get(
            f"{SUPABASE_URL}/rest/v1/nhanvien?select=*&limit=5",
            headers=headers
        )
        
        if response.status_code == 200:
            data = response.json()
            print(f"✅ Found {len(data)} records in nhanvien table")
            
            if data:
                print("\n📋 First record structure:")
                first_record = data[0]
                for key, value in first_record.items():
                    print(f"  - {key}: {value}")
                
                print(f"\n📊 Total records shown: {len(data)}")
            else:
                print("📭 No records found in nhanvien table")
        else:
            print(f"❌ Error querying nhanvien: {response.status_code}")
            print(f"Response: {response.text}")
            
    except Exception as e:
        print(f"❌ Error: {e}")
    
    # Try to query employees table
    print("\n🔍 Querying employees table...")
    try:
        response = requests.get(
            f"{SUPABASE_URL}/rest/v1/employees?select=*&limit=5",
            headers=headers
        )
        
        if response.status_code == 200:
            data = response.json()
            print(f"✅ Found {len(data)} records in employees table")
            
            if data:
                print("\n📋 First record structure:")
                first_record = data[0]
                for key, value in first_record.items():
                    print(f"  - {key}: {value}")
                
                print(f"\n📊 Total records shown: {len(data)}")
            else:
                print("📭 No records found in employees table")
        else:
            print(f"❌ Error querying employees: {response.status_code}")
            print(f"Response: {response.text}")
            
    except Exception as e:
        print(f"❌ Error: {e}")
    
    # List all tables using SQL
    print("\n🔍 Listing all tables...")
    try:
        sql_query = {
            "query": """
                SELECT table_name 
                FROM information_schema.tables 
                WHERE table_schema = 'public'
                ORDER BY table_name;
            """
        }
        
        response = requests.post(
            f"{SUPABASE_URL}/rest/v1/rpc/exec_sql",
            headers=headers,
            json=sql_query
        )
        
        if response.status_code == 200:
            data = response.json()
            print("📋 Tables in database:")
            for table in data:
                print(f"  - {table['table_name']}")
        else:
            print(f"❌ Error listing tables: {response.status_code}")
            print(f"Response: {response.text}")
            
    except Exception as e:
        print(f"❌ Error listing tables: {e}")

if __name__ == "__main__":
    query_supabase() 