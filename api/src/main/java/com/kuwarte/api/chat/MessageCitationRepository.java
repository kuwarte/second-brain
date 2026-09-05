package com.kuwarte.api.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageCitationRepository extends JpaRepository<MessageCitation, UUID> {
}
