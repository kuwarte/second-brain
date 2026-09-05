package com.kuwarte.api.chat.dto;

import com.kuwarte.api.document.DocumentChunk;

import java.util.UUID;

public record CitationResponse(
        UUID documentId,
        String filename,
        String chunkText) {

    public static CitationResponse toResponse(DocumentChunk chunk) {
        return new CitationResponse(
                chunk.getDocument().getId(),
                chunk.getDocument().getFilename(),
                chunk.getContent());
    }
}
