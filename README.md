# Document Assistant API

Backend API for document management with AI-powered Q&A.
Built with Spring Boot (Java) and FastAPI (Python).

## Stack
- Java 21 + Spring Boot 3.3
- PostgreSQL 16
- Qdrant (vector database)
- OpenAI API + ElevenLabs
- Docker + Docker Compose

## Architecture
Spring Boot manage documents (upload to disk) and communicate with FastAPI microservice.
FastAPI microservice manage IA and Qdrant (to save vectors).

## Getting Started

1. Clone the repo
2. Copy `application.properties.example` to `application.properties` and fill in your values
3. Run: `docker compose up -d`
4. Start the backend: `./mvnw spring-boot:run`

## API Endpoints (Week 1)
POST /api/auth/register  - Register a new user
POST /api/auth/login     - Login and receive JWT
POST /api/documents/upload - Upload a PDF (requires JWT)
GET  /api/documents      - List user's documents (requires JWT)

## Status
Week 1 complete: Auth + document upload
Week 2 in progress: AI microservice (FastAPI + Qdrant + OpenAI)