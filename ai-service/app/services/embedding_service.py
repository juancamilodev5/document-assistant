from openai import OpenAI
from app.config import settings
from typing import List

client = OpenAI(api_key=settings.openai_api_key)


def generate_embeddings(texts: List[str]) -> List[List[float]]:
    """
    convert a text list to numeric vectors

    text-embedding-3-small generates vectors of 1536 dimensions.
    each number represents a semantic characteristic of the text
    similar texts have similar vectors -> semantic search
    """
    response = client.embeddings.create(
        model="text-embedding-3-small",
        input=texts
    )
    return [item.embedding for item in response.data]


def generate_single_embedding(text: str) -> List[float]:
    return generate_embeddings([text])[0]