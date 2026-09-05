package com.kuwarte.api.auth.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("An account with email " + email + " already exists");
    }
}
