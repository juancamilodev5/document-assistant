from fastapi import APIRouter
from pydantic import BaseModel
from app.services.document_service import process, answer
from app.services.models.document_models import QuestionRequest, ProcessRequest

router = APIRouter(prefix="/documents", tags=["documents"])

@router.post("/process")
async def process_document(request: ProcessRequest):
    chunks = await process(request.file_path, request.document_id)
    return {"message": "Document processed", "chunks": chunks}

@router.post("/ask")
async def ask_question(request: QuestionRequest):
    return await answer(document_id=request.document_id, question=request.question)