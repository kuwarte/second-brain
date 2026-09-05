package com.kuwarte.api.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenDenylistRepository extends JpaRepository<DenylistedToken, String> {

    boolean existsByJti(String jti);
}
