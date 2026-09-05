package com.kuwarte.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenDenylistService {

    private final TokenDenylistRepository repository;

    @Transactional
    public void revoke(String jti, Instant expiresAt) {
        if (repository.existsById(jti)) {
            return;
        }
        repository.save(new DenylistedToken(jti, expiresAt));
    }

    @Transactional(readOnly = true)
    public boolean isRevoked(String jti) {
        return repository.existsByJti(jti);
    }
}
