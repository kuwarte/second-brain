# Database

## Entities

- **users** — auth identity
- **documents** — uploaded files, owned by a user
- **document_chunks** — text chunks + pgvector embeddings
- **conversations** — chat threads
- **messages** — turns within a conversation
- **message_citations** — join table linking an answer message to the chunks
  it cited

## Migrations

Managed with Flyway, in `api/src/main/resources/db/migration/`.

- `V1__init_schema.sql` — core tables
- `V2__enable_pgvector.sql` — enables the `vector` extension, adds the
  embedding column

_TODO: keep this list updated as new migrations are added._
