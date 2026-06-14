from qdrant_client import QdrantClient
from qdrant_client.models import (
    Distance, VectorParams, PointStruct, Filter,
    FieldCondition, MatchValue
)
from app.config import settings
from typing import List
import uuid

client = QdrantClient(host=settings.qdrant_host, port=settings.qdrant_port)


def ensure_collection():
    """create qdrant collection if not exists"""
    collections = [c.name for c in client.get_collections().collections]
    if settings.collection_name not in collections:
        client.create_collection(
            collection_name=settings.collection_name,
            vectors_config=VectorParams(
                size=1536,
                distance=Distance.COSINE  # mide similitud entre vectores
            )
        )


def store_chunks(document_id: int, chunks: List[str], embeddings: List[List[float]]):
    """
    Guarda cada chunk con su vector en Qdrant.

    each point has:
    - id: unique (UUID)
    - vector: chunk embedding
    - payload: metadata (document_id, chunk text)

    payload is a key — when qdrant found this vector, will return the original text too
    """
    ensure_collection()

    points = [
        PointStruct(
            id=str(uuid.uuid4()),
            vector=embedding,
            payload={
                "document_id": document_id,
                "text": chunk,
                "chunk_index": i
            }
        )
        for i, (chunk, embedding) in enumerate(zip(chunks, embeddings))
    ]

    client.upsert(
        collection_name=settings.collection_name,
        points=points
    )


def search_similar(query_embedding: List[float], document_id: int, limit: int = 4):
    """
    search similar chunks to the user question

    filter by document_id
    """
    results = client.search(
        collection_name=settings.collection_name,
        query_vector=query_embedding,
        query_filter=Filter(
            must=[FieldCondition(
                key="document_id",
                match=MatchValue(value=document_id)
            )]
        ),
        limit=limit,
        with_payload=True
    )
    return [hit.payload["text"] for hit in results]