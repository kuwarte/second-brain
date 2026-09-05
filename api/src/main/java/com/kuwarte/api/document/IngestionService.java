package com.kuwarte.api.document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuwarte.api.document.enums.DocumentStatus;
import com.kuwarte.api.ollama.OllamaClient;
import com.pgvector.PGvector;

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
    private final OllamaClient ollamaClient;

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
            List<String> chunkTexts = chunkingService.chunk(text, DEFAULT_CHUNK_SIZE, DEFAULT_OVERLAP);

            if (chunkTexts.isEmpty()) {
                throw new IllegalStateException("No extractable text found in file");
            }

            List<List<Float>> embeddings = ollamaClient.embed(chunkTexts);

            for (int i = 0; i < chunkTexts.size(); i++) {
                PGvector vector = toPGvector(embeddings.get(i));

                DocumentChunk chunk = DocumentChunk.builder()
                        .document(document)
                        .content(chunkTexts.get(i))
                        .chunkIndex(i)
                        .embedding(vector)
                        .build();
                chunkRepository.save(chunk);
            }

            document.setStatus(DocumentStatus.READY);
            documentRepository.save(document);

            log.info("Ingested document {} into {} chunks", document.getId(), chunkTexts.size());

        } catch (Exception e) {
            log.error("Ingestion failed for document {}", document.getId(), e);
            document.setStatus(DocumentStatus.FAILED);
            documentRepository.save(document);
        }
    }

    private PGvector toPGvector(List<Float> embedding) {
        float[] array = new float[embedding.size()];
        for (int i = 0; i < embedding.size(); i++) {
            array[i] = embedding.get(i);
        }
        return new PGvector(array);
    }
}
