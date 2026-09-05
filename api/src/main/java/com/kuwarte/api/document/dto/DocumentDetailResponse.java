package com.kuwarte.api.document.dto;

import com.kuwarte.api.document.Document;
import com.kuwarte.api.document.enums.DocumentStatus;

import java.time.Instant;
import java.util.UUID;

public record DocumentDetailResponse(
        UUID id,
        String filename,
        DocumentStatus status,
        long chunkCount,
        Instant uploadedAt) {

    public static DocumentDetailResponse toResponse(Document document, long chunkCount) {
        return new DocumentDetailResponse(
                document.getId(),
                document.getFilename(),
                document.getStatus(),
                chunkCount,
                document.getUploadedAt());
    }
}
