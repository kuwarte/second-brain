package com.kuwarte.api.document;

import com.kuwarte.api.ollama.OllamaClient;
import com.kuwarte.api.preferences.UserPreferencesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngestionService {

    private static final double OVERLAP_RATIO = 0.1;

    private final TextExtractor textExtractor;
    private final ChunkingService chunkingService;
    private final OllamaClient ollamaClient;
    private final IngestionPersistence persistence;
    private final UserPreferencesService preferencesService;

    /**
     * Now @Async: the caller (DocumentController) no longer waits for this
     * to finish. Takes the file's raw bytes rather than an InputStream,
     * since MultipartFile's stream is tied to the original request and is
     * no longer valid once that request completes and this runs on a
     * separate thread.
     */
    @Async
    public void ingest(Document document, byte[] fileBytes) {
        persistence.markProcessing(document);

        try {
            int chunkSize = preferencesService.getForUser(document.getUser()).getChunkSize();
            int overlap = overlapFor(chunkSize);

            String text = textExtractor.extract(new ByteArrayInputStream(fileBytes), document.getContentType());
            List<String> chunkTexts = chunkingService.chunk(text, chunkSize, overlap);

            if (chunkTexts.isEmpty()) {
                throw new IllegalStateException("No extractable text found in file");
            }

            List<List<Float>> embeddings = ollamaClient.embed(chunkTexts);
            persistence.persistChunksAndMarkReady(document, chunkTexts, embeddings);

            log.info("Ingested document {} into {} chunks (chunkSize={})",
                    document.getId(), chunkTexts.size(), chunkSize);

        } catch (Exception e) {
            log.error("Ingestion failed for document {}", document.getId(), e);
            persistence.markFailed(document);
        }
    }

    private int overlapFor(int chunkSize) {
        return Math.max(0, (int) (chunkSize * OVERLAP_RATIO));
    }
}
