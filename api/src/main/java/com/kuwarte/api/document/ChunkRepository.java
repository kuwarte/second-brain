package com.kuwarte.api.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChunkRepository extends JpaRepository<DocumentChunk, UUID> {

    List<DocumentChunk> findAllByDocumentIdOrderByChunkIndexAsc(UUID documentId);

    long countByDocumentId(UUID documentId);

    void deleteAllByDocumentId(UUID documentId);

    /**
     * Finds the chunks most similar to the given embedding, restricted to
     * documents owned by userId. The <=> operator is pgvector's cosine
     * distance — smaller means more similar, so we order ascending.
     *
     * The embedding is passed as its pgvector text literal (e.g. "[0.1,0.2,...]")
     * and cast to vector in SQL, since Spring Data's native query binding
     * doesn't know how to map the PGvector Java type directly.
     */
    @Query(value = """
            SELECT c.* FROM document_chunks c
            JOIN documents d ON c.document_id = d.id
            WHERE d.user_id = :userId
            ORDER BY c.embedding <=> CAST(:embedding AS vector)
            LIMIT :limit
            """, nativeQuery = true)
    List<DocumentChunk> findSimilarChunks(
            @Param("userId") UUID userId,
            @Param("embedding") String embedding,
            @Param("limit") int limit);
}
