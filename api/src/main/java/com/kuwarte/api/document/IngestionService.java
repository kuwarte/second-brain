package com.kuwarte.api.document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuwarte.api.document.enums.DocumentStatus;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngestionService {

    private static final int DEFAULT_CHUNK_SIZE = 500;
    private static final int DEFAULT_OVERLAP = 50;

    private final TextExtractor textExtractor;
    private final ChunkingService chunkingService;
    private final DocumentRepository documentRepository;
    private final ChunkRepository chunkRepository;

    /**
     * Runs the full ingestion pipeline for a document that's already been
     * saved with status PENDING: extract text, chunk it, save the chunks,
     * then flip status to READY (or FAILED if anything goes wrong).
     *
     * Synchronous for now — the caller waits for this to finish. Once the
     * pipeline is proven correct, this is the natural place to add @Async
     * so uploads return immediately while ingestion runs in the background.
     */
    @Transactional
    public void ingest(Document document, InputStream fileStream) {
        document.setStatus(DocumentStatus.PROCESSING);
        documentRepository.save(document);

        try {
            String text = textExtractor.extract(fileStream, document.getContentType());
            List<String> chunks = chunkingService.chunk(text, DEFAULT_CHUNK_SIZE, DEFAULT_OVERLAP);

            for (int i = 0; i < chunks.size(); i++) {
                DocumentChunk chunk = DocumentChunk.builder()
                        .document(document)
                        .content(chunks.get(i))
                        .chunkIndex(i)
                        .build();
                chunkRepository.save(chunk);
            }

            document.setStatus(DocumentStatus.READY);
            documentRepository.save(document);

            log.info("Ingested document {} into {} chunks", document.getId(), chunks.size());

        } catch (Exception e) {
            log.error("Ingestion failed for document {}", document.getId(), e);
            document.setStatus(DocumentStatus.FAILED);
            documentRepository.save(document);
        }
    }
}
