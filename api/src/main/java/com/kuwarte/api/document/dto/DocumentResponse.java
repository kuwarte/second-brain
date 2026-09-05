package com.kuwarte.api.document.dto;

import com.kuwarte.api.document.Document;
import com.kuwarte.api.document.enums.DocumentStatus;

import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        String filename,
        DocumentStatus status,
        Instant uploadedAt) {

    public static DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getFilename(),
                document.getStatus(),
                document.getUploadedAt());
    }
}
