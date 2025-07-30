from pydantic import BaseModel

class ChucVuBase(BaseModel):
    tencv: str

class ChucVuCreate(ChucVuBase):
    macv: str

class ChucVuUpdate(ChucVuBase):
    pass

class ChucVuResponse(ChucVuBase):
    macv: str
    
    class Config:
        from_attributes = True 