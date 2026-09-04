# Architecture

## Overview

- **web** (Next.js) — upload UI, chat UI. Talks only to the backend API.
- **api** (Spring Boot) — auth, document ingestion, retrieval, chat orchestration.
  The only service that talks to Postgres and Ollama.
- **postgres** (pgvector) — stores users, documents, chunks + embeddings,
  conversations, messages.
- **ollama** — generates embeddings and chat answers, running locally.

## Request flow

1. User uploads a document → `api` extracts text, chunks it, calls Ollama for
   embeddings, stores chunks + embeddings in Postgres.
2. User asks a question → `api` embeds the question, finds similar chunks via
   pgvector similarity search, sends question + chunks to Ollama, returns the
   answer with citations.

## Package structure (api/)

Feature-based packages: `security`, `auth`, `profile`, `preference`,
`document`, `chat`, `ollama`, `common`. See source tree for details.
