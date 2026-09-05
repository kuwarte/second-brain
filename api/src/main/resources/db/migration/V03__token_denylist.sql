CREATE TABLE token_denylist (
    jti        VARCHAR(36) PRIMARY KEY,
    expires_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_token_denylist_expires_at ON token_denylist(expires_at);
