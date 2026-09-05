package com.kuwarte.api.chat;

import com.kuwarte.api.chat.dto.CitationResponse;
import com.kuwarte.api.chat.dto.MessageResponse;
import com.kuwarte.api.document.DocumentChunk;
import com.kuwarte.api.ollama.OllamaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuwarte.api.chat.enums.MessageRole;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RetrievalService retrievalService;
    private final OllamaClient ollamaClient;
    private final MessageRepository messageRepository;
    private final MessageCitationRepository citationRepository;

    @Transactional
    public MessageResponse askQuestion(UUID userId, Conversation conversation, String question) {
        // 1. Save the user's message first
        Message userMessage = Message.builder()
                .conversation(conversation)
                .role(MessageRole.USER)
                .content(question)
                .build();
        messageRepository.save(userMessage);

        // 2. Retrieve relevant chunks, scoped to this user's own documents
        List<DocumentChunk> chunks = retrievalService.findRelevantChunks(userId, question);

        // 3. Build a grounded prompt and generate an answer
        String prompt = buildPrompt(question, chunks);
        String answer = ollamaClient.generate(prompt);

        // 4. Save the assistant's message and its citations
        Message assistantMessage = Message.builder()
                .conversation(conversation)
                .role(MessageRole.ASSISTANT)
                .content(answer)
                .build();
        messageRepository.save(assistantMessage);

        List<CitationResponse> citationResponses = chunks.stream()
                .map(chunk -> {
                    MessageCitation citation = MessageCitation.builder()
                            .message(assistantMessage)
                            .chunk(chunk)
                            .build();
                    citationRepository.save(citation);
                    return CitationResponse.toResponse(chunk);
                })
                .collect(Collectors.toList());

        return MessageResponse.toResponse(assistantMessage, citationResponses);
    }

    private String buildPrompt(String question, List<DocumentChunk> chunks) {
        if (chunks.isEmpty()) {
            return """
                    Answer the following question. No relevant source material was found,
                    so say you don't have enough information to answer from the user's documents.

                    Question: %s
                    """.formatted(question);
        }

        String context = chunks.stream()
                .map(chunk -> "- (from \"%s\") %s".formatted(chunk.getDocument().getFilename(), chunk.getContent()))
                .collect(Collectors.joining("\n"));

        return """
                Answer the question using only the context below. If the context
                doesn't contain the answer, say you don't know rather than guessing.

                Context:
                %s

                Question: %s
                """.formatted(context, question);
    }
}
