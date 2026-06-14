from fastapi import FastAPI
from app.routers import documents
from app.services.qdrant_service import ensure_collection

app = FastAPI(title="Document Assistant AI Service")

@app.on_event("startup")
async def startup():
    ensure_collection()

app.include_router(documents.router)

@app.get("/health")
def health():
    return {"status": "ok"}