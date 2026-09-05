package com.kuwarte.api.chat;

import com.kuwarte.api.chat.dto.*;
import com.kuwarte.api.common.exception.ResourceNotFoundException;
import com.kuwarte.api.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ChatController {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessageCitationRepository citationRepository;
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ConversationResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody(required = false) CreateConversationRequest request) {

        Conversation conversation = Conversation.builder()
                .user(principal.getUser())
                .title(request != null ? request.title() : null)
                .build();
        conversationRepository.save(conversation);

        return ResponseEntity.status(HttpStatus.CREATED).body(ConversationResponse.toResponse(conversation));
    }

    @GetMapping
    public List<ConversationResponse> list(@AuthenticationPrincipal UserPrincipal principal) {
        return conversationRepository.findAllByUserIdOrderByCreatedAtDesc(principal.getId())
                .stream()
                .map(ConversationResponse::toResponse)
                .toList();
    }

    @GetMapping("/{id}/messages")
    public List<MessageResponse> messages(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {

        Conversation conversation = requireOwnedConversation(principal, id);

        List<Message> conversationMessages = messageRepository
                .findAllByConversationIdOrderByCreatedAtAsc(conversation.getId());

        List<UUID> messageIds = conversationMessages.stream().map(Message::getId).toList();

        Map<UUID, List<CitationResponse>> citationsByMessageId = citationRepository
                .findAllByMessageIdIn(messageIds)
                .stream()
                .collect(Collectors.groupingBy(
                        c -> c.getMessage().getId(),
                        Collectors.mapping(c -> CitationResponse.toResponse(c.getChunk()), Collectors.toList())));

        return conversationMessages.stream()
                .map(message -> MessageResponse.toResponse(
                        message,
                        citationsByMessageId.getOrDefault(message.getId(), List.of())))
                .toList();
    }

    @PostMapping("/{id}/messages")
    public MessageResponse ask(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody AskRequest request) {

        Conversation conversation = requireOwnedConversation(principal, id);
        return chatService.askQuestion(principal.getId(), conversation, request.content());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {

        Conversation conversation = requireOwnedConversation(principal, id);
        conversationRepository.delete(conversation); // cascades to messages/citations via DB FK
        return ResponseEntity.noContent().build();
    }

    private Conversation requireOwnedConversation(UserPrincipal principal, UUID id) {
        return conversationRepository.findByIdAndUserId(id, principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + id));
    }
}
