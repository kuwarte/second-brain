package com.kuwarte.api.chat;

import com.kuwarte.api.chat.dto.CitationResponse;
import com.kuwarte.api.chat.dto.MessageResponse;
import com.kuwarte.api.chat.enums.MessageRole;
import com.kuwarte.api.document.DocumentChunk;
import com.kuwarte.api.llm.ChatModelRouter;
import com.kuwarte.api.preferences.UserPreferences;
import com.kuwarte.api.preferences.UserPreferencesRepository;
import com.kuwarte.api.preferences.enums.ChatProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RetrievalService retrievalService;
    private final ChatModelRouter chatModelRouter;
    private final MessageRepository messageRepository;
    private final MessageCitationRepository citationRepository;
    private final UserPreferencesRepository preferencesRepository;

    @Transactional
    public MessageResponse askQuestion(UUID userId, Conversation conversation, String question) {
        Message userMessage = Message.builder()
                .conversation(conversation)
                .role(MessageRole.USER)
                .content(question)
                .build();
        messageRepository.save(userMessage);

        List<DocumentChunk> chunks = retrievalService.findRelevantChunks(userId, question);

        String prompt = buildPrompt(question, chunks);

        UserPreferences preferences = preferencesRepository.findByUserId(userId).orElse(null);
        ChatProvider provider = preferences != null ? preferences.getChatProvider() : ChatProvider.OLLAMA;
        String model = preferences != null ? preferences.getChatModel() : "llama3.2";

        String answer = chatModelRouter.generate(provider, prompt, model);

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
