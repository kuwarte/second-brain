package com.kuwarte.api.document;

import com.kuwarte.api.common.exception.ResourceNotFoundException;
import com.kuwarte.api.document.dto.DocumentDetailResponse;
import com.kuwarte.api.document.dto.DocumentResponse;
import com.kuwarte.api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final ChunkRepository chunkRepository;
    private final IngestionService ingestionService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DocumentResponse> upload(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("file") MultipartFile file) throws IOException {

        String contentType = file.getContentType();
        if (contentType == null || !TextExtractor.SUPPORTED_CONTENT_TYPES.contains(contentType)) {
            throw new TextExtractor.UnsupportedFileTypeException(contentType);
        }

        Document document = Document.builder()
                .user(principal.getUser())
                .filename(file.getOriginalFilename())
                .contentType(contentType)
                .build();
        documentRepository.save(document);

        byte[] fileBytes = file.getBytes(); // read now, while the request/stream is still valid
        ingestionService.ingest(document, fileBytes); // returns immediately — @Async

        return ResponseEntity.status(HttpStatus.CREATED).body(DocumentResponse.toResponse(document));
    }

    @GetMapping
    public List<DocumentResponse> list(@AuthenticationPrincipal UserPrincipal principal) {
        return documentRepository.findAllByUserIdOrderByUploadedAtDesc(principal.getId())
                .stream()
                .map(DocumentResponse::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public DocumentDetailResponse detail(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {

        Document document = documentRepository.findByIdAndUserId(id, principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + id));

        long chunkCount = chunkRepository.countByDocumentId(document.getId());
        return DocumentDetailResponse.toResponse(document, chunkCount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {

        Document document = documentRepository.findByIdAndUserId(id, principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + id));

        documentRepository.delete(document);
        return ResponseEntity.noContent().build();
    }
}
