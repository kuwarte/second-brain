package com.kuwarte.api.document;

import com.kuwarte.api.document.enums.DocumentStatus;
import com.pgvector.PGvector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
class IngestionPersistence {

    private final DocumentRepository documentRepository;
    private final ChunkRepository chunkRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void markProcessing(Document document) {
        document.setStatus(DocumentStatus.PROCESSING);
        documentRepository.save(document);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void persistChunksAndMarkReady(Document document, List<String> chunkTexts, List<List<Float>> embeddings) {
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
    }

    // Also clears any chunks already written for this document, so a failed
    // ingestion never leaves stray chunks behind and a retry starts clean.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void markFailed(Document document) {
        chunkRepository.deleteAllByDocumentId(document.getId());
        document.setStatus(DocumentStatus.FAILED);
        documentRepository.save(document);
    }

    private PGvector toPGvector(List<Float> embedding) {
        float[] array = new float[embedding.size()];
        for (int i = 0; i < embedding.size(); i++)
            array[i] = embedding.get(i);
        return new PGvector(array);
    }
}
