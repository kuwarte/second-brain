package com.kuwarte.api.document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.kuwarte.api.ollama.OllamaClient;
import com.kuwarte.api.preferences.UserPreferencesService;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngestionService {

    // Fallback overlap ratio when computing overlap from a user's preferred
    // chunk size (see overlapFor). Kept modest so most of each chunk is new
    // content, not repeated context.
    private static final double OVERLAP_RATIO = 0.1;

    private final TextExtractor textExtractor;
    private final ChunkingService chunkingService;
    private final OllamaClient ollamaClient;
    private final IngestionPersistence persistence;
    private final UserPreferencesService preferencesService;

    /**
     * Runs the full ingestion pipeline for a document that's already been
     * saved with status PENDING: extract text, chunk it, save the chunks,
     * then flip status to READY (or FAILED if anything goes wrong).
     *
     * Deliberately not one big transaction. Text extraction is CPU-bound and
     * the embedding call is a network round-trip to Ollama — neither needs a
     * DB connection held open for its duration. The DB-touching steps run in
     * their own short transactions via IngestionPersistence instead, so a
     * slow embed call doesn't tie up a pooled connection the whole time.
     *
     * document.getUser() is safe to read here (unlike the equivalent lookup
     * in ChatService): `document` was just built in-memory in this same
     * request with an already-fully-loaded User object assigned directly,
     * not hydrated from the DB as a lazy proxy.
     *
     * Synchronous for now — the caller waits for this to finish. Once the
     * pipeline is proven correct, this is the natural place to add @Async
     * so uploads return immediately while ingestion runs in the background.
     */
    public void ingest(Document document, InputStream fileStream) {
        persistence.markProcessing(document);

        try {
            int chunkSize = preferencesService.getForUser(document.getUser()).getChunkSize();
            int overlap = overlapFor(chunkSize);

            String text = textExtractor.extract(fileStream, document.getContentType());
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

    /**
     * ChunkingService requires 0 <= overlap < chunkSize. A fixed overlap
     * constant (e.g. 50) would violate that for a small user-chosen
     * chunkSize, so scale it instead — capped well below chunkSize.
     */
    private int overlapFor(int chunkSize) {
        return Math.max(0, (int) (chunkSize * OVERLAP_RATIO));
    }
}
