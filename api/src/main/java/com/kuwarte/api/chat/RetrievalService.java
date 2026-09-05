package com.kuwarte.api.chat;

import com.kuwarte.api.document.ChunkRepository;
import com.kuwarte.api.document.DocumentChunk;
import com.kuwarte.api.ollama.OllamaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrievalService {

    private static final int TOP_K = 5;

    private final OllamaClient ollamaClient;
    private final ChunkRepository chunkRepository;

    /**
     * Embeds the question and returns the most similar chunks belonging to
     * the given user, ordered by relevance (most similar first).
     */
    public List<DocumentChunk> findRelevantChunks(UUID userId, String question) {
        List<Float> embedding = ollamaClient.embedOne(question);
        String vectorLiteral = toVectorLiteral(embedding);
        return chunkRepository.findSimilarChunks(userId, vectorLiteral, TOP_K);
    }

    /**
     * Converts a float embedding into pgvector's text literal format,
     * e.g. [0.12,0.98,...], which Postgres can cast directly to `vector`.
     */
    private String toVectorLiteral(List<Float> embedding) {
        return embedding.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
