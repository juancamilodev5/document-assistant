from pypdf import PdfReader
from typing import List


def extract_text(file_path: str) -> str:
    reader = PdfReader(file_path)
    text = ""
    for page in reader.pages:
        text += page.extract_text() or ""
    print(f"Extracted {len(text)} characters, {len(text.split())} words")
    return text


def chunk_text(text: str, chunk_size: int = 500, overlap: int = 50) -> List[str]:
    """
    chunk_size approximate tokens per chunk
    overlap allows no one answer lost
    """
    words = text.split()
    chunks = []
    start = 0

    while start < len(words):
        end = start + chunk_size
        chunk = " ".join(words[start:end])
        chunks.append(chunk)
        start += chunk_size - overlap

    return [c for c in chunks if len(c.strip()) > 50]