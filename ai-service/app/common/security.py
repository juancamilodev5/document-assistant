from fastapi import Header, HTTPException
from app.config import settings

async def verify_internal_key(x_internal_api_key: str = Header(...)):
    if x_internal_api_key != settings.internal_api_key:
        raise HTTPException(status_code=401, detail="unauthorized")