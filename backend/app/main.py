from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.database.connection import engine
from app.models import employee
from app.routes import employees

# Create database tables (commented out to avoid startup errors)
# employee.Base.metadata.create_all(bind=engine)

# Create FastAPI app
app = FastAPI(
    title="PhiLong Employee Management System",
    description="API for managing employee information",
    version="1.0.0"
)

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:3000", 
        "http://127.0.0.1:3000",
        "http://localhost:8080",
        "http://127.0.0.1:8080",
        "file://",
        "null"
    ],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routers
app.include_router(employees.router)

@app.get("/")
def read_root():
    return {
        "message": "PhiLong Employee Management System API",
        "version": "1.0.0",
        "docs": "/docs"
    }

@app.get("/health")
def health_check():
    return {"status": "healthy"}

@app.post("/init-db")
def init_database():
    """Initialize database tables"""
    try:
        employee.Base.metadata.create_all(bind=engine)
        return {"message": "Database tables created successfully"}
    except Exception as e:
        return {"error": f"Failed to create tables: {str(e)}"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)