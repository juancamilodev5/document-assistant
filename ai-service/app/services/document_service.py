from fastapi import HTTPException
from app.services.pdf_service import extract_text, chunk_text
from app.services.embedding_service import generate_embeddings, generate_single_embedding
from app.services.qdrant_service import store_chunks, search_similar
import httpx
from app.config import settings
from openai import OpenAI

async def process(file_path: str, document_id: int) -> int:
    """
        spring boot call this endpoint after user uploads a document.
        this endpoint do all ia pipeline
    """
    try:
        text = extract_text(file_path)
        if not text.strip():
            raise HTTPException(400, "could not extract text from PDF")

        chunks = chunk_text(text)

        embeddings = generate_embeddings(chunks)

        store_chunks(document_id, chunks, embeddings)

        # notify spring boot document is ready
        async with httpx.AsyncClient() as client:
            await client.patch(
                f"{settings.spring_boot_url}/api/documents/{document_id}/status",
                json={"status": "READY"},
                headers = {"X-Internal-Api-Key": settings.internal_api_key}
            )

        return len(chunks)
    except Exception as e:
        print('error processing document: ', e)
        async with httpx.AsyncClient() as client:
            await client.patch(
                f"{settings.spring_boot_url}/api/documents/{document_id}/status",
                json={"status": "FAILED"},
                headers={"X-Internal-Api-Key": settings.internal_api_key}
            )
        raise HTTPException(500, str(e))

async def answer(document_id: int, question: str) -> dict:
    """
        receive question, search relevants chunks in qdrant,
        generates answer with openai
    """

    query_embedding = generate_single_embedding(question)

    # top 4 relevants chunks
    relevant_chunks = search_similar(query_embedding, document_id, limit=4)

    if not relevant_chunks:
        raise HTTPException(404, "no relevant content found for this question")

    # build context with chunks
    context = "\n\n".join(relevant_chunks)

    # call openai with context + question
    openai_client = OpenAI(api_key=settings.openai_api_key)
    response = openai_client.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            {
                "role": "system",
                "content": (
                    "You are a helpful assistant. Answer questions based ONLY "
                    "on the provided context. If the answer is not in the context, "
                    "say so clearly."
                )
            },
            {
                "role": "user",
                "content": f"Context:\n{context}\n\nQuestion: {question}"
            }
        ],
        max_tokens=500
    )

    return {
        "answer": response.choices[0].message.content,
        "chunks_used": len(relevant_chunks)
    }