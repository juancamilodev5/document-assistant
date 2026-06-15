# Document Assistant API

Backend API for document management with AI-powered Q&A.
Built with Spring Boot (Java) and FastAPI (Python).

- Java 21 + Spring Boot 4.1
- Python 3.12 + FastAPI
- PostgreSQL 16
- Qdrant (vector database)
- OpenAI API (embeddings + GPT-4o-mini)
- Docker + Docker Compose

## Architecture
Spring Boot handles authentication, document storage, and
orchestration. FastAPI handles the AI pipeline: PDF extraction,
chunking, embeddings, vector storage, and RAG-based Q&A.

## Getting Started

### Prerequisites
- Docker + Docker Compose
- OpenAI API key

### Setup

1. Clone the repo
```bash
   git clone https://github.com/juancamilodev5/document-assistant.git
   cd document-assistant
```

2. Create a `.env` file in the root:
```env
   OPENAI_API_KEY=sk-...
   JWT_SECRET=your-jwt-secret
   INTERNAL_API_KEY=your-internal-api-key
```

3. Run everything:
```bash
   docker compose up --build
```

Spring Boot will be available at `http://localhost:8080`.
FastAPI docs at `http://localhost:8000/docs` (internal service).

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register and receive JWT |
| POST | `/api/auth/login` | Login and receive JWT |

### Documents (require JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/documents/upload` | Upload a PDF |
| GET | `/api/documents` | List user's documents |
| POST | `/api/documents/ask` | Ask a question about a document |

## How it works

1. User uploads a PDF → Spring Boot saves it and responds immediately
2. In background, Spring Boot calls FastAPI to process the document
3. FastAPI extracts text, chunks it, generates embeddings, stores in Qdrant
4. Document status updates to `READY`
5. User asks a question → Spring Boot calls FastAPI → RAG search in Qdrant → GPT-4o-mini generates answer