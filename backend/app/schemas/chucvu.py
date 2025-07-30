from pydantic import BaseModel
from typing import Optional

class ChucVuBase(BaseModel):
    tencv: str
    mota: Optional[str] = None

class ChucVuCreate(ChucVuBase):
    macv: str

class ChucVuUpdate(ChucVuBase):
    pass

class ChucVuResponse(ChucVuBase):
    macv: str
    
    class Config:
        from_attributes = True 