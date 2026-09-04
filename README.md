# Second Brain

A personal RAG-powered knowledge base. Upload your own notes, coursework, and
project docs, then ask questions and get answers grounded in your own content
with source citations.

Stack: Spring Boot, Next.js, PostgreSQL (pgvector), Ollama, Docker.

## Table of contents

- [Quick start](#quick-start)
- [Documentation](#documentation)
- [Project structure](#project-structure)
- [Tech stack](#tech-stack)

## Quick start

1. Copy `.env.example` to `.env` and fill in real values.
2. Start the database:
   ```bash
   docker compose up -d postgres
   ```
3. Pull the Ollama models used for chat and embeddings:
   ```bash
   ollama pull llama3.2
   ollama pull nomic-embed-text
   ```
4. Run the backend from `api/` (see [docs/setup.md](docs/setup.md) for details).
5. Run the frontend from `web/` (see [docs/setup.md](docs/setup.md) for details).

## Documentation

| Doc | Covers |
|---|---|
| [docs/setup.md](docs/setup.md) | Full local setup, environment variables, running each service |
| [docs/architecture.md](docs/architecture.md) | System overview, how the pieces talk to each other |
| [docs/database.md](docs/database.md) | Schema, entity relationships, migrations |
| [docs/api.md](docs/api.md) | REST endpoints, request/response shapes |

## Project structure

```
second-brain/
├── docker-compose.yml
├── .env
├── api/          # Spring Boot backend
├── web/          # Next.js frontend
└── docs/         # Project documentation
```

## Tech stack

- **Backend** — Spring Boot, Spring Security (JWT), Spring Data JPA, Flyway
- **Database** — PostgreSQL with pgvector
- **AI** — Ollama (local LLM, runs chat + embeddings)
- **Frontend** — Next.js
- **Infra** — Docker Compose
