# Setup

## Prerequisites

- Docker Desktop
- Java 21
- Node 20+
- Ollama installed locally

## Environment variables

Copy `.env.example` to `.env` at the project root and fill in real values.

## Database

```bash
docker compose up -d postgres
```

Verify it's running:

```bash
docker ps
```

## Backend (api/)

_TODO: run instructions once the Spring Boot app is bootable._

## Frontend (web/)

_TODO: run instructions once the Next.js app exists._

## Ollama models

```bash
ollama pull llama3.2
ollama pull nomic-embed-text
```
