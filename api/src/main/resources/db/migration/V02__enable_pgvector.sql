CREATE EXTENSION IF NOT EXISTS vector;

-- 768 dimensions matches nomic-embed-text. If you switch embedding models
-- later, this dimension must match that model's output size.
ALTER TABLE document_chunks
    ADD COLUMN embedding VECTOR(768);

-- HNSW index for fast approximate nearest-neighbor search.
-- vector_cosine_ops matches cosine similarity, the usual choice for text embeddings.
CREATE INDEX idx_document_chunks_embedding
    ON document_chunks
    USING hnsw (embedding vector_cosine_ops);
