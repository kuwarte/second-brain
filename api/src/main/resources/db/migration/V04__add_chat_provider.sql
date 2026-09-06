ALTER TABLE user_preferences
    ADD COLUMN chat_provider VARCHAR(20) NOT NULL DEFAULT 'OLLAMA';
