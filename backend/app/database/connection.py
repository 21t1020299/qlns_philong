from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
import os
from dotenv import load_dotenv

load_dotenv()

# Database URL - try Supabase first, fallback to SQLite
DATABASE_URL = os.getenv("DATABASE_URL", "sqlite:///./philong.db")

# Using Supabase database
# If the direct connection doesn't work, try the pooler connection
if "supabase.co" in DATABASE_URL and "db." in DATABASE_URL:
    # Try alternative Supabase connection format
    DATABASE_URL = "postgresql://postgres.wsisepsdhioaikdzrnmd:PhiLong2025!@aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres"

print(f"Using database: {DATABASE_URL}")

# Create SQLAlchemy engine
engine = create_engine(DATABASE_URL)

# Create SessionLocal class
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Create Base class
Base = declarative_base()

# Dependency to get database session
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()