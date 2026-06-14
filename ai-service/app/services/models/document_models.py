from pydantic import BaseModel

class ProcessRequest(BaseModel):
    document_id: int
    file_path: str

class QuestionRequest(BaseModel):
    document_id: int
    question: str